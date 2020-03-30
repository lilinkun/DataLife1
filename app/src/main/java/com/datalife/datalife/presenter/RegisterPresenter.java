package com.datalife.datalife.presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.datalife.datalife.R;
import com.datalife.datalife.activity.RegisterActivity;
import com.datalife.datalife.base.BasePresenter;
import com.datalife.datalife.bean.RegisterBean;
import com.datalife.datalife.contract.RegisterContract;
import com.datalife.datalife.http.HttpMethods;
import com.datalife.datalife.http.HttpService;
import com.datalife.datalife.manager.DataManager;
import com.datalife.datalife.model.LoginModel;
import com.datalife.datalife.mvp.IModel;
import com.datalife.datalife.mvp.IView;
import com.datalife.datalife.subscriber.CommonSubscriber;
import com.datalife.datalife.transformer.DefaultTransformer;
import com.datalife.datalife.util.PhoneFormatCheckUtils;

import java.util.HashMap;
import java.util.Map;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by LG on 2018/1/17.
 */

public class RegisterPresenter implements Presenter {
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;
    private RegisterBean mRegisterBean;
    private RegisterContract.View mRegisterView;

    public RegisterPresenter (Context mContext){
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
        mRegisterView = (RegisterContract.View) view;
    }

    @Override
    public void attachIncomingIntent(Intent intent) {

    }

    public void register(String username,String psw,String mobile,String code,String sessionId){

        if(username == null || username.isEmpty()){
            mRegisterView.showPromptMessage(R.string.prompt_login_name_not_empty);
            return;
        }

        if(username.toString().trim().length() > 15 || username.toString().trim().length() < 3){
            mRegisterView.showPromptMessage(R.string.prompt_login_name_not_allow);
            return;
        }

        if(mobile == null || mobile.isEmpty() || !PhoneFormatCheckUtils.isChinaPhoneLegal(mobile)){
            mRegisterView.showPromptMessage(R.string.prompt_phone_number_invalid);
            return;
        }

        if(code == null || code.isEmpty()){
            mRegisterView.showPromptMessage(R.string.prompt_verification_code_not_empty);
            return;
        }

        if(psw == null || psw.isEmpty()){
            mRegisterView.showPromptMessage(R.string.prompt_login_passwrod_not_empty);
            return;
        }

        if(psw.toString().trim().length() > 20 || psw.toString().trim().length() < 6){
            mRegisterView.showPromptMessage(R.string.prompt_passwrod_not_allow);
            return;
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("cls", "userbase");
        params.put("fun", "Register");
        params.put("user_name", username);
        params.put("user_password", psw);
        params.put("mobile", mobile);
        params.put("Code", code);
        params.put("SessionId", sessionId);
        mCompositeSubscription.add(manager.register(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RegisterBean>() {
                    @Override
                    public void onCompleted() {
                        if (mRegisterBean != null){
                            if (mRegisterBean.getStatus().equals("success")){
                                mRegisterView.onSuccess(mRegisterBean);
                            }else{
                                mRegisterView.onError(mRegisterBean.getDesc());
                            }

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mRegisterView.onError("请求失败！！");
                    }

                    @Override
                    public void onNext(RegisterBean registerBean) {
                        mRegisterBean = registerBean;
                    }
                })
        );
    };
}
