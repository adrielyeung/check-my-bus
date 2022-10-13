package com.adriel.checkmybus.constants;

public class Constants {

    // API Path
    public static final String KMB_ETA_PATH = "https://data.etabus.gov.hk/v1/transport/kmb/";
    public static final String CTB_ETA_PATH = "https://rt.data.gov.hk/v1.1/transport/citybus-nwfb/";

    // Company code
    public static final String CTB_COMPANY = "ctb";
    public static final String NWFB_COMPANY = "nwfb";
    public static final String KMB_COMPANY = "kmb";
    public static final String COMPANY_SEPARATOR = " / ";

    // Regex for jointly-operated routes
    public static final String JOINT_OPERATED_ROUTES =
            "^N?[1369][0-9][0-9][A-Za-z]?$|^S[0-9]+$|^R[0-9]+$|^X[0-9]+$";
    public static final String CROSS_HARBOUR_ROUTES =
            "^N?[1369][0-9][0-9][A-Za-z]?$";

    // Callback error codes
    public static final int CALLBACK_NO_ERROR = 0;
    public static final int CALLBACK_EMPTY_OUTPUT = 1;
    public static final int CALLBACK_EXCEPTION = 99;

    // Layout attributes
    public static final int LAYOUT_ZERO_DP = 0;
    public static final float LAYOUT_WEIGHT_ONE = 1f;
    public static final int LAYOUT_MARGIN_FIVE_DP = 5;

    // Datetime formatter
    public static final String DATE_TIME_FORMATTER = "yyyy-MM-dd'T'HH:mm:ssXXX";
    public static final String TIME_FORMATTER = "HH:mm";
    public static final String TIMEZONE_HONG_KONG = "Asia/Hong_Kong";

    // Numeric constants
    public static final double LOCATION_DIFF_TOLERANCE = 2E-3;

}
