package com.example.blits.service;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDex;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class App extends Application {
    public static final String TAG = App.class.getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Prefs preferences;
    private static App mInstance;
    private static Application sApplication;

    public static Application getApplication() {
        return sApplication;
    }


    public static Context getContext() {
        return getApplication().getApplicationContext();
    }

    public static Prefs getPref() {
        return preferences;
    }

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        if (Build.VERSION.SDK_INT <= 19) {
//            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
//        }
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        mInstance = this;
        sApplication = this;
        preferences = new Prefs(sApplication);



    }


    public static synchronized App getInstance()
    {
        return mInstance;
    }

    public RequestQueue getRequestQueue()
    {
        if (mRequestQueue == null)
        {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

//    public ImageLoader getImageLoader()
//    {
//        getRequestQueue();
//        if (mImageLoader == null)
//        {
//            mImageLoader = new ImageLoader(this.mRequestQueue, new LruBitmapCache());
//        }
//        return this.mImageLoader;
//    }

    public <T> void addToRequestQueue(Request<T> req, String tag)
    {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req)
    {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag)
    {
        if (mRequestQueue != null)
        {
            mRequestQueue.cancelAll(tag);
        }
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
