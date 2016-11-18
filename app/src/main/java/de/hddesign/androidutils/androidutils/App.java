package de.hddesign.androidutils.androidutils;

import android.app.Application;

import de.hddesign.androidutils.androidutils.network.RestService;
import de.hddesign.androidutils.androidutils.network.RestServiceFactory;


public class App extends Application {

    private RestService restService;

    @Override
    public void onCreate() {
        super.onCreate();

        restService = RestServiceFactory.create(this);
    }

    public RestService getRestService() {
        return restService;
    }
}
