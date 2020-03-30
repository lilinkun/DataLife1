package com.datalife.datalife.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.datalife.datalife.R;
import com.datalife.datalife.app.ProApplication;
import com.datalife.datalife.base.BaseActivity;
import com.datalife.datalife.bean.RegisterBean;
import com.datalife.datalife.bean.RegisterUserInfo;
import com.datalife.datalife.contract.RegisterContract;
import com.datalife.datalife.presenter.RegisterPresenter;
import com.datalife.datalife.util.DeviceData;
import com.datalife.datalife.util.IDatalifeConstant;
import com.datalife.datalife.util.UToast;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscription;

/**
 * Created by LG on 2018/1/16.
 */

public class RegisterActivity extends BaseActivity implements RegisterContract.View{

    @BindView(R.id.iv_leftback)
    ImageView mLeftBack;
    @BindView(R.id.et_register_account)
    EditText mRegisterAccount;
    @BindView(R.id.et_register_code)
    EditText mRegisterCode;
    @BindView(R.id.et_register_phone)
    EditText mRegisterPhone;
    @BindView(R.id.et_register_psw)
    EditText mRegisterPsw;

    private RegisterPresenter mRegisterPresenter = new RegisterPresenter(this);

    private RegisterUserInfo registerUserInfo = new RegisterUserInfo();
    private String username;
    private String psw;
    String sessionid = "";


    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initEventAndData() {

        if (getIntent() != null){
            sessionid  = getIntent().getStringExtra("SESSIONID");
        }else{
            sessionid = DeviceData.getUniqueId(this);
        }


        mRegisterPresenter.onCreate();
        mRegisterPresenter.attachView(this);
    }

    @OnClick({R.id.tv_usage_protocol,R.id.iv_leftback,R.id.tv_send_vcode,R.id.btn_register_over})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_usage_protocol:
//                UToast.show(this,);
                Intent intent = new Intent();
                intent.setClass(this,WebViewActivity.class);
                startActivity(intent);
                break;

            case R.id.iv_leftback:
                finish();
                break;

            case R.id.tv_send_vcode:
//                toast("asasads");
                break;

            case R.id.btn_register_over:

                username = mRegisterAccount.getText().toString();
                psw = mRegisterPsw.getText().toString();
                String mobile = mRegisterPhone.getText().toString();
                String Code = mRegisterCode.getText().toString();

                mRegisterPresenter.register(username,psw,mobile,Code, sessionid);

                break;
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void showPromptMessage(String message) {
        UToast.show(this,message);
    }

    @Override
    public void showPromptMessage(int resId) {
        UToast.show(this,resId);
    }


    @Override
    public void onSuccess(RegisterBean mRegisterBean) {
        Toast.makeText(getApplicationContext(),mRegisterBean.toString(), Toast.LENGTH_SHORT).show();

        registerUserInfo.setUsername(username);
        registerUserInfo.setPsw(psw);

        Intent intent = new Intent();
        intent.putExtra(IDatalifeConstant.REGISTERBEAN,registerUserInfo);
        setResult(RESULT_OK,intent);
        finish();

    }

    @Override
    public void onError(String result) {
        Toast.makeText(getApplicationContext(),result, Toast.LENGTH_SHORT).show();
    }
}
