package com.adriel.checkmybus.utils;

import android.widget.EditText;

import androidx.annotation.NonNull;

import com.adriel.checkmybus.callback.EtaReturnCallback;
import com.adriel.checkmybus.constants.Constants;
import com.adriel.checkmybus.model.Route;
import com.adriel.checkmybus.model.RouteStop;
import com.adriel.checkmybus.model.Stop;
import com.adriel.checkmybus.model.StopETA;

public class ValidationUtils {

    public static boolean isEditTextEmpty(EditText editText) {
        return (editText.getText() == null || editText.getText().toString().isEmpty());
    }

    public static int validateRouteCallback(@NonNull EtaReturnCallback<Route> routeCallback) {
        if (isInvalidCallback(routeCallback)) {
            return Constants.CALLBACK_EXCEPTION;
        }

        if (routeCallback.getEtaReturnModel().getData() == null ||
                routeCallback.getEtaReturnModel().getData().getRoute() == null) {
            return Constants.CALLBACK_EMPTY_OUTPUT;
        }
        return Constants.CALLBACK_NO_ERROR;
    }

    public static int validateRouteStopCallback(@NonNull EtaReturnCallback<RouteStop> routeStopCallback) {
        if (isInvalidCallback(routeStopCallback)) {
            return Constants.CALLBACK_EXCEPTION;
        }

        if (routeStopCallback.getEtaReturnModel().getData() == null ||
                routeStopCallback.getEtaReturnModel().getData().isEmpty()) {
            return Constants.CALLBACK_EMPTY_OUTPUT;
        }
        return Constants.CALLBACK_NO_ERROR;
    }

    public static int validateStopCallback(@NonNull EtaReturnCallback<Stop> stopCallback) {
        if (isInvalidCallback(stopCallback)) {
            return Constants.CALLBACK_EXCEPTION;
        }

        if (stopCallback.getEtaReturnModel().getData() == null ||
                stopCallback.getEtaReturnModel().getData().getStop() == null) {
            return Constants.CALLBACK_EMPTY_OUTPUT;
        }
        return Constants.CALLBACK_NO_ERROR;
    }

    public static int validateStopEtaCallback(@NonNull EtaReturnCallback<StopETA> stopEtaCallback) {
        if (isInvalidCallback(stopEtaCallback)) {
            return Constants.CALLBACK_EXCEPTION;
        }

        if (stopEtaCallback.getEtaReturnModel().getData() == null ||
                stopEtaCallback.getEtaReturnModel().getData().isEmpty()) {
            return Constants.CALLBACK_EMPTY_OUTPUT;
        }
        return Constants.CALLBACK_NO_ERROR;
    }

    private static boolean isInvalidCallback(@NonNull EtaReturnCallback<?> callback) {
        return (callback.getEtaReturnModel() == null ||
                callback.getErrorBody() != null ||
                callback.getThrowable() != null);
    }
}
