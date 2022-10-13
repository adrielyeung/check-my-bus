package com.adriel.checkmybus.utils;

import com.adriel.checkmybus.constants.Constants;

import java.util.regex.Pattern;

public class JointOperationUtils {

    public static String getCtbNwfbFromJointOpRoute(String company) {
        if (company.contains(Constants.CTB_COMPANY)) {
            return Constants.CTB_COMPANY;
        }

        if (company.contains(Constants.NWFB_COMPANY)) {
            return Constants.NWFB_COMPANY;
        }

        return "";
    }

    public static boolean isCtbNwfbOperator(String company) {
        return (company.equalsIgnoreCase(Constants.CTB_COMPANY) ||
                company.equalsIgnoreCase(Constants.NWFB_COMPANY));
    }

    public static boolean isJointOpRoute(String company) {
        return company.contains(Constants.COMPANY_SEPARATOR);
    }

    public static boolean isJointCrossHarbourRoute(String company, String routeNumber) {
        return isJointOpRoute(company)
                && Pattern.matches(Constants.CROSS_HARBOUR_ROUTES, routeNumber);
    }

}
