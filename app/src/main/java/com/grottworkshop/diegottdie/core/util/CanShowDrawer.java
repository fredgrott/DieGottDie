package com.grottworkshop.diegottdie.core.util;

import mortar.Blueprint;

/**
 * Created by fgrott on 9/4/2014.
 */
public interface CanShowDrawer<S extends Blueprint> {
    void showDrawer(S screen);
}
