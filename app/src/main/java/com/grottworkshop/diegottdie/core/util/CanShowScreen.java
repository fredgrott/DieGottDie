package com.grottworkshop.diegottdie.core.util;

import flow.Flow;
import mortar.Blueprint;

/**
 * Created by fgrott on 9/4/2014.
 */
public interface CanShowScreen<S extends Blueprint> {
    void showScreen(S screen, S oldScreen, Flow.Direction direction);
}
