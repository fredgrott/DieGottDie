package com.grottworkshop.diegottdie;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.grottworkshop.diegottdie.core.android.AndroidModule;
import com.grottworkshop.diegottdie.core.util.GsonParcer;
import com.grottworkshop.diegottdie.ui.UiModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import flow.Parcer;

/**
 * Created by fgrott on 9/2/2014.
 */
@Module(
        includes = {
                UiModule.class,
                DataModule.class,
                AndroidModule.class
        },
        injects = {
                DiegottdieApp.class
        },
        library = true
)
public final class DiegottdieModule {

    private final DiegottdieApp app;

    public DiegottdieModule(DiegottdieApp app) {
        this.app = app;
    }

    @Provides @Singleton
    Application provideApplication() {
        return app;
    }

    @Provides @Singleton Gson provideGson() {
        return new GsonBuilder()
                .create();
    }

    @Provides
    @Singleton
    Parcer<Object> provideParcer(Gson gson) {
        return new GsonParcer<>(gson);
    }

}
