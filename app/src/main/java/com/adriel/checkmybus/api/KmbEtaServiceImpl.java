package com.adriel.checkmybus.api;

import com.adriel.checkmybus.callback.EtaReturnCallback;
import com.adriel.checkmybus.callback.EtaReturnListener;
import com.adriel.checkmybus.constants.Constants;
import com.adriel.checkmybus.model.Route;
import com.adriel.checkmybus.model.RouteStop;
import com.adriel.checkmybus.model.Stop;
import com.adriel.checkmybus.model.StopETA;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class KmbEtaServiceImpl {

    private static KmbEtaServiceImpl kmbEtaServiceImpl;
    private static KmbEtaService kmbEtaService;
    private static CtbEtaService ctbEtaService;
    private static Retrofit kmbRetrofit;
    private static Retrofit ctbRetrofit;

    private KmbEtaServiceImpl() {
        kmbRetrofit = new Retrofit.Builder()
                .baseUrl(Constants.KMB_ETA_PATH)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        kmbEtaService = kmbRetrofit.create(KmbEtaService.class);

        ctbRetrofit = new Retrofit.Builder()
                .baseUrl(Constants.CTB_ETA_PATH)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ctbEtaService = ctbRetrofit.create(CtbEtaService.class);
    }

    public static KmbEtaServiceImpl getInstance() {
        if (kmbEtaServiceImpl == null)
            kmbEtaServiceImpl = new KmbEtaServiceImpl();
        return kmbEtaServiceImpl;
    }

    public void getRouteByNumber(String routeNumber, String direction,
                                 EtaReturnListener<EtaReturnCallback<Route>>
                                         routeCallbackListener) {
        // 1 - Get origin / destination by route number and direction
        new Thread(() -> {
            Call<Route> routeCall = kmbEtaService.getRouteByNumber(routeNumber, direction);

            EtaReturnCallback<Route> routeCallback = new EtaReturnCallback<>(routeCallbackListener,
                    Constants.KMB_COMPANY);
            routeCall.enqueue(routeCallback);
        }).start();
    }

    public void getAllStopsByRoute(String routeNumber, String direction,
                                   EtaReturnListener<EtaReturnCallback<RouteStop>>
                                           routeStopCallbackListener) {
        // 2 - Get all stops by route number, direction
        new Thread(() -> {
            Call<RouteStop> routeStopCall = kmbEtaService.getAllStopsByRoute(routeNumber, direction);

            EtaReturnCallback<RouteStop> routeStopCallback = new EtaReturnCallback<>(
                    routeStopCallbackListener, Constants.KMB_COMPANY);
            routeStopCall.enqueue(routeStopCallback);
        }).start();
    }

    public void getStopNameById(String stopId,
                                EtaReturnListener<EtaReturnCallback<Stop>> stopCallbackListener) {
        // 3 - Get stop name by stop ID
        new Thread(() -> {
            Call<Stop> stopCall = kmbEtaService.getStopNameById(stopId);

            EtaReturnCallback<Stop> stopCallback = new EtaReturnCallback<>(stopCallbackListener,
                    Constants.KMB_COMPANY);
            stopCall.enqueue(stopCallback);
        }).start();
    }

    public void getStopEtaById(String stopId,
                               EtaReturnListener<EtaReturnCallback<StopETA>> stopEtaCallbackListener) {
        // 4 - Get stop ETA by stop ID
        new Thread(() -> {
            Call<StopETA> stopCall = kmbEtaService.getStopEtaById(stopId);

            EtaReturnCallback<StopETA> stopCallback = new EtaReturnCallback<>(
                    stopEtaCallbackListener, Constants.KMB_COMPANY);
            stopCall.enqueue(stopCallback);
        }).start();
    }

}
