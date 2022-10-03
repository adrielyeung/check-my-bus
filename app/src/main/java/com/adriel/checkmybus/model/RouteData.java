package com.adriel.checkmybus.model;

public class RouteData {

    private String co;
    private String route;
    private String bound;
    private String service_type;
    private String orig_en;
    private String orig_tc;
    private String orig_sc;
    private String dest_en;
    private String dest_tc;
    private String dest_sc;
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

    public String getOrig_en() {
        return orig_en;
    }

    public void setOrig_en(String orig_en) {
        this.orig_en = orig_en;
    }

    public String getOrig_tc() {
        return orig_tc;
    }

    public void setOrig_tc(String orig_tc) {
        this.orig_tc = orig_tc;
    }

    public String getOrig_sc() {
        return orig_sc;
    }

    public void setOrig_sc(String orig_sc) {
        this.orig_sc = orig_sc;
    }

    public String getDest_en() {
        return dest_en;
    }

    public void setDest_en(String dest_en) {
        this.dest_en = dest_en;
    }

    public String getDest_tc() {
        return dest_tc;
    }

    public void setDest_tc(String dest_tc) {
        this.dest_tc = dest_tc;
    }

    public String getDest_sc() {
        return dest_sc;
    }

    public void setDest_sc(String dest_sc) {
        this.dest_sc = dest_sc;
    }

    public String getData_timestamp() {
        return data_timestamp;
    }

    public void setData_timestamp(String data_timestamp) {
        this.data_timestamp = data_timestamp;
    }

}
