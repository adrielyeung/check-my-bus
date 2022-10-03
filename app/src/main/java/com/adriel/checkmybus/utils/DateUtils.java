package com.adriel.checkmybus.utils;

import com.adriel.checkmybus.constants.Constants;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {

    public static String convertDateToString(Date datetime, String formatter) {
        // Create an instance of SimpleDateFormat used for formatting
        // the string representation of date according to the chosen pattern
        DateFormat df = new SimpleDateFormat(formatter, Locale.TRADITIONAL_CHINESE);
        df.setTimeZone(TimeZone.getTimeZone(Constants.TIMEZONE_HONG_KONG));

        // Using DateFormat format method we can create a string
        // representation of a date with the defined format.
        return df.format(datetime);
    }

    public static Date convertStringToDate(String datetimeString, String formatter) throws ParseException {
        // Create an instance of SimpleDateFormat used for formatting
        // the string representation of date according to the chosen pattern
        DateFormat df = new SimpleDateFormat(formatter, Locale.TRADITIONAL_CHINESE);

        // Using DateFormat format method we can create a string
        // representation of a date with the defined format.
        return df.parse(datetimeString);
    }

}
