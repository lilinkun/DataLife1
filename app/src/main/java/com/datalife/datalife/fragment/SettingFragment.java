package com.datalife.datalife.fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.View;

import com.datalife.datalife.R;
import com.datalife.datalife.activity.SimplebackActivity;
import com.datalife.datalife.app.AppManager;
import com.datalife.datalife.base.BaseFragment;
import com.datalife.datalife.bean.RegisterBean;
import com.datalife.datalife.contract.SettingContract;
import com.datalife.datalife.presenter.RegisterPresenter;
import com.datalife.datalife.presenter.SettingPresenter;
import com.datalife.datalife.util.DeviceData;

import butterknife.BindView;
import butterknife.OnClick;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by LG on 2018/1/17.
 */

public class SettingFragment extends BaseFragment implements SettingContract.SettingView{

    private SettingPresenter mSettingPresenter = new SettingPresenter(getActivity());

    @Override
    protected int getlayoutId() {
        return R.layout.fragment_setting;
    }

    @Override
    protected void initEventAndData() {
        mSettingPresenter.onCreate();
        mSettingPresenter.attachView(this);
    }

    @OnClick({R.id.rl_evaluate,R.id.rl_feedback,R.id.rl_about_app,R.id.rl_scavenging_caching,R.id.btn_exit,R.id.rl_update_app})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.rl_evaluate:

                break;

            case R.id.rl_feedback:

                break;

            case R.id.rl_about_app:

                break;

            case R.id.rl_scavenging_caching:

                break;

            case R.id.btn_exit:

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login", MODE_PRIVATE);
                sharedPreferences.edit().putBoolean("login",false).commit();
                String sessionid = "";
                if (sharedPreferences.getString("sessionid","") != null && sharedPreferences.getString("sessionid","").trim().toString().length() != 0){
                    sessionid = sharedPreferences.getString("sessionid","");
                }else{
                    sessionid = DeviceData.getUniqueId(getActivity());
                }


                mSettingPresenter.loginout(sessionid);

                break;

            case R.id.rl_update_app:



                break;
        }
    }

    @Override
    public void showPromptMessage(int resId) {

    }

    @Override
    public void showPromptMessage(String message) {

    }

    @Override
    public void loginoutSuccess(RegisterBean mRegisterBean) {
        if (mRegisterBean.getStatus().equals("success")){
            AppManager.getAppManager().AppExit(getActivity());
        }else{
            toast("清除数据失败");
        }
    }

    @Override
    public void loginoutFail(String failMsg) {

    }
}
