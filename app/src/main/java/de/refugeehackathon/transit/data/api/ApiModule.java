package de.refugeehackathon.transit.data.api;

import android.util.Log;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import de.refugeehackathon.transit.BuildConfig;
import retrofit.MoshiConverterFactory;
import retrofit.Retrofit;

public final class ApiModule {

    public PoiService providePoisService() {
        return createRetrofit(BuildConfig.API_BASE_URL)
                .create(PoiService.class);
    }

    private Retrofit createRetrofit(String baseUrl) {
        OkHttpClient httpClient = new OkHttpClient();
        if (BuildConfig.DEBUG) {
            httpClient.interceptors().add(new LoggingInterceptor());
        }
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(MoshiConverterFactory.create())
                .client(httpClient)
                .build();
    }

    private class LoggingInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            long t1 = System.nanoTime();
            Log.i(getClass().getName(), String.format("Sending request %s on %s%n%s",
                    request.url(), chain.connection(), request.headers()));

            Response response = chain.proceed(request);

            long t2 = System.nanoTime();
            Log.i(getClass().getName(), String.format("Received response for %s in %.1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers()));

            return response;
        }

    }

}
