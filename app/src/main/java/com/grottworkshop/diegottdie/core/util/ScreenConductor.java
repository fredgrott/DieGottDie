package com.grottworkshop.diegottdie.core.util;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.widget.DrawerLayout;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.grotttworkshop.diegottdie.R;
import com.grottworkshop.diegottdie.core.StateBlueprint;
import com.grottworkshop.diegottdie.core.TransitionScreen;
import com.grottworkshop.diegottdie.core.anim.SimpleAnimatorListener;
import com.grottworkshop.diegottdie.core.anim.Transitions;
import com.grottworkshop.diegottdie.util.Views;

import butterknife.ButterKnife;
import flow.Flow;
import flow.Layouts;
import mortar.Blueprint;
import mortar.Mortar;
import mortar.MortarScope;

/**
 * A conductor that can swap subviews within a container view.
 * <p/>
 *
 * @param <S> the type of the screens that serve as a {@link mortar.Blueprint} for subview. Must
 *            be annotated with {@link flow.Layout}, suitable for use with {@link flow.Layouts#createView}.
 * Created by fgrott on 9/4/2014.
 */
public class ScreenConductor<S extends Blueprint> implements CanShowScreen<S>, CanShowDrawer<S> {

    // Using static view ids to find and replace core layouts
    private final static int drawerViewId  = Views.generateViewId();
    private final static int contentViewId = Views.generateViewId();

    private final Context   context;
    private final ViewGroup container;

    private AnimatorSet screenTransition;

    /**
     * @param container the container used to host child views. Typically this is a {@link
     *                  android.widget.FrameLayout} under the action bar. In this case
     *                  it is designed to hold two children - main content and a navigation
     *                  drawer.
     */
    public ScreenConductor(Context context, ViewGroup container) {
        this.context = context;
        this.container = container;
    }

    public void showScreen(S newScreen, S oldScreen, final Flow.Direction direction) {

        // Cancel previous transition and set end values
        if (screenTransition != null) {
            screenTransition.end();
        }

        final View oldChild = getContentView();

        if (destroyOldScope(newScreen, oldChild)) {
            storeViewState(oldChild, oldScreen);
            final View newChild = createNewChildView(newScreen, contentViewId);

            Transitions.Animators transitions = null;
            if (oldChild != null) {
                switch (direction) {
                    case FORWARD:
                        // Load animations from Transition annotations, store them into backstack and set them to views
                        storeTransitions(oldScreen, newScreen);
                        transitions = Transitions.forward(context, newScreen);
                        break;
                    case BACKWARD:
                        if (newScreen instanceof TransitionScreen) {
                            // Try to load animations from a screen and set them
                            int[] transitionIds = ((TransitionScreen) newScreen).getTransitions();
                            transitions = Transitions.backward(context, transitionIds);
                        }
                        break;
                    case REPLACE:
                        // no animations
                        break;
                }
            }

            if (oldChild != null) {
                // Settings animator for each view and removing the old view
                // after animation ends
                if (transitions != null) {
                    transitions.out.setTarget(oldChild);
                    transitions.in.setTarget(newChild);
                    screenTransition = new AnimatorSet();
                    screenTransition.playTogether(transitions.out, transitions.in);
                    screenTransition.addListener(new SimpleAnimatorListener() {
                        @Override public void onAnimationEnd(Animator animation) {
                            container.removeView(oldChild);
                        }
                    });
                } else {
                    // remove view immediately if no transitions to run
                    container.removeView(oldChild);
                }
            }

            container.addView(newChild, 0);

            if (screenTransition != null) {
                screenTransition.start();
            }

            // Makes the new view z-index higher than the old view
            // for transitions forward to make feel more natural
            if (direction == Flow.Direction.FORWARD) {
                container.post(new Runnable() {
                    @Override public void run() {
                        container.bringChildToFront(newChild);
                        container.requestLayout();
                        container.invalidate();
                    }
                });
            }
        }

    }

    public void showDrawer(S screen) {
        View oldChild = getDrawerView();

        if (destroyOldScope(screen, oldChild)) {
            View newChild = createNewChildView(screen, drawerViewId);

            if (oldChild != null) {
                container.removeView(oldChild);
            }

            // Set some basic layout parameters so the drawer works
            DrawerLayout.LayoutParams params = new DrawerLayout.LayoutParams(
                    context.getResources().getDimensionPixelSize(R.dimen.navigation_drawer_width),
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
            params.gravity = Gravity.LEFT;
            newChild.setLayoutParams(params);

            container.addView(newChild, 1);
        }
    }

    /**
     * Destroys old child scope if it was different than the new one. Returns true
     * if successful
     */
    protected boolean destroyOldScope(S screen, View oldChild) {
        MortarScope myScope = Mortar.getScope(context);
        if (oldChild != null) {
            MortarScope oldChildScope = Mortar.getScope(oldChild.getContext());
            if (oldChildScope.getName().equals(screen.getMortarScopeName())) {
                return false;
            }
            myScope.destroyChild(oldChildScope);
        }
        return true;
    }

    /**
     * Creates a new child View from a given screen and sets its view Id
     */
    protected View createNewChildView(S screen, final int viewId) {
        MortarScope myScope = Mortar.getScope(context);
        MortarScope newChildScope = myScope.requireChild(screen);
        Context childContext = newChildScope.createContext(context);
        View newChild = Layouts.createView(childContext, screen);
        newChild.setId(viewId);
        return newChild;
    }

    /**
     * Store view hierarchy state into a Screen that will be pushed into
     * the backstack of Flow
     */
    protected void storeViewState(View view, S screen) {
        if (screen != null && screen instanceof StateBlueprint) {
            SparseArray<Parcelable> state = new SparseArray<>();
            view.saveHierarchyState(state);
            ((StateBlueprint) screen).setViewState(state);
        }
    }

    /**
     * Store transitions that were used from one screen to another
     *
     * @param oldScreen
     * @param newScreen
     */
    private void storeTransitions(S oldScreen, S newScreen) {
        if (oldScreen != null && oldScreen instanceof TransitionScreen) {
            ((TransitionScreen) oldScreen).setTransitions(Transitions.getTransitionResources(newScreen.getClass()));
        }
    }

    private View getContentView() {
        return ButterKnife.findById(container, contentViewId);
    }

    private View getDrawerView() {
        return ButterKnife.findById(container, drawerViewId);
    }
}

