package com.datalife.datalife.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.datalife.datalife.R;
import com.datalife.datalife.activity.HealthMonitorActivity;
import com.datalife.datalife.bean.NewsInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by LG on 2018/2/3.
 */

public class HealthNewsAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<NewsInfo> newsInfos;


    public HealthNewsAdapter(Context context,ArrayList<NewsInfo> newsInfos){
        this.context = context;
        this.newsInfos = newsInfos;
    }

    @Override
    public int getCount() {
        return newsInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return newsInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        if (convertView == null){

            viewHolder = new ViewHolder();

            convertView = LayoutInflater.from(context).inflate(R.layout.newsadapter,null);
            viewHolder.contentTv = (TextView) convertView.findViewById(R.id.tv_content);
            viewHolder.readnumTv = (TextView) convertView.findViewById(R.id.tv_readnum);
            viewHolder.evaluateNum = (TextView) convertView.findViewById(R.id.tv_evaluatenum);
            viewHolder.dateTv = (TextView) convertView.findViewById(R.id.tv_date);
            viewHolder.newsIv = (ImageView) convertView.findViewById(R.id.iv_news);

            convertView.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.contentTv.setText(newsInfos.get(position).getContent());
        viewHolder.readnumTv.setText(newsInfos.get(position).getReadingnum());
        viewHolder.dateTv.setText(newsInfos.get(position).getDate());
        viewHolder.evaluateNum.setText(newsInfos.get(position).getEvaluatenum());

        Picasso.with(viewHolder.newsIv.getContext()).load(newsInfos.get(position).getNewspic()).into(viewHolder.newsIv);


        return convertView;
    }

    class ViewHolder{
        TextView contentTv;
        TextView readnumTv;
        TextView evaluateNum;
        TextView dateTv;
        ImageView newsIv;

    }
}
