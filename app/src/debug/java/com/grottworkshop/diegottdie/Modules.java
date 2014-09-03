package com.grottworkshop.diegottdie;

import com.grotttworkshop.diegottdie.DiegottdieApp;
import com.grotttworkshop.diegottdie.DiegottdieModule;

/**
 * Created by fgrott on 9/2/2014.
 */
final class Modules {
    static Object[] list(DiegottdieApp app) {
        return new Object[] {
                new DiegottdieModule(app),
                new DebugDiegottdieModule()
        };
    }

    private Modules() {
        // No instances.
    }
}
