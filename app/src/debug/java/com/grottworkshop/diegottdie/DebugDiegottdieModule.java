package com.grottworkshop.diegottdie;

import com.grotttworkshop.diegottdie.DiegottdieModule;

import dagger.Module;

/**
 * Created by fgrott on 9/2/2014.
 */
@Module(
        addsTo = DiegottdieModule.class,
        includes = {

        },
        overrides = true
)
public final  class DebugDiegottdieModule {
}
