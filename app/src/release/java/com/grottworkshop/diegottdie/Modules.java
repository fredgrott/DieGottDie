package com.grottworkshop.diegottdie;

/**
 * Modules class only provided in release and
 * debug and probably test build variants so
 * can override or add modules per build
 * variant characteristics such as
 * debugging api odules with fakeapimodule dagger
 * modules.
 *
 * Created by fgrott on 9/2/2014.
 */
final class Modules {
    static Object[] list(DiegottdieApp app) {
        return new Object[] {
                new DiegottdieModule(app)
        };
    }

    private Modules() {
        // No instances.
    }
}
