package com.datalife.datalife.activity;

import android.view.View;
import android.webkit.WebView;

import com.datalife.datalife.R;
import com.datalife.datalife.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LG on 2018/1/28.
 */

public class WebViewActivity extends BaseActivity {

    @BindView(R.id.wv_usage_protocol)
    WebView mUsageProtocolWv;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_web;
    }

    @OnClick({R.id.ic_protocol_back})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ic_protocol_back:

                finish();

                break;
        }
    }

    @Override
    protected void initEventAndData() {

        mUsageProtocolWv.loadUrl("https://auth.100zt.com/News/showAgreement/52");

    }
}
