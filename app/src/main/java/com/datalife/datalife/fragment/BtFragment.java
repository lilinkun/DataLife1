package com.datalife.datalife.fragment;

import android.databinding.ObservableBoolean;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.datalife.datalife.R;
import com.datalife.datalife.activity.HealthMonitorActivity;
import com.datalife.datalife.base.BaseHealthFragment;
import com.datalife.datalife.bean.Bt;
import com.datalife.datalife.interf.OnBpListener;
import com.datalife.datalife.interf.OnHealthClickListener;
import com.datalife.datalife.interf.OnTitleBarClickListener;
import com.datalife.datalife.util.DataLifeUtil;
import com.datalife.datalife.widget.CircleProgressBar;
import com.linktop.MonitorDataTransmissionManager;
import com.linktop.infs.OnBtResultListener;
import com.linktop.whealthService.MeasureType;
import com.linktop.whealthService.task.BtTask;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;
import lib.linktop.obj.DataFile;
import lib.linktop.obj.LoadBtBean;
import lib.linktop.sev.HmLoadDataTool;

/**
 * Created by LG on 2018/1/18.
 * 体温
 */
public class BtFragment extends MeasureFragment
        implements OnBtResultListener ,HealthMonitorActivity.MyBtTextChange{

    private final ObservableBoolean isUnitF = new ObservableBoolean(false);
    private Bt model;
    private BtTask mBtTask;

    private final int BTDATA = 0x111;

    @BindView(R.id.switch_unit)
    SwitchCompat switchUnit;
    @BindView(R.id.tv_temp_start)
    TextView mTempStartTv;
    @BindView(R.id.tv_temp)
    TextView mTempTv;
    @BindView(R.id.btn_starttest)
    Button mStartTestBtn;
    @BindView(R.id.bt_progress)
    CircleProgressBar mProgressBar;
    @BindView(R.id.iv_opration)
    ImageView iv_opration;
    @BindView(R.id.tv_bt)
    TextView mBtTv;
    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case BTDATA:
                    DataLifeUtil.startAlarm(getActivity());
                    if(model.getTemp() > 43 || model.getTemp() < 35){
                        toast("请准确测量");
                        return;
                    }else if (model.getTemp() >= 38){
                        mBtTv.setText("发烧");
                    } else{
                        mBtTv.setText("正常");
                    }
                    DataLifeUtil.saveBtData(getActivity(),String.valueOf(model.getTemp()));

                    mTempTv.setText(model.getTemp()+"");
                    mStartTestBtn.setText(getString(R.string.start));
                    double temp = model.getTemp()- 35;
                    if (progressBar != null && !progressBar.isShown()) {
                        progressBar.setVisibility(View.VISIBLE);
                        progressBar.setProgress((int)temp);
                        if (model.getTemp() >= 38){
                            progressBar.setProgressDrawable(getResources().getDrawable(R.color.red));
                        }
                    }
                    mProgressBar.setProgress(temp);
                    break;
            }
        }
    };


    public BtFragment() {
    }

    @Override
    public boolean startMeasure() {
        if(mBtTask!= null) {
            mBtTask.start();
        } else {
            MonitorDataTransmissionManager.getInstance().startMeasure(MeasureType.BT);
            mStartTestBtn.setText("测量中");
        }
        return true;
    }

    @Override
    public void stopMeasure() {
        mStartTestBtn.setText("开始");
    }

    @Override
    protected void getchange(String str) {
        mStartTestBtn.setText(str);
    }

    @Override
    public void clickUploadData(View v) {
    }

    @Override
    public String getTitle() {
        return "体温（Body temperature）";
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mHcService != null) {
            mBtTask = mHcService.getBleDevManager().getBtTask();
            mBtTask.setOnBtResultListener(this);
        } else {
            MonitorDataTransmissionManager.getInstance().setOnBtResultListener(this);
        }

        ((HealthMonitorActivity)getActivity()).setOnBtChangeLisener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected int getlayoutId() {
        return R.layout.fragment_bt;
    }

    @Override
    protected void initEventAndData() {
//        onBtListener = (OnBtListener) getActivity();
        model = new Bt();
        switchUnit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isUnitF.set(isChecked);
            }
        });
    }

    @OnClick({R.id.btn_starttest,R.id.iv_head_left,R.id.iv_head_right,R.id.iv_opration})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_starttest:

//                if (MonitorDataTransmissionManager.getInstance().isMeasuring()){
//                    stopMeasure();
//                }else{
                    clickMeasure(view);
//                }

                break;
            case R.id.iv_head_left:
                onDataListener.onBack();
                break;

            case R.id.iv_head_right:

                if (iv_opration != null && !iv_opration.isShown()){
                    iv_opration.setVisibility(View.VISIBLE);
                }

                break;

            case R.id.iv_opration:

                if (iv_opration != null && iv_opration.isShown()){
                    iv_opration.setVisibility(View.GONE);
                }

                break;
        }
    }

    @Override
    public void reset() {
        model.reset();
    }

    /*
    * 默认返回单位为摄氏度的温度值，若需要华氏度的温度值，根据公式转换
    * 本Demo使用Databinding的方式，详情请看该布局的温度显示控件TextView
    * */
    @Override
    public void onBtResult(double tempValue) {
        model.setTemp(tempValue);
        model.setTs(System.currentTimeMillis() / 1000L);
        myHandler.sendEmptyMessage(BTDATA);
        resetState();
    }

    @Override
    public void showPromptMessage(int resId) {

    }

    @Override
    public void showPromptMessage(String message) {

    }

    @Override
    public void onHidePic() {
        if (iv_opration != null && iv_opration.isShown()){
            iv_opration.setVisibility(View.GONE);
        }else{
            onDataListener.onBack();
        }
    }
}

