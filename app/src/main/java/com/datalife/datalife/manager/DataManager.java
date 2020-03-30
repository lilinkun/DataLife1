package com.datalife.datalife.manager;

import android.content.Context;

import com.datalife.datalife.bean.FamilyUserInfo;
import com.datalife.datalife.bean.LoginBean;
import com.datalife.datalife.bean.PageBean;
import com.datalife.datalife.bean.RegisterBean;
import com.datalife.datalife.bean.ResultBean;
import com.datalife.datalife.bean.ResultFamily;
import com.datalife.datalife.http.RetrofitHelper;
import com.datalife.datalife.http.RetrofitService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Observable;

/**
 * Created by LG on 2018/1/31.
 */

public class DataManager {
    private RetrofitService mRetrofitService;
    public DataManager(Context context){
        this.mRetrofitService = RetrofitHelper.getInstance(context).getServer();
    }

    public Observable<RegisterBean> register(HashMap<String, String> mHashMap){
        return mRetrofitService.register(mHashMap);
    }

    public Observable<ResultBean<LoginBean>> login(HashMap<String, String> mHashMap){
        return mRetrofitService.login(mHashMap);
    }

    public Observable<RegisterBean> loginout(HashMap<String, String> mHashMap){
        return mRetrofitService.loginout(mHashMap);
    }

    public Observable<ResultBean> addFamilyUser(HashMap<String, String> mHashMap){
        return mRetrofitService.addfamilyuser(mHashMap);
    }

    public Observable<ResultFamily<ArrayList<FamilyUserInfo>,PageBean>> getFamilyList(HashMap<String,String> mHashMap){
        return mRetrofitService.getfamilylist(mHashMap);
    }

    public Observable<ResultBean> sendDeviceInfo(HashMap<String ,String> mHashMap){
        return mRetrofitService.sendDeviceInfo(mHashMap);
    }

}
