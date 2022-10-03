package com.adriel.checkmybus.api;

import com.adriel.checkmybus.model.Route;
import com.adriel.checkmybus.model.RouteStop;
import com.adriel.checkmybus.model.Stop;
import com.adriel.checkmybus.model.StopETA;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface KmbEtaService {

    @GET("route/{routeNumber}/{bound}/1")
    Call<Route> getRouteByNumber(@Path("routeNumber") String routeNumber,
                                 @Path("bound") String bound);

    @GET("route-stop/{routeNumber}/{bound}/1")
    Call<RouteStop> getAllStopsByRoute(@Path("routeNumber") String routeNumber,
                                       @Path("bound") String bound);

    @GET("stop/{stopId}")
    Call<Stop> getStopNameById(@Path("stopId") String stopId);

    @GET("stop-eta/{stopId}")
    Call<StopETA> getStopEtaById(@Path("stopId") String stopId);

}
