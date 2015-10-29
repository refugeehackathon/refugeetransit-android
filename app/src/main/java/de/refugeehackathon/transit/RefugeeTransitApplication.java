package de.refugeehackathon.transit;

import android.app.Application;
import android.support.annotation.NonNull;

import de.refugeehackathon.transit.data.api.ApiModule;

public class RefugeeTransitApplication extends Application {

    private ApiModule mApiModule;

    public
    @NonNull
    ApiModule getApiModule() {
        if (mApiModule == null) {
            mApiModule = new ApiModule();
        }
        return mApiModule;
    }

}
