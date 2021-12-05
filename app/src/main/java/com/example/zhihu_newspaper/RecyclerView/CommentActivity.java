package com.example.zhihu_newspaper.RecyclerView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhihu_newspaper.MainActivity;
import com.example.zhihu_newspaper.R;

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

public class CommentActivity extends AppCompatActivity {
    public List<Map<String, Object>> list_long = new ArrayList<Map<String, Object>>();
    public List<Map<String, Object>> list_short = new ArrayList<Map<String, Object>>();
    public List<Map<String, Object>> list_comments = new ArrayList<Map<String, Object>>();
    public CommentsAdapter commentsAdapter;
    private Bundle bundle;
    private TextView textView;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        bundle = getIntent().getExtras();
        textView = findViewById(R.id.tv_Top);
        String id = bundle.getString("id");
        String long_comments = bundle.getString("long_comments");
        String short_comments = bundle.getString("short_comments");
        String comments = bundle.getString("comments");
        textView.setText(comments + "条评论");
        Map<String, Object> map_comments = new HashMap<String, Object>();
        map_comments.put("long_comments", long_comments);
        map_comments.put("short_comments", short_comments);
        map_comments.put("comments", comments);
        list_comments.add(map_comments);
        String url_long = "https://news-at.zhihu.com/api/4/story/" + id + "/long-comments";
        Log.d("888", url_long);
        String url_short = "https://news-at.zhihu.com/api/4/story/" + id + "/short-comments";
        Thread thread_long = new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL(url_long);
                    Log.d("888", url.toString());
                    connection = (HttpURLConnection) url.openConnection();
                    Log.d("666", connection.toString());
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
                    showResponse_Long(response.toString(), list_long);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.setLayoutManager(new LinearLayoutManager(CommentActivity.this));
                            commentsAdapter = new CommentsAdapter(CommentActivity.this, list_long, list_short,list_comments, new CommentsAdapter.OnItemClickListener() {
                                @Override
                                public void onClick(int pos) {
                                    Toast.makeText(CommentActivity.this, "Cg", Toast.LENGTH_SHORT).show();
                                }
                            });
                            Log.d("ppa", "aa");
                            recyclerView.setAdapter(commentsAdapter);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setContentView(R.layout.networkdisconnected);
                            ImageView imageView = findViewById(R.id.imageView);
                            imageView.setImageResource(R.drawable.networkdisconnected);
                            Toast.makeText(CommentActivity.this, "请检查网络连接！Master!", Toast.LENGTH_SHORT).show();
                        }
                    });
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
        Thread thread_short = new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL(url_short);
                    connection = (HttpURLConnection) url.openConnection();
                    Log.d("URL", url.toString());
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


                    showResponse_short(response.toString(), list_short);
                    thread_long.start();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                } catch (Exception e) {

                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setContentView(R.layout.networkdisconnected);
                            ImageView imageView = findViewById(R.id.imageView);
                            imageView.setImageResource(R.drawable.networkdisconnected);
                            Toast.makeText(CommentActivity.this, "请检查网络连接！Master!", Toast.LENGTH_SHORT).show();
                        }
                    });
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
        thread_short.start();
    }
    public void showResponse_Long(String JsonData, List<Map<String, Object>> list) {

        try {
            JSONObject jsonObject = new JSONObject(JsonData);
            JSONArray jsonArray = jsonObject.getJSONArray("comments");
            Log.d("777", jsonArray.toString());
            for (int i = 0; i < jsonArray.length(); i++)
            {
                if (jsonArray.length() == 0) break;
                Map<String, Object> map1 = new HashMap<String, Object>();
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String author = jsonObject1.getString("author");
                map1.put("author",author);
                String content = jsonObject1.getString("content");
                map1.put("content",content);
                String avatar = jsonObject1.getString("avatar");
                map1.put("avatar",avatar);
                String id = jsonObject1.getString("id");
                map1.put("id",id);
                String likes = jsonObject1.getString("likes");
                map1.put("likes",likes);
                String time = jsonObject1.getString("time");
                map1.put("time",time);
//                if (jsonObject1.getJSONObject("reply_to") != null )
//                {
//                    JSONObject reply_to = jsonObject1.getJSONObject("reply_to");
//                    Log.d("reply_to", reply_to.toString());
//                    String content_reply = reply_to.getString("content");
//                    map1.put("content_reply",content_reply);
//                    String staus_reply = reply_to.getString("status");
//                    map1.put("staus_reply",staus_reply);
//                    String id_reply = reply_to.getString("id");
//                    map1.put("id_reply",id_reply);
//                    String author_reply = reply_to.getString("author");
//                    map1.put("author_reply",author_reply);
//                    if(staus_reply != "0")
//                    {
//                        String err_msg = reply_to.getString("err_msg");
//                        map1.put("err_msg",err_msg);
//                    }
//                }

                list.add(map1);
            }
            Log.d("999",list.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public void showResponse_short(String JsonData, List<Map<String, Object>> list) {

        try {
            JSONObject jsonObject = new JSONObject(JsonData);
            JSONArray jsonArray = jsonObject.getJSONArray("comments");
            for (int i = 0; i < jsonArray.length(); i++)
            {
                if (jsonArray.length() == 0)  break;
                Map<String, Object> map1 = new HashMap<String, Object>();
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String author = jsonObject1.getString("author");
                map1.put("author",author);
                String content = jsonObject1.getString("content");
                map1.put("content",content);
                String avatar = jsonObject1.getString("avatar");
                map1.put("avatar",avatar);
                String id = jsonObject1.getString("id");
                map1.put("id",id);
                String likes = jsonObject1.getString("likes");
                map1.put("likes",likes);
                String time = jsonObject1.getString("time");
                map1.put("time",time);
//                if (jsonObject1.getJSONObject("reply_to") != null )
//                {
//                    JSONObject reply_to = jsonObject1.getJSONObject("reply_to");
//                    Log.d("reply_to", reply_to.toString());
//                    String content_reply = reply_to.getString("content");
//                    map1.put("content_reply",content_reply);
//                    String staus_reply = reply_to.getString("status");
//                    map1.put("staus_reply",staus_reply);
//                    String id_reply = reply_to.getString("id");
//                    map1.put("id_reply",id_reply);
//                    String author_reply = reply_to.getString("author");
//                    map1.put("author_reply",author_reply);
//                    if(staus_reply != "0")
//                    {
//                        String err_msg = reply_to.getString("err_msg");
//                        map1.put("err_msg",err_msg);
//                    }
//                }
                list.add(map1);
            }
            Log.d("999",list.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

