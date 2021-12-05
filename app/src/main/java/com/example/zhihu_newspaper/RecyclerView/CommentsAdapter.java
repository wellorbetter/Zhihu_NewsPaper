package com.example.zhihu_newspaper.RecyclerView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zhihu_newspaper.R;

import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.LongViewHolder>{

    public Context mContext;
    public List<Map<String, Object>> list_long;
    public List<Map<String, Object>> list_short;
    public List<Map<String, Object>> list_comments;
    private OnItemClickListener mListener;
    private int size_long;
    private int size_short;
    private final int LONG = 1;
    private final int SHORT = 2;


    public CommentsAdapter(Context context ,List<Map<String, Object>>list_long,List<Map<String, Object>>list_short,List<Map<String, Object>> list_comments ,OnItemClickListener listener){
        this.mContext = context;
        this.list_long = list_long;
        this.mListener = listener;
        this.list_short = list_short;
        this.list_comments = list_comments;
        size_long = Integer.valueOf(list_comments.get(0).get("long_comments").toString());
        size_short = Integer.valueOf(list_comments.get(0).get("short_comments").toString());
        Log.d("999", String.valueOf(size_short));
        Log.d("999", String.valueOf(size_long));
    }
    @NonNull
    @Override
    public LongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new LongViewHolder(LayoutInflater.from(mContext).inflate(R.layout.comments_long, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LongViewHolder holder, int i) {
        if(size_long != 0)
        {
            if(i == 0)
            {
                holder.tv_long_top.setText(size_long + "条长评");
            }
            holder.tv1.setText(list_long.get(i).get("author").toString());
            holder.tv2.setText(list_long.get(i).get("content").toString());
            Glide.with(holder.itemView).load(list_long.get(i).get("avatar").toString()).into(holder.imageView);
            holder.tv3.setText(list_long.get(i).get("time").toString());
            size_long--;
        }
        else if (size_short != 0)
        {
            Log.d("555", String.valueOf(list_long.size()));
            if(i - list_long.size() == 0)
            {
                holder.tv_short_top.setText(size_short + "条短评");
            }
            holder.tv1.setText(list_short.get(i - list_long.size()).get("author").toString());
            holder.tv2.setText(list_short.get(i - list_long.size()).get("content").toString());
            Glide.with(holder.itemView).load(list_short.get(i - list_long.size()).get("avatar").toString()).into(holder.imageView);
            holder.tv3.setText(list_short.get(i - list_long.size()).get("time").toString());
            size_short--;
        }
    }
//    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
//        Log.d("调用", "调用");
//        View view = null;
//        RecyclerView.ViewHolder holder = null;
//        if(LONG == i)
//        {
//            view = LayoutInflater.from(mContext).inflate(R.layout.comments_long,parent,false);
//            holder = new LongViewHolder(view);
//        }
//        else {
//            if (SHORT == i)
//            {
//                view = LayoutInflater.from(mContext).inflate(R.layout.comments_short,parent,false);
//                holder = new shortViewHolder(view);
//            }
//            else{
//                view = LayoutInflater.from(mContext).inflate(R.layout.show,parent,false);
//                holder = new showViewHolder(view);
//            }
//        }
//        assert holder != null;
//        return holder;
//    }


//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i){
//        Log.d("thechange", String.valueOf(i));
//        if (viewHolder instanceof LongViewHolder)
//        {
//            if (i == 0)
//            {
//                ((LongViewHolder) viewHolder).tv_top.setText(list_comments.get(0).get("comments_long") +"条长评");
//            }
//            ((LongViewHolder) viewHolder).tv1.setText(list_long.get(i).get("author").toString());
//            ((LongViewHolder) viewHolder).tv2.setText(list_long.get(i).get("content").toString());
//            Glide.with(viewHolder.itemView).load(list_long.get(i).get("avatar").toString()).into(((LongViewHolder) viewHolder).imageView);
//            ((LongViewHolder) viewHolder).tv3.setText(list_long.get(i).get("time").toString());
//        }
//        else if (viewHolder instanceof shortViewHolder)
//        {
//            if (i - list_long.size() == 0)
//            {
//                ((shortViewHolder) viewHolder).tv_top.setText(list_comments.get(0).get("short_comments") +"条长评");
//            }
//            Log.d("++", "draw_short");
//            Log.d("///*", String.valueOf(i - list_long.size()));
//            ((shortViewHolder) viewHolder).tv1.setText(list_short.get(i ).get("author").toString());
//            ((shortViewHolder) viewHolder).tv2.setText(list_short.get(i).get("content").toString());
//            Glide.with(viewHolder.itemView).load(list_short.get(i).get("avatar").toString()).into(((shortViewHolder) viewHolder).imageView);
//            ((shortViewHolder) viewHolder).tv3.setText(list_short.get(i).get("time").toString());
//        }
//        else{
//            ((showViewHolder) viewHolder).tv1.setText("没有更多了！");
//        }
//    }
    @Override
    public int getItemCount() {
        int number = list_long.size() + list_short.size();
        return number;
    }

    static class LongViewHolder extends RecyclerView.ViewHolder{
        private TextView tv1;private TextView tv2;private TextView tv3;private TextView tv_long_top;
        private ImageView imageView; private Button button1; private Button button2;private TextView tv_short_top;
        public LongViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_long_top = (TextView) itemView.findViewById(R.id.tv_long_top);tv_short_top = (TextView) itemView.findViewById(R.id.tv_short_top);
        tv1 = (TextView) itemView.findViewById(R.id.textView_long_1);tv2 = (TextView) itemView.findViewById(R.id.textView_long_2);
        tv3 = (TextView) itemView.findViewById(R.id.textView_long_3);imageView = (ImageView) itemView.findViewById(R.id.image_long);
        button1 = (Button) itemView.findViewById(R.id.btn_long_1);button2 = (Button) itemView.findViewById(R.id.btn_long_2);
        }
    }

//    class shortViewHolder extends RecyclerView.ViewHolder{
//
//        private TextView tv1;private TextView tv2;private TextView tv3;private TextView tv_top;
//        private ImageView imageView; private Button button1; private Button button2;
//        public shortViewHolder(@NonNull View itemView) {
//            super(itemView);
//            tv_top = (TextView) itemView.findViewById(R.id.tv_short_top);
//            tv1 = (TextView) itemView.findViewById(R.id.textView_short_1);tv2 = (TextView) itemView.findViewById(R.id.textView_short_2);
//            tv3 = (TextView) itemView.findViewById(R.id.textView_short_3);imageView = (ImageView) itemView.findViewById(R.id.image_short);
//            button1 = (Button) itemView.findViewById(R.id.btn_short_1);button2 = (Button) itemView.findViewById(R.id.btn_short_2);
//        }
//    }
//    class showViewHolder extends RecyclerView.ViewHolder{
//
//        private TextView tv1;
//        public showViewHolder(@NonNull View itemView) {
//            super(itemView);
//            tv1 = (TextView) itemView.findViewById(R.id.textview);
//        }
//    }
    public interface OnItemClickListener{
        void onClick(int pos);
    }
//    public void add(List<Map<String, Object>> addMessageList) {
//        //增加数据
//        int position = list_long.size();
//        list_long.addAll(position, addMessageList);
//        notifyItemInserted(position);
//    }
//
//    public void refresh(List<Map<String, Object>> newList) {
//        //刷新数据
//        list_long.removeAll(list_long);
//        list_long.addAll(newList);
//        notifyDataSetChanged();
//    }
//    @Override
//    public int getItemViewType(int pos)
//    {
//        int ret = 0;
//        Log.d("++", "判断");
//        ;
//        if(size_long != 0)
//        {
//            Log.d("change", String.valueOf(size_long));
//            size_long--;
//            ret = LONG;
//        }
//        else if (size_short != 0)
//        {
//            Log.d("change", String.valueOf(size_short));
//            size_short--;
//            ret = SHORT;
//        }
//        Log.d("ret", String.valueOf(ret));
//        return super.getItemViewType(pos);
//    }
}
