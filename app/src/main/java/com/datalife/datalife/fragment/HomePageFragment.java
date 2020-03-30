package com.datalife.datalife.fragment;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;

import com.datalife.datalife.R;
import com.datalife.datalife.activity.HealthMonitorActivity;
import com.datalife.datalife.adapter.HealthNewsAdapter;
import com.datalife.datalife.base.BaseFragment;
import com.datalife.datalife.bean.NewsInfo;
import com.datalife.datalife.util.UIHelper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LG on 2018/1/18.
 */

public class HomePageFragment extends BaseFragment {


    @BindView(R.id.ic_health_news)
    ListView mNewsLv;

    ArrayList<NewsInfo> newsInfos = new ArrayList<>();

    @Override
    protected int getlayoutId() {
        return R.layout.fragment_homepage;
    }

    @Override
    protected void initEventAndData() {

        newsInfos.clear();

        NewsInfo newsInfo = new NewsInfo();
        newsInfo.setContent("你是谁啊");
        newsInfo.setEvaluatenum("20评价");
        newsInfo.setDate("2018-2-3");
        newsInfo.setReadingnum("100阅读量");
        newsInfo.setNewspic("http://img2.cache.netease.com/auto/2016/7/28/201607282215432cd8a.jpg");

        NewsInfo newsInfo1 = new NewsInfo();
        newsInfo1.setContent("neusoft");
        newsInfo1.setEvaluatenum("34评价");
        newsInfo1.setDate("2018-2-6");
        newsInfo1.setReadingnum("90阅读量");
        newsInfo1.setNewspic("https://gss3.bdstatic.com/7Po3dSag_xI4khGkpoWK1HF6hhy/baike/c0%3Dbaike116%2C5%2C5%2C116%2C38/sign=8bac12c4830a19d8df0e8c575293e9ee/cc11728b4710b9128fefbd8bc0fdfc039245229c.jpg");


        newsInfos.add(newsInfo);
        newsInfos.add(newsInfo1);
        HealthNewsAdapter healthNewsAdapter = new HealthNewsAdapter(getActivity(),newsInfos);

        mNewsLv.setAdapter(healthNewsAdapter);

    }

    @OnClick({R.id.rl_health_testing})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.rl_health_testing:
                UIHelper.launcherForResult(getActivity(), HealthMonitorActivity.class,111);
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == 111){
                toast("asdasd");
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
