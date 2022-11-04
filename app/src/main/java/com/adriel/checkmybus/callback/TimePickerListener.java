package com.adriel.checkmybus.callback;

@FunctionalInterface
public interface TimePickerListener {

    void callback(int hourPicked, int minPicked);

}
