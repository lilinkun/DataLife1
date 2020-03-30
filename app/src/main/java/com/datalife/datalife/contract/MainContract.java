package com.datalife.datalife.contract;

import com.datalife.datalife.bean.FamilyUserInfo;
import com.datalife.datalife.bean.PageBean;
import com.datalife.datalife.bean.ResultBean;
import com.datalife.datalife.bean.ResultFamily;
import com.datalife.datalife.mvp.IView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LG on 2018/1/15.
 */

public interface MainContract {
    public interface MainView extends IView{
        public void onBackFamilyListDataSuccess(ResultFamily<ArrayList<FamilyUserInfo>,PageBean> listResultBean);
        public void onBackFamilyListDataFail(String str);
    }

    public interface MainPresenter{
    }
}
