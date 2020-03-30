package com.datalife.datalife.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.datalife.datalife.R;

import java.util.ArrayList;

/**
 * Created by LG on 2018/2/7.
 */

public class MemberItemAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<String> arrayList = new ArrayList<>();


    public MemberItemAdapter(Context context,ArrayList<String> list){
        this.mContext = context;
        this.arrayList = list;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_member,null);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.tv_member_item);
            convertView.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.textView.setText(arrayList.get(position));


        return convertView;
    }

    class ViewHolder{
        TextView textView;
    }
}
