package com.example.zhihu_newspaper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.zhihu_newspaper.RecyclerView.RecyclerViewAdapter;
import com.example.zhihu_newspaper.RecyclerView.WebDetailActivity;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.api.ScrollBoundaryDecider;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public List<Map<String, Object>> list = new ArrayList<>();
    public List<Map<String, Object>> list2 = new ArrayList<>();
    public List<Map<String, Object>> list_id = new ArrayList<>();
    public List<Map<String, Object>> list4 = new ArrayList<>();
    public List<Map<String, Object>> list5 = new ArrayList<>();
    public List<Map<String, Object>> list6 = new ArrayList<>();
    public int date;public int cnt = 0;
    private RecyclerView mRecycleView;
    private RecyclerViewAdapter myAdapter;
    private Calendar calendar = Calendar.getInstance();
    private int year = calendar.get(Calendar.YEAR);
    private int month = calendar.get(Calendar.MONTH)+1;
    private int day = calendar.get(Calendar.DAY_OF_MONTH);
    private int day_before = calendar.get(Calendar.DAY_OF_MONTH);
    private int month_before = calendar.get(Calendar.MONTH)+1;
    private int year_before = calendar.get(Calendar.YEAR);
    private String month_;private String day_;String date_;
    private String day_use;private String month_use;
    com.scwang.smartrefresh.layout.SmartRefreshLayout refreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecycleView = (RecyclerView) findViewById(R.id.rv_main);
        refreshLayout = (com.scwang.smartrefresh.layout.SmartRefreshLayout) findViewById(R.id.refreshView);
        mRecycleView.setItemAnimator(null);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL("https://daily.zhihu.com/api/3/news/before/" +date_);
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
                            // can change something here
                            mRecycleView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                            myAdapter = new RecyclerViewAdapter(MainActivity.this, list, list4, new RecyclerViewAdapter.OnItemClickListener() {
                                @Override
                                public void onClick(int pos) {
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("pos",pos);
                                    bundle.putString("URL",(String) list.get(pos).get("url"));
                                    bundle.putString("id",(String) list4.get(pos).get("id"));
                                    Intent intent = new Intent(MainActivity.this, WebDetailActivity.class);
                                    intent.putExtras(bundle);
                                    startActivity(intent, bundle);
                                }
                            });
                            mRecycleView.setAdapter(myAdapter);
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
                            Toast.makeText(MainActivity.this, "请检查网络连接！Master!", Toast.LENGTH_SHORT).show();
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
        Thread thread_id = new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                day++;
                switch (day)
                {
                    case 1: day_ = "01";break; case 2: day_ = "02";break; case 3: day_ = "03";break;
                    case 4: day_ = "04";break; case 5: day_ = "05";break; case 6: day_ = "06";break;
                    case 7: day_ = "07";break; case 8: day_ = "08";break; case 9: day_ = "09";break;
                    default: day_ = String.valueOf(day);
                }
                switch (month)
                {
                    case 1: month_ = "01";break; case 2: month_ = "02";break; case 3: month_ = "03";break;
                    case 4: month_ = "04";break; case 5: month_ = "05";break; case 6: month_ = "06";break;
                    case 7: month_ = "07";break; case 8: month_ = "08";break; case 9: month_ = "09";break;
                    case 10: month_ = "10";break; case 11: month_ = "11";break; case 12: month_ = "12";break;
                }
                try {
                    URL url = new URL("https://daily.zhihu.com/api/3/news/before/" +String.valueOf(year)+month_+ day_);
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
                    showResponse_id(response.toString(), list_id);
                    for(int i = 0; i < list_id.size(); i++)
                    {
                        Map<String, Object> map = new HashMap<String, Object>();
                        String id = list_id.get(i).get("id").toString();
                        map.put("id", id);
                        list4.add(map);
                    }
                    date_ = String.valueOf(year)+month_+ day_;
                    thread.start();
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setContentView(R.layout.networkdisconnected);
                            ImageView imageView = findViewById(R.id.imageView);
                            imageView.setImageResource(R.drawable.networkdisconnected);
                            Toast.makeText(MainActivity.this, "请检查网络连接！Master!", Toast.LENGTH_SHORT).show();
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
        thread_id.start();
//        Thread thread_date = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                HttpURLConnection connection = null;
//                BufferedReader reader = null;
//                try {
//                    URL url = new URL("https://daily.zhihu.com/api/3/stories/latest");
//                    connection = (HttpURLConnection) url.openConnection();
//                    connection.setRequestMethod("GET");
//                    connection.setConnectTimeout(8000);
//                    connection.setReadTimeout(8000);
//                    InputStream in = connection.getInputStream();
//                    StringBuilder response = new StringBuilder();
//                    reader = new BufferedReader(new InputStreamReader(in));
//                    String line;
//
//                    while ((line = reader.readLine()) != null) {
//                        response.append(line);
//                    }
//                    showResponse_date(response.toString(), list2);
//                    date = Integer.valueOf(list2.get(0).get("date").toString());
//                    thread_id.start();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            setContentView(R.layout.networkdisconnected);
//                            ImageView imageView = findViewById(R.id.imageView);
//                            imageView.setImageResource(R.drawable.networkdisconnected);
//                            Toast.makeText(MainActivity.this, "请检查网络连接！Master!", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                } finally {
//                    if (reader != null) {
//                        try {
//                            reader.close();
//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                }
//            }
//        });

//        Thread thread_ = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                HttpURLConnection connection = null;
//                BufferedReader reader = null;
//                try {
//                    URL url = new URL("https://daily.zhihu.com/api/3/news/before/" +date_);
//                    connection = (HttpURLConnection) url.openConnection();
//                    connection.setRequestMethod("GET");
//                    connection.setConnectTimeout(8000);
//                    connection.setReadTimeout(8000);
//                    InputStream in = connection.getInputStream();
//                    StringBuilder response = new StringBuilder();
//                    reader = new BufferedReader(new InputStreamReader(in));
//                    String line;
//                    while ((line = reader.readLine()) != null) {
//                        response.append(line);
//                    }
//                    showResponse(response.toString(), list);
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            // can change something here
//                            mRecycleView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
//                            myAdapter = new RecyclerViewAdapter(MainActivity.this, list, list4, new RecyclerViewAdapter.OnItemClickListener() {
//                                @Override
//                                public void onClick(int pos) {
//                                    Bundle bundle = new Bundle();
//                                    bundle.putInt("pos",pos);
//                                    bundle.putString("URL",(String) list.get(pos).get("url"));
//                                    bundle.putString("id",(String) list4.get(pos).get("id"));
//                                    Intent intent = new Intent(MainActivity.this, WebDetailActivity.class);
//                                    intent.putExtras(bundle);
//                                    startActivity(intent, bundle);
//                                }
//                            });
//                            mRecycleView.setAdapter(myAdapter);
//                        }
//                    });
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            setContentView(R.layout.networkdisconnected);
//                            ImageView imageView = findViewById(R.id.imageView);
//                            imageView.setImageResource(R.drawable.networkdisconnected);
//                            Toast.makeText(MainActivity.this, "请检查网络连接！Master!", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                } finally {
//                    if (reader != null) {
//                        try {
//                            reader.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//        });
//        Thread get_id = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                HttpURLConnection connection = null;
//                BufferedReader reader = null;
//                try {
//                    URL url = new URL("https://news-at.zhihu.com/api/3/news/before/" + String.valueOf(date));
//                    connection = (HttpURLConnection) url.openConnection();
//                    connection.setRequestMethod("GET");
//                    connection.setConnectTimeout(8000);
//                    connection.setReadTimeout(8000);
//                    InputStream in = connection.getInputStream();
//                    StringBuilder response = new StringBuilder();
//                    reader = new BufferedReader(new InputStreamReader(in));
//                    String line;
//                    while ((line = reader.readLine()) != null) {
//                        response.append(line);
//                    }
//                    showResponse_id(response.toString(), list_id);
//                    for(int i = 0; i < list_id.size(); i++)
//                    {
//                        Map<String, Object> map = new HashMap<String, Object>();
//                        String id = list_id.get(i).get("id").toString();
//                        map.put("id", id);
//                        list4.add(map);
//                    }
//                    date_ = String.valueOf(date);
//                    thread_.start();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            setContentView(R.layout.networkdisconnected);
//                            ImageView imageView = findViewById(R.id.imageView);
//                            imageView.setImageResource(R.drawable.networkdisconnected);
//                            Toast.makeText(MainActivity.this, "请检查网络连接！Master!", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                } finally {
//                    if (reader != null) {
//                        try {
//                            reader.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//        });
        final Thread[] thread_refresh = {new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL("https://daily.zhihu.com/api/3/stories/latest");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "正在加载，请稍后", Toast.LENGTH_LONG).show();
                        }
                    });
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    StringBuilder response = new StringBuilder();
                    reader = new BufferedReader(new InputStreamReader(in));
                    String line;

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    showResponse(response.toString(), list5);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // can change something here
                            myAdapter.refresh(list5);
                            Log.d("refresh", list5.toString());
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
                            Toast.makeText(MainActivity.this, "请检查网络连接！Master!", Toast.LENGTH_SHORT).show();
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
        })};

        final Thread[] thread_loadmore = {new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("run", "run");
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                // day的问题

                try {
                    URL url = new URL("https://daily.zhihu.com/api/3/news/before/" + String.valueOf(year_before) + month_use + day_use);

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
                    showResponse(response.toString(), list6);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            myAdapter.add(list6);
                        }
                    });
