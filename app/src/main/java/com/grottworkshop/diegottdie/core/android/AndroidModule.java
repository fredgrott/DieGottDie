package com.grottworkshop.diegottdie.core.android;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by fgrott on 9/2/2014.
 */
@Module(injects = {}, library = true)
public class AndroidModule {

    @Provides
    @Singleton
    ActionBarPresenter provideActionBarPresenter() {
        return new ActionBarPresenter();
    }

}
