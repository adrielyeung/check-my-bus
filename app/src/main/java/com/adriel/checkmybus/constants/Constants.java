package com.adriel.checkmybus.constants;

public class Constants {

    // API Path
    public static final String KMB_ETA_PATH = "https://data.etabus.gov.hk/v1/transport/kmb/";
    public static final String CTB_ETA_PATH = "https://rt.data.gov.hk/v1.1/transport/citybus-nwfb/";
    public static final String CTB_COMPANY = "ctb";
    public static final String NWFB_COMPANY = "nwfb";
    public static final String KMB_COMPANY = "kmb";

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

}