//                    date = Integer.parseInt(String.valueOf(year_before)+month_use+ day_use);
//                    Log.d("dda", String.valueOf(date));
//                    get_id.start();
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setContentView(R.layout.networkdisconnected);
                            ImageView imageView = findViewById(R.id.imageView);
                            imageView.setImageResource(R.drawable.networkdisconnected);
                            Toast.makeText(MainActivity.this, "请检查网络连接！Master!", Toast.LENGTH_SHORT).show();
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
        })};
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                thread_refresh[0].start();
                 thread_refresh[0] = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HttpURLConnection connection = null;
                        BufferedReader reader = null;
                        try {
                            URL url = new URL("https://daily.zhihu.com/api/3/stories/latest");
                            connection = (HttpURLConnection) url.openConnection();
                            connection.setRequestMethod("GET");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "正在加载，请稍后", Toast.LENGTH_SHORT).show();
                                }
                            });
                            connection.setConnectTimeout(8000);
                            connection.setReadTimeout(8000);
                            InputStream in = connection.getInputStream();
                            StringBuilder response = new StringBuilder();
                            reader = new BufferedReader(new InputStreamReader(in));
                            String line;

                            while ((line = reader.readLine()) != null) {
                                response.append(line);
                            }
                            showResponse(response.toString(), list5);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // can change something here
                                    Toast.makeText(MainActivity.this, "刷新完成！", Toast.LENGTH_SHORT).show();
                                    myAdapter.refresh(list5);

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
                                    Toast.makeText(MainActivity.this, "请检查网络连接！Master!", Toast.LENGTH_SHORT).show();
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
                refreshLayout.finishRefresh(2000);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                switch (day_before) { case 1: day_use = "01";break; case 2: day_use = "02";break; case 3: day_use = "03";break; case 4: day_use = "04";break;
                    case 5: day_use = "05";break; case 6: day_use = "06";break; case 7: day_use = "07";break; case 8: day_use = "08";break;
                    case 9: day_use = "09";break; default: day_use = String.valueOf(day_before); }
                switch (month_before) { case 1: month_use = "01";break; case 2: month_use = "02";break; case 3: month_use = "03";break;
                    case 4: month_use = "04";break; case 5: month_use = "05";break; case 6: month_use = "06";break;
                    case 7: month_use = "07";break; case 8: month_use = "08";break; case 9: month_use = "09";break;
                    case 10: month_use = "10";break; case 11: month_use = "11";break; case 12: month_use = "12";break; }
                    Log.d("dada", String.valueOf(day_before));
                thread_loadmore[0].start();
                if (day_before == 1) {
                    if (month_before == 3) {
                        if ((year_before % 4 == 0) || (year_before % 100 != 0 && year_before % 400 == 0)) {
                            month_before--;
                            day_before = 29;
                        } else {
                            month_before--;
                            day_before = 28;
                        }
                    } else if (month_before == 1) {
                        year_before--;
                        month_before = 12;
                        day_before = 31;
                    } else if (month_before == 2 || month_before == 4 || month_before == 6 || month_before == 8 || month_before == 9 || month_before == 11) {
                        month_before--;
                        day_before = 31;
                    } else {
                        month_before--;
                        day_before = 30;
                    }
                } else {
                    day_before--;
                }Log.d("dada", String.valueOf(day_before));
                thread_loadmore[0] = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("run", "run");
                        HttpURLConnection connection = null;
                        BufferedReader reader = null;
                        // day的问题

                        try {
                            URL url = new URL("https://daily.zhihu.com/api/3/news/before/" + String.valueOf(year_before) + month_use + day_use);

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
                            showResponse(response.toString(), list6);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    myAdapter.add(list6);
                                }
                            });
