package com.datalife.datalife.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import com.datalife.datalife.R;
import com.datalife.datalife.adapter.FragmentAdapter;
import com.datalife.datalife.adapter.FragmentsAdapter;
import com.datalife.datalife.base.BaseActivity;
import com.datalife.datalife.base.BaseFragment;
import com.datalife.datalife.base.BaseRecordFragment;
import com.datalife.datalife.fragment.BpFragment;
import com.datalife.datalife.fragment.BpRecordFragment;
import com.datalife.datalife.fragment.BtFragment;
import com.datalife.datalife.fragment.BtRecordFragment;
import com.datalife.datalife.fragment.ECGFragment;
import com.datalife.datalife.fragment.EcgRecordFragment;
import com.datalife.datalife.fragment.HrRecordFragment;
import com.datalife.datalife.fragment.SPO2HFragment;
import com.datalife.datalife.fragment.Spo2hRecordFragment;
import com.datalife.datalife.widget.CustomViewPager;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LG on 2018/2/5.
 */

public class RecordActivity extends BaseActivity implements ViewPager.OnPageChangeListener, TabLayout.OnTabSelectedListener {


    private final SparseArray<BaseRecordFragment> sparseArray = new SparseArray<>();

    @BindView(R.id.view_page)
    CustomViewPager customViewPager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.text_day)
    TextView mDayTv;
    @BindView(R.id.text_week)
    TextView mWeekTv;
    @BindView(R.id.text_month)
    TextView mMonthTv;
    @BindView(R.id.text_year)
    TextView mYearTv;



    @Override
    protected int getLayoutId() {
        return R.layout.activity_record;
    }

    @Override
    protected void initEventAndData() {

        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        getMenusFragments();
        adapter.setFragments(sparseArray);
        customViewPager.setOffscreenPageLimit(4);
        customViewPager.setPageMargin(10);
        customViewPager.setAdapter(adapter);
        tabLayout.addOnTabSelectedListener(this);
        customViewPager.addOnPageChangeListener(this);
        customViewPager.setCurrentItem(0, false);
        tabLayout.setupWithViewPager(customViewPager);


    }


    @OnClick({R.id.text_day,R.id.text_week,R.id.text_month,R.id.text_year,R.id.iv_head_left})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.text_day:
                setSelectTime(1);
                break;
            case R.id.text_week:
                setSelectTime(2);
                break;
            case R.id.text_month:
                setSelectTime(3);
                break;
            case R.id.text_year:
                setSelectTime(4);
                break;
            case R.id.iv_head_left:
                finish();
                break;
        }
    }

    public void setSelectTime(int type) {

        switch (type) {
            case 1:
                mDayTv.setBackgroundResource(R.drawable.shape_text);
                mWeekTv.setBackgroundResource(R.color.white);
                mMonthTv.setBackgroundResource(R.color.white);
                mYearTv.setBackgroundResource(R.color.white);

                mDayTv.setTextColor(getResources().getColor(R.color.white));
                mWeekTv.setTextColor(getResources().getColor(R.color.black));
                mMonthTv.setTextColor(getResources().getColor(R.color.black));
                mYearTv.setTextColor(getResources().getColor(R.color.black));
                break;
            case 2:
                mWeekTv.setBackgroundResource(R.drawable.shape_text);
                mDayTv.setBackgroundResource(R.color.white);
                mMonthTv.setBackgroundResource(R.color.white);
                mYearTv.setBackgroundResource(R.color.white);

                mDayTv.setTextColor(getResources().getColor(R.color.black));
                mWeekTv.setTextColor(getResources().getColor(R.color.white));
                mMonthTv.setTextColor(getResources().getColor(R.color.black));
                mYearTv.setTextColor(getResources().getColor(R.color.black));
                break;
            case 3:
                mMonthTv.setBackgroundResource(R.drawable.shape_text);
                mWeekTv.setBackgroundResource(R.color.white);
                mDayTv.setBackgroundResource(R.color.white);
                mYearTv.setBackgroundResource(R.color.white);
                mDayTv.setTextColor(getResources().getColor(R.color.black));
                mWeekTv.setTextColor(getResources().getColor(R.color.black));
                mMonthTv.setTextColor(getResources().getColor(R.color.white));
                mYearTv.setTextColor(getResources().getColor(R.color.black));
                break;
            case 4:
                mYearTv.setBackgroundResource(R.drawable.shape_text);
                mWeekTv.setBackgroundResource(R.color.white);
                mMonthTv.setBackgroundResource(R.color.white);
                mDayTv.setBackgroundResource(R.color.white);
                mDayTv.setTextColor(getResources().getColor(R.color.black));
                mWeekTv.setTextColor(getResources().getColor(R.color.black));
                mMonthTv.setTextColor(getResources().getColor(R.color.black));
                mYearTv.setTextColor(getResources().getColor(R.color.white));
        }
    }

    private void getMenusFragments() {
        sparseArray.put(0, new Spo2hRecordFragment());
        sparseArray.put(1, new HrRecordFragment());
        sparseArray.put(2, new BpRecordFragment());
        sparseArray.put(3, new BtRecordFragment());
        sparseArray.put(4, new EcgRecordFragment());
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
