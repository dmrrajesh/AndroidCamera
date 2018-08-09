package info.androidhive.androidcamera.volly;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.mime.MultipartEntityBuilder;
import cz.msebera.android.httpclient.entity.mime.content.FileBody;
import info.androidhive.androidcamera.TaskImageModel;

/**
 * Created by Rajesh Saini on 1/31/2018.
 */

public class FPRequest {
    private final String PATH = "http://localhost/selfie/upload.php";
    private final int RETRY_TIME = 10000;
    private final String TOKEN = "@:D8F6K-5";
    private final String CONTENT_TYPE = "application/json";
    private String boundary = "SwA" + Long.toString(System.currentTimeMillis()) + "SwA";

    public enum Filter {
        LOGIN, GET_USER_TASKS, GET_TASK_STATUS_LIST, GET_USER_LIST, CREATE_TASK,
        CREATE_TASK_BASE64, UPDATE_TASK_STATUS, UPDATE_TASK, GET_TASK, UPLOAD_BASE64,
        GET_UNAPPROVED_TASKS, DELETE_TASK, TASK_COMMENT
    }

    private Context mContext;


    public FPRequest(Context context) {
        mContext = context;
    }

    public void doGet(String url, final FPServerResponse fpResponse, final Filter filter, String tag) {
        fpResponse.setProgressView(true);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, PATH.concat(url), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        fpResponse.setProgressView(false);
                        fpResponse.onSuccess(filter, response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        fpResponse.setProgressView(false);
                        fpResponse.onError(error);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", TOKEN);
                //params.put("Content-Type", CONTENT_TYPE);
                return params;
            }

            @Override
            public Request<?> setRetryPolicy(RetryPolicy retryPolicy) {
                retryPolicy = new DefaultRetryPolicy(RETRY_TIME,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                return super.setRetryPolicy(retryPolicy);
            }
        };
        jsObjRequest.setTag(tag);
        FPVolley.getInstance(mContext).addToRequestQueue(jsObjRequest);
    }

