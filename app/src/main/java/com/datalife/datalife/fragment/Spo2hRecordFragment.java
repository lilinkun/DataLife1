package com.datalife.datalife.fragment;

import android.graphics.Point;
import android.view.View;
import android.widget.ListView;

import com.datalife.datalife.R;
import com.datalife.datalife.adapter.Spo2hRecordAdapter;
import com.datalife.datalife.base.BaseRecordFragment;
import com.datalife.datalife.bean.ChartEntity;
import com.datalife.datalife.dao.Spo2hDao;
import com.datalife.datalife.db.DBManager;
import com.datalife.datalife.util.DensityUtil;
import com.datalife.datalife.widget.LineChart;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by LG on 2018/2/5.
 */

public class Spo2hRecordFragment extends BaseRecordFragment {

    @BindView(R.id.chart)
    LineChart lineChart;
    @BindView(R.id.lv_spo2h)
    ListView listView;

    public Spo2hRecordFragment(){

    }

    @Override
    protected int getlayoutId() {
                return R.layout.fragment_spo2hrecord;
    }


    @Override
    protected void initEventAndData() {
        lineChart.setVisibility(View.VISIBLE);

        Point point = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(point);

        int s = DensityUtil.dip2px(getActivity(),(float) point.x);
        toast("s:" + s );

        List<ChartEntity> data = new ArrayList<>();
        List<Float> sd = new ArrayList<>();
        int sa = 0;

        List<String> strs = new ArrayList<>();

        strs.add("2018-1-14");
        strs.add("2018-1-15");
        strs.add("2018-1-16");
        strs.add("2018-1-17");
        strs.add("2018-1-18");

        for(int i =0;i<strs.size();i++){
            sa = (int)(i+Math.random()*(20-i+1));
            data.add(new ChartEntity(strs.get(i), (float) (sa*20)));
        }

        lineChart.setData(data,60);
        lineChart.startAnimation(2000);

        DBManager dbManager = DBManager.getInstance(getActivity());
        List<Spo2hDao> userList = dbManager.queryUserList();
        ArrayList<Spo2hDao> spo2hDaos = new ArrayList<>();
        for (Spo2hDao spo2hDao : userList) {
            spo2hDaos.add(spo2hDao);
        }

        if (spo2hDaos.size() != 0){
            listView.setAdapter(new Spo2hRecordAdapter(getActivity(),spo2hDaos));
        }

    }

    @Override
    public String getTitle() {
        return "血氧";
    }

    @Override
    public void showPromptMessage(int resId) {

    }

    @Override
    public void showPromptMessage(String message) {

    }

}
