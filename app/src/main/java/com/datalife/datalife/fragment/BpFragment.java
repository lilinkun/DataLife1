package com.datalife.datalife.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.datalife.datalife.R;
import com.datalife.datalife.activity.HealthMonitorActivity;
import com.datalife.datalife.base.BaseHealthFragment;
import com.datalife.datalife.bean.Bp;
import com.datalife.datalife.interf.OnResponseListener;
import com.datalife.datalife.util.DataLifeUtil;
import com.datalife.datalife.widget.CustomColorBar;
import com.datalife.datalife.widget.CustomSeekbar;
import com.linktop.MonitorDataTransmissionManager;
import com.linktop.infs.OnBpResultListener;
import com.linktop.whealthService.MeasureType;
import com.linktop.whealthService.task.BpTask;

import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lib.linktop.obj.DataFile;
import lib.linktop.obj.LoadBpBean;
import lib.linktop.sev.HmLoadDataTool;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by LG on 2018/1/18.
 * 血压
 */
public class BpFragment extends MeasureFragment
        implements OnBpResultListener,OnResponseListener ,HealthMonitorActivity.MyBpTextChange{

    private Bp model;
    private BpTask mBpTask;

    @BindView(R.id.tv_sbp)
    TextView mSbpTv;
    @BindView(R.id.tv_dbp)
    TextView mDbpTv;
    @BindView(R.id.tv_hr)
    TextView mHrTv;
    @BindView(R.id.btn_starttest)
    Button mClickTestBtn;
    @BindView(R.id.ic_round_test)
    ImageView MIcRoundIv;
    @BindView(R.id.pb_dbp)
    ProgressBar mDbpPb;
    @BindView(R.id.pb_sbp)
    ProgressBar mSbpPb;
    @BindView(R.id.iv_bp_operation)
    ImageView mIvOperation;

    private final int BPDATA = 0x123;

    private Context context;

    private ArrayList<String> volume_sections = new ArrayList<String>();

    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case BPDATA:

                    DataLifeUtil.startAlarm(getActivity());

                    mSbpTv.setText(model.getSbp() + "");
                    mDbpTv.setText(model.getDbp() + "");
                    mHrTv.setText(model.getDbp() + "/" + model.getSbp());
                    mClickTestBtn.setText(R.string.start);

                    mDbpPb.setVisibility(View.VISIBLE);
                    mSbpPb.setVisibility(View.VISIBLE);
                    mDbpPb.setProgress(model.getDbp());
                    mSbpPb.setProgress(model.getSbp());

                    try{
                        DataLifeUtil.saveBpData(getActivity(),DataLifeUtil.serialize(model));
                    }catch (Exception e){
                        toast("血压没保存");
                    }

                    break;
            }
        }
    };


    public BpFragment(){

    }

    @Override
    public boolean startMeasure() {
        if (mBpTask != null) {
            if (mHcService.getBleDevManager().getBatteryTask().getPower() < 20) {
                toast("设备电量过低，请充电\nLow power.Please charge.");
                setStart();
                return false;
            }
            mBpTask.start();
        } else {
            if (MonitorDataTransmissionManager.getInstance().getBatteryValue() < 20) {
                toast("设备电量过低，请充电\nLow power.Please charge.");
                setStart();
                return false;
            }
            MonitorDataTransmissionManager.getInstance().startMeasure(MeasureType.BP);

            mClickTestBtn.setText("测量中");

                Animation rotate = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_anim);
                if (rotate != null) {
                    MIcRoundIv.startAnimation(rotate);
                }  else {
                    MIcRoundIv.setAnimation(rotate);
                    MIcRoundIv.startAnimation(rotate);
                }

        }
        return true;
    }

    @Override
    public void stopMeasure() {
        if (mBpTask != null) {
            mBpTask.stop();
        } else {
            MonitorDataTransmissionManager.getInstance().stopMeasure();
            setStart();
        }
    }

    public void setStart(){
        mClickTestBtn.setText("开始");
        if (MIcRoundIv != null) {
            MIcRoundIv.clearAnimation();
        }
    }

    @Override
    protected void getchange(String str) {
        mClickTestBtn.setText(str);

        if (str.equals("测量中")) {
            Animation rotate = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_anim);
            if (rotate != null) {
                MIcRoundIv.startAnimation(rotate);
            } else {
                MIcRoundIv.setAnimation(rotate);
                MIcRoundIv.startAnimation(rotate);
            }
        }
    }

    @Override
    public void clickUploadData(View v) {
        if (model == null || model.isEmptyData()) {
            toast("不能上传空数据");
            return;
        }
//        HmLoadDataTool.getInstance().uploadData(DataFile.DATA_BP, model);
    }

    @Override
    public String getTitle() {
        return "血压（Blood pressure）";
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mHcService != null) {
            mBpTask = mHcService.getBleDevManager().getBpTask();
            mBpTask.setOnBpResultListener(this);
        } else {
            //设置血压测量回调接口
            MonitorDataTransmissionManager.getInstance().setOnBpResultListener(this);
        }
        ((HealthMonitorActivity)getActivity()).setOnBpChangeLisener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected int getlayoutId() {
        return R.layout.fragment_bp;
    }

    @Override
    protected void initEventAndData() {
        model = new Bp();
    }

    @Override
    public void reset() {
        model.reset();
    }


    @OnClick({R.id.iv_head_left,R.id.btn_starttest,R.id.iv_head_right,R.id.iv_bp_operation})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_starttest:
//                onDataListener.onTesting();
                clickMeasure(view);


                break;
            case R.id.iv_head_left:
                onDataListener.onBack();
                break;

            case R.id.iv_head_right:
                if (mIvOperation != null && !mIvOperation.isShown()){
                    mIvOperation.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.iv_bp_operation:
                if (mIvOperation != null && mIvOperation.isShown()){
                    mIvOperation.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    public void onBpResult(final int systolicPressure, final int diastolicPressure, final int heartRate) {
        //测量时间（包括本demo其他测量项目的测量时间），既可以以点击按钮开始测试的那个时间为准，
        // 也可以以测量结果出来时为准，看需求怎么定义
        //这里demo演示，为了方便，采用后者。
        MIcRoundIv.clearAnimation();
        model.setTs(System.currentTimeMillis() / 1000L);
        model.setSbp(systolicPressure);
        model.setDbp(diastolicPressure);
        model.setHr(heartRate);
        myHandler.sendEmptyMessage(BPDATA);
        resetState();
    }

    @Override
    public void onLeakError(int errorType) {
        resetState();
        Observable.just(errorType)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer error) {
                        int textId = 0;
                        switch (error) {
                            case 0:
                                textId = R.string.leak_and_check;
                                setStart();
                                break;
                            case 1:
                                textId = R.string.measurement_void;
                                setStart();
                                break;
                            default:
                                break;
                        }
                        if (textId != 0)
                            Toast.makeText(getContext(), getString(textId), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void showPromptMessage(int resId) {

    }

    @Override
    public void showPromptMessage(String message) {

    }

    @Override
    public void onTouchResponse(int volume) {

    }

    @Override
    public void onHidePic() {
        if (mIvOperation != null && mIvOperation.isShown()){
            mIvOperation.setVisibility(View.GONE);
        }else{
            onDataListener.onBack();
        }
    }

}
