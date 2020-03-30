package com.datalife.datalife.contract;

import com.datalife.datalife.mvp.IView;

/**
 * Created by LG on 2018/2/8.
 */

public interface HealthMonitorContract {

    public interface HealthMonitorView extends IView{
        public void sendSuccess(String str);
        public void sendFail(String str);

    }

    public interface HealthMonitorPresenter{

    }

}
