package com.example.blits.service;

import com.example.blits.R;

public class BaseURL {

//    public static String baseUrl = "http://studio81.co.id:5060/";//vps
    public static String baseUrl = App.getApplication().getString(R.string.end_point);

    public static String login = baseUrl + "users/login";
    public static String register = baseUrl + "users/create";

}
