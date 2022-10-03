package com.adriel.checkmybus.model;

public class RouteStopData {

    private String co;
    private String route;
    private String dir;
    private String bound;
    private String service_type;
    private String seq;
    private String stop;
    // Names are inserted by calling Stop API
    private String name_en;
    private String name_tc;
    private String name_sc;
    private String data_timestamp;

    public String getCo() {
        return co;
    }

    public void setCo(String co) {
        this.co = co;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getBound() {
        return bound;
    }

    public void setBound(String bound) {
        this.bound = bound;
    }

    public String getService_type() {
        return service_type;
    }

    public void setService_type(String service_type) {
        this.service_type = service_type;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

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

    public String getData_timestamp() {
        return data_timestamp;
    }

    public void setData_timestamp(String data_timestamp) {
        this.data_timestamp = data_timestamp;
    }

}
