package com.adriel.checkmybus.api;

import com.adriel.checkmybus.constants.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FirebaseServiceImpl {

    private static FirebaseServiceImpl firebaseServiceImpl;
    private static FirebaseService firebaseService;
    private static Retrofit firebaseRetrofit;

    private FirebaseServiceImpl() {
        firebaseRetrofit = new Retrofit.Builder()
                .baseUrl(Constants.CTB_ETA_PATH)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        firebaseService = firebaseRetrofit.create(FirebaseService.class);
    }

    public static FirebaseServiceImpl getInstance() {
        if (firebaseServiceImpl == null)
            firebaseServiceImpl = new FirebaseServiceImpl();
        return firebaseServiceImpl;
    }

    public void addNotificationJob() {

    }

}
