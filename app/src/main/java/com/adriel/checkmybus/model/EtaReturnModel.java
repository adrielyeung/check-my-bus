package com.adriel.checkmybus.model;

public class EtaReturnModel {

    private String type;
    private String version;
    private String generated_timestamp;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getGenerated_timestamp() {
        return generated_timestamp;
    }

    public void setGenerated_timestamp(String generated_timestamp) {
        this.generated_timestamp = generated_timestamp;
    }

}
