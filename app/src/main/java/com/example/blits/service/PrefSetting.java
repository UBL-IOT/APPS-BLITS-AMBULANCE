package com.example.blits.service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.blits.MainActivity;

public class PrefSetting {

    public static String guid;
    public static String fullname;
    public static String username;
    public static String password;
    public static String email;
    public static String phone;
    public static String address;
    public static String role;
    public static String created_at;
    public static String updated_at;

    Activity activity;

    public PrefSetting(Activity activity) {
        this.activity = activity;
    }

    public SharedPreferences getSharedPreferences() {
        SharedPreferences preferences = activity.getSharedPreferences("AccessDetails", Context.MODE_PRIVATE);
        return preferences;
    }

    public void isLogin(SessionManager sessionManager, SharedPreferences preferences) {
        sessionManager = new SessionManager(activity);
        if (sessionManager.isLoggedIn()) {
            preferences = getSharedPreferences();
            guid = preferences.getString("GUID", "");
            fullname = preferences.getString("FULLNAME", "");
            username = preferences.getString("USERNAME", "");
            password = preferences.getString("PASSWORD", "");
            email = preferences.getString("EMAIL", "");
            phone = preferences.getString("PHONE", "");
            address = preferences.getString("ADDRESS", "");
            role = preferences.getString("ROLE", "");
            created_at = preferences.getString("CREATED_AT", "");
            updated_at = preferences.getString("UPDATED_AT", "");
        } else {
            sessionManager.setLogin(false);
            sessionManager.setSessId(0);
            Intent i = new Intent(activity, activity.getClass());
            activity.startActivity(i);
            activity.finish();
        }
    }

    public void checkLogin(SessionManager sessionManager, SharedPreferences preferences) {
        sessionManager = new SessionManager(activity);
        guid = preferences.getString("GUID", "");
        fullname = preferences.getString("FULLNAME", "");
        username = preferences.getString("USERNAME", "");
        password = preferences.getString("PASSWORD", "");
        email = preferences.getString("EMAIL", "");
        phone = preferences.getString("PHONE", "");
        address = preferences.getString("ADDRESS", "");
        role = preferences.getString("ROLE", "");
        created_at = preferences.getString("CREATED_AT", "");
        updated_at = preferences.getString("UPDATED_AT", "");
        if (sessionManager.isLoggedIn()) {
            if (role.equals("2")) {
                Intent i = new Intent(activity, MainActivity.class);
                activity.startActivity(i);
                activity.finish();
            }
        }
    }

    public void storeRegIdSharedPreferences(Context context, String guid, String fullname, String username,
                                            String password, String email, String phone, String address, String role, String created_at, String updated_at, SharedPreferences preferences) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("GUID", guid);
        editor.putString("FULLNAME", fullname);
        editor.putString("USERNAME", username);
        editor.putString("PASSWORD", password);
        editor.putString("EMAIL", email);
        editor.putString("PHONE", phone);
        editor.putString("ADDRESS", address);
        editor.putString("ROLE", role);
        editor.putString("CREATED_AT", created_at);
        editor.putString("UPDATED_AT", updated_at);
        editor.commit();
    }

}
