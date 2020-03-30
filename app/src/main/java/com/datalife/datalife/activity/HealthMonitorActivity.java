package com.datalife.datalife.activity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.datalife.datalife.R;
import com.datalife.datalife.adapter.FragmentsAdapter;
import com.datalife.datalife.adapter.FragmentsHealthAdapter;
import com.datalife.datalife.app.ProApplication;
import com.datalife.datalife.base.BaseHealthActivity;
import com.datalife.datalife.base.BaseHealthFragment;
import com.datalife.datalife.contract.HealthMonitorContract;
import com.datalife.datalife.fragment.BpFragment;
import com.datalife.datalife.fragment.BtFragment;
import com.datalife.datalife.fragment.ECGFragment;
import com.datalife.datalife.fragment.MeasureFragment;
import com.datalife.datalife.fragment.MonitorInfoFragment;
import com.datalife.datalife.fragment.SPO2HFragment;
import com.datalife.datalife.interf.OnBpListener;
import com.datalife.datalife.interf.OnDataListener;
import com.datalife.datalife.interf.OnHealthClickListener;
import com.datalife.datalife.interf.OnPageListener;
import com.datalife.datalife.interf.OnTitleBarClickListener;
import com.datalife.datalife.service.HcService;
import com.datalife.datalife.util.AlertDialogBuilder;
import com.datalife.datalife.util.DataLifeUtil;
import com.datalife.datalife.widget.CustomTitleBar;
import com.datalife.datalife.widget.CustomViewPager;
import com.linktop.DeviceType;
import com.linktop.MonitorDataTransmissionManager;
import com.linktop.constant.BluetoothState;
import com.linktop.whealthService.MeasureType;

import butterknife.BindView;

/**
 * Created by LG on 2018/1/18.
 * 健康检测仪演示界面
 */

public class HealthMonitorActivity extends BaseHealthActivity implements MonitorDataTransmissionManager.OnServiceBindListener,OnDataListener,
        ViewPager.OnPageChangeListener,OnPageListener,ServiceConnection {

    @BindView(R.id.view_pager)
    public CustomViewPager viewPager;
    @BindView(R.id.btn_measure)
    Button btnMeasure;
    @BindView(R.id.btn_upload_data)
    Button btnUploadData;

    private final SparseArray<BaseHealthFragment> sparseArray = new SparseArray<>();
    private int mPosition;
    public boolean isShowUploadButton;
    private MyBpTextChange myBpTextChange;//实现接口
    private MyBtTextChange myBtTextChange;//实现接口
    private MySpo2hTextChange mySpo2hTextChange;//实现接口
    private MyEcgTextChange myEcgTextChange;//实现接口
    public HcService mHcService;
    private MonitorInfoFragment mMonitorInfoFragment;

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HcService.BLE_STATE:
                    final int state = (int) msg.obj;
                    Log.e("Message", "receive state:" + state);
                    if (state == BluetoothState.BLE_NOTIFICATION_ENABLED) {
                        mHcService.dataQuery(HcService.DATA_QUERY_SOFTWARE_VER);
                    } else {
                        mMonitorInfoFragment.onBleState(state);
                    }
                    break;
            }
        }
    };
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mHcService = ((HcService.LocalBinder) service).getService();
        mHcService.setHandler(mHandler);
        mHcService.initBluetooth();
        //UI
        FragmentsHealthAdapter adapter = new FragmentsHealthAdapter
                (getSupportFragmentManager());
        getMenusFragments();
        adapter.setFragments(sparseArray);
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mHcService = null;
    }

    public interface MyBpTextChange {
        public void onHidePic();
    }
    public interface MyBtTextChange {
        public void onHidePic();
    }
    public interface MySpo2hTextChange {
        public void onHidePic();
    }
    public interface MyEcgTextChange {
        public void onHidePic();
    }

    public void setOnBpChangeLisener(MyBpTextChange onChangeLisener){
        myBpTextChange = onChangeLisener;
    }
    public void setOnBtChangeLisener(MyBtTextChange onChangeLisener){
        myBtTextChange = onChangeLisener;
    }
    public void setOnSpo2hChangeLisener(MySpo2hTextChange onChangeLisener){
        mySpo2hTextChange = onChangeLisener;
    }
    public void setOnEcgChangeLisener(MyEcgTextChange onChangeLisener){
        myEcgTextChange = onChangeLisener;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_monitor);

        viewPager.setCanScroll(false);//设置viewpager不能滑动
        viewPager.setOffscreenPageLimit(4);
        viewPager.setPageMargin(10);
        viewPager.addOnPageChangeListener(this);
        viewPager.setCurrentItem(0, false);
        //绑定服务，
        // 类型是 HealthMonitor（HealthMonitor健康检测仪），
        if (ProApplication.isUseCustomBleDevService) {
            Intent serviceIntent = new Intent(this, HcService.class);
            bindService(serviceIntent, this, BIND_AUTO_CREATE);
        } else {
            //绑定服务，
            // 类型是 HealthMonitor（HealthMonitor健康检测仪），
            MonitorDataTransmissionManager.getInstance().bind(DeviceType.HealthMonitor, this);
        }
    }

    @Override
    protected void onDestroy() {
        if (ProApplication.isUseCustomBleDevService) {
            unbindService(this);
        } else {
            //解绑服务
            MonitorDataTransmissionManager.getInstance().unBind();
        }
        ProApplication.isShowUploadButton.set(false);
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        viewPager.setCurrentItem(0);
    }

    @Override
    public void onServiceBind() {
        /*
        * 为避免某些不是自己所要的设备出现在蓝牙设备扫描列表中，需要调用该API去设置蓝牙设备名称前缀白名单。
        * 蓝牙设备扫描时以白名单内的字段作为设备名前缀的设备将被添加到蓝牙设备扫描列表中，其余的过滤。
        * PS：不使用该API，设备列表将不过滤。
        *     若使用该API，请代入有效的资源ID。
        * */
        MonitorDataTransmissionManager.getInstance().setScanDevNamePrefixWhiteList(this,R.array.health_monitor_dev_name_prefixes);
        //服务绑定成功后加载各个测量界面
        FragmentsHealthAdapter adapter = new FragmentsHealthAdapter(getSupportFragmentManager());
        getMenusFragments();
        adapter.setFragments(sparseArray);
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onServiceUnbind() {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mPosition = position;
        Log.e("onPageSelected", "position:" + mPosition + ", isShowUploadButton ? " + isShowUploadButton);
        btnUploadData.setVisibility((mPosition > 0 && isShowUploadButton) ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    public void clickUploadData(View v) {
        if (mPosition > 0) {
            sparseArray.get(mPosition).uploadData();
        }
    }

    private void getMenusFragments() {
        mMonitorInfoFragment = new MonitorInfoFragment();
        sparseArray.put(0, mMonitorInfoFragment);
        sparseArray.put(1, new BpFragment());
        sparseArray.put(2, new BtFragment());
        sparseArray.put(3, new SPO2HFragment());
        sparseArray.put(4, new ECGFragment());
    }

    public void reset() {
        viewPager.setCanScroll(true);
        btnMeasure.setText("点击开始测量");
    }

    @Override
    public void onBack() {

        onBackMeasuring();
        viewPager.setCurrentItem(0);
    }

    @Override
    public void onPage(int page) {
        viewPager.setCurrentItem(page);
        mPosition = page;

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            onBackMeasuring();

            if (viewPager.getCurrentItem() == DataLifeUtil.PAGE_HOME){
                finish();
            }else{
                HidePic();
            }
            return true;
        }
        return  super.onKeyDown(keyCode, event);
    }

    private void onBackMeasuring(){
        if (ProApplication.isUseCustomBleDevService){
            if(mHcService.getBleDevManager().isMeasuring()){
                new AlertDialogBuilder
                        (this)
                        .setTitle("提示")
                        .setMessage("测量中，若要退出界面请先停止测量。")
                        .setPositiveButton("好的", null)
                        .create().show();
                return;
            }
        }else{
            if (MonitorDataTransmissionManager.getInstance().isMeasuring()) {
                new AlertDialog.Builder(this)
                        .setTitle("提示")
                        .setMessage("测量中，若要退出界面请先停止测量。")
                        .setPositiveButton("好的", null)
                        .create().show();
                return;
            }
        }
    }


    /**
     * 隐藏引导图片
     */
    public void HidePic(){
        myBpTextChange.onHidePic();
        myBtTextChange.onHidePic();
        myEcgTextChange.onHidePic();
        mySpo2hTextChange.onHidePic();
    }

}
