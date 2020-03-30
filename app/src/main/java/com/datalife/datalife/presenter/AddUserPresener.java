package com.datalife.datalife.presenter;

import android.content.Context;
import android.content.Intent;

import com.datalife.datalife.R;
import com.datalife.datalife.bean.LoginBean;
import com.datalife.datalife.bean.RegisterBean;
import com.datalife.datalife.bean.ResultBean;
import com.datalife.datalife.contract.AddFamilyUserContract;
import com.datalife.datalife.contract.LoginContract;
import com.datalife.datalife.manager.DataManager;
import com.datalife.datalife.mvp.IView;

import java.util.HashMap;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by LG on 2018/2/4.
 */

public class AddUserPresener implements Presenter{
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;
    private ResultBean mResultBean;
    private AddFamilyUserContract.AddFamilyView addFamilyView;


    public AddUserPresener(Context mContext){
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
        addFamilyView = (AddFamilyUserContract.AddFamilyView)view;
    }

    @Override
    public void attachIncomingIntent(Intent intent) {
    }


    public void addFamilyUser(String Member_Name ,String Member_Portrait,String Member_Stature,String Member_Weight,String Member_DateOfBirth,String Member_Sex,String Member_Status,String Member_IsDefault,String SessionId){

        HashMap<String, String> params = new HashMap<>();
        params.put("cls", "BigDataMember");
        params.put("fun", "BigDataMemberCreate");
        params.put("Member_Name", Member_Name);
        params.put("Member_Portrait", Member_Portrait);
        params.put("Member_Stature", Member_Stature);
        params.put("Member_Weight",Member_Weight);
        params.put("Member_DateOfBirth",Member_DateOfBirth);
        params.put("Member_Status",Member_Status);
        params.put("Member_Sex",Member_Sex);
        params.put("Member_IsDefault",Member_IsDefault);
        params.put("SessionId",SessionId);
        mCompositeSubscription.add(manager.addFamilyUser(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultBean>() {
                    @Override
                    public void onCompleted() {
                        if (mResultBean != null){
                            if (mResultBean.getStatus().equals("success")){
                                addFamilyView.onsuccess(mResultBean);
                            }else{
                                addFamilyView.onfail(mResultBean.getDesc());
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        addFamilyView.onfail("请求失败！！");
                    }

                    @Override
                    public void onNext(ResultBean ResultBean) {
                        mResultBean = ResultBean;
                    }
                })
        );
    }

}