//                    date = Integer.parseInt(String.valueOf(year_before)+month_use+ day_use);
//                    Log.d("dda", String.valueOf(date));
//                    get_id.start();
                        } catch (Exception e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    setContentView(R.layout.networkdisconnected);
                                    ImageView imageView = findViewById(R.id.imageView);
                                    imageView.setImageResource(R.drawable.networkdisconnected);
                                    Toast.makeText(MainActivity.this, "请检查网络连接！Master!", Toast.LENGTH_SHORT).show();
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
                refreshLayout.finishLoadMore(2000);
            }
        });
        refreshLayout.setScrollBoundaryDecider(new ScrollBoundaryDecider() {
            @Override
            public boolean canRefresh(View content) {
                if (mRecycleView == null) return false;
                if (mRecycleView.computeVerticalScrollOffset()==0)
                    return true;
                return false;
            }

            @Override
            public boolean canLoadMore(View content) {
                if (mRecycleView == null) return false;
                //获取recyclerView的高度
                mRecycleView.getHeight();
                //整个View控件的高度
                int scrollRange = mRecycleView.computeVerticalScrollRange();
                //当前屏幕之前滑过的距离
                int scrollOffset = mRecycleView.computeVerticalScrollOffset();
                //当前屏幕显示的区域高度
                int scrollExtent = mRecycleView.computeVerticalScrollExtent();
                int height = mRecycleView.getHeight();
                if(height>scrollRange){
                    return false;
                }
                if (scrollRange <=scrollOffset+scrollExtent){
                    return true;
                }
                return false;
            }
        });
    }
    public void showResponse(String JsonData, List<Map<String, Object>> list) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            JSONObject jsonObject = new JSONObject(JsonData);
            Map<String, Object> map_date = new HashMap<String, Object>();
            String d = jsonObject.getString("date");
            map_date.put(d,"date");list2.add(map_date);
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
                JSONArray s = jsonObject1.getJSONArray(("images"));
                String images  = s.getString(0);
                map1.put("images", images);
                String type = jsonObject1.getString("type");
                map1.put("type", type);
                String id = jsonObject1.getString("id");
                map1.put("id", id);
                list.add(i, map1);
            }
