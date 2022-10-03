package com.adriel.checkmybus;

import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.adriel.checkmybus.api.CtbEtaServiceImpl;
import com.adriel.checkmybus.api.KmbEtaServiceImpl;
import com.adriel.checkmybus.callback.EtaReturnCallback;
import com.adriel.checkmybus.callback.EtaReturnListener;
import com.adriel.checkmybus.constants.Constants;
import com.adriel.checkmybus.model.StopETA;
import com.adriel.checkmybus.model.StopETAData;
import com.adriel.checkmybus.utils.DateUtils;
import com.adriel.checkmybus.utils.ValidationUtils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

public class SelectTimeActivity extends AppCompatActivity {

    private TextView companyTextView;
    private TextView routeNumberTextView;
    private TextView routeDirectionTextView;
    private TextView busStopTextView;
    private TextView etaTextView;
    private Button refreshButton;
    private RadioGroup leftRadioGroup;
    private RadioGroup rightRadioGroup;
    private RadioGroup.OnCheckedChangeListener leftRadioGroupListener;
    private RadioGroup.OnCheckedChangeListener rightRadioGroupListener;

    private String company;
    private String routeNumber;
    private String direction;
    private String bound;
    private String busStopName;
    private String busStopId;
    private String busStopSeq;

    private Bundle params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_time);

        companyTextView = findViewById(R.id.companySelectTimeTextView);
        routeNumberTextView = findViewById(R.id.routeNumberSelectTimeTextView);
        routeDirectionTextView = findViewById(R.id.routeDirectionSelectTimeTextView);
        busStopTextView = findViewById(R.id.busStopTextView);
        etaTextView = findViewById(R.id.etaTextView);
        refreshButton = findViewById(R.id.refreshButton);
        leftRadioGroup = findViewById(R.id.leftRadioGroup);
        rightRadioGroup = findViewById(R.id.rightRadioGroup);
        leftRadioGroup.clearCheck();
        rightRadioGroup.clearCheck();
        // Reset all radio buttons from other group
        leftRadioGroupListener = (radioGroup, checkedId) -> {
            if (checkedId != -1) {
                rightRadioGroup.setOnCheckedChangeListener(null);
                rightRadioGroup.clearCheck();
                rightRadioGroup.setOnCheckedChangeListener(rightRadioGroupListener);
            }
        };

        rightRadioGroupListener = (radioGroup, checkedId) -> {
            if (checkedId != -1) {
                leftRadioGroup.setOnCheckedChangeListener(null);
                leftRadioGroup.clearCheck();
                leftRadioGroup.setOnCheckedChangeListener(leftRadioGroupListener);
            }
        };
        leftRadioGroup.setOnCheckedChangeListener(leftRadioGroupListener);
        rightRadioGroup.setOnCheckedChangeListener(rightRadioGroupListener);

        params = getIntent().getExtras();
        company = params.getString(getString(R.string.bundle_company));
        companyTextView.setText(company.toUpperCase(Locale.ROOT));
        direction = params.getString(getString(R.string.bundle_direction));
        bound = params.getString(getString(R.string.bundle_bound));
        routeNumber = params.getString(getString(R.string.bundle_route_num));
        routeNumberTextView.setText(routeNumber);
        busStopName = params.getString(getString(R.string.bundle_stop_name));
        busStopId = params.getString(getString(R.string.bundle_stop_id));
        busStopSeq = params.getString(getString(R.string.bundle_stop_seq));
        busStopTextView.setText(String.format("%s. %s", busStopSeq, busStopName));

        refreshButton.setOnClickListener(view -> getNextEta(busStopId));

        getNextEta(busStopId);
    }

    private void getNextEta(String stopId) {
        // 4 - Find ETA by stop ID
        EtaReturnListener<EtaReturnCallback<StopETA>> stopEtaCallbackListener =
                this::stopEtaCallbackListener;

        switch (company) {
            case Constants.KMB_COMPANY:
                KmbEtaServiceImpl kmbEtaService = KmbEtaServiceImpl.getInstance();
                kmbEtaService.getStopEtaById(stopId, stopEtaCallbackListener);
                break;
            case Constants.CTB_COMPANY:
            case Constants.NWFB_COMPANY:
                CtbEtaServiceImpl ctbEtaService = CtbEtaServiceImpl.getInstance();
                ctbEtaService.getStopEtaById(company, stopId, routeNumber, stopEtaCallbackListener);
                break;
            default:
                break;
        }

    }

    private void stopEtaCallbackListener(EtaReturnCallback<StopETA> stopEtaCallback) {
        int returnCode = ValidationUtils.validateStopEtaCallback(stopEtaCallback);
        switch (returnCode) {
            case Constants.CALLBACK_EXCEPTION:
                Toast.makeText(getApplicationContext(), getString(R.string.application_exception),
                        Toast.LENGTH_LONG).show();
                return;
            case Constants.CALLBACK_EMPTY_OUTPUT:
                etaTextView.setText(getString(R.string.eta_not_found));
                return;
            default:
                break;
        }

        // Each stop has multiple routes, each route has multiple departures
        // For terminus, each route will have ETA of both directions
        // Get selected route number, direction, stop seq number closest to and smaller than selected
        // (in CTB ETA, if more than 1 possible routes for a number, stop seq number in RouteStop
        // API may be greater than ETA API since it contains some unused stops),
        // and smallest ETA seq
        StopETAData selectedStopETA = null;
        for (StopETAData stopETA : stopEtaCallback.getEtaReturnModel().getData()) {
            if (!routeNumber.equals(stopETA.getRoute()) ||
                !bound.substring(0, 1).equalsIgnoreCase(stopETA.getDir()) ||
                stopETA.getSeq() > Integer.parseInt(busStopSeq) ||
                stopETA.getEta() == null || stopETA.getEta().isEmpty()) {
                continue;
            }

            // Get a larger stop seq (closer to selected stop seq)
            // or a smaller ETA seq (earlier) only if same stop seq
            if (selectedStopETA == null ||
                stopETA.getSeq() > selectedStopETA.getSeq() ||
                (stopETA.getSeq() == selectedStopETA.getSeq() &&
                    stopETA.getEta_seq() < selectedStopETA.getEta_seq())) {
                selectedStopETA = stopETA;
            }
        }

        if (selectedStopETA == null) {
            etaTextView.setText(getString(R.string.eta_not_found));
            return;
        }

        Date earliestEta;
        try {
            earliestEta = selectedStopETA.getEtaDateTime();
        } catch (ParseException e) {
            etaTextView.setText(getString(R.string.application_exception));
            return;
        }

        long timeFromNowMs = Math.abs(earliestEta.getTime() - Calendar.getInstance().getTime().getTime());
        long timeFromNowMins = TimeUnit.MINUTES.convert(timeFromNowMs, TimeUnit.MILLISECONDS);

        etaTextView.setText(String.format(getString(R.string.eta_description),
                DateUtils.convertDateToString(earliestEta, Constants.TIME_FORMATTER), timeFromNowMins));
        routeDirectionTextView.setText(selectedStopETA.getDest_en());

    }

}