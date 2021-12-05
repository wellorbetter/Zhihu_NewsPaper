package com.example.zhihu_newspaper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.zhihu_newspaper.RecyclerView.RecyclerViewAdapter;
import com.example.zhihu_newspaper.RecyclerView.WebDetailActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.google.android.gms.internal.zzir.runOnUiThread;

public class JSON {
    public List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL("https://daily.zhihu.com/api/3/stories/latest");
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(8000);
                connection.setReadTimeout(8000);
                InputStream in = connection.getInputStream();
                StringBuilder response = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(in));
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }


                showResponse(response.toString(), list);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            } catch (Exception e) {

                e.printStackTrace();

            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    });
    public void showResponse(String JsonData, List<Map<String, Object>> list) {

        try {

            Map<String, Object> map = new HashMap<String, Object>();
            JSONObject jsonObject = new JSONObject(JsonData);
            String date = jsonObject.getString("date");
            map.put("date", date);
            JSONArray jsonArray = jsonObject.getJSONArray("stories");
            for (int i = 0; i < jsonArray.length(); i++) {
                Map<String, Object> map1 = new HashMap<String, Object>();
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String image_hue = jsonObject1.getString("image_hue");
                map1.put("image_hue", image_hue);
                String title = jsonObject1.getString("title");
                map1.put("title", title);
                String url = jsonObject1.getString("url");
                map1.put("url", url);
                String hint = jsonObject1.getString("hint");
                map1.put("hint", hint);
                int ga_prefix = jsonObject1.getInt("ga_prefix");
                map1.put("ga_prefix", ga_prefix);
                String images = jsonObject1.getString("images");
                map1.put("images", images);
                String type = jsonObject1.getString("type");
                map1.put("type", type);
                String id = jsonObject1.getString("id");
                map1.put("id", id);
                list.add(i, map1);
            }
            JSONArray jsonArray1 = jsonObject.getJSONArray("top_stories");
            for (int i = 0; i < jsonArray1.length(); i++) {
                Map<String, Object> map1 = new HashMap<String, Object>();
                JSONObject jsonObject2 = jsonArray1.getJSONObject(i);
                String image_hue = jsonObject2.getString("image_hue");
                map1.put("image_hue", image_hue);
                String title = jsonObject2.getString("title");
                map1.put("title", title);
                String url = jsonObject2.getString("url");
                map1.put("url", url);
                String hint = jsonObject2.getString("hint");
                map1.put("hint", hint);
                int ga_prefix = jsonObject2.getInt("ga_prefix");
                map1.put("ga_prefix", ga_prefix);
                String image = jsonObject2.getString("image");
                map1.put("image", image);
                String type = jsonObject2.getString("type");
                map1.put("type", type);
                String id = jsonObject2.getString("id");
                map1.put("id", id);
                list.add(i + jsonArray.length(), map1);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
