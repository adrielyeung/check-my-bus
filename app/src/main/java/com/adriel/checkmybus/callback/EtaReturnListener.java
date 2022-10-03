package com.adriel.checkmybus.callback;

@FunctionalInterface
public interface EtaReturnListener<BusModelCallback> {

    void callback(BusModelCallback busModelCallback);

}
