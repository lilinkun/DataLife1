package com.datalife.datalife.base;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;

import com.datalife.datalife.app.AppManager;
import com.datalife.datalife.mvp.IView;
import com.datalife.datalife.util.DeviceData;
import com.datalife.datalife.util.UToast;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by LG on 2018/1/17.
 */

public abstract class BaseActivity extends AppCompatActivity{

    Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        unbinder = ButterKnife.bind(this);
        //初始化事件跟获取数据以及一些准备工作, 由于使用了ButterKnife, findViewById和基本的Click事件都不会在这里
        initEventAndData();
//        getSupportActionBar().hide();
        //集中管理Activity
        AppManager.getAppManager().addActivity(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unbinder.unbind();
//        AppManager.getAppManager().finishActivity(this);
    }

    protected abstract int getLayoutId();

    protected  abstract void initEventAndData();

    protected void toast(@StringRes int resId) {
        UToast.show(this, resId);
    }

    protected void toast(@StringRes int resId, int duration) {
        UToast.show(this, resId, duration);
    }

    protected void toast(@NonNull String text) {
        UToast.show(this, text);
    }

    protected void toast(@NonNull String text, int duration) {
        UToast.show(this, text, duration);
    }


    /**
     * 跳转到其他界面
     * @param cls 目标Activity
     */
    protected void startOtherActivity(Class cls){
        Intent intent = new Intent(this,cls);
        startActivity(intent);
    }

    /**
     * 跳转到其他界面，有返回结果
     * @param cls 目标Activity
     * @param requestCode 请求码
     */
    protected void startOtherActivityForResult(Class cls,int requestCode){
        Intent intent = new Intent(this,cls);
        startActivityForResult(intent,requestCode);
    }

}
