package info.androidhive.androidcamera.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;

import java.util.regex.Pattern;

/**
 * Created by Rajesh Saini on 2/19/2018.
 */

public class Validation {
    private Context context;

    private Validation(Context context) {
        this.context = context;
    }

    public static Validation build(Context context) {
        return new Validation(context);
    }

    public Boolean isEmail(String text) {
        return !TextUtils.isEmpty(text) && Patterns.EMAIL_ADDRESS.matcher(text).matches();
    }

    public Boolean isPersonName(String text) {
        return !TextUtils.isEmpty(text) /*&& REX_PERSON_NAME().matcher(text).matches()*/;
    }

    public Boolean isMobile(String text) {
        return text.matches("^[0-9]{10}$");
    }

    public Boolean isUserName(String text) {
        return !TextUtils.isEmpty(text);
    }

    public Boolean isPassword(String text) {
        return !TextUtils.isEmpty(text);
    }

    private Pattern REX_PERSON_NAME() {
        return Pattern.compile("^[A-Za-z\\s]+$");
    }
}
