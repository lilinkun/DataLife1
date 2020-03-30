package com.datalife.datalife.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.datalife.datalife.R;
import com.datalife.datalife.activity.HealthMonitorActivity;
import com.datalife.datalife.bean.SPO2H;
import com.datalife.datalife.dao.Spo2hDao;
import com.datalife.datalife.db.DBManager;
import com.datalife.datalife.util.DataLifeUtil;
import com.datalife.datalife.widget.OxWaveView;
import com.linktop.MonitorDataTransmissionManager;
import com.linktop.infs.OnSPO2HResultListener;
import com.linktop.whealthService.MeasureType;
import com.linktop.whealthService.task.OxTask;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LG on 2018/1/18.
 */

public class SPO2HFragment extends MeasureFragment
        implements OnSPO2HResultListener, HealthMonitorActivity.MySpo2hTextChange {

    private SPO2H model;
    private OxTask mOxTask;

    @BindView(R.id.tv_spo2h)
    TextView mSpo2hTv;
//    @BindView(R.id.tv_spo2h_hr)
//    TextView mSpo2hHrTv;
    @BindView(R.id.oxWave)
    OxWaveView mOxWaveTv;
    @BindView(R.id.iv_spo2h)
    ImageView ivspo2h;
    @BindView(R.id.btn_starttest)
    Button mStartTestBtn;
    @BindView(R.id.tv_spo2h_value)
    TextView mSpo2hValueTv;
    @BindView(R.id.progressbar)
    ProgressBar mProgressBar;
    @BindView(R.id.ic_heartrate)
    ImageView mIvHeartrate;


    private final int SPO2HDATA = 0x222;


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case SPO2HDATA:

                    DataLifeUtil.startAlarm(getActivity());
//                    mSpo2hHrTv.setText(model.getHr() + "");
                    mSpo2hTv.setText(model.getValue() + "");
                    if (mProgressBar != null && !mProgressBar.isShown()){
                        mProgressBar.setVisibility(View.VISIBLE);
                    }

                    mSpo2hValueTv.setText(model.getValue() + "");

                    DataLifeUtil.saveSpo2hData(getActivity(),model.getValue()+"");

                    DBManager dbManager = DBManager.getInstance(getActivity());
                    Spo2hDao spo2hDao = new Spo2hDao();
                    spo2hDao.setCreateTime(DataLifeUtil.getCurrentTime());
                    spo2hDao.setName("liguo");
                    spo2hDao.setSpo2hValue(model.getValue()+"");
                    dbManager.insertUser(spo2hDao);

                    break;
            }

        }
    };

    public SPO2HFragment() {
    }

    @Override
    public boolean startMeasure() {
        if (mOxTask != null) {
            mOxTask.start();
        } else {
            MonitorDataTransmissionManager.getInstance().startMeasure(MeasureType.SPO2H);

            mStartTestBtn.setText("测量中");
            if (mIvHeartrate != null && mIvHeartrate.isShown()) {
                mIvHeartrate.setVisibility(View.GONE);
            }

        }
        return true;
    }

    @Override
    public void stopMeasure() {
        if (mOxTask != null) {
            mOxTask.stop();
        } else {
            MonitorDataTransmissionManager.getInstance().stopMeasure();
        }
    }

    @Override
    protected void getchange(String str) {
        mStartTestBtn.setText(str);
        if (str != null  && str.equals("测量中")){
            if (mIvHeartrate != null && mIvHeartrate.isShown()) {
                mIvHeartrate.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void clickUploadData(View v) {
    }

    @Override
    public String getTitle() {
        return "血氧 & 心率（SPO₂H & Heart rate）";
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mHcService != null) {
            mOxTask = mHcService.getBleDevManager().getOxTask();
            mOxTask.setOnSPO2HResultListener(this);
        } else {
            MonitorDataTransmissionManager.getInstance().setOnSPO2HResultListener(this);
        }

        ((HealthMonitorActivity)getActivity()).setOnSpo2hChangeLisener(this);
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected int getlayoutId() {
        return R.layout.fragment_spo2h;
    }

    @Override
    protected void initEventAndData() {
        model = new SPO2H();
    }

    @Override
    public void reset() {
        model.reset();
        mOxWaveTv.clear();
    }

    @Override
    public void onSPO2HResult(int spo2h, int heartRate) {
        model.setTs(System.currentTimeMillis() / 1000L);
        model.setValue(spo2h);
        model.setHr(heartRate);

        handler.sendEmptyMessage(SPO2HDATA);
        resetState();
    }

    @OnClick({R.id.btn_starttest,R.id.iv_head_left,R.id.iv_head_right,R.id.iv_spo2h})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_starttest:
                clickMeasure(view);
                break;
            case R.id.iv_head_left:

                onDataListener.onBack();
                break;

            case R.id.iv_head_right:

                if (ivspo2h != null && !ivspo2h.isShown()){
                    ivspo2h.setVisibility(View.VISIBLE);
                }

                break;

            case R.id.iv_spo2h:


                if (ivspo2h != null && ivspo2h.isShown()){
                    ivspo2h.setVisibility(View.GONE);
                }

                break;
        }
    }


    @Override
    public void onSPO2HWave(int value) {
        mOxWaveTv.preparePoints(value);
    }

    @Override
    public void showPromptMessage(int resId) {

    }

    @Override
    public void showPromptMessage(String message) {

    }


    @Override
    public void onHidePic() {
        if (ivspo2h != null && ivspo2h.isShown()){
            ivspo2h.setVisibility(View.GONE);
            return;
        }else{
            onDataListener.onBack();
        }
    }
}