package com.datalife.datalife.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.datalife.datalife.R;
import com.datalife.datalife.app.ProApplication;
import com.datalife.datalife.base.BaseActivity;
import com.datalife.datalife.base.BaseMvpActivity;
import com.datalife.datalife.base.BasePresenter;
import com.datalife.datalife.bean.LoginBean;
import com.datalife.datalife.bean.RegisterBean;
import com.datalife.datalife.bean.RegisterUserInfo;
import com.datalife.datalife.bean.ResultBean;
import com.datalife.datalife.contract.LoginContract;
import com.datalife.datalife.presenter.LoginPresenter;
import com.datalife.datalife.presenter.LoginPresenter1;
import com.datalife.datalife.util.Code;
import com.datalife.datalife.util.DataLifeUtil;
import com.datalife.datalife.util.DeviceData;
import com.datalife.datalife.util.IDatalifeConstant;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by LG on 2018/1/15.
 */

public class LoginActivity extends BaseActivity implements LoginContract.LoginView {

    @BindView(R.id.et_account)//账号
            EditText inputEmail;
    @BindView(R.id.et_psd)//密码
            EditText inputPassword;
    @BindView(R.id.ic_vcode)//验证码图片
            ImageView iv_vcode;
    @BindView(R.id.et_vcode)
            EditText et_vcode;
    @BindView(R.id.ll_vcode)
            LinearLayout ll_vcode;//验证码layout


    LoginPresenter1 mLoginPresenter1 = new LoginPresenter1(this);

    private String realCode;
    //登陆错误次数
    private int loginNum = 0;

    private String SESSIONID = "";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initEventAndData() {
        mLoginPresenter1.onCreate();
        mLoginPresenter1.attachView(this);
        iv_vcode.setImageBitmap(Code.getInstance().createBitmap());
        realCode = Code.getInstance().getCode().toLowerCase();

        SESSIONID = DeviceData.getUniqueId(this);
    }

    @OnClick({R.id.btn_login,R.id.tv_register,R.id.ic_vcode})
    protected void otherViewClick(View view) {
        switch (view.getId()){

            case R.id.tv_register:
                mLoginPresenter1.registerText(this,SESSIONID);
                break;
            case R.id.btn_login:

                if (loginNum >= 2){
                    ll_vcode.setVisibility(View.VISIBLE);
                    String phoneCode = et_vcode.getText().toString().toLowerCase();
                    if (phoneCode.trim().length() == 0){
                        toast(R.string.input_vcode);
                    } else if (phoneCode.equals(realCode)) {
                        mLoginPresenter1.login(getAccount(), getPwd(), SESSIONID);
                    } else {
                        toast(R.string.toast_vcode_error);
                    }
                }else{
                    mLoginPresenter1.login(getAccount(), getPwd(), SESSIONID);
                }

                break;

            case R.id.ic_vcode:
                iv_vcode.setImageBitmap(Code.getInstance().createBitmap());
                realCode = Code.getInstance().getCode().toLowerCase();
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLoginPresenter1.onStop();
    }

    @Override
    public String getPwd() {
        return inputPassword.getText().toString().trim();
    }

    @Override
    public void loginSuccess(ResultBean<LoginBean> mLoginBean) {
        LoginBean loginBean = mLoginBean.getData();
        String datalife = null;
        try {
            datalife = DataLifeUtil.serialize(loginBean);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        sharedPreferences.edit().putString("sessionid",SESSIONID).putBoolean("login",true).putString("logininfo",datalife).commit();


        Intent intent = new Intent();
        intent.setClass(this,MainActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    public String getAccount() {
        return inputEmail.getText().toString().trim();
    }


    @Override
    public void loginFail(String failMsg) {
        toast(failMsg + "");
    }


    public boolean checkNull() {
        boolean isNull = false;
        if (TextUtils.isEmpty(getAccount())) {
            inputEmail.setError(getResources().getString(R.string.account_is_null));
            loginNum++;
            isNull = true;
        } else if (TextUtils.isEmpty(getPwd())) {
            inputPassword.setError(getResources().getString(R.string.psw_is_null));
            loginNum++;
            isNull = true;
        }
        return isNull;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){
            if (requestCode == IDatalifeConstant.RESULT_REGISTER){
                RegisterUserInfo registerUserInfo = (RegisterUserInfo) data.getSerializableExtra(IDatalifeConstant.REGISTERBEAN);

                mLoginPresenter1.login(registerUserInfo.getUsername(), registerUserInfo.getPsw(),SESSIONID);
            }
        }
    }

    @Override
    public void showPromptMessage(int resId) {

    }

    @Override
    public void showPromptMessage(String message) {

    }
}
