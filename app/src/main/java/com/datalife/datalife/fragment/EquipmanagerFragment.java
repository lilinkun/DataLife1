package com.datalife.datalife.fragment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.datalife.datalife.R;
import com.datalife.datalife.adapter.BlueToothAdapter;
import com.datalife.datalife.adapter.HomeAdapter;
import com.datalife.datalife.base.BaseFragment;
import com.datalife.datalife.bean.Bluetoothbean;
import com.datalife.datalife.util.BluetoothUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LG on 2018/1/17.
 */

public class EquipmanagerFragment extends BaseFragment {

    private List<String> mDatas;

    private HomeAdapter homeAdapter;
    private BluetoothAdapter adapter;

    @BindView(R.id.lv_bluetooth)
    ListView mBluetoothLv;

    // 广播接收发现蓝牙设备
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            Bundle b = intent.getExtras();
            Object[] lstName = b.keySet().toArray();

            // 显示所有收到的消息及其细节
            for (int i = 0; i < lstName.length; i++) {
                String keyName = lstName[i].toString();
                Log.e("bluetooth", keyName + ">>>" + String.valueOf(b.get(keyName)));
            }
            BluetoothDevice device;
                // 搜索发现设备时，取得设备的信息；注意，这里有可能重复搜索同一设备
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // 添加到ListView的Adapter中。
                toast("设备名字:" + device.getName() + "\n设备地址:" + device.getAddress());
                Log.v("EquipmanagerFragment","设备名字:" + device.getName() + "\n设备地址:" + device.getAddress());

                /*ArrayList<Bluetoothbean> bluetoothbeans = new ArrayList<>();
                Bluetoothbean bluetoothbean = new Bluetoothbean();
                bluetoothbean.setMacStr(device.getAddress());
                bluetoothbean.setBlueName(device.getName());
                bluetoothbeans.add(bluetoothbean);
                BlueToothAdapter bluetoothAdapter = new BlueToothAdapter(getActivity(),bluetoothbeans);
                mBluetoothLv.setAdapter(bluetoothAdapter);*/
            }
        }
    };

    @Override
    protected int getlayoutId() {
        return R.layout.fragment_equipment;
    }

    @Override
    protected void initEventAndData() {

        IntentFilter intent = new IntentFilter();
        intent.addAction(BluetoothDevice.ACTION_FOUND);//搜索发现设备
        intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);//状态改变
        intent.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);//行动扫描模式改变了
        intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);//动作状态发生了变化
        intent.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        getActivity().registerReceiver(mReceiver, intent);
        adapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothUtils.getBlutoothUtils().getConnectBt(getActivity(),adapter);

    }

    @Override
    protected void otherevent() {
        getActivity().unregisterReceiver(mReceiver);
    }

    @OnClick(R.id.btn_bluetooth_search)
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_bluetooth_search:

                if (adapter == null)
                {
                    // 设备不支持蓝牙
                    toast("设备不支持蓝牙");
                }
                // 打开蓝牙
                if (!adapter.isEnabled())
                {
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    // 设置蓝牙可见性，最多300秒
                    intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                    startActivity(intent);
                }

                // 寻找蓝牙设备，android会将查找到的设备以广播形式发出去
                adapter.startDiscovery();
                break;
        }
    }


    @Override
    public void showPromptMessage(int resId) {

    }

    @Override
    public void showPromptMessage(String message) {

    }
}