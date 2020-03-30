package com.datalife.datalife.fragment;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.datalife.datalife.R;
import com.datalife.datalife.activity.MainActivity;
import com.datalife.datalife.activity.RecordActivity;
import com.datalife.datalife.activity.ShowDownloadDataActivity;
import com.datalife.datalife.adapter.BindDevListAdapter;
import com.datalife.datalife.adapter.MemberItemAdapter;
import com.datalife.datalife.app.ProApplication;
import com.datalife.datalife.base.BaseHealthFragment;
import com.datalife.datalife.bean.Bp;
import com.datalife.datalife.bean.ECG;
import com.datalife.datalife.bean.FamilyUserInfo;
import com.datalife.datalife.bean.LoginBean;
import com.datalife.datalife.binding.ObservableString;
import com.datalife.datalife.contract.HealthMonitorContract;
import com.datalife.datalife.interf.OnPageListener;
import com.datalife.datalife.interf.OnTitleBarClickListener;
import com.datalife.datalife.interf.onTitleBarSetListener;
import com.datalife.datalife.presenter.HealthMonitorPresenter;
import com.datalife.datalife.presenter.LoginPresenter1;
import com.datalife.datalife.service.HcService;
import com.datalife.datalife.util.AlertDialogBuilder;
import com.datalife.datalife.util.DataLifeUtil;
import com.datalife.datalife.util.DefaultPicEnum;
import com.datalife.datalife.util.DeviceData;
import com.datalife.datalife.util.PermissionManager;
import com.datalife.datalife.util.UIHelper;
import com.datalife.datalife.widget.CustomRecyclerView;
import com.datalife.datalife.widget.RoundImageView;
import com.linktop.MonitorDataTransmissionManager;
import com.linktop.constant.BluetoothState;
import com.linktop.constant.DeviceInfo;
import com.linktop.constant.WareType;
import com.linktop.infs.OnBatteryListener;
import com.linktop.infs.OnBleConnectListener;
import com.linktop.infs.OnDeviceInfoListener;
import com.linktop.infs.OnDeviceVersionListener;
import com.linktop.whealthService.BleDevManager;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import lib.linktop.common.CssSubscriber;
import lib.linktop.common.LogU;
import lib.linktop.common.ResultPair;
import lib.linktop.intf.OnCssSocketRunningListener;
import lib.linktop.obj.Device;
import lib.linktop.obj.LoadBean;
import lib.linktop.obj.Member;
import lib.linktop.sev.CssServerApi;
import lib.linktop.sev.HmLoadDataTool;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by LG on 2018/1/18.
 */
