package com.adriel.checkmybus.callback;

import androidx.annotation.NonNull;

import com.adriel.checkmybus.model.EtaReturnModel;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EtaReturnCallback<T extends EtaReturnModel> implements Callback<T> {

    private String company;
    private T etaReturnModel;
    private ResponseBody errorBody;
    private Throwable throwable;
    private EtaReturnListener<EtaReturnCallback<T>> listener;

    public EtaReturnCallback(EtaReturnListener<EtaReturnCallback<T>> listener, String company) {
        this.listener = listener;
        this.company = company;
    }

    @Override
    public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
        if (response.isSuccessful()) {
            etaReturnModel = response.body();
        } else {
            errorBody = response.errorBody();
        }

        listener.callback(this);
    }

    @Override
    public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
        throwable = t;
        listener.callback(this);
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public T getEtaReturnModel() {
        return etaReturnModel;
    }

    public void setEtaReturnModel(T etaReturnModel) {
        this.etaReturnModel = etaReturnModel;
    }

    public ResponseBody getErrorBody() {
        return errorBody;
    }

    public void setErrorBody(ResponseBody errorBody) {
        this.errorBody = errorBody;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public EtaReturnListener<EtaReturnCallback<T>> getListener() {
        return listener;
    }

    public void setListener(EtaReturnListener<EtaReturnCallback<T>> listener) {
        this.listener = listener;
    }
}
