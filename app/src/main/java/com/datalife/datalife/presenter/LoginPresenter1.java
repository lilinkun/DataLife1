package com.datalife.datalife.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.datalife.datalife.R;
import com.datalife.datalife.activity.RegisterActivity;
import com.datalife.datalife.bean.LoginBean;
import com.datalife.datalife.bean.RegisterBean;
import com.datalife.datalife.bean.ResultBean;
import com.datalife.datalife.contract.LoginContract;
import com.datalife.datalife.contract.RegisterContract;
import com.datalife.datalife.manager.DataManager;
import com.datalife.datalife.mvp.IView;
import com.datalife.datalife.util.IDatalifeConstant;
import com.datalife.datalife.util.PhoneFormatCheckUtils;

import java.util.HashMap;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by LG on 2018/1/31.
 */

public class LoginPresenter1 implements Presenter {
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;
    private ResultBean<LoginBean> mLoginBean;
    private LoginContract.LoginView mLoginView;

    public LoginPresenter1(Context mContext){
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
        mLoginView = (LoginContract.LoginView)view;
    }

    @Override
    public void attachIncomingIntent(Intent intent) {

    }

    /**
     * 注册
     * @param mContext
     */
    public void registerText(Activity mContext,String sessionid){
        Intent intent = new Intent();
        intent.setClass(mContext, RegisterActivity.class);
        intent.putExtra("SESSIONID" , sessionid);
        mContext.startActivityForResult(intent, IDatalifeConstant.RESULT_REGISTER);
    }

    /**
     * 登陆
     */
    public void login(String username,String psw,String sessionId){
        if(username == null || username.isEmpty()){
            mLoginView.showPromptMessage(R.string.prompt_login_name_not_empty);
            return;
        }

        if(psw == null || psw.isEmpty()){
            mLoginView.showPromptMessage(R.string.prompt_login_passwrod_not_empty);
            return;
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("cls", "userbase");
        params.put("fun", "Login");
        params.put("UserName", username);
        params.put("PassWord", psw);
        params.put("SessionId", sessionId);
        params.put("MobileType","android");
        mCompositeSubscription.add(manager.login(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultBean<LoginBean>>() {
                    @Override
                    public void onCompleted() {
                        if (mLoginBean != null){
                            if (mLoginBean.getStatus().equals("success")){
                                mLoginView.loginSuccess(mLoginBean);
                            }else{
                                mLoginView.loginFail(mLoginBean.getDesc());
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mLoginView.loginFail("请求失败！！");
                    }

                    @Override
                    public void onNext(ResultBean<LoginBean> ResultBean) {
                        mLoginBean = ResultBean;
                    }
                })
        );
    }

}
