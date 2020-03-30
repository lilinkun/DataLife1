package com.datalife.datalife.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.datalife.datalife.R;
import com.datalife.datalife.bean.Bluetoothbean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LG on 2018/1/28.
 */

public class BlueToothAdapter extends BaseAdapter {

    private Context context;
    private List<Bluetoothbean> bluetoothbeans;

    public BlueToothAdapter(Context context ,ArrayList<Bluetoothbean> bluetoothbeans){
        this.context = context;
        this.bluetoothbeans = bluetoothbeans;
    }

    @Override
    public int getCount() {
        return bluetoothbeans.size();
    }

    @Override
    public Object getItem(int position) {
        return bluetoothbeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_item,parent, false);

            viewHolder = new ViewHolder();

            viewHolder.tv_mac = (TextView) convertView.findViewById(R.id.tv_bluemac);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_bluename);


            viewHolder.tv_mac.setText(bluetoothbeans.get(position).getMacStr());
            viewHolder.tv_name.setText(bluetoothbeans.get(position).getBlueName());


        }else{
            convertView.getTag();
        }


        return convertView;
    }

    class ViewHolder{
        TextView tv_name;
        TextView tv_mac;
    }
}
