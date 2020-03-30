package com.datalife.datalife.fragment;

import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.ObservableFloat;
import android.databinding.ObservableInt;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.datalife.datalife.R;
import com.datalife.datalife.activity.ECGLargeChartActivity;
import com.datalife.datalife.activity.HealthMonitorActivity;
import com.datalife.datalife.base.BaseHealthFragment;
import com.datalife.datalife.bean.ECG;
import com.datalife.datalife.binding.ObservableString;
import com.datalife.datalife.util.DataLifeUtil;
import com.datalife.datalife.widget.EcgWaveView;
import com.linktop.MonitorDataTransmissionManager;
import com.linktop.infs.OnEcgResultListener;
import com.linktop.whealthService.MeasureType;
import com.linktop.whealthService.task.EcgTask;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import lib.linktop.obj.DataFile;
import lib.linktop.obj.LoadECGBean;
import lib.linktop.sev.HmLoadDataTool;

/**
 * Created by LG on 2018/1/18.
 */

public class ECGFragment extends MeasureFragment
        implements OnEcgResultListener,HealthMonitorActivity.MyEcgTextChange{

    @BindView(R.id.fl_openECGLarge)
    FrameLayout mOpenECGLargeFl;
    @BindView(R.id.ecg_draw_chart)
    EcgWaveView mEcgWaveView;
    @BindView(R.id.tv_ecg_hr)
    TextView mEcgHrTv;
    @BindView(R.id.tv_ecg_hrv)
    TextView mEcgHrvTv;
    @BindView(R.id.tv_ecg_mood)
    TextView mEcgMoodTv;
    @BindView(R.id.tv_ecg_br)
    TextView mEcgBrTv;
    @BindView(R.id.tv_rrMin)
    TextView mEcgRrMinTv;
    @BindView(R.id.tv_rrMax)
    TextView mEcgRrMaxTv;
    @BindView(R.id.iv_opration)
    ImageView ivOpration;
    @BindView(R.id.btn_starttest)
    Button mStartTestBtn;
    @BindView(R.id.iv_wave)
    ImageView mWaveIV;

    private ECG model;
    private EcgTask mEcgTask;
    private final ObservableInt pagerSpeed = new ObservableInt(1);
    private final ObservableFloat calibration = new ObservableFloat(1.0f);
    private final ObservableString pagerSpeedStr = new ObservableString("25mm/s");
    private final ObservableString calibrationStr = new ObservableString("10mm/mV");
    private final StringBuilder ecgWaveBuilder = new StringBuilder();

    public final int ECGHANDDATA = 0x3313;

    private Handler myEcgHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case ECGHANDDATA:

                    DataLifeUtil.startAlarm(getActivity());

                    mEcgBrTv.setText(model.getBr() + "");
                    mEcgRrMaxTv.setText(model.getRrMax() + "");
                    mEcgRrMinTv.setText(model.getRrMin() + "");
                    mEcgHrTv.setText(model.getHr() + "");
                    mEcgMoodTv.setText(model.getMood() + "");
                    mEcgHrvTv.setText(model.getHrv() + "");

                    try{
                        DataLifeUtil.saveEcgData(getActivity(),DataLifeUtil.serialize(model));
                    }catch (Exception e){
                        toast("心电图数据未保存");
                    }

                    break;

                case 222:

                    int rr = msg.getData().getInt("rr");
                    int rrmax = msg.getData().getInt("rrmax");

                    break;
            }
        }
    };


    public ECGFragment() {
    }

    @Override
    public boolean startMeasure() {
        if (mEcgTask != null) {
            if (mEcgTask.isModuleExist()) {
                mEcgTask.initEcgTg();
                mEcgTask.start();
                return true;
            } else {
                toast("This Device's ECG module is not exist.");
                return false;
            }
        } else {
            if (MonitorDataTransmissionManager.getInstance().isEcgModuleExist()) {
                MonitorDataTransmissionManager.getInstance().startMeasure(MeasureType.ECG);
                mStartTestBtn.setText("测量中");
                return true;
            } else {
                toast("This Device's ECG module is not exist.");
                return false;
            }
        }
    }

    @Override
    public void stopMeasure() {
        if (mEcgTask != null) {
            mEcgTask.stop();
        } else {
            MonitorDataTransmissionManager.getInstance().stopMeasure();
            mStartTestBtn.setText("开始");
        }
    }

    @Override
    protected void getchange(String str) {
        mStartTestBtn.setText(str);
    }

    @Override
    public void clickUploadData(View v) {
        if (model == null || model.isEmptyData()) {
            toast("不能上传空数据");
            return;
        }
//        HmLoadDataTool.getInstance().uploadData(DataFile.DATA_ECG, model);
    }

    @Override
    public String getTitle() {
        return "心电图（ECG）";
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);if (mHcService != null) {
            mEcgTask = mHcService.getBleDevManager().getEcgTask();
            mEcgTask.setOnEcgResultListener(this);
            //It is better to set user information who is measuring ECG.
            //if it is not,SDK will use a default user information.
            // Of course,it may reduce the accuracy of the measurement results.
            mEcgTask.setUserInfo("liguo", 27, 166, 65, false);
        } else {
            MonitorDataTransmissionManager.getInstance().setECGUerInfo("liguo", 27, 166, 65, false);
            MonitorDataTransmissionManager.getInstance().setOnEcgResultListener(this);
        }
        ((HealthMonitorActivity)this.getActivity()).setOnEcgChangeLisener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected int getlayoutId() {
        return R.layout.fragment_ecg;
    }

    @Override
    protected void initEventAndData() {
        model = new ECG();
        mEcgWaveView.setCalibration(calibration.get());
        mEcgWaveView.setPagerSpeed(pagerSpeed.get());
    }

    @Override
    public void reset() {
        model.reset();
        ecgWaveBuilder.setLength(0);
        mEcgWaveView.clear();
    }

    long startTs = 0L;
    int i = 0;

    /*
    * 心电图数据点
    * */
    @Override
    public synchronized void onDrawWave(int wave) {
//        i++;
//        if (startTs == 0L) startTs = System.currentTimeMillis();
        //将数据点在心电图控件里描绘出来
        mEcgWaveView.preparePoint(wave);
        //将数据点存入容器，查看大图使用
        ecgWaveBuilder.append(wave).append(",");
    }

    @Override
    public void onSignalQuality(int i) {

    }

    @Override
    public void onAvgHr(int avgHr) {
        model.setHr(avgHr);
//        myHandler.sendEmptyMessage(222);
//        mEcgHrTv.setText(avgHr + "");
    }

    @Override
    public void onRRMax(int rr) {
        model.setRrMax(rr);
//        myHandler.sendEmptyMessage(222);
//        mEcgRrMaxTv.setText(rr + "");
    }

    @Override
    public void onRRMin(int rr) {
        model.setRrMin(rr);
//        Message message = new Message();
//        Bundle bundle = new Bundle();
//        message.setData(bundle);
//        bundle.putInt("rr",rr);
//        message.what = 222;
//        myEcgHandler.sendMessage(message);
//        mEcgRrMinTv.setText(rr + "");
    }

    @Override
    public void onHrv(int hrv) {
        model.setHrv(hrv);
    }

    @Override
    public void onMood(int mood) {
        model.setMood(mood);
    }

    @Override
    public void onBr(int br) {
        model.setBr(br);
    }

    /*
    * 心电图测量持续时间,该回调一旦触发说明一次心电图测量结束
    * */
    @Override
    public void onEcgDuration(long duration) {
        final long l = (System.currentTimeMillis() - startTs) / 1000L;
        final long l1 = i / l;
        Log.e("onEcgDuration", "" + l1);
        startTs = 0L;
        i = 0;
        model.setDuration(duration);
        model.setTs(System.currentTimeMillis() / 1000L);
        String ecgWave = ecgWaveBuilder.toString();
        ecgWave = ecgWave.substring(0, ecgWave.length() - 1);
        model.setWave(ecgWave);

        myEcgHandler.sendEmptyMessage(ECGHANDDATA);

        resetState();
    }

    @OnClick({R.id.fl_openECGLarge, R.id.btn_starttest,R.id.iv_head_left,R.id.iv_head_right,R.id.iv_opration,R.id.iv_wave})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.fl_openECGLarge:
                openECGLarge();
                break;
            case R.id.btn_starttest:
                clickMeasure(view);
                break;
            case R.id.iv_head_left:
                onDataListener.onBack();
                break;
            case R.id.iv_head_right:

                if (ivOpration != null && !ivOpration.isShown()){
                    ivOpration.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.iv_opration:
                if (ivOpration != null && ivOpration.isShown()){
                    ivOpration.setVisibility(View.GONE);
                }
                break;
        }

    }

    @Override
    public void uploadData() {
        if (model == null || model.isEmptyData()) {
            toast("不能上传空数据");
            return;
        }


//        final LoadECGBean loadBean = new LoadECGBean(model.getTs(), model.getDuration(), model.getRrMax(), model.getRrMin(),
//                model.getHr(), model.getHrv(), model.getMood(), model.getBr(), model.getWave());
//        HmLoadDataTool.getInstance().uploadData(DataFile.DATA_ECG, loadBean);
    }

    public void openECGLarge() {
        Intent intent = new Intent(mActivity, ECGLargeChartActivity.class);
        intent.putExtra("pagerSpeed", pagerSpeed.get());
        intent.putExtra("calibration", calibration.get());
        intent.putExtra("model", model);
        startActivity(intent);
    }

    /*
    * 点击设置时间基准(走纸速度)
    * 该值反应心电图x轴的幅度，设置的值这里没做保存，请自行保存，以便下次启动该页面时自动设置已保存的值
    * */
    public void clickSetPagerSpeed() {
        int checkedItem = pagerSpeed.get() - 1;
        onShowSingleChoiceDialog(true, "时间基准", R.array.ecg_pager_speed, checkedItem);
    }

    /*
    * 点击设置增益
    * 该值反应心电图y轴的幅度，设置的值这里没做保存，请自行保存，以便下次启动该页面时自动设置已保存的值
    * */
    public void clickSetCalibration() {
        int checkedItem = calibration.get() == 0.5f ? 0 : (int) calibration.get();
        onShowSingleChoiceDialog(false, "增益", R.array.ecg_calibration, checkedItem);
    }

    private void onShowSingleChoiceDialog(final boolean isTimeRef, String titleResId, int itemsId, int checkedItem) {
        final String[] items = getResources().getStringArray(itemsId);
        new AlertDialog.Builder(mActivity)
                .setTitle(titleResId)
                .setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isTimeRef) {
                            pagerSpeed.set(which + 1);
                            pagerSpeedStr.set(items[which]);
                        } else {
                            calibration.set(which == 0 ? 0.5f : which);
                            calibrationStr.set(items[which]);
                        }
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.close, null)
                .create()
                .show();
    }

    @Override
    public void showPromptMessage(int resId) {

    }

    @Override
    public void showPromptMessage(String message) {

    }


    @Override
    public void onHidePic() {
        if (ivOpration != null && ivOpration.isShown()){
            ivOpration.setVisibility(View.GONE);
        }else{
            onDataListener.onBack();
        }
    }

}
