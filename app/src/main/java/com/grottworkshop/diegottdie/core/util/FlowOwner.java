package com.grottworkshop.diegottdie.core.util;

import android.os.Bundle;
import android.view.View;

import com.grottworkshop.diegottdie.util.Lists;

import java.util.List;

import flow.Backstack;
import flow.Flow;
import flow.Parcer;
import mortar.Blueprint;
import mortar.ViewPresenter;

/**
 * Created by fgrott on 9/4/2014.
 * @param <S>   the type parameter
 * @param <V>   the type parameter
 */
public abstract class FlowOwner<S extends Blueprint, V extends View & CanShowScreen<S> & CanShowDrawer<S>>
        extends ViewPresenter<V> implements Flow.Listener {

    private static final String FLOW_KEY = "FLOW_KEY";

    private final Parcer<Object> parcer;

    private Flow flow;

    /**
     * Instantiates a new Flow owner.
     *
     * @param parcer the parcer
     */
    protected FlowOwner(Parcer<Object> parcer) {
        this.parcer = parcer;
    }

    @Override
    public void onLoad(Bundle savedInstanceState) {
        super.onLoad(savedInstanceState);

        if (flow == null) {
            Backstack backstack;

            if (savedInstanceState != null) {
                backstack = Backstack.from(savedInstanceState.getParcelable(FLOW_KEY), parcer);
            } else {
                backstack = Backstack.fromUpChain(getFirstScreen());
            }

            flow = new Flow(backstack, this);
        }

        //noinspection unchecked
        showScreen((S) flow.getBackstack().current().getScreen(), null, null);
    }

    @Override
    public void onSave(Bundle outState) {
        super.onSave(outState);
        outState.putParcelable(FLOW_KEY, flow.getBackstack().getParcelable(parcer));
    }

    @Override
    public void go(Backstack backstack, Flow.Direction flowDirection) {
        //noinspection unchecked
        S newScreen = (S) backstack.current().getScreen();

        S oldScreen = null;
        if (flowDirection == Flow.Direction.FORWARD && backstack.size() > 1) {
            List<Backstack.Entry> entries = Lists.newArrayList(backstack.reverseIterator());
            Backstack.Entry oldEntry = entries.get(entries.size() - 2);
            //noinspection unchecked
            oldScreen = (S) oldEntry.getScreen();
        }

        showScreen(newScreen, oldScreen, flowDirection);
    }

    /**
     * Show screen.
     *
     * @param newScreen the new screen
     * @param oldScreen the old screen
     * @param flowDirection the flow direction
     */
    protected void showScreen(S newScreen, S oldScreen, Flow.Direction flowDirection) {
        V view = getView();
        if (view == null) return;

        view.showScreen(newScreen, oldScreen, flowDirection);
        view.showDrawer(getDrawerScreen());
    }

    /**
     * Gets flow.
     *
     * @return the flow
     */
    public final Flow getFlow() {
        return flow;
    }

    /**
     * Returns the first screen shown by this presenter.
     * @return the first screen
     */
    protected abstract S getFirstScreen();

    /**
     * Returns the screen of the navigation drawer
     * @return the drawer screen
     */
    protected abstract S getDrawerScreen();
}