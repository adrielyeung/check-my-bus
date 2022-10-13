package com.adriel.checkmybus.model;

import com.adriel.checkmybus.constants.Constants;
import com.adriel.checkmybus.utils.DateUtils;

import java.text.ParseException;
import java.util.Date;

public class StopETAData {

    private String co;
    private String route;
    private String dir;
    private String service_type;
    private int seq;
    private String stop;
    private String dest_tc;
    private String dest_sc;
    private String dest_en;
    private int eta_seq;
    private String eta;
    private String rmk_tc;
    private String rmk_sc;
    private String rmk_en;
    private String data_timestamp;

    private Date etaDateTime;

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

    public String getService_type() {
        return service_type;
    }

    public void setService_type(String service_type) {
        this.service_type = service_type;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getStop() {
        return stop;
    }

    public void setStop(String stop) {
        this.stop = stop;
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

    public String getDest_en() {
        return dest_en;
    }

    public void setDest_en(String dest_en) {
        this.dest_en = dest_en;
    }

    public int getEta_seq() {
        return eta_seq;
    }

    public void setEta_seq(int eta_seq) {
        this.eta_seq = eta_seq;
    }

    public String getEta() {
        return eta;
    }

    public void setEta(String eta) {
        this.eta = eta;
    }

    public String getRmk_tc() {
        return rmk_tc;
    }

    public void setRmk_tc(String rmk_tc) {
        this.rmk_tc = rmk_tc;
    }

    public String getRmk_sc() {
        return rmk_sc;
    }

    public void setRmk_sc(String rmk_sc) {
        this.rmk_sc = rmk_sc;
    }

    public String getRmk_en() {
        return rmk_en;
    }

    public void setRmk_en(String rmk_en) {
        this.rmk_en = rmk_en;
    }

    public String getData_timestamp() {
        return data_timestamp;
    }

    public void setData_timestamp(String data_timestamp) {
        this.data_timestamp = data_timestamp;
    }

    public Date getEtaDateTime() throws ParseException {
        if (etaDateTime == null && eta != null && !eta.isEmpty()) {
            etaDateTime = DateUtils.convertStringToDate(eta, Constants.DATE_TIME_FORMATTER);
        }
        return etaDateTime;
    }

    public void setEtaDateTime(Date etaDateTime) {
        this.etaDateTime = etaDateTime;
    }

}
