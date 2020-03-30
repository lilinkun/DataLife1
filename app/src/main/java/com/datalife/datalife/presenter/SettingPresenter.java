package com.datalife.datalife.presenter;

import android.content.Context;
import android.content.Intent;

import com.datalife.datalife.bean.RegisterBean;
import com.datalife.datalife.contract.RegisterContract;
import com.datalife.datalife.contract.SettingContract;
import com.datalife.datalife.fragment.SettingFragment;
import com.datalife.datalife.manager.DataManager;
import com.datalife.datalife.mvp.IView;

import java.util.HashMap;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by LG on 2018/1/31.
 */

public class SettingPresenter implements Presenter{
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;
    private SettingContract.SettingView settingView;
    private RegisterBean mRegisterBean;


    public SettingPresenter(Context mContext) {
        this.mContext = mContext;
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
        settingView = (SettingContract.SettingView) view;
    }

    @Override
    public void attachIncomingIntent(Intent intent) {

    }

    public void loginout(String sessionid){
        HashMap<String, String> params = new HashMap<>();
        params.put("fun", "Logout");
        params.put("cls", "userbase");
        params.put("SessionId", sessionid);
        mCompositeSubscription.add(manager.loginout(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RegisterBean>() {
                    @Override
                    public void onCompleted() {
                        if (mRegisterBean != null){
                            if (mRegisterBean.getStatus().equals("success")){
                                settingView.loginoutSuccess(mRegisterBean);
                            }else{
                                settingView.loginoutFail(mRegisterBean.getDesc());
                            }

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        settingView.loginoutFail("请求失败！！");
                    }

                    @Override
                    public void onNext(RegisterBean registerBean) {
                        mRegisterBean = registerBean;
                    }
                })
        );
    };
}
