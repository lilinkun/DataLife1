package com.datalife.datalife.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.datalife.datalife.R;
import com.datalife.datalife.dao.Spo2hDao;

import java.util.ArrayList;

/**
 * Created by LG on 2018/2/9.
 */

public class Spo2hRecordAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Spo2hDao> spo2hDaos ;

    public Spo2hRecordAdapter(Context context,ArrayList<Spo2hDao> spo2hDaos){
            this.mContext = context;
            this.spo2hDaos = spo2hDaos;
    }

    @Override
    public int getCount() {
        return spo2hDaos.size();
    }

    @Override
    public Object getItem(int position) {
        return spo2hDaos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null){

            convertView = LayoutInflater.from(mContext).inflate(R.layout.spo2h_record_adapter,null);

            viewHolder = new ViewHolder();

            viewHolder.mTimeTesting = (TextView) convertView.findViewById(R.id.tv_testing_time);

            viewHolder.mWeightTv = (TextView) convertView.findViewById(R.id.tv_weight_value);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.mTimeTesting.setText(spo2hDaos.get(position).getCreateTime());
        viewHolder.mWeightTv.setText(spo2hDaos.get(position).getSpo2hValue());

        return convertView;
    }

    class ViewHolder{
        TextView mTimeTesting;
        TextView mWeightTv;

    }
}
