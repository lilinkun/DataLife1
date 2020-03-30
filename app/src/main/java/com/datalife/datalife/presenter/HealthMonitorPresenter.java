package com.datalife.datalife.presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.datalife.datalife.activity.HealthMonitorActivity;
import com.datalife.datalife.bean.LoginBean;
import com.datalife.datalife.bean.ResultBean;
import com.datalife.datalife.contract.HealthMonitorContract;
import com.datalife.datalife.contract.LoginContract;
import com.datalife.datalife.manager.DataManager;
import com.datalife.datalife.mvp.IView;

import java.util.HashMap;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by LG on 2018/2/8.
 */

public class HealthMonitorPresenter implements Presenter {
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;
    private HealthMonitorContract.HealthMonitorView mHealthMonitorView;
    private ResultBean resultBean;

    public HealthMonitorPresenter(Context context){
        this.mContext = context;
    }

    @Override
    public void onCreate() {
        manager = new DataManager(mContext);
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {
        if (mCompositeSubscription.hasSubscriptions()){
            mCompositeSubscription.unsubscribe();
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void attachView(IView view) {
        mHealthMonitorView = (HealthMonitorContract.HealthMonitorView) view;
    }

    @Override
    public void attachIncomingIntent(Intent intent) {

    }


    public void sendDeviceInfo(String MachineName,String MachineSn,String SessionId){


        Log.v("HealthMonitorPresenter:" , "MachineName:" + MachineName + ",MachineSn:" + MachineSn + ",SessionId:"+SessionId);
        HashMap<String , String> hashMap = new HashMap<>();
        hashMap.put("cls","MachineBind");
        hashMap.put("fun","MachineBindCreate");
        hashMap.put("MachineName",MachineName);
        hashMap.put("MachineSn",MachineSn);
        hashMap.put("SessionId",SessionId);
        mCompositeSubscription.add(manager.sendDeviceInfo(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultBean>() {
                    @Override
                    public void onCompleted() {
                        if (resultBean != null){
                            if (resultBean.getStatus().equals("success")){
                                mHealthMonitorView.sendSuccess("success");
                            }else{
                                mHealthMonitorView.sendFail(resultBean.getDesc());
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mHealthMonitorView.sendFail("请求失败！！");
                    }

                    @Override
                    public void onNext(ResultBean ResultBean) {
                        resultBean = ResultBean;
                    }
                })
        );
    }

}
