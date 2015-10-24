package de.refugeehackathon.transit;

import android.app.Application;

import de.refugeehackathon.transit.data.api.ApiModule;

public class RefugeeTransitApplication extends Application {

    private ApiModule mApiModule;

    public ApiModule getApiModule() {
        if (mApiModule == null) {
            mApiModule = new ApiModule();
        }
        return mApiModule;
    }

}
