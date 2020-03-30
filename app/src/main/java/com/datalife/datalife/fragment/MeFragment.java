package com.datalife.datalife.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.datalife.datalife.R;
import com.datalife.datalife.activity.SimplebackActivity;
import com.datalife.datalife.app.AppManager;
import com.datalife.datalife.base.BaseFragment;
import com.datalife.datalife.bean.LoginBean;
import com.datalife.datalife.bean.SimpleBackPage;
import com.datalife.datalife.util.DataLifeUtil;
import com.datalife.datalife.util.UIHelper;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/1/12.
 */

public class MeFragment extends BaseFragment {

    @BindView(R.id.tv_user_name)
    TextView mUserNameTv;
    @BindView(R.id.tv_user_tel)
    TextView mTelTv;

    @Override
    protected int getlayoutId() {
        return R.layout.fragment_me;
    }

    @Override
    protected void initEventAndData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login",Context.MODE_PRIVATE);
        if (sharedPreferences != null) {
            String str = sharedPreferences.getString("logininfo", "");
            try {
                LoginBean loginBean = (LoginBean)DataLifeUtil.deSerialization(str);

                mUserNameTv.setText(loginBean.getUser_name());
                mTelTv.setText(loginBean.getMobile());

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    @OnClick({R.id.rl_userinfo,R.id.rl_shoppingcar,R.id.rl_collectionmanager,R.id.rl_equipmanager,R.id.rl_famalymanager,R.id.rl_planmanager,R.id.rl_setting})
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.rl_userinfo:

                UIHelper.showSimpleBackForResult(getActivity(),SimplebackActivity.RESULT_USERINFO,SimpleBackPage.USERINFO);

                break;

            case R.id.rl_equipmanager:

                UIHelper.showSimpleBackForResult(getActivity(), SimplebackActivity.RESULT_EQUIPMANAGER, SimpleBackPage.EQUIPMANAGER);

                break;


            case R.id.rl_setting:

                UIHelper.showSimpleBackForResult(getActivity(),SimplebackActivity.RESULT_SETTING,SimpleBackPage.SYSTEMSETTING);

                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){
            if (requestCode == SimplebackActivity.RESULT_SETTING){
                    getActivity().finish();
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