//            JSONArray jsonArray1 = jsonObject.getJSONArray("top_stories");
//            for (int i = 0; i < jsonArray1.length(); i++) {
//                Map<String, Object> map1 = new HashMap<String, Object>();
//                JSONObject jsonObject2 = jsonArray1.getJSONObject(i);
//                String image_hue = jsonObject2.getString("image_hue");
//                map1.put("image_hue", image_hue);
//                String title = jsonObject2.getString("title");
//                map1.put("title", title);
//                String url = jsonObject2.getString("url");
//                map1.put("url", url);
//                String hint = jsonObject2.getString("hint");
//                map1.put("hint", hint);
//                int ga_prefix = jsonObject2.getInt("ga_prefix");
//                map1.put("ga_prefix", ga_prefix);
//                String image = jsonObject2.getString("image");
//                map1.put("image", image);
//                String type = jsonObject2.getString("type");
//                map1.put("type", type);
//                String id = jsonObject2.getString("id");
//                map1.put("id", id);
//                list.add(i + jsonArray.length(), map1);
//            }
            Log.d("45888", list.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void showResponse_id(String JsonData, List<Map<String, Object>> list) {

        try {

            JSONObject jsonObject = new JSONObject(JsonData);
            JSONArray jsonArray = jsonObject.getJSONArray("stories");
            for (int i = 0; i < jsonArray.length(); i++) {
                Map<String, Object> map1 = new HashMap<String, Object>();
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String id = jsonObject1.getString("id");
                map1.put("id", id);
                list.add(i, map1);
            }
            Log.d("45888", list.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void get_id(String JsonData, List<Map<String, Object>> list) {

        try {

            JSONObject jsonObject = new JSONObject(JsonData);
            JSONArray jsonArray = jsonObject.getJSONArray("stories");
            for (int i = 0; i < jsonArray.length(); i++) {
                Map<String, Object> map1 = new HashMap<String, Object>();
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String id = jsonObject1.getString("id");
                map1.put("id", id);
                list.add(i, map1);
            }
            Log.d("45888", list.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
//    public void showResponse_date(String JsonData, List<Map<String, Object>> list) {
//
//        try {
//            Map<String, Object> map = new HashMap<String, Object>();
//            JSONObject jsonObject = new JSONObject(JsonData);
//            int date = jsonObject.getInt("date");
//            map.put("date", date);list.add(map);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

}