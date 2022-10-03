package com.adriel.checkmybus.model;

import java.util.List;

public class StopETA extends EtaReturnModel {

    private List<StopETAData> data;

    public List<StopETAData> getData() {
        return data;
    }

    public void setData(List<StopETAData> data) {
        this.data = data;
    }

}
