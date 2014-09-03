package com.grottworkshop.diegottdie;

import android.app.Application;
import android.content.Context;

import com.grotttworkshop.diegottdie.BuildConfig;
import com.grottworkshop.diegottdie.ui.ActivityHierarchyServer;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.ObjectGraph;
import mortar.Mortar;
import mortar.MortarScope;
import timber.log.Timber;

/**
 * Created by fgrott on 9/2/2014.
 */
public class DiegottdieApp extends Application {

    public static final String MORTAR_SCOPE = "mortar_scope";

    private MortarScope applicationScope;

    @Inject
    ActivityHierarchyServer activityHierarchyServer;

    @Override public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            // TODO Crashlytics.start(this);
            // TODO Timber.plant(new CrashlyticsTree());
        }

        buildObjectGraphAndInject();

        registerActivityLifecycleCallbacks(activityHierarchyServer);
    }

    private void buildObjectGraphAndInject() {
        long start = System.nanoTime();

        ObjectGraph objectGraph = ObjectGraph.create(Modules.list(this));
        objectGraph.inject(this);
        applicationScope = Mortar.createRootScope(BuildConfig.DEBUG, objectGraph);

        long diff = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        Timber.i("Global object graph creation took %sms", diff);
    }

    public void rebuildOjectGraphAndInject() {
        Mortar.destroyRootScope(applicationScope);
        buildObjectGraphAndInject();
    }

    @Override
    public Object getSystemService(String name) {
        if (Mortar.isScopeSystemService(name)) {
            return applicationScope;
        }
        return super.getSystemService(name);
    }

    public static DiegottdieApp get(Context context) {
        return (DiegottdieApp) context.getApplicationContext();
    }

}
