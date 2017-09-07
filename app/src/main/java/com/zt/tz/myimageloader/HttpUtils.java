package com.zt.tz.myimageloader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2016-05-28 17:29
 * QQ:xxxxxxxx
 */
public class HttpUtils {
    public static InputStream download(String key) throws MalformedURLException, IOException{
        HttpURLConnection connection= (HttpURLConnection) new URL(key).openConnection();
        return connection.getInputStream();
    }

    public static List<String> resolveImageData(String json) throws Exception {
        List<String> list = new ArrayList<String>();
        JSONObject object = new JSONObject(json);
        JSONArray data = object.getJSONArray("data");
        for (int i = 0; i < data.length() - 1; i++) {
            String objURL = data.getJSONObject(i).getString("objURL");
            list.add(objURL);
        }
        return list;
    }
    public static String sendGETRequest(String path) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(path)
                .openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        InputStream inStream = conn.getInputStream();
        byte[] data = StreamTool.read(inStream);
        String result = new String(data);
        return result;
        // }else{
        // return null;
        // }

    }
}
