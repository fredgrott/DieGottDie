package com.grottworkshop.diegottdie.data;

import android.app.Application;
import android.content.SharedPreferences;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;
import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import timber.log.Timber;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by fgrott on 9/3/2014.
 */
@Module(
        //includes = ApiModule.class,
        complete = false,
        library = true
)
public final class DataModule {
    /**
     * The DISK _ cACHE _ sIZE.
     */
    static final int DISK_CACHE_SIZE = 50 * 1024 * 1024; // 50MB

    /**
     * Provide shared preferences.
     *
     * @param app the app
     * @return the shared preferences
     */
    @Provides @Singleton
    SharedPreferences provideSharedPreferences(Application app) {
        return app.getSharedPreferences("Diegottdie", MODE_PRIVATE);
    }

    /**
     * Provide ok http client.
     *
     * @param app the app
     * @return the ok http client
     */
    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Application app) {
        return createOkHttpClient(app);
    }

    /**
     * Create ok http client.
     *
     * @param app the app
     * @return the ok http client
     */
    static OkHttpClient createOkHttpClient(Application app) {
        OkHttpClient client = new OkHttpClient();

        // Install an HTTP cache in the application cache directory.
        try {
            File cacheDir = new File(app.getCacheDir(), "http");
            Cache cache = new Cache(cacheDir, DISK_CACHE_SIZE);
            client.setCache(cache);
        } catch (IOException e) {
            Timber.e(e, "Unable to install disk cache.");
        }

        return client;
    }


}
