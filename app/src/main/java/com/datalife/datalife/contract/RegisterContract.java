package com.datalife.datalife.contract;

import com.datalife.datalife.bean.RegisterBean;
import com.datalife.datalife.mvp.IView;

/**
 * Created by LG on 2018/1/17.
 */

public interface RegisterContract {

    public interface View extends IView{
        void onSuccess(RegisterBean mRegisterBean);
        void onError(String result);
    }

    public interface Presenter {
        void loginout(String SessionId);
    }

}
