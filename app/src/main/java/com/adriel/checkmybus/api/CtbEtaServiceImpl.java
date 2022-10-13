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

public class CtbEtaServiceImpl {

    private static CtbEtaServiceImpl ctbEtaServiceImpl;
    private static CtbEtaService ctbEtaService;
    private static Retrofit ctbRetrofit;

    private CtbEtaServiceImpl() {
        ctbRetrofit = new Retrofit.Builder()
                .baseUrl(Constants.CTB_ETA_PATH)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ctbEtaService = ctbRetrofit.create(CtbEtaService.class);
    }

    public static CtbEtaServiceImpl getInstance() {
        if (ctbEtaServiceImpl == null)
            ctbEtaServiceImpl = new CtbEtaServiceImpl();
        return ctbEtaServiceImpl;
    }

    public void getRouteByNumber(String company, String routeNumber,
                                 EtaReturnListener<EtaReturnCallback<Route>>
                                         routeCallbackListener) {
        // 1 - Get origin / destination by route number and direction
        new Thread(() -> {
            Call<Route> routeCall = ctbEtaService.getRouteByNumber(company, routeNumber);

            EtaReturnCallback<Route> routeCallback = new EtaReturnCallback<>(routeCallbackListener,
                    company);
            routeCall.enqueue(routeCallback);
        }).start();
    }

    public void getAllStopsByRoute(String company, String routeNumber, String direction,
                                   EtaReturnListener<EtaReturnCallback<RouteStop>>
                                           routeStopCallbackListener) {
        // 2 - Get all stops by route number, direction
        new Thread(() -> {
            Call<RouteStop> routeStopCall = ctbEtaService.getAllStopsByRoute(company, routeNumber, direction);

            EtaReturnCallback<RouteStop> routeStopCallback = new EtaReturnCallback<>(
                    routeStopCallbackListener, company);
            routeStopCall.enqueue(routeStopCallback);
        }).start();
    }

    public void getStopNameById(String company, String stopId,
                                EtaReturnListener<EtaReturnCallback<Stop>> stopCallbackListener) {
        // 3 - Get stop name by stop ID
        new Thread(() -> {
            Call<Stop> stopCall = ctbEtaService.getStopNameById(stopId);

            EtaReturnCallback<Stop> stopCallback = new EtaReturnCallback<>(stopCallbackListener,
                    company);
            stopCall.enqueue(stopCallback);
        }).start();
    }

    public void getStopEtaById(String company, String stopId, String routeNumber,
                               EtaReturnListener<EtaReturnCallback<StopETA>> stopEtaCallbackListener) {
        // 4 - Get stop ETA by stop ID
        new Thread(() -> {
            Call<StopETA> stopCall = ctbEtaService.getStopEtaById(company, stopId, routeNumber);

            EtaReturnCallback<StopETA> stopCallback = new EtaReturnCallback<>(stopEtaCallbackListener,
                    company);
            stopCall.enqueue(stopCallback);
        }).start();
    }

}
