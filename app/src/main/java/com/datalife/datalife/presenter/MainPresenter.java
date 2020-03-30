package com.datalife.datalife.presenter;

import android.content.Context;
import android.content.Intent;

import com.datalife.datalife.bean.FamilyUserInfo;
import com.datalife.datalife.bean.PageBean;
import com.datalife.datalife.bean.ResultBean;
import com.datalife.datalife.bean.ResultFamily;
import com.datalife.datalife.contract.AddFamilyUserContract;
import com.datalife.datalife.contract.MainContract;
import com.datalife.datalife.manager.DataManager;
import com.datalife.datalife.mvp.IView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by LG on 2018/2/4.
 */

public class MainPresenter implements Presenter {
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;
    private ResultFamily<ArrayList<FamilyUserInfo>,PageBean>  listResultBean;
    private MainContract.MainView mainView;


    public MainPresenter(Context mContext){
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
        mainView = (MainContract.MainView)view;
    }

    @Override
    public void attachIncomingIntent(Intent intent) {
    }

    public void getFamilyDataList(String sessionId,String PageCount,String PageIndex){

        HashMap<String ,String> hashMap = new HashMap<>();
        hashMap.put("SessionId",sessionId);
        hashMap.put("cls","BigDataMember");
        hashMap.put("fun","BigDataMemberList");
        hashMap.put("PageCount",PageCount);
        hashMap.put("PageIndex",PageIndex);

        mCompositeSubscription.add(manager.getFamilyList(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultFamily<ArrayList<FamilyUserInfo>,PageBean>>() {
                    public void onCompleted() {
                        if (listResultBean != null){
                            if (listResultBean.getStatus().equals("success")){
                                mainView.onBackFamilyListDataSuccess(listResultBean);
                            }else{
                                mainView.onBackFamilyListDataFail(listResultBean.getDesc());
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mainView.onBackFamilyListDataFail("请求失败！！");
                    }

                    @Override
                    public void onNext(ResultFamily<ArrayList<FamilyUserInfo>,PageBean>  ResultBean) {
                        listResultBean = ResultBean;
                    }
                }));



    }
}
