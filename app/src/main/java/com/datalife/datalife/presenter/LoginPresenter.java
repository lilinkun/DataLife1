package com.datalife.datalife.presenter;

import android.content.Context;

import com.datalife.datalife.activity.LoginActivity;
import com.datalife.datalife.base.BasePresenter;
import com.datalife.datalife.bean.RegisterBean;
import com.datalife.datalife.contract.LoginContract;
import com.datalife.datalife.model.LoginModel;
import com.datalife.datalife.mvp.IModel;
import com.datalife.datalife.mvp.IView;
import com.datalife.datalife.util.LogUtils;

import java.util.HashMap;

/**
 * Created by LG on 2018/1/15.
 */

public class LoginPresenter extends BasePresenter<LoginActivity> implements
        LoginContract.LoginPresenter {

    @Override
    public void login(String name, String pwd,String SessionId) {
        /*if (!getIView().checkNull()) {
            ((LoginModel) getiModelMap().get("login")).login(name, pwd,SessionId, new LoginModel
                    .InfoHint() {
                @Override
                public void successInfo(RegisterBean bean) {
                    getIView().loginSuccess(bean);  //成功
                }

                @Override`
                public void failInfo(String str) {
                    LogUtils.e("LoginPresenter.failInfo", str);

                    getIView().loginFail(str);  //失败
                }
            });
        }*/
    }

    @Override
    public void registerText(Context context) {

    }

    /*@Override
    public void register(Context context) {
        ((LoginModel) getiModelMap().get("login")).register(context);
    }*/


    @Override
    public HashMap<String, IModel> getiModelMap() {
        return loadModelMap(new LoginModel());
    }

    @Override
    public HashMap<String, IModel> loadModelMap(IModel... models) {
        HashMap<String, IModel> map = new HashMap<>();
        map.put("login", models[0]);
        return map;
    }
}
