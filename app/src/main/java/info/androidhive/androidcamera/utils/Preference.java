package info.androidhive.androidcamera.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.Set;

import info.androidhive.androidcamera.SplashActivity;

/**
 * Created by Rajesh Saini on 2/19/2018.
 */

public class Preference {
    private Context context;

    private Preference(Context context) {
        this.context = context;
    }

    public static Preference build(Context context) {
        return new Preference(context);
    }

    private SharedPreferences getPreferences() {
        return context.getSharedPreferences("console", Context.MODE_PRIVATE);
    }

    private SharedPreferences.Editor getEditor() {
        return getPreferences().edit();
    }

    /**
     * This function clear all keys
     */
    public void remove() {
        getEditor().clear().commit();
    }

    /**
     * This function clear passed key
     */
    public void remove(String key) {
        getEditor().remove(key).commit();
    }

    public void set(String key, String value) {
        getEditor().putString(key, value).commit();
    }

    public void set(String key, Boolean value) {
        getEditor().putBoolean(key, value).commit();
    }

    public void set(String key, Float value) {
        getEditor().putFloat(key, value).commit();
    }

    public void set(String key, Integer value) {
        getEditor().putInt(key, value).commit();
    }

    public void set(String key, Long value) {
        getEditor().putLong(key, value).commit();
    }

    public void set(String key, Set<String> value) {
        getEditor().putStringSet(key, value).commit();
    }

    public String get(String key, String defaultValue) {
        return getPreferences().getString(key, defaultValue);
    }

    public Boolean get(String key, Boolean defaultValue) {
        return getPreferences().getBoolean(key, defaultValue);
    }

    public Float get(String key, Float defaultValue) {
        return getPreferences().getFloat(key, defaultValue);
    }

    public Integer get(String key, Integer defaultValue) {
        return getPreferences().getInt(key, defaultValue);
    }

    public Set<String> get(String key, Set<String> defaultValue) {
        return getPreferences().getStringSet(key, defaultValue);
    }

    public Long get(String key, Long defaultValue) {
        return getPreferences().getLong(key, defaultValue);
    }

    public Boolean isLogin() {
        return get(Constant.Pref.User.LOGIN_STATUS, false);
    }
    
    public void isUserValid() {
        if (!isLogin()) {
            remove();
            context.startActivity(new Intent(context, SplashActivity.class));
            ((Activity) context).finish();
        }
    }

}
