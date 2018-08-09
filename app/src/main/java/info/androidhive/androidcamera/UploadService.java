package info.androidhive.androidcamera;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;

import info.androidhive.androidcamera.utils.Preference;
import info.androidhive.androidcamera.volly.FPRequest;
import info.androidhive.androidcamera.volly.FPResponse;

public class UploadService extends Service {
    private static final String TAG = UploadService.class.getSimpleName();
    private FPRequest mFPRequest;
    private Preference mPreference;

    public UploadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mFPRequest = new FPRequest(this);
        mPreference = Preference.build(this);
        if (intent.getAction().equalsIgnoreCase("UPLOAD")) {
            if (intent.hasExtra("PATH")) {
                String path = intent.getStringExtra("PATH");
                /*String base64Image = Utility.Image.encodeBase64Image(path);
                Map<String, String> map = new HashMap();
                map.put("image","data:image/jpeg;base64,"+base64Image);
                if (mFPRequest != null) {
                    mFPRequest.doPostNoCache(mPreference.get(Constant.Pref.Setting.IP_OR_HOST_PATH, ""), map, mFPResponse, FPRequest.Filter.UPLOAD_BASE64, TAG);
                }*/
                Log.d(TAG, path);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private FPResponse mFPResponse = new FPResponse() {
        @Override
        public void onSuccess(FPRequest.Filter filter, String response) {
            Toast.makeText(UploadService.this, "Success", Toast.LENGTH_SHORT).show();
            if (filter == FPRequest.Filter.UPLOAD_BASE64) {
            }
            Log.d(TAG, response);
            stopSelf();
        }

        @Override
        public void onError(VolleyError error) {
            Toast.makeText(UploadService.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            stopSelf();
        }

        @Override
        public void setProgressView(Boolean aBoolean) {
        }
    };
}