public class MonitorInfoFragment  extends BaseHealthFragment
        implements OnDeviceVersionListener, OnBleConnectListener, OnBatteryListener, OnDeviceInfoListener,HealthMonitorContract.HealthMonitorView {

    private static final int REQUEST_OPEN_BT = 0x23;
    private int MESSAGE_BLE_CONNECTING_DEVICE = 0x234;

    private final ObservableString btnText = new ObservableString("打开蓝牙");
    private final ObservableString power = new ObservableString("");
    private final ObservableString id = new ObservableString("");//当前选定的设备id
    private final ObservableString key = new ObservableString("");//当前选定的设备key
    private final ObservableString softVer = new ObservableString("");
    private final ObservableString hardVer = new ObservableString("");
    private final ObservableString firmVer = new ObservableString("");
    private final ObservableBoolean isLogin = ProApplication.isLogin;
    private final ObservableInt isDevBind = new ObservableInt(0);
    private boolean showScanList = false;
    private BleDeviceListDialogFragment mBleDeviceListDialogFragment;
    private OnPageListener onPageListener;

    private BindDevListAdapter mAdapter;
    private String MCONNECTSTR = "connectStr";
    private int CONNECTHANDLER = 0x1231;

    private boolean isCssSocketOpen;
    private Subscription subscription;

    private  List<FamilyUserInfo> familyUserInfos= MainActivity.familyUserInfos;
    private LoginBean loginBean= MainActivity.loginBean;

    @BindView(R.id.btn_connect)
    TextView mConnectBtn;
    @BindView(R.id.tv_bp_total)
    TextView mBpTotalTv;
    @BindView(R.id.tv_temp)
    TextView mTempTv;
    @BindView(R.id.tv_spo2h)
    TextView mSpo2hTv;
    @BindView(R.id.tv_sbpanddbp)
    TextView mSbpAndDbp;
    @BindView(R.id.tv_hr)
    TextView mHrTv;
    @BindView(R.id.tv_hrv)
    TextView mHrvTv;
    @BindView(R.id.tv_br)
    TextView mBrTv;
    @BindView(R.id.tv_account)
    TextView mAccountTv;
    @BindView(R.id.iv_username)
    RoundImageView roundImageView;
    @BindView(R.id.ll_change_member)
    LinearLayout mChangeMemberLL;
    @BindView(R.id.ll_member)
    LinearLayout mMemberLayout;
    @BindView(R.id.ll_electrocardiogram)
    LinearLayout mElectrocarDiogramLayout;

    String deviceId = "";


    HealthMonitorPresenter healthMonitorPresenter = new HealthMonitorPresenter(getActivity());

    private Handler myHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == CONNECTHANDLER){
                if (msg.getData() == null)
                    return;
                String resStr = msg.getData().getString(MCONNECTSTR);
                if (resStr == null){
                    return;
                }
                if (resStr != null || resStr.trim().length() != 0) {
                    mConnectBtn.setText(resStr);
                }
            }else if(msg.what == MESSAGE_BLE_CONNECTING_DEVICE){
                Toast.makeText(mActivity, "蓝牙连接中...", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public MonitorInfoFragment() {
    }

    @Override
    public String getTitle() {
        return "设备基本信息（Device information）";
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (ProApplication.isUseCustomBleDevService) {
            BleDevManager bleDevManager = mHcService.getBleDevManager();
            mHcService.setOnDeviceVersionListener(this);
            bleDevManager.getBatteryTask().setBatteryStateListener(this);
            bleDevManager.getDeviceTask().setOnDeviceInfoListener(this);
        } else {
            MonitorDataTransmissionManager.getInstance().setOnBleConnectListener(this);
            MonitorDataTransmissionManager.getInstance().setOnBatteryListener(this);
            MonitorDataTransmissionManager.getInstance().setOnDevIdAndKeyListener(this);
            MonitorDataTransmissionManager.getInstance().setOnDeviceVersionListener(this);
        }
    }

    @Override
    public void onDestroy() {
        //demo中把该界面当成主界面（相对于健康检测仪而言，当然也可以认为是上层的Activity），当销毁主界面前，应该把蓝牙连接断掉
        if (MonitorDataTransmissionManager.getInstance().isConnected())
            MonitorDataTransmissionManager.getInstance().disConnectBle();
        if (isLogin.get()) {
            //该长连接，设备连接蓝牙时启动，设备断开蓝牙停止。
            HmLoadDataTool.getInstance().destroyCssSocket();
            isDevBind.set(0);
        }
        super.onDestroy();
    }

    protected int getlayoutId() {
        return R.layout.fragment_health_info;
    }

    @Override
    protected void initEventAndData() {

        healthMonitorPresenter.onCreate();
        healthMonitorPresenter.attachView(this);

        initData();

        if (loginBean != null){
            mAccountTv.setText(loginBean.getUser_name());
        }

        onPageListener = (OnPageListener) getActivity();

        mConnectBtn.setVisibility(View.VISIBLE);

        onBleState(MonitorDataTransmissionManager.getInstance().getBleState());
        if (isLogin.get()) {
            mAdapter = new BindDevListAdapter(getContext(), id);
//            customRecyclerView.setAdapter(mAdapter);
            getDevList(false);
        }
    }

    @OnClick({R.id.rl_temp,R.id.rl_oxygen,R.id.rl_ecg,R.id.rl_heartrate,R.id.iv_head_left,R.id.btn_connect,R.id.ll_history_record,R.id.ll_change_member})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.rl_temp:
                onPageListener.onPage(DataLifeUtil.PAGE_TEMP);
                break;

            case R.id.rl_oxygen:
                onPageListener.onPage(DataLifeUtil.PAGE_SPO2H);
                break;


            case R.id.rl_ecg:
                onPageListener.onPage(DataLifeUtil.PAGE_ECG);
                break;


            case R.id.rl_heartrate:
                onPageListener.onPage(DataLifeUtil.PAGE_BP);
                break;

            case R.id.iv_head_left:
                getActivity().finish();
                break;

            case R.id.btn_connect:
                clickConnect();
                break;

            case R.id.ll_history_record:
//                onPageListener.onPage(DataLifeUtil.PAGE_RECORD);
                UIHelper.launcher(getActivity(), RecordActivity.class);
                break;

            case R.id.ll_change_member:

                if (familyUserInfos != null){
                    ArrayList arrayList = new ArrayList();

                    for (int i = 0;i <familyUserInfos.size();i++) {
                        arrayList.add(familyUserInfos.get(i).getMember_Name());
                    }

                    View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.member_popup_listview, null);

                    ListView listView = (ListView) contentView.findViewById(R.id.lv_member);
                    listView.setVisibility(View.VISIBLE);
                    listView.setAdapter(new MemberItemAdapter(getActivity(),arrayList));

                    final PopupWindow popupWindow = new PopupWindow(contentView,
                            LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.WRAP_CONTENT, true);
                    popupWindow.setContentView(contentView);
                    popupWindow.showAsDropDown(mMemberLayout);
                    popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            mElectrocarDiogramLayout.setBackgroundResource(R.color.transparent);
                        }
                    });
                    mElectrocarDiogramLayout.setBackgroundResource(R.drawable.ic_transparent_bg);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            mAccountTv.setText(familyUserInfos.get(position).getMember_Name());
                            roundImageView.setImageResource(DefaultPicEnum.getPageByValue(familyUserInfos.get(position).getMember_Portrait()).getResPic());
                            popupWindow.dismiss();

                        }
                    });
                }
                break;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean permissionGranted = PermissionManager.isPermissionGranted(grantResults);
        switch (requestCode) {
            case PermissionManager.requestCode_location:
                if (permissionGranted) {
                    try {
                        Thread.sleep(1000L);
                        clickConnect();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getContext(), "没有定位权限", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_OPEN_BT:

                //蓝牙启动结果
                //蓝牙启动结果
                Toast.makeText(getContext(), resultCode == Activity.RESULT_OK ? "蓝牙已打开" : "蓝牙打开失败", Toast.LENGTH_SHORT).show();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void reset() {
        power.set("");
        id.set("");
        key.set("");
        softVer.set("");
        hardVer.set("");
        firmVer.set("");
    }

    /******
     * 以上两个回调值，可以根据设备ID保存在SP里，
     * 这样可以在某些未连接设备但已知设备ID的情况下，
     * 直接获取并显示设备的软硬件版本号
     * 但是切记，设备升级软硬件，会更新版本号，所以每次连接蓝牙设备都应该读取软硬件版本号，
     * 若有做本地保存，及时更新本地保存，才能保证任何情况下显示版本号都是最新的。
     **************************************************************/
    @Override
    public void onBLENoSupported() {
        Toast.makeText(getContext(), "蓝牙不支持", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onOpenBLE() {
        startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), REQUEST_OPEN_BT);
    }

    @Override
    public void onBleState(int bleState) {
        Message message = new Message();
        message.what = CONNECTHANDLER;
        Bundle bundle = new Bundle();
        switch (bleState) {

            case BluetoothState.BLE_CLOSED:
//                mConnectBtn.setText("打开蓝牙");
                bundle.putString(MCONNECTSTR,"打开蓝牙");
                reset();
                isDevBind.set(0);
                break;
            case BluetoothState.BLE_OPENED_AND_DISCONNECT:
                reset();
                isDevBind.set(0);

                bundle.putString(MCONNECTSTR,"点击连接");

                /**
                 * 自动连接
                 */
                clickConnect();
                break;
            case BluetoothState.BLE_CONNECTING_DEVICE:
                bundle.putString(MCONNECTSTR,"连接中");
                break;
            case BluetoothState.BLE_CONNECTED_DEVICE:
                bundle.putString(MCONNECTSTR,"已连接");
                break;
        }

        message.setData(bundle);
        myHandler.sendMessage(message);
    }

    @Override
    public void onUpdateDialogBleList() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mBleDeviceListDialogFragment != null && mBleDeviceListDialogFragment.isShowing()) {
                    mBleDeviceListDialogFragment.refresh();
                }
            }
        });
    }

    /*
    * 设备插着USB充电线，未充满电的状态
    *
    *
    * */
    @Override
    public void onBatteryCharging() {
        power.set("充电中...");
//        mPowerTv.setText("充电中...");
    }

    /*
    * 设备拔掉USB充电线，正常使用
    * */
    @Override
    public void onBatteryQuery(int batteryValue) {
//        mPowerTv.setText(batteryValue + "%");
    }

    /*
    * 设备插着USB充电线，已充满电的状态
    * */
    @Override
    public void onBatteryFull() {
//        mPowerTv.setText("已充满");
    }

    /**
     * 定时定次向服务器请求设备列表，以确认设备是否绑定。
     * 循环几次，每次间隔多长时间，自行讨论确定。
     */
    private void loopCheckDevIsBind() {
        subscription = Observable.interval(1, 5, TimeUnit.SECONDS)
                .take(5)
                .flatMap(new Func1<Long, Observable<ResultPair<List<Device>>>>() {
                    @Override
                    public Observable<ResultPair<List<Device>>> call(Long aLong) {
                        aLong++;
                        LogU.e("CSSHttpUtil", "第:" + aLong + "次向服务器确认是否绑定");
                        return CssServerApi.getDevList();
                    }
                })
                .flatMap(new Func1<ResultPair<List<Device>>, Observable<Device>>() {
                    @Override
                    public Observable<Device> call(ResultPair<List<Device>> listResultPair) {
                        if (listResultPair.first == 200) {
                            final List<Device> second = listResultPair.second;
                            return Observable.from(second);
                        }
                        return null;
                    }
                })
                .filter(new Func1<Device, Boolean>() {
                    @Override
                    public Boolean call(Device device) {
                        return null != device && device.getDevId().equals(id.get());
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Device>() {
                    private boolean isBindSuccess = false;

                    @Override
                    public void onCompleted() {
                        if (!isBindSuccess) {
                            toast("绑定失败");
                            MonitorDataTransmissionManager.getInstance().disConnectBle();
                            HmLoadDataTool.getInstance().destroyCssSocket();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Device device) {
                        isBindSuccess = device != null;
                        if (isBindSuccess) {
                            id.set(device.getDevId());
//                            mDeviceId.setText(device.getDevId());
                            isDevBind.set(1);
                            mAdapter.addItem(device);
                            toast("绑定成功");
                            subscription.unsubscribe();
                        }
                    }
                });

    }

    private void getDevList(final boolean isToast) {
        CssServerApi.getDevList()
                .subscribe(new CssSubscriber<List<Device>>() {
                    @Override
                    public void onNextRequestSuccess(List<Device> devices) {
                        mAdapter.clearItems();
                        mAdapter.addItems(devices);
                        checkDevIsBind(devices, isToast);
                    }

                    @Override
                    public void onNextRequestFailed(int status) {
                        switch (status) {
                            case -1:
                                toast("网络断开了，检查网络");
                                break;
                            default:
                                toast("请求失败");
                                break;
                        }
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("getDevList - onError", e.getMessage());
                    }
                });
    }

    private void checkDevIsBind(List<Device> list, final boolean isToast) {
        // 登录时，页面刚创建 id 为空，确定 所获取的绑定设备列表是否有设备
        if (TextUtils.isEmpty(id.get())) {
            //若有设备，拣选列表第一个设备作为当前选定设备。
            if (list.size() > 0) {
                final Device currDev = list.get(0);
                id.set(currDev.getDevId());
//                mDeviceId.setText(currDev.getDevId());
                isDevBind.set(currDev.isPrimaryBind() ? 1 : 2);
            }
        } else
            Observable.from(list)
                    .filter(new Func1<Device, Boolean>() {
                        @Override
                        public Boolean call(Device device) {
                            Log.e("checkDevIsBind - call", "mDevId:" + id.get() + ", deviceId:" + device.getDevId());
                            return device.getDevId().equals(id.get());
                        }
                    })
                    .subscribe(new Subscriber<Device>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(Device device) {
//                            mDeviceId.setText(device.getDevId());
                            isDevBind.set(device.isPrimaryBind() ? 1 : 2);
                            if (isToast)
                                toast(device.isPrimaryBind() ? "绑定成功" : "关注成功");
                        }
                    });
    }

    /**
     * 点击 绑定 or 解绑 设备
     */
    public void clickBindDev() {
        if (isDevBind.get() > 0) {
            CssServerApi.unbindDev(id.get())
                    .subscribe(new CssSubscriber<Integer>() {
                        @Override
                        public void onNextRequestSuccess(Integer state) {
                            switch (state) {
                                case 0:
                                    toast("设备解绑成功");
                                    //该长连接，设备连接蓝牙时启动，设备断开蓝牙停止。
                                    if (MonitorDataTransmissionManager.getInstance().isConnected()) {
                                        MonitorDataTransmissionManager.getInstance().disConnectBle();
                                        HmLoadDataTool.getInstance().destroyCssSocket();
                                    }
                                    isDevBind.set(0);
                                    getDevList(false);
                                    break;
                                case 1:
                                    toast("帐号格式不对");
                                    break;
                                case 2:
                                    toast("设备id格式不正确 / api_key来源非法");
                                    break;
                                case 3:
                                    toast("已经是解绑状态");
                                    break;
                                case 4:
                                    toast("设备不属于当前帐号");
                                    break;
                                case 5:
                                    toast("子帐号解关注成功");
                                    if (MonitorDataTransmissionManager.getInstance().isConnected()) {
                                        MonitorDataTransmissionManager.getInstance().disConnectBle();
                                        HmLoadDataTool.getInstance().destroyCssSocket();
                                    }
                                    isDevBind.set(0);
                                    getDevList(false);
                                    break;
                                case 6:
                                    toast("设备io参数以及主帐号不匹配（实际上和2有些重复）");
                                    break;
                                default:
                                    break;
                            }
                        }

                        @Override
                        public void onNextRequestFailed(int status) {
                            switch (status) {
                                case -1:
                                    toast("请检查网络连接");
                                    break;
                                default:
                                    toast("请求失败");
                                    break;
                            }
                        }

                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            toast("请求失败，error：" + e.toString());
                        }
                    });
        } else {
            if (!MonitorDataTransmissionManager.getInstance().isConnected()) {
                toast("请先通过蓝牙连接设备");
                return;
            }
            CssServerApi.bindDev(id.get(), key.get())
                    .subscribe(new CssSubscriber<Integer>() {
                        @Override
                        public void onNextRequestSuccess(Integer state) {
                            switch (state) {
                                case 0://绑定成功,重新获取设备列表确认绑定
                                    getDevList(true);
                                    break;
                                case 1:// 等待设备回复（绑定中）；
                                    toast("等待设备回复（绑定中）");
                                    loopCheckDevIsBind();
                                    break;
                                case 2://工厂（设备）未登记
                                    toast("工厂（设备）未登记");
                                    break;
                                case 3://设备已被其他人绑定，可提示用户关注设备
                                    new AlertDialog.Builder(getActivity())
                                            .setCancelable(false)
                                            .setTitle("提示")
                                            .setMessage("该设备已被其他用户绑定，可以选择关注它。")
                                            .setNegativeButton("不了，谢谢", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    MonitorDataTransmissionManager.getInstance().disConnectBle();
                                                    HmLoadDataTool.getInstance().destroyCssSocket();
                                                    dialogInterface.dismiss();
                                                }
                                            })
                                            .setPositiveButton("关注", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    requestPrimaryAcc();
                                                    dialogInterface.dismiss();
                                                }
                                            })
                                            .create().show();
                                    break;
                                case 4://二维码错误
                                    toast("二维码错误");
                                    break;
                                case 5:// apikey 错误
                                    toast("apikey错误");
                                    break;
                                case 6:// app和设备不匹配
                                    toast("app和设备不匹配");
                                    break;
                                default://基本不会出现该种情况，但是，谁知道呢。。。
                                    toast("未知错误,错误码:" + state);
                                    break;
                            }
                        }

                        @Override
                        public void onNextRequestFailed(int status) {
                            switch (status) {
                                case -1:
                                    toast("网络断开了，检查网络");
                                    break;
                                default:
                                    toast("请求失败");
                                    break;
                            }
                        }

                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            toast("请求失败，error：" + e.toString());
                        }
                    });
        }
    }

    /**
     * 点击切换蓝牙连接状态
     */
    public void clickConnect() {
        if (ProApplication.isUseCustomBleDevService) {
            final boolean isObtain = PermissionManager.isObtain(this, PermissionManager.PERMISSION_LOCATION, PermissionManager.requestCode_location);
            if (!isObtain) {
                return;
            } else {
                if (!PermissionManager.canScanBluetoothDevice(getContext())) {
                    new AlertDialogBuilder(mActivity)
                            .setTitle("提示")
                            .setMessage("Android 6.0及以上系统需要打开GPS才能扫描蓝牙设备。")
                            .setNegativeButton(android.R.string.cancel, null)
                            .setPositiveButton("打开GPS", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    PermissionManager.openGPS(mActivity);
                                }
                            }).create().show();
                    return;
                }
            }
            if (mHcService.isConnected) {
                mHcService.disConnect();
            } else {
                final int bluetoothEnable = mHcService.isBluetoothEnable();
                if (bluetoothEnable == -1) {
                    onBLENoSupported();
                } else if (bluetoothEnable == 0) {
                    onOpenBLE();
                } else {
                    mHcService.quicklyConnect();
                }
            }
        } else

        {
            final int bleState = MonitorDataTransmissionManager.getInstance().getBleState();
            Log.e("clickConnect", "bleState:" + bleState);
            switch (bleState) {
                case BluetoothState.BLE_CLOSED:
                    MonitorDataTransmissionManager.getInstance().bleCheckOpen();
                    break;
                case BluetoothState.BLE_OPENED_AND_DISCONNECT:
                    if (MonitorDataTransmissionManager.getInstance().isScanning()) {
                        new AlertDialog.Builder(mActivity)
                                .setTitle("提示")
                                .setMessage("正在扫描设备，请稍后...")
                                .setNegativeButton(android.R.string.cancel, null)
                                .setPositiveButton("停止扫描", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        MonitorDataTransmissionManager.getInstance().scan(false);
                                    }
                                }).create().show();
                    } else {
                        final boolean isObtain = PermissionManager.isObtain(this, PermissionManager.PERMISSION_LOCATION, PermissionManager.requestCode_location);
                        if (isObtain) {
                            if (PermissionManager.canScanBluetoothDevice(getContext())) {
                                if (showScanList) {
                                    connectByDeviceList();
                                } else {
                                    MonitorDataTransmissionManager.getInstance().scan(true);
                                }
                            } else {
                                new AlertDialog.Builder(mActivity)
                                        .setTitle("提示")
                                        .setMessage("Android 6.0及以上系统需要打开GPS才能扫描蓝牙设备。")
                                        .setNegativeButton(android.R.string.cancel, null)
                                        .setPositiveButton("打开GPS", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                PermissionManager.openGPS(mActivity);
                                            }
                                        }).create().show();
                            }
                        }
                    }
                    break;
                case BluetoothState.BLE_CONNECTING_DEVICE:
                    Message message = new Message();
                    message.what = MESSAGE_BLE_CONNECTING_DEVICE;
                    myHandler.sendMessage(message);
                    break;
                case BluetoothState.BLE_CONNECTED_DEVICE:
                case BluetoothState.BLE_NOTIFICATION_DISABLED:
                    toast("没有连接设备");
                    break;
                case BluetoothState.BLE_NOTIFICATION_ENABLED:
                    MonitorDataTransmissionManager.getInstance().disConnectBle();
                    HmLoadDataTool.getInstance().destroyCssSocket();
                    break;
            }
        }
    }

    public void initData(){

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        if (sharedPreferences != null){
            String bt = sharedPreferences.getString("bt","");
            String bp = sharedPreferences.getString("bp","");
            String spo2h = sharedPreferences.getString("spo2h","");
            String ecg = sharedPreferences.getString("ecg","");
            String hr = "";
            String hrv = "";
            String br = "";
            String sbpanddbp = "";
            if (bp != null && bp.trim().length() != 0 && ecg != null &&  ecg.trim().length() != 0) {
                try {
                    Bp bp1 = (Bp) DataLifeUtil.unSerialization(bp);
                    ECG ecg1 = (ECG) DataLifeUtil.unSerialization(ecg);
                    bp = bp1.getSbp() + "/" + bp1.getDbp();
                    sbpanddbp = ecg1.getRrMax() + "/" + ecg1.getRrMin();
                    hr = ecg1.getHr()+"";
                    hrv = ecg1.getHrv()+"";
                    br = ecg1.getBr() + "";
                } catch (Exception e) {
                    Log.e("MonitorInfoFragment:", e.getMessage());
                    e.getMessage();
                }
            }

            mSpo2hTv.setText(spo2h);
            mBpTotalTv.setText(bp);
            mTempTv.setText(bt);
            mSbpAndDbp.setText(sbpanddbp);
            mHrTv.setText(hr);
            mHrvTv.setText(hrv);
            mBrTv.setText(br);
        }

    }


    public void clickShowDownloadData() {
        if (HmLoadDataTool.getInstance().hasMember()) {
            UIHelper.launcher(getActivity(), ShowDownloadDataActivity.class);
        } else {
            toast("请先点击获取家庭成员");
        }
    }

    //获取家庭成员
    public void clickGetFamilyMember() {
        // 单个设备ID获取家庭成员
        CssServerApi.getFamilyMemberList(id.get())
                .subscribe(new CssSubscriber<List<Member>>() {
                    @Override
                    public void onNextRequestSuccess(List<Member> members) {
                        //只要是已绑定的设备，家庭成员列表至少有一个成员，也就是绑定这台设备的用户自己，且自己一定会排在列表首位
                        final Member myMember = members.get(0);
                        //所传入的Member对象一定匹配所建立的CssSocket时所传入的设备ID，否则数据无法上传成功
                        HmLoadDataTool.getInstance().setMember(myMember);
                        toast("家庭成员列表获取成功，请看Log。");
                        Log.e("clickGetFamilyMember", "**********start:devId = " + myMember.devId + "***********");
                        for (Member member : members) {
                            Log.e("clickGetFamilyMember", member.toString());
                        }
                        Log.e("clickGetFamilyMember", "**********end***********");
                    }

                    @Override
                    public void onNextRequestFailed(int status) {

                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
       /*   //若是想要获取设备列表中的所有设备ID对应的家庭成员列表，可以采用rxJava Observable的merge()函数，如下:
      List<Observable<ResultPair<List<Member>>>> list = new ArrayList<>();
        for (Device dev : devList) {
            final Observable<ResultPair<List<Member>>> familyMemberList = CssServerApi.getFamilyMemberList(dev.getDevId());
            list.add(familyMemberList);
        }
        Observable.merge(list)
                .subscribe(new CssSubscriber<List<Member>>() {
//                    根据merge的特性，onNextRequestSuccess、onNextRequestFailed、onError的总回调次数等于 list 的size，也就是等于设备列表的size
//                    最终响应回调onCompleted，代表整个merge过程结束。
                    @Override
                    public void onNextRequestSuccess(List<Member> members) {
                        //只要是已绑定的设备，家庭成员列表至少有一个成员，也就是绑定这台设备的用户自己，且一定会排在列表首位
                        Log.e("clickGetFamilyMember", "**********start:devId = " + members.get(0).devId + "***********");
                        for (Member member : members) {
                            Log.e("clickGetFamilyMember", member.toString());
                        }
                        Log.e("clickGetFamilyMember", "**********end***********");
                    }

                    @Override
                    public void onNextRequestFailed(int status) {

                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });*/
    }

    /*
    * 从设备列表中选择设备连接（用于周围环境有多台相同型号蓝牙设备的情况，避免连错）
    * */
    private void connectByDeviceList() {
        mBleDeviceListDialogFragment = new BleDeviceListDialogFragment();
        mBleDeviceListDialogFragment.show(mActivity.getSupportFragmentManager(), "");
    }

    /**
     * 向主账号请求关注设备
     * 若请求成功，服务器将向主账号对应的手机号发送短信验证码，该过程是一个耗时过程。
     */
    private void requestPrimaryAcc() {
        CssServerApi.followDevReq(id.get(), key.get())
                .subscribe(new CssSubscriber<ResultPair<String[]>>() {
                    @Override
                    public void onNextRequestSuccess(ResultPair<String[]> resultPair) {
                        final int state = resultPair.first;
                        //打Log的若出现，说明SDK内部参数配置存在问题，toast的才是正常情况下有可能出现的请求异常。
                        switch (state) {
                            case 0://成功，同时返回值里包含id（表示pid），account(主帐号)
                                String[] info = resultPair.second;
                                showCheckFollowPermissionDialog(info);
                                break;
                            case 1: //邮箱格式不对，正常能登录成功应该不会出现该情况
                                Log.e("requestPrimaryAcc", "邮箱格式不对，正常能登录成功应该不会出现该情况");
                                break;
                            case 2://qr码解析失败
                                Log.e("requestPrimaryAcc", "qr码解析失败");
                                break;
                            case 3://设备未被主帐号绑定
                                toast("请求关注失败，设备未被绑定");
                                break;
                            case 4://已是主帐号
                                toast("请求关注失败，您已是该设备的主帐号");
                                break;
                            case 5://子帐号数量已到上限
                                toast("请求关注失败，设备的关注账号已达上限");
                                break;
                            case 6://子帐号电话号码格式不对
                                Log.e("requestPrimaryAcc", "子帐号电话号码格式不对");
                                break;
                            case 7://主帐号电话号码格式不对
                                Log.e("requestPrimaryAcc", "主帐号电话号码格式不对");
                                break;
                            case 8://已是子帐号
                                toast("您已关注该设备，不能重复关注");
                                break;
                            case 9://api_key验证不通过，用户帐号和api_key不匹配
                                Log.e("requestPrimaryAcc", "api_key验证不通过");
                                break;
                            case 10://app和设备不匹配
                                Log.e("requestPrimaryAcc", "app和设备不匹配");
                                break;
                            case 11://主帐号为社交属性，无法关注
                                /*
                                 *这种情况是因为主帐号是用第三方平台帐号登录（如facebook等，目前服务器也仅支持Facebook）。
                                 * 若工程没有导入第三方平台登录，可忽略该选项。
                                 * */
                                toast("主帐号是社交社交帐号属性，无法关注");
                                break;
                        }
                    }

                    @Override
                    public void onNextRequestFailed(int status) {
                        switch (status) {
                            case -1:
                                toast("网络断开了，检查网络");
                                break;
                            default:
                                toast("请求失败");
                                break;
                        }
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("requestPrimaryAcc", e.getMessage());
                    }
                });
    }

    /**
     * 确认关注设备
     * 要关注设备的账号用户向主账号用户索取验证码，确认成功即认为该账号已成功关注了该设备
     */
    private void showCheckFollowPermissionDialog(String[] info) {
        FollowDevCheckDialogFragment dialogFragment = new FollowDevCheckDialogFragment();
        dialogFragment.setCancelable(false);
        dialogFragment.setInfo(info);
        dialogFragment.setOnUpdateListener(new BaseDialogFragment.OnUpdateListener() {
            @Override
            public void onUpdateSuccess() {
                getDevList(false);
            }
        });
        dialogFragment.show(getActivity().getSupportFragmentManager(), "");
    }

    @Override
    public void onDeviceInfo(DeviceInfo device) {
        Log.e("onDeviceInfo", device.toString());
        deviceId = device.getDeviceId();
        String deviceKey = device.getDeviceKey();
//        如果需要id 和 key 中的字母参数小写，可以如下转换
        deviceId = deviceId.toLowerCase();
        deviceKey = deviceKey.toLowerCase();
        id.set(deviceId);
//        mDeviceId.setText(deviceId);
        key.set(deviceKey);
//        mDeviceKey.setText(deviceKey);
        if (isLogin.get()) {
            //从服务器确认是否绑定
            getDevList(false);
            startUpCssDev();
        }

        String deviceName = mHcService.returnDeviceName();
        healthMonitorPresenter.sendDeviceInfo(deviceName,deviceId, DeviceData.getUniqueId(getActivity()));
    }

    @Override
    public void onReadDeviceInfoFailed() {
        id.set("无法获取设备ID");
        key.set("无法获取设备key");
//        mDeviceId.setText("无法获取设备ID");
//        mDeviceKey.setText("无法获取设备key");
    }

    private void startUpCssDev() {
        HmLoadDataTool.getInstance().createCssSocket(getActivity().getApplication(), id.get(), key.get(),
                new OnCssSocketRunningListener() {

                    @Override
                    public void onDataUploadSuccess(LoadBean bean) {
                        //                          ↑↑↑↑↑↑↑↑↑↑↑↑↑
                        //  返回的上传对象，可以在执行此保存到数据库的操作
                        Log.e("startUpCssDev", "数据上传成功");
                        toast("数据上传成功");
                        if (bean != null) {
                            Log.e("startUpCssDev", "onDataUploadSuccess:" + bean.toString());
                        }
                    }

                    @Override
                    public void onDataUploadFail() {
                        Log.e("startUpCssDev", "数据上传失败...");
                        toast("数据上传失败");
                    }

                    @Override
                    public void onActivating() {
                        Log.e("startUpCssDev", "CSS Socket模块激活中...");
                        HmLoadDataTool.getInstance().checkSocketActive();
                    }

                    /**
                     * 这里的激活成功指的是CSS Socket激活成功，此时Css Socket还要向服务器反馈激活成功的信息
                     * 所以设备是否激活成功，应该以服务器的设备列表是否有该设备为准，不要将此回调函数作为设备激活成功的依据
                     * {@link MonitorInfoFragment#loopCheckDevIsBind()}
                     **/
                    @Override
                    public void onActiveSuccess() {
                        Log.e("startUpCssDev", "CSS Socket模块激活成功");
//                        mActivity.isShowUploadButton = true;
//                        if (isDevBind.get() == 0) {
//                            getDevList(false);
//                            clickGetFamilyMember(null);
//                        }
                    }

                    @Override
                    public void onActiveFail(String reason) {
                        Log.e("startUpCssDev", "CSS Socket模块激活失败，reason:" + reason);
//                        mActivity.isShowUploadButton = false;
                        // CSS Socket模块激活失败，SDK内部已销毁CSS模块，此时可选择尝试重启模块，多次尝试不成功要及时断开蓝牙连接
                        // 也可选择立即断开蓝牙
                        // CSS Socket 模块与设备蓝牙连接模块相辅相成， 应该遵循如下原则：
                        // 蓝牙连接成功，启动该模块，只有该模块被初始化成功并激活成功，才能继续保持蓝牙的连接，当模块未初始化成功或未激活成功，
                        // SDK内部已直接销毁模块，所以此时也应该及时断开蓝牙连接。
                        // 同理，当蓝牙连接断开后，也应该及时销毁CSS Socket模块。
                        MonitorDataTransmissionManager.getInstance().disConnectBle();
                        HmLoadDataTool.getInstance().destroyCssSocket();
                    }

                    @Override
                    public void onFreeze() {
                        Log.e("startUpCssDev", "CSS Socket模块已被冻结");
                    }

                    @Override
                    public void onInitializeSuccess() {
                        Log.e("startUpCssDev", "CSS Socket模块初始化成功");
                        isCssSocketOpen = true;
                        if (isDevBind.get() > 0) {
                            HmLoadDataTool.getInstance().checkSocketActive();
                        } else {
                            Log.e("startUpCssDev", "onInitializeSuccess" + "需要绑定");
                        }
                    }

                    @Override
                    public void onInitializeFail(String reason) {
                        Log.e("startUpCssDev", "CSS Socket模块接初始化失败，reason:" + reason);
                        isCssSocketOpen = false;
                        MonitorDataTransmissionManager.getInstance().disConnectBle();
                        HmLoadDataTool.getInstance().destroyCssSocket();
                    }

                    @Override
                    public void onSocketDisconnect() {
                        //模块内部有断开重连机制，所以这里不需要销毁模块，也不需要断开蓝牙连接
                        // 当然可以自己增加判断，断开连接时，连续几次重连失败，再断开蓝牙连接和销毁CSS Socket模块
                        Log.e("startUpCssDev", "CSS Socket模块与服务器断开连接");
                        isCssSocketOpen = false;
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
    public void sendSuccess(String str) {

    }

    @Override
    public void sendFail(String str) {
        toast(str);
    }

    @Override
    public void onDeviceVersion(WareType wareType, String version) {
        switch (wareType) {
            case VER_SOFTWARE:
                softVer.set(version);
                if (mHcService != null) {
                    mHcService.dataQuery(HcService.DATA_QUERY_HARDWARE_VER);
                }
                break;
            case VER_HARDWARE:
                hardVer.set(version);
                if (mHcService != null) {
                    mHcService.dataQuery(HcService.DATA_QUERY_FIRMWARE_VER);
                }
                break;
            case VER_FIRMWARE:
                firmVer.set(version);
                if (mHcService != null) {
                    mHcService.dataQuery(HcService.DATA_QUERY_CONFIRM_ECG_MODULE_EXIST);
                }
                break;
        }
    }
}