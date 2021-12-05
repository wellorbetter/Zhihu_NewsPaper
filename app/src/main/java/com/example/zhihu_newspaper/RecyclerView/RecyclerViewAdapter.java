package com.example.zhihu_newspaper.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zhihu_newspaper.R;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.LongViewHolder> {
    public Context mContext;
    public List<Map<String, Object>>list;
    public List<Map<String, Object>>list4;
    private OnItemClickListener mListener;

    public RecyclerViewAdapter(Context context ,List<Map<String, Object>>list,List<Map<String, Object>>list4,OnItemClickListener listener){
        this.mContext = context;
        this.list = list;
        this.mListener = listener;
        this.list4 = list4;
    }


    @NonNull
    @Override
    public LongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new LongViewHolder(LayoutInflater.from(mContext).inflate(R.layout.recycler_item, parent, false));
    }




            @Override
    public void onBindViewHolder(@NonNull LongViewHolder holder, int i) {

            holder.textView1.setText((CharSequence) list.get(i).get("title"));
            holder.textView2.setText((CharSequence) list.get(i).get("hint"));
            if(list.get(i).get("images") != null)
            {
                Glide.with(holder.itemView)
                        .load(list.get(i).get("images"))
                        .into(holder.imageView);
            }
            else
            {
                Glide.with(holder.itemView)
                        .load(list.get(i).get("image"))
                        .into(holder.imageView);

            }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClick(i);
                }
            });

    }


    @Override
    public int getItemCount() {
        Log.d("data",list.toString());
        Log.d("data", String.valueOf(list.size()));
        return list.size();
    }
    static class LongViewHolder extends RecyclerView.ViewHolder{
        private TextView textView1;private ImageView imageView;
        private TextView textView2;private TextView textView_date;

        public LongViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_date = (TextView) itemView.findViewById(R.id.textView_date);
            textView1 = (TextView) itemView.findViewById(R.id.tv);
            textView2 = (TextView) itemView.findViewById(R.id.tv_1);
            imageView = itemView.findViewById(R.id.im);
        }
    }
    static class DateViewHolder extends RecyclerView.ViewHolder{
        private TextView textView1;

        public DateViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = (TextView) itemView.findViewById(R.id.textview);
        }
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    public interface OnItemClickListener{
        void onClick(int pos);

    }
    public void add(List<Map<String, Object>> addMessageList) {
        //增加数据
        int position = list.size();
        list.addAll(list.size(),addMessageList);
        Log.d("loadmore", list.toString());
        Log.d("loadmore", addMessageList.toString());
        notifyItemInserted(position);
    }

    public void refresh(List<Map<String, Object>> newList) {
        //刷新数据
        list.removeAll(list);
        list.addAll(newList);
//        for(int i = 0; i < newList.size(); i++)
//        {
//            Log.d("45455454","464445");
//
//            Map<String, Object> map1 = new HashMap<String, Object>();
//            String image_hue = newList.get(i).get("image_hue").toString();
//            map1.put("image_hue", image_hue);
//            String title = newList.get(i).get("title").toString();
//            map1.put("title", title);
//            String url = newList.get(i).get("url").toString();
//            map1.put("url", url);
//            String hint = newList.get(i).get("hint").toString();
//            map1.put("hint", hint);
//            String ga_prefix = newList.get(i).get("ga_prefix").toString();
//            map1.put("ga_prefix", ga_prefix);
//            String images = newList.get(i).get("images").toString();
//            map1.put("images", images);
//            String type = newList.get(i).get("type").toString();
//            map1.put("type", type);
//            String id = newList.get(i).get("id").toString();
//            map1.put("id", id);
//            Log.d("map", map1.toString());
//            list.add(map1);
//        }
        notifyDataSetChanged();
    }


}
