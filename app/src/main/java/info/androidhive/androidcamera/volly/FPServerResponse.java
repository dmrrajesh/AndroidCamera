package info.androidhive.androidcamera.volly;

import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by Rajesh Saini on 1/31/2018.
 */

public interface FPServerResponse {
    void onSuccess(FPRequest.Filter filter, String response);

    void onSuccess(FPRequest.Filter filter, int position, String response);

    void onSuccess(FPRequest.Filter filter, JSONObject response);

    void onSuccess(FPRequest.Filter filter, int position, JSONObject response);

    void onError(VolleyError error);

    void setProgressView(Boolean aBoolean);

}
