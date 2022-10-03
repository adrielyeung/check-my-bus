package com.adriel.checkmybus;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import com.adriel.checkmybus.api.CtbEtaServiceImpl;
import com.adriel.checkmybus.api.KmbEtaServiceImpl;
import com.adriel.checkmybus.callback.EtaReturnCallback;
import com.adriel.checkmybus.callback.EtaReturnListener;
import com.adriel.checkmybus.constants.Constants;
import com.adriel.checkmybus.model.RouteStop;
import com.adriel.checkmybus.model.RouteStopData;
import com.adriel.checkmybus.model.Stop;
import com.adriel.checkmybus.utils.ValidationUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SelectStopActivity extends AppCompatActivity {

    private TextView companyTextView;
    private TextView routeNumberTextView;
    private TextView routeDirectionTextView;

    private String company;
    private String routeNumber;
    private String bound;
    private String direction;

    private Bundle params;

    private Map<String, String> stopIdNameMap;
    private List<RouteStopData> routeStopList;
    private int stopCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_stop);

        companyTextView = findViewById(R.id.companyTextView);
        routeNumberTextView = findViewById(R.id.routeNumberTextView);
        routeDirectionTextView = findViewById(R.id.routeDirectionTextView);

        params = getIntent().getExtras();
        company = params.getString(getString(R.string.bundle_company));
        companyTextView.setText(company.toUpperCase(Locale.ROOT));
        direction = params.getString(getString(R.string.bundle_direction));
        routeDirectionTextView.setText(direction);
        routeNumber = params.getString(getString(R.string.bundle_route_num));
        routeNumberTextView.setText(routeNumber);
        bound = params.getString(getString(R.string.bundle_bound));

        EtaReturnListener<EtaReturnCallback<RouteStop>> routeStopCallbackListener =
                this::routeStopCallbackListener;
        switch (company) {
            case Constants.KMB_COMPANY:
                KmbEtaServiceImpl kmbEtaService = KmbEtaServiceImpl.getInstance();
                // 2 - Find all stop IDs of route and direction chosen
                kmbEtaService.getAllStopsByRoute(routeNumber, bound,
                        routeStopCallbackListener);
                break;
            case Constants.CTB_COMPANY:
            case Constants.NWFB_COMPANY:
                CtbEtaServiceImpl ctbEtaService = CtbEtaServiceImpl.getInstance();
                ctbEtaService.getAllStopsByRoute(company, routeNumber, bound,
                        routeStopCallbackListener);
                break;
            default:
                break;
        }
    }

    private void routeStopCallbackListener(EtaReturnCallback<RouteStop> routeStopCallback) {
        int returnCode = ValidationUtils.validateRouteStopCallback(routeStopCallback);
        switch (returnCode) {
            case Constants.CALLBACK_EXCEPTION:
            case Constants.CALLBACK_EMPTY_OUTPUT:
                Toast.makeText(getApplicationContext(), getString(R.string.application_exception),
                        Toast.LENGTH_LONG).show();
                return;
            default:
                break;
        }

        // Add button for each stop (stopIdNameMap to store stop ID - name mapping,
        // and routeStopList to store ordering of stops (may have duplicated stop IDs if stopping
        // more than once, e.g. circular route))
        stopIdNameMap = new HashMap<>();
        routeStopList = routeStopCallback.getEtaReturnModel().getData();

        for (RouteStopData routeStop : routeStopList) {
            String stopId = routeStop.getStop();
            stopIdNameMap.put(stopId, "");
        }

        // Call for stop name for each unique stop ID to avoid duplicate calls
        // stopCount to keep track of how many calls made to Stop API
        stopCount = stopIdNameMap.size();
        for (Map.Entry<String, String> entry : stopIdNameMap.entrySet()) {
            EtaReturnListener<EtaReturnCallback<Stop>> stopCallbackListener =
                    this::stopCallbackListener;
            switch (company) {
                case Constants.KMB_COMPANY:
                    KmbEtaServiceImpl etaService = KmbEtaServiceImpl.getInstance();
                    // 3 - Get stop name by stop ID
                    etaService.getStopNameById(entry.getKey(), stopCallbackListener);
                    break;
                case Constants.CTB_COMPANY:
                case Constants.NWFB_COMPANY:
                    CtbEtaServiceImpl ctbEtaService = CtbEtaServiceImpl.getInstance();
                    ctbEtaService.getStopNameById(company, entry.getKey(), stopCallbackListener);
                    break;
                default:
                    break;
            }
        }
    }

    private void stopCallbackListener(EtaReturnCallback<Stop> stopCallback) {
        int returnCode = ValidationUtils.validateStopCallback(stopCallback);
        switch (returnCode) {
            case Constants.CALLBACK_EXCEPTION:
            case Constants.CALLBACK_EMPTY_OUTPUT:
                Toast.makeText(getApplicationContext(), getString(R.string.application_exception),
                        Toast.LENGTH_LONG).show();
                return;
            default:
                break;
        }

        stopIdNameMap.put(stopCallback.getEtaReturnModel().getData().getStop(),
                stopCallback.getEtaReturnModel().getData().getName_en());

        if (--stopCount == 0) {
            addStopButtons();
        }
    }

    private void addStopButtons() {
        for (RouteStopData routeStopData : routeStopList) {
            String stopID = routeStopData.getStop();
            String stopName = stopIdNameMap.get(stopID);
            String stopSeq = routeStopData.getSeq();
            Button button = new Button(getApplicationContext());
            button.setText(String.format("%s. %s", stopSeq, stopName));
            button.setOnClickListener(view -> {
                params.putString(getString(R.string.bundle_stop_name), stopName);
                params.putString(getString(R.string.bundle_stop_id), stopID);
                params.putString(getString(R.string.bundle_stop_seq), stopSeq);

                Intent intent = new Intent(this, SelectTimeActivity.class);
                intent.putExtras(params);
                startActivity(intent);
            });
            button.setBackgroundTintList(AppCompatResources.getColorStateList(
                    getApplicationContext(), R.color.purple_500));
            button.setTextColor(getResources().getColor(R.color.white));

            LinearLayout linearLayout = findViewById(R.id.stopLayout);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    Constants.LAYOUT_ZERO_DP,
                    Constants.LAYOUT_WEIGHT_ONE);
            layoutParams.leftMargin = Constants.LAYOUT_MARGIN_FIVE_DP;
            layoutParams.rightMargin = Constants.LAYOUT_MARGIN_FIVE_DP;
            layoutParams.topMargin = Constants.LAYOUT_MARGIN_FIVE_DP;
            layoutParams.bottomMargin = Constants.LAYOUT_MARGIN_FIVE_DP;
            linearLayout.addView(button, layoutParams);
        }
    }

}