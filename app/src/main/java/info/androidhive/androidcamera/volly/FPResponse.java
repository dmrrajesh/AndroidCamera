package info.androidhive.androidcamera.volly;

import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by Rajesh Saini on 1/31/2018.
 */

public abstract class FPResponse implements FPServerResponse {
    @Override
    public void onSuccess(FPRequest.Filter filter, String response) {

    }

    @Override
    public void onSuccess(FPRequest.Filter filter, JSONObject response) {

    }

    @Override
    public void onSuccess(FPRequest.Filter filter, int position, String response) {

    }

    @Override
    public void onSuccess(FPRequest.Filter filter, int position, JSONObject response) {

    }

    @Override
    public void onError(VolleyError error) {

    }

    @Override
    public void setProgressView(Boolean aBoolean) {

    }
}
