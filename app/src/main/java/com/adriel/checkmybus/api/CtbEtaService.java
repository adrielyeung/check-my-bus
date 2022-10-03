package com.adriel.checkmybus.api;

import com.adriel.checkmybus.model.Route;
import com.adriel.checkmybus.model.RouteStop;
import com.adriel.checkmybus.model.Stop;
import com.adriel.checkmybus.model.StopETA;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CtbEtaService {

    @GET("route/{company}/{routeNumber}")
    Call<Route> getRouteByNumber(@Path("company") String company,
                                 @Path("routeNumber") String routeNumber);

    @GET("route-stop/{company}/{routeNumber}/{bound}")
    Call<RouteStop> getAllStopsByRoute(@Path("company") String company,
                                       @Path("routeNumber") String routeNumber,
                                       @Path("bound") String bound);

    @GET("stop/{stopId}")
    Call<Stop> getStopNameById(@Path("stopId") String stopId);

    @GET("eta/{company}/{stopId}/{routeNumber}")
    Call<StopETA> getStopEtaById(@Path("company") String company,
                                 @Path("stopId") String stopId,
                                 @Path("routeNumber") String routeNumber
                                 );

}
