package com.datalife.datalife.contract;

/**
 * Created by LG on 2018/1/17.
 */

public class MeContract {

    public interface MeView{

    }


    public interface MePresenter{
        //订单管理
        void ordermanager();
        //购物车管理
        void shoppingcarmanager();
        //设备管理
        void equitManager();
        //系统设置
        void systemsetting();
    }

}
