package com.grottworkshop.diegottdie.core;

import android.os.Parcelable;
import android.util.SparseArray;

import mortar.Blueprint;

/**
 * Created by fgrott on 9/4/2014.
 */
public interface StateBlueprint extends Blueprint {

    public void setViewState(SparseArray<Parcelable> viewState);

}
