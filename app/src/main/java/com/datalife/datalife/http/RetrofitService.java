package com.datalife.datalife.http;

import com.datalife.datalife.bean.FamilyUserInfo;
import com.datalife.datalife.bean.LoginBean;
import com.datalife.datalife.bean.PageBean;
import com.datalife.datalife.bean.RegisterBean;
import com.datalife.datalife.bean.ResultBean;
import com.datalife.datalife.bean.ResultFamily;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by LG on 2018/1/31.
 */

public interface RetrofitService {

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<LoginBean>> login(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<RegisterBean> register(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("Api/")
    Observable<RegisterBean> loginout(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean> addfamilyuser(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultFamily<ArrayList<FamilyUserInfo>,PageBean>> getfamilylist(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean> sendDeviceInfo(@FieldMap Map<String,String> params);

}
