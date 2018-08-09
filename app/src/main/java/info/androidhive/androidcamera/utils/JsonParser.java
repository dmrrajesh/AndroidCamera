package info.androidhive.androidcamera.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Rajesh Saini on 2/23/2018.
 */

public class JsonParser {
    public static Map<String, String> toHashMap(JSONObject object) throws JSONException {
        Map<String, String> map = new HashMap<>();
        if (object == null)
            return map;
        Iterator<String> keys = object.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            String value = object.getString(key);
            map.put(key, value);
        }
        return map;
    }
}
