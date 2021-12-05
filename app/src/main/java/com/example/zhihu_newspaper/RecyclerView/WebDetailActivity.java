package com.example.zhihu_newspaper.RecyclerView;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
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

public class WebDetailActivity extends AppCompatActivity {
    private Bundle bundle;
    private Bundle bundle1;
    private WebView webView;
    private TextView textView;
    private FrameLayout frameLayout;public List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    private Button button1;private Button button2;private Button button3;
    private Button button4;private Button button5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_detail);
        frameLayout = findViewById(R.id.web_frame);
        bundle = getIntent().getExtras();
        String id = bundle.getString("id");
        textView = findViewById(R.id.textView_appreciation);
        button1 = findViewById(R.id.btn_1);button2 = findViewById(R.id.btn_2);
        button3 = findViewById(R.id.btn_3);button4 = findViewById(R.id.btn_4);
        button5 = findViewById(R.id.btn_5);
        initView();
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WebDetailActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        Thread thread_info = new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL("https://news-at.zhihu.com/api/3/story-extra/" + id);
                    connection = (HttpURLConnection) url.openConnection();
                    Log.d("987", id);
                    Log.d("987", "https://news-at.zhihu.com/api/3/story-extra/" + id);
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


                    showResponse_info(response.toString(), list);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(list.get(0).get("comments").toString());
                            Log.d("comments",(list.get(0).get("comments").toString()));
                            button2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String short_comments = list.get(0).get("short_comments").toString();
                                    String popularity = list.get(0).get("popularity").toString();
                                    String long_comments = list.get(0).get("long_comments").toString();
                                    String comments = list.get(0).get("comments").toString();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("id", id);
                                    bundle.putString("short_comments",short_comments);
                                    bundle.putString("popularity",popularity);
                                    bundle.putString("long_comments",long_comments);
                                    bundle.putString("comments",comments);
                                    Intent intent = new Intent(WebDetailActivity.this, CommentActivity.class);
                                    intent.putExtras(bundle);
                                    startActivity(intent, bundle);
                                }
                            });

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
                            Toast.makeText(WebDetailActivity.this, "请检查网络连接！Master!", Toast.LENGTH_SHORT).show();
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
        thread_info.start();
    }
    public void showResponse_info(String JsonData, List<Map<String, Object>> list) {

        try {

            Map<String, Object> map = new HashMap<String, Object>();
            JSONObject jsonObject = new JSONObject(JsonData);
            String long_comments = jsonObject.getString("long_comments");
            String popularity = jsonObject.getString("popularity");
            String short_comments = jsonObject.getString("short_comments");
            String comments = jsonObject.getString("comments");
            map.put("popularity",popularity);
            map.put("long_comments",long_comments);
            map.put("short_comments",short_comments);
            map.put("comments",comments);
            list.add(map);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("list",list.toString());
    }
    private void initView() {
        webView = new WebView(this);
        WebSettings settings = webView.getSettings();
        settings.setDomStorageEnabled(true);
        //解决一些图片加载问题
        settings.setJavaScriptEnabled(true);
        settings.setBlockNetworkImage(false);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        frameLayout.addView(webView);
        String url = bundle.getString("URL");
        webView.loadUrl(url);
    }

    //监听BACK按键，有可以返回的页面时返回页面
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            if(webView.canGoBack()) {
                webView.goBack();
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.setTag(null);
            webView.clearHistory();

            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }
}


