package com.adriel.checkmybus.model;

import java.util.List;

public class RouteStop extends EtaReturnModel {

    private List<RouteStopData> data;

    public List<RouteStopData> getData() {
        return data;
    }

    public void setData(List<RouteStopData> data) {
        this.data = data;
    }

}
