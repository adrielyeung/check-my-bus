package com.adriel.checkmybus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.adriel.checkmybus.model.Route;
import com.adriel.checkmybus.model.RouteStop;
import com.adriel.checkmybus.utils.ValidationUtils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button searchButton;
    private EditText routeEditText;
    private TextView outboundTextView;
    private TextView inboundTextView;
    private Button outboundButton;
    private Button inboundButton;
    private LinearLayout companyLayout;
    private TextView selectCompanyTextView;

    private String company;
    private String routeNumber;
    private Map<String, Route> companyRouteMap;
    private int invalidCount;
    private String outboundDestination;
    private String inboundDestination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchButton = findViewById(R.id.searchButton);
        routeEditText = findViewById(R.id.routeEditText);
        outboundTextView = findViewById(R.id.outboundTextView);
        inboundTextView = findViewById(R.id.inboundTextView);
        selectCompanyTextView = findViewById(R.id.selectCompanyTextView);
        outboundButton = findViewById(R.id.outboundButton);
        inboundButton = findViewById(R.id.inboundButton);
        companyLayout = findViewById(R.id.companyLayout);

        searchButton.setOnClickListener(searchButtonListener);

        companyRouteMap = new HashMap<>();
    }

    private void routeCallbackListener(EtaReturnCallback<Route> routeCallback) {
        int returnCode = ValidationUtils.validateRouteCallback(routeCallback);
        switch (returnCode) {
            case Constants.CALLBACK_EXCEPTION:
                Toast.makeText(getApplicationContext(), getString(R.string.application_exception),
                        Toast.LENGTH_LONG).show();
                return;
            case Constants.CALLBACK_EMPTY_OUTPUT:
                if (--invalidCount == 0) {
                    selectCompanyTextView.setText(getString(R.string.wrong_route_number));
                }
                return;
            default:
                break;
        }

        companyRouteMap.put(routeCallback.getCompany(), routeCallback.getEtaReturnModel());
        selectCompanyTextView.setText(getString(R.string.select_company_description));
        addCompanyButton(routeCallback.getCompany());
    }

    private void addCompanyButton(String company) {
        Button button = new Button(getApplicationContext());
        button.setText(company.toUpperCase(Locale.ROOT));
        EtaReturnListener<EtaReturnCallback<RouteStop>> routeStopCallbackListener =
                this::routeStopCallbackListener;
        switch (company) {
            case Constants.KMB_COMPANY:
                button.setOnClickListener(view -> {
                    resetRoute(false, company);
                    // 2 - Determine if route is circular (only outbound, no inbound stops)
                    KmbEtaServiceImpl kmbEtaService = KmbEtaServiceImpl.getInstance();
                    kmbEtaService.getAllStopsByRoute(routeNumber, getString(R.string.direction_inbound),
                            routeStopCallbackListener);
                    });
                break;
            case Constants.CTB_COMPANY:
            case Constants.NWFB_COMPANY:
                button.setOnClickListener(view -> {
                    resetRoute(false, company);
                    CtbEtaServiceImpl ctbEtaService = CtbEtaServiceImpl.getInstance();
                    ctbEtaService.getAllStopsByRoute(company, routeNumber, getString(R.string.direction_inbound),
                            routeStopCallbackListener);
                });
                break;
            default:
                break;
        }

        button.setBackgroundTintList(AppCompatResources.getColorStateList(
                getApplicationContext(), R.color.purple_500));
        button.setTextColor(getResources().getColor(R.color.white));

        LinearLayout linearLayout = findViewById(R.id.companyLayout);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                Constants.LAYOUT_ZERO_DP,
                LinearLayout.LayoutParams.MATCH_PARENT,
                Constants.LAYOUT_WEIGHT_ONE);
        layoutParams.leftMargin = Constants.LAYOUT_MARGIN_FIVE_DP;
        layoutParams.rightMargin = Constants.LAYOUT_MARGIN_FIVE_DP;
        layoutParams.topMargin = Constants.LAYOUT_MARGIN_FIVE_DP;
        layoutParams.bottomMargin = Constants.LAYOUT_MARGIN_FIVE_DP;
        linearLayout.addView(button, layoutParams);
    }

    private void routeStopCallbackListener(EtaReturnCallback<RouteStop> routeStopCallback) {
        Route companyRoute = companyRouteMap.get(routeStopCallback.getCompany());
        outboundDestination = (companyRoute == null) ? "" : companyRoute.getData().getDest_en();
        outboundTextView.setText(outboundDestination);
        outboundButton.setEnabled(true);
        outboundButton.setOnClickListener(outboundButtonListener);

        int returnCode = ValidationUtils.validateRouteStopCallback(routeStopCallback);
        switch (returnCode) {
            case Constants.CALLBACK_EXCEPTION:
                Toast.makeText(getApplicationContext(), getString(R.string.application_exception),
                        Toast.LENGTH_LONG).show();
                return;
            case Constants.CALLBACK_EMPTY_OUTPUT:
                return;
            default:
                break;
        }

        inboundDestination = (companyRoute == null) ? "" : companyRoute.getData().getOrig_en();
        inboundTextView.setText(inboundDestination);
        inboundButton.setEnabled(true);
        inboundButton.setOnClickListener(inboundButtonListener);
    }

    private void resetRoute(boolean isSearchButton, String company) {
        if (isSearchButton) {
            invalidCount = 0;
            companyLayout.removeAllViews();
            selectCompanyTextView.setText("");
        }
        this.company = company;
        outboundTextView.setText("");
        outboundButton.setEnabled(false);
        inboundTextView.setText("");
        inboundButton.setEnabled(false);
    }

    private void switchToSelectStopScreen(boolean outbound) {
        Bundle params = new Bundle();
        params.putString(getString(R.string.bundle_company),
                company);
        params.putString(getString(R.string.bundle_direction),
                outbound ? outboundDestination : inboundDestination);
        params.putString(getString(R.string.bundle_route_num),
                routeNumber);
        params.putString(getString(R.string.bundle_bound),
                outbound ? getString(R.string.direction_outbound) :
                getString(R.string.direction_inbound));

        Intent intent = new Intent(this, SelectStopActivity.class);
        intent.putExtras(params);

        startActivity(intent);
    }

    View.OnClickListener searchButtonListener = view -> {
        resetRoute(true, null);

        if (ValidationUtils.isEditTextEmpty(routeEditText)) {
            Toast.makeText(getApplicationContext(), getString(R.string.empty_input),
                    Toast.LENGTH_LONG).show();
            return;
        }

        routeNumber = routeEditText.getText().toString().toUpperCase(Locale.ROOT);

        KmbEtaServiceImpl kmbEtaService = KmbEtaServiceImpl.getInstance();
        CtbEtaServiceImpl ctbEtaService = CtbEtaServiceImpl.getInstance();

        // 1 - Get route by route number entered
        EtaReturnListener<EtaReturnCallback<Route>> routeCallbackListener =
                this::routeCallbackListener;
        kmbEtaService.getRouteByNumber(routeNumber, getString(R.string.direction_outbound), routeCallbackListener);
        invalidCount++;
        ctbEtaService.getRouteByNumber(Constants.CTB_COMPANY, routeNumber, routeCallbackListener);
        invalidCount++;
        // NWFB START
        ctbEtaService.getRouteByNumber(Constants.NWFB_COMPANY, routeNumber, routeCallbackListener);
        invalidCount++;
        // NWFB END
    };

    View.OnClickListener outboundButtonListener = view -> {
        switchToSelectStopScreen(true);
    };

    View.OnClickListener inboundButtonListener = view -> {
        switchToSelectStopScreen(false);
    };
}