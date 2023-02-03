package com.example.blits.util;

import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fiyyanp on 2/1/2018.
 */

public class Utils {

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private static final Pattern VALID_INDONESIA_PHONE_NUMBER_REGEX =
            Pattern.compile("\\(?(?:\\+62|62|0)(?:\\d{2,3})?\\)?[ .-]?\\d{2,4}[ .-]?\\d{2,4}[ .-]?\\d{2,4}");

    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    public static String convertMongoDate(String val) {
        ISO8601DateFormat df = new ISO8601DateFormat();
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM, yyyy HH:mm");
        try {
            Date d = df.parse(val);
            String finalStr = outputFormat.format(d);
            val = finalStr;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return val;
    }

    public static String generateImage(String val) {
        ISO8601DateFormat df = new ISO8601DateFormat();
        SimpleDateFormat outputFormat = new SimpleDateFormat("ddMMM,yyyyHH:mm:ss");
        try {
            Date d = df.parse(val);
            String finalStr = outputFormat.format(d);
            val = finalStr;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return val;
    }

    public static String convertMongoDateWithoutTIme(String val) {
        ISO8601DateFormat df = new ISO8601DateFormat();
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM, yyyy");
        try {
            Date d = df.parse(val);
            String finalStr = outputFormat.format(d);
            val = finalStr;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return val;
    }

    public static String convertRupiah(String val) {

        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        kursIndonesia.setDecimalFormatSymbols(formatRp);
        String biaya =kursIndonesia.format(Long.valueOf(val));
        return biaya;
    }
}