    public void doPostAuthentication(final String url, final Map<String, String> map, final FPResponse fpResponse, final Filter filter, String tag) {
        fpResponse.setProgressView(true);
        StringRequest request = new StringRequest(Request.Method.POST, PATH.concat(url), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                fpResponse.setProgressView(false);
                fpResponse.onSuccess(filter, response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                fpResponse.setProgressView(false);
                fpResponse.onError(error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", TOKEN);
                return params;
            }

            @Override
            public byte[] getBody() {
                if (map != null && map.size() > 0) {
                    return encodeParameters(map, getParamsEncoding());
                }
                return null;
            }

            @Override
            public Request<?> setRetryPolicy(RetryPolicy retryPolicy) {
                retryPolicy = new DefaultRetryPolicy(RETRY_TIME,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                return super.setRetryPolicy(retryPolicy);
            }
        };
        request.setTag(tag);
        FPVolley.getInstance(mContext).addToRequestQueue(request);
    }

    public void doPostAuthenticationJson(final String url, final Map<String, String> map, final FPResponse fpResponse, final Filter filter, String tag) {
        fpResponse.setProgressView(true);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, PATH.concat(url), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        fpResponse.setProgressView(false);
                        fpResponse.onSuccess(filter, response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        fpResponse.setProgressView(false);
                        fpResponse.onError(error);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", TOKEN);
                //params.put("Content-Type", CONTENT_TYPE);
                return params;
            }

            @Override
            public byte[] getBody() {
                if (map != null && map.size() > 0) {
                    return encodeParameters(map, getParamsEncoding());
                }
                return null;
            }

            @Override
            public Request<?> setRetryPolicy(RetryPolicy retryPolicy) {
                retryPolicy = new DefaultRetryPolicy(RETRY_TIME,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                return super.setRetryPolicy(retryPolicy);
            }
        };
        jsObjRequest.setTag(tag);
        FPVolley.getInstance(mContext).addToRequestQueue(jsObjRequest);
    }

    public void doPostWithCache(String url, final Map<String, String> map, final FPResponse fpResponse, final Filter filter, String tag) {
        url = PATH.concat(url);
        final RequestQueue queue = FPVolley.getInstance(mContext).getRequestQueue();
        if (queue != null) {
            Cache cache = queue.getCache();
            if (cache != null) {
                Cache.Entry entry = cache.get(url);
                if (entry != null) {
                    Log.d("Entry", entry.toString());
                    try {
                        String response = new String(entry.data, "UTF-8");
                        fpResponse.onSuccess(filter, response);
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        fpResponse.setProgressView(true);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                fpResponse.setProgressView(false);
                fpResponse.onSuccess(filter, response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                fpResponse.setProgressView(false);
                fpResponse.onError(error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", TOKEN);
                return params;
            }

            @Override
            public byte[] getBody() {
                /*if (map != null && map.size() > 0) {
                    Map<String, String> params = map;
                    if (!params.containsKey("UserID"))
                        if (!mPreference.get(Constant.Pref.User.ID, "").equals(""))
                            params.put("UserID", mPreference.get(Constant.Pref.User.ID, ""));
                    return encodeParameters(params, getParamsEncoding());
                } else {
                    Map<String, String> params = new HashMap<>();
                    if (!params.containsKey("UserID"))
                        if (!mPreference.get(Constant.Pref.User.ID, "").equals(""))
                            params.put("UserID", mPreference.get(Constant.Pref.User.ID, ""));
                    return encodeParameters(params, getParamsEncoding());
                }*/
                if (map != null)
                    return encodeParameters(map, getParamsEncoding());
                else
                    return encodeParameters(new HashMap<String, String>(), getParamsEncoding());
            }

            @Override
            public Request<?> setRetryPolicy(RetryPolicy retryPolicy) {
                retryPolicy = new DefaultRetryPolicy(RETRY_TIME,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                return super.setRetryPolicy(retryPolicy);
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                    if (cacheEntry == null) {
                        cacheEntry = new Cache.Entry();
                    }
                    final long cacheHitButRefreshed = 3 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
                    final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
                    long now = System.currentTimeMillis();
                    final long softExpire = now + cacheHitButRefreshed;
                    final long ttl = now + cacheExpired;
                    cacheEntry.data = response.data;
                    cacheEntry.softTtl = softExpire;
                    cacheEntry.ttl = ttl;
                    String headerValue;
                    headerValue = response.headers.get("Date");
                    if (headerValue != null) {
                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    if (response.headers.containsKey("Last-Modified"))
                        headerValue = response.headers.get("Last-Modified");
                    if (headerValue != null) {
                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    cacheEntry.responseHeaders = response.headers;
                    final String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(jsonString, cacheEntry);
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }
            }
        };

        request.setTag(tag);
        FPVolley.getInstance(mContext).addToRequestQueue(request);
    }

    public void doPostNoCache(String url, final Map<String, String> map, final FPResponse fpResponse, final Filter filter, String tag) {
        url = PATH.replace("localhost", url);
        fpResponse.setProgressView(true);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                fpResponse.setProgressView(false);
                fpResponse.onSuccess(filter, response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                fpResponse.setProgressView(false);
                fpResponse.onError(error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", TOKEN);
                return params;
            }

            @Override
            public byte[] getBody() {
                if (map != null)
                    return encodeParameters(map, getParamsEncoding());
                else
                    return encodeParameters(new HashMap<String, String>(), getParamsEncoding());
            }

            @Override
            public Request<?> setRetryPolicy(RetryPolicy retryPolicy) {
                retryPolicy = new DefaultRetryPolicy(RETRY_TIME,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                return super.setRetryPolicy(retryPolicy);
            }
        };
        request.setTag(tag);
        FPVolley.getInstance(mContext).addToRequestQueue(request);
    }

    public void doPostNoCache(String url, final Map<String, String> map, final FPResponse fpResponse, final Filter filter, String tag, final int position) {
        url = PATH.concat(url);
        fpResponse.setProgressView(true);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                fpResponse.setProgressView(false);
                fpResponse.onSuccess(filter, position, response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                fpResponse.setProgressView(false);
                fpResponse.onError(error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", TOKEN);
                return params;
            }

            @Override
            public byte[] getBody() {
                if (map != null)
                    return encodeParameters(map, getParamsEncoding());
                else
                    return encodeParameters(new HashMap<String, String>(), getParamsEncoding());
            }

            @Override
            public Request<?> setRetryPolicy(RetryPolicy retryPolicy) {
                retryPolicy = new DefaultRetryPolicy(RETRY_TIME,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                return super.setRetryPolicy(retryPolicy);
            }
        };
        request.setTag(tag);
        FPVolley.getInstance(mContext).addToRequestQueue(request);
    }

    private HttpEntity mHttpEntity;

    private HttpEntity buildMultipartEntity(List<TaskImageModel> imageModels, Map<String, String> map) {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        for (TaskImageModel model : imageModels) {
            try {
                Uri uri = model.getUri();
                File file = new File(uri.getPath());
                FileBody fileBody = new FileBody(file);
                builder.addPart("file", fileBody);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (Map.Entry<String, String> entry : map.entrySet()) {
            builder.addTextBody(entry.getKey(), entry.getValue());
        }
        return builder.build();
    }

    public void doPostMultipart(String url, final Map<String, String> map, List<TaskImageModel> imageModels, final FPResponse fpResponse, final Filter filter, String tag) {
        url = PATH.concat(url);
        fpResponse.setProgressView(true);
        mHttpEntity = buildMultipartEntity(imageModels, map);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                fpResponse.setProgressView(false);
                fpResponse.onSuccess(filter, response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                fpResponse.setProgressView(false);
                fpResponse.onError(error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", TOKEN);
                return params;
            }

            @Override
            public byte[] getBody() {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                try {
                    mHttpEntity.writeTo(bos);
                    return bos.toByteArray();
                } catch (IOException e) {
                    VolleyLog.e("" + e);
                    return null;
                } catch (OutOfMemoryError e) {
                    VolleyLog.e("" + e);
                    return null;
                }
            }

            @Override
            public Request<?> setRetryPolicy(RetryPolicy retryPolicy) {
                retryPolicy = new DefaultRetryPolicy(RETRY_TIME,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                return super.setRetryPolicy(retryPolicy);
            }

            @Override
            public String getBodyContentType() {
                String content_type = "multipart/form-data; boundary=" + boundary + "; charset=utf-8";
                return content_type;
            }
        };

        request.setTag(tag);
        FPVolley.getInstance(mContext).addToRequestQueue(request);
    }

    public void doPostJson(final String url, final Map<String, String> map, final FPResponse fpResponse, final Filter filter, String tag, final Boolean isCache) {
        if (isCache) {
            final RequestQueue queue = FPVolley.getInstance(mContext).getRequestQueue();
            if (queue != null) {
                Cache cache = queue.getCache();
                if (cache != null) {
                    Cache.Entry entry = cache.get(url);
                    if (entry != null) {
                        Log.d("Entry", entry.toString());
                        try {
                            String response = new String(entry.data, "UTF-8");
                            JSONObject object = new JSONObject(response);
                            fpResponse.onSuccess(filter, object);
                            return;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        fpResponse.setProgressView(true);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, PATH.concat(url), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        fpResponse.setProgressView(false);
                        fpResponse.onSuccess(filter, response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        fpResponse.setProgressView(false);
                        fpResponse.onError(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                if (map != null && map.size() > 0) {
                    Map<String, String> params = map;
                    return params;
                } else {
                    Map<String, String> params = new HashMap<>();
                    return params;
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", TOKEN);
                return params;
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                    if (cacheEntry == null) {
                        cacheEntry = new Cache.Entry();
                    }
                    final long cacheHitButRefreshed = 3 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
                    final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
                    long now = System.currentTimeMillis();
                    final long softExpire = now + cacheHitButRefreshed;
                    final long ttl = now + cacheExpired;
                    cacheEntry.data = response.data;
                    cacheEntry.softTtl = softExpire;
                    cacheEntry.ttl = ttl;
                    String headerValue;
                    headerValue = response.headers.get("Date");
                    if (headerValue != null) {
                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    headerValue = response.headers.get("Last-Modified");
                    if (headerValue != null) {
                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    cacheEntry.responseHeaders = response.headers;
                    final String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    if (isCache)
                        return Response.success(new JSONObject(jsonString), cacheEntry);
                    else
                        return Response.success(new JSONObject(jsonString), new Cache.Entry());
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (JSONException e) {
                    return Response.error(new ParseError(e));
                }
            }
        };
        jsObjRequest.setTag(tag);
        FPVolley.getInstance(mContext).addToRequestQueue(jsObjRequest);
    }

    public void onCancelAll(String tag) {
        FPVolley.getInstance(mContext).getRequestQueue().cancelAll(tag);
    }

    private byte[] encodeParameters(Map<String, String> params, String paramsEncoding) {
        StringBuilder encodedParams = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                encodedParams.append(URLEncoder.encode(entry.getKey(), paramsEncoding));
                encodedParams.append('=');
                encodedParams.append(URLEncoder.encode(entry.getValue(), paramsEncoding));
                encodedParams.append('&');
            }
            return encodedParams.toString().getBytes(paramsEncoding);
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + paramsEncoding, uee);
        }
    }
}
