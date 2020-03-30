package com.datalife.datalife.contract;

import android.content.Context;

import com.datalife.datalife.bean.LoginBean;
import com.datalife.datalife.bean.RegisterBean;
import com.datalife.datalife.bean.ResultBean;
import com.datalife.datalife.mvp.IView;

/**
 * Created by LG on 2018/1/31.
 */

public class SettingContract {

    public interface SettingView extends IView {
        void loginoutSuccess(RegisterBean mRegisterBean);
        void loginoutFail(String failMsg);
    }

    public interface SettingPresenter{
        void loginout(Context context);
    }

}
