package com.datalife.datalife.contract;

import com.datalife.datalife.bean.ResultBean;
import com.datalife.datalife.mvp.IView;

/**
 * Created by LG on 2018/2/4.
 */

public interface AddFamilyUserContract {

    public interface AddFamilyView extends IView{
        public void onsuccess(ResultBean resultBean);
        public void onfail(String failMsg);
    }

    public interface Presenter{

    }
}
