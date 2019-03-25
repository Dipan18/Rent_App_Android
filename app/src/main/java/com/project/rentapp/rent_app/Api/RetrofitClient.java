package com.project.rentapp.rent_app.Api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "http://10.0.2.2:8181/rent_app_API/api/";
    private static RetrofitClient mInstance;
    private Retrofit retrofit;

    private RetrofitClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized RetrofitClient getmInstance() {
        if (mInstance == null) {
            mInstance = new RetrofitClient();
        }

        return mInstance;
    }

    public Api getApi() {
        return retrofit.create(Api.class);
    }
}
