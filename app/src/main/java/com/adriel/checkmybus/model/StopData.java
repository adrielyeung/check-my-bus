package com.adriel.checkmybus.model;

public class StopData {

    private String stop;
    private String name_en;
    private String name_tc;
    private String name_sc;
    private String lat;
    private String _long;
    private String data_timestamp;

    public String getStop() {
        return stop;
    }

    public void setStop(String stop) {
        this.stop = stop;
    }

    public String getName_en() {
        return name_en;
    }

    public void setName_en(String name_en) {
        this.name_en = name_en;
    }

    public String getName_tc() {
        return name_tc;
    }

    public void setName_tc(String name_tc) {
        this.name_tc = name_tc;
    }

    public String getName_sc() {
        return name_sc;
    }

    public void setName_sc(String name_sc) {
        this.name_sc = name_sc;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLong() {
        return _long;
    }

    public void setLong(String _long) {
        this._long = _long;
    }

    public String getData_timestamp() {
        return data_timestamp;
    }

    public void setData_timestamp(String data_timestamp) {
        this.data_timestamp = data_timestamp;
    }

}
