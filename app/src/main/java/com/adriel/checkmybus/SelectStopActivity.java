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
import com.adriel.checkmybus.model.StopData;
import com.adriel.checkmybus.utils.JointOperationUtils;
import com.adriel.checkmybus.utils.ValidationUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public class SelectStopActivity extends AppCompatActivity {

    private TextView companyTextView;
    private TextView routeNumberTextView;
    private TextView routeDirectionTextView;

    private String company;
    private String routeNumber;
    private String bound;
    private String direction;

    private Bundle params;

    private Map<String, StopData> stopIdDataMap;
    private Map<String, StopData> jointStopIdDataMap;
    private Map<String, String> jointOpStopIdMap;
    private List<RouteStopData> routeStopList;
    private List<RouteStopData> jointRouteStopList;
    private int stopCount;
    private int jointStopCount;

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
            case Constants.CTB_COMPANY:
            case Constants.NWFB_COMPANY:
                CtbEtaServiceImpl ctbEtaService = CtbEtaServiceImpl.getInstance();
                // 2 - Find all stop IDs of route and direction chosen
                ctbEtaService.getAllStopsByRoute(company, routeNumber, bound,
                        routeStopCallbackListener);
                break;
            case Constants.KMB_COMPANY:
                KmbEtaServiceImpl kmbEtaService = KmbEtaServiceImpl.getInstance();
                kmbEtaService.getAllStopsByRoute(routeNumber, bound,
                        routeStopCallbackListener);
                break;
            default:
                // Jointly-operated routes, need to call both companies' APIs to get both stop IDs
                // For cross-harbour routes, KMB vs CTB/NWFB outbound / inbound directions are opposite
                kmbEtaService = KmbEtaServiceImpl.getInstance();
                kmbEtaService.getAllStopsByRoute(routeNumber, bound,
                        routeStopCallbackListener);
        }
    }

    private void jointRouteStopCallbackListener(EtaReturnCallback<RouteStop> routeStopCallback) {
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

        jointStopIdDataMap = new HashMap<>();
        jointRouteStopList = routeStopCallback.getEtaReturnModel().getData();
        for (RouteStopData routeStop : jointRouteStopList) {
            String stopId = routeStop.getStop();
            jointStopIdDataMap.put(stopId, null);
        }
        // Call for stop data for each unique stop ID to avoid duplicate calls
        // stopCount to keep track of how many calls made to Stop API
        jointStopCount = jointStopIdDataMap.size();
        for (Map.Entry<String, StopData> entry : jointStopIdDataMap.entrySet()) {
            EtaReturnListener<EtaReturnCallback<Stop>> jointStopCallbackListener =
                    this::jointStopCallbackListener;
            CtbEtaServiceImpl ctbEtaService = CtbEtaServiceImpl.getInstance();
            ctbEtaService.getStopNameById(JointOperationUtils.getCtbNwfbFromJointOpRoute(company),
                    entry.getKey(), jointStopCallbackListener);
        }
    }

    private void jointStopCallbackListener(EtaReturnCallback<Stop> stopCallback) {
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

        jointStopIdDataMap.put(stopCallback.getEtaReturnModel().getData().getStop(),
                stopCallback.getEtaReturnModel().getData());

        if (--jointStopCount == 0) {
            // If companies' route stop list is not the same size, try to match stop
            // according to location (latitude)
            matchStopId();
            addStopButtons();
        }
    }

    private void matchStopId() {
        int jointCount = 0;
        for (RouteStopData routeStop : routeStopList) {
            StopData kmbStopData = stopIdDataMap.get(routeStop.getStop());
            if (kmbStopData != null && jointCount < jointRouteStopList.size()) {
                StopData ctbStopData = jointStopIdDataMap.get(jointRouteStopList.get(jointCount++).getStop());
                if (ctbStopData != null) {
                    double kmbLat = Double.parseDouble(kmbStopData.getLat());
                    double kmbLong = Double.parseDouble(kmbStopData.getLong());
                    double ctbLat = Double.parseDouble(ctbStopData.getLat());
                    double ctbLong = Double.parseDouble(ctbStopData.getLong());

                    while ((Math.abs(kmbLat - ctbLat) >= Constants.LOCATION_DIFF_TOLERANCE ||
                            Math.abs(kmbLong - ctbLong) >= Constants.LOCATION_DIFF_TOLERANCE) &&
                            jointCount < jointRouteStopList.size()) {
                        ctbStopData = jointStopIdDataMap.get(jointRouteStopList.get(jointCount++).getStop());
                        ctbLat = (ctbStopData == null) ? kmbLat : Double.parseDouble(ctbStopData.getLat());
                        ctbLong = (ctbStopData == null) ? ctbLong : Double.parseDouble(ctbStopData.getLong());
                    }

                    if (ctbStopData != null && Math.abs(kmbLat - ctbLat) <= Constants.LOCATION_DIFF_TOLERANCE) {
                        jointOpStopIdMap.put(kmbStopData.getStop(), ctbStopData.getStop());
                    }
                }

                if (jointCount >= jointRouteStopList.size()) {
                    break;
                }
            }
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
        stopIdDataMap = new HashMap<>();
        if (JointOperationUtils.isJointOpRoute(company)) {
            jointOpStopIdMap = new HashMap<>();
        }
        routeStopList = routeStopCallback.getEtaReturnModel().getData();

        for (RouteStopData routeStop : routeStopList) {
            String stopId = routeStop.getStop();
            stopIdDataMap.put(stopId, null);
            if (JointOperationUtils.isJointOpRoute(company)) {
                jointOpStopIdMap.put(stopId, null);
            }
        }

        // Call for stop name for each unique stop ID to avoid duplicate calls
        // stopCount to keep track of how many calls made to Stop API
        stopCount = stopIdDataMap.size();
        for (Map.Entry<String, StopData> entry : stopIdDataMap.entrySet()) {
            EtaReturnListener<EtaReturnCallback<Stop>> stopCallbackListener =
                    this::stopCallbackListener;
            switch (company) {
                case Constants.CTB_COMPANY:
                case Constants.NWFB_COMPANY:
                    // 3 - Get stop name by stop ID
                    CtbEtaServiceImpl ctbEtaService = CtbEtaServiceImpl.getInstance();
                    ctbEtaService.getStopNameById(company, entry.getKey(), stopCallbackListener);
                    break;
                case Constants.KMB_COMPANY:
                default:
                    KmbEtaServiceImpl etaService = KmbEtaServiceImpl.getInstance();
                    etaService.getStopNameById(entry.getKey(), stopCallbackListener);
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

        stopIdDataMap.put(stopCallback.getEtaReturnModel().getData().getStop(),
                stopCallback.getEtaReturnModel().getData());

        if (--stopCount == 0) {
            // Call for stop ID mapping for jointly-operated routes
            if (JointOperationUtils.isJointOpRoute(company)) {
                EtaReturnListener<EtaReturnCallback<RouteStop>> jointRouteStopCallbackListener =
                        this::jointRouteStopCallbackListener;
                CtbEtaServiceImpl ctbEtaService = CtbEtaServiceImpl.getInstance();
                String ctbBound = bound;
                if (Pattern.matches(Constants.CROSS_HARBOUR_ROUTES, routeNumber)) {
                    ctbBound = bound.equals(getString(R.string.direction_outbound)) ?
                            getString(R.string.direction_inbound) : getString(R.string.direction_outbound);
                }
                ctbEtaService.getAllStopsByRoute(JointOperationUtils.getCtbNwfbFromJointOpRoute(company),
                        routeNumber, ctbBound, jointRouteStopCallbackListener);
            } else {
                addStopButtons();
            }
        }
    }

    private void addStopButtons() {
        for (RouteStopData routeStopData : routeStopList) {
            String stopId = routeStopData.getStop();
            String stopName = (stopIdDataMap.containsKey(stopId) && stopIdDataMap.get(stopId) != null) ?
                    stopIdDataMap.get(stopId).getName_en() : "";
            String stopSeq = routeStopData.getSeq();
            Button button = new Button(getApplicationContext());
            button.setText(String.format("%s. %s", stopSeq, stopName));
            button.setOnClickListener(view -> {
                params.putString(getString(R.string.bundle_stop_name), stopName);
                params.putString(getString(R.string.bundle_stop_id), stopId);
                params.putString(getString(R.string.bundle_stop_seq), stopSeq);
                if (jointOpStopIdMap != null && jointOpStopIdMap.containsKey(stopId)) {
                    String jointStopId = jointOpStopIdMap.get(stopId);
                    params.putString(getString(R.string.bundle_joint_stop_id), jointStopId);
                    params.putString(getString(R.string.bundle_joint_stop_name),
                            (jointStopIdDataMap.containsKey(jointStopId) &&
                                    jointStopIdDataMap.get(jointStopId) != null) ?
                                    jointStopIdDataMap.get(jointStopId).getName_en() : "");
                }

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