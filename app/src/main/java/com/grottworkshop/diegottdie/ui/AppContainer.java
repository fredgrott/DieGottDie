package com.grottworkshop.diegottdie.ui;

import android.app.Activity;
import android.view.ViewGroup;

import com.grottworkshop.diegottdie.DiegottdieApp;

import static butterknife.ButterKnife.findById;


/**
 * An indirection which allows controlling the root container used for each activity.
 * Created by fgrott on 9/2/2014.
 */
public interface AppContainer {
    /** The root {@link android.view.ViewGroup} into which the activity should place its contents. */
    ViewGroup get(Activity activity, DiegottdieApp app);

    /** An {@link AppContainer} which returns the normal activity content view. */
    AppContainer DEFAULT = new AppContainer() {
        @Override public ViewGroup get(Activity activity, DiegottdieApp app) {
            return findById(activity, android.R.id.content);
        }
    };
}
