package com.datalife.datalife.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.datalife.datalife.R;
import com.datalife.datalife.adapter.FragmentsAdapter;
import com.datalife.datalife.adapter.NavAdapter;
import com.datalife.datalife.base.BaseActivity;
import com.datalife.datalife.base.BaseFragment;
import com.datalife.datalife.bean.FamilyUserInfo;
import com.datalife.datalife.bean.LoginBean;
import com.datalife.datalife.bean.NavUserInfo;
import com.datalife.datalife.bean.PageBean;
import com.datalife.datalife.bean.ResultBean;
import com.datalife.datalife.bean.ResultFamily;
import com.datalife.datalife.bean.SimpleBackPage;
import com.datalife.datalife.contract.MainContract;
import com.datalife.datalife.fragment.HealthHomeFragment;
import com.datalife.datalife.fragment.HomePageFragment;
import com.datalife.datalife.fragment.MallFragment;
import com.datalife.datalife.fragment.MeFragment;
import com.datalife.datalife.interf.OnNavDeleteListener;
import com.datalife.datalife.presenter.MainPresenter;
import com.datalife.datalife.util.DataLifeUtil;
import com.datalife.datalife.util.DeviceData;
import com.datalife.datalife.util.UIHelper;
import com.datalife.datalife.util.UToast;
import com.datalife.datalife.widget.RoundImageView;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener, NavigationView.OnNavigationItemSelectedListener,MainContract.MainView{

    @BindView(R.id.view_pager) ViewPager mViewPager;
    @BindView(R.id.tv_me) TextView mMeText;
    @BindView(R.id.tv_homepage) TextView mHomePageText;
    @BindView(R.id.tv_health_home) TextView mHealthHomeText;
    @BindView(R.id.tv_mall) TextView mMallText;
    @BindView(R.id.ic_homepage) ImageView mHomePageImage;
    @BindView(R.id.ic_health_home) ImageView mHealthImage;
    @BindView(R.id.ic_mall) ImageView mMallImage;
    @BindView(R.id.ic_me) ImageView mMeImage;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;
    @BindView(R.id.nav_view) NavigationView navigationView;
    @BindView(R.id.gv_nav) GridView mNavGridView;
    @BindView(R.id.ic_nav_subtract) ImageView mNavSubtractIv;
    @BindView(R.id.ic_nav_plus) ImageView mNavPlusIv;
    @BindView(R.id.tv_nav_name)  TextView mNavNameTv;
    @BindView(R.id.iv_account) RoundImageView mHeadAccountIv;

    private final SparseArray<BaseFragment> sparseArray = new SparseArray<>();
    public static List<FamilyUserInfo> familyUserInfos;
    public static LoginBean loginBean;
    private NavAdapter navAdapter;

    public static int SCREEN_WIDTH = 0;
    public static int SCREEN_HEIGHT = 0;


    private MainPresenter mainPresenter = new MainPresenter(this);

    @Override
    protected void initEventAndData() {
        mainPresenter.onCreate();
        mainPresenter.attachView(this);
        setSupportActionBar(mToolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        mToolbar.setNavigationIcon(R.mipmap.ic_homepage_userinfo);
        navigationView.setNavigationItemSelectedListener(this);
        onPageBind();
        changeTab(0);

        WindowManager wm = this.getWindowManager();
        SCREEN_WIDTH= wm.getDefaultDisplay().getWidth();
        SCREEN_HEIGHT= wm.getDefaultDisplay().getHeight();

        loginBean = DataLifeUtil.getLoginData(this);
        if (loginBean != null){
            mNavNameTv.setText(loginBean.getUser_name());

            if (loginBean.getHeadPic() != null && loginBean.getHeadPic().trim().length() != 0){
                Picasso.with(mHeadAccountIv.getContext()).load(loginBean.getHeadPic()).into(mHeadAccountIv);
            }
        }

        String sessionid = DeviceData.getUniqueId(this);

        mainPresenter.getFamilyDataList(sessionid,"3","1");


    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_nav;
    }

    int i = 0;

    @OnClick({R.id.ll_health_home,R.id.ll_homepage,R.id.ll_mall,R.id.ll_me,R.id.ic_nav_plus,R.id.ic_nav_subtract})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.ll_health_home:
                changeTab(2);
                mViewPager.setCurrentItem(2,false);
                break;
            case R.id.ll_homepage:
                changeTab(0);
                mViewPager.setCurrentItem(0,false);
                break;
            case R.id.ll_mall:
                changeTab(1);
                mViewPager.setCurrentItem(1,false);
                break;
            case R.id.ll_me:
                changeTab(3);
                mViewPager.setCurrentItem(3,false);
                break;
            case R.id.ic_nav_plus:

                UIHelper.showSimpleBackForResult(this, SimplebackActivity.RESULT_ADDUSER,SimpleBackPage.ADDUSER);

                break;

            case R.id.ic_nav_subtract:
                i++;
                if (navAdapter != null){
                    if (i%2 == 1){
                        navAdapter.onDelete();
                        mNavSubtractIv.setImageResource(R.mipmap.ic_change_delete);
                    }else{
                        navAdapter.onDeleteGone();
                        mNavSubtractIv.setImageResource(R.mipmap.ic_nav_subtract);
                    }
                }

                break;
        }
    }


    public void onPageBind() {
        FragmentsAdapter adapter = new FragmentsAdapter(getSupportFragmentManager());
        getMenusFragments();
        adapter.setFragments(sparseArray);
        mViewPager.setAdapter(adapter);
        mViewPager.setOnPageChangeListener(this);
    }

    private void getMenusFragments() {
        sparseArray.put(0, new HomePageFragment());
        sparseArray.put(1, new MallFragment());
        sparseArray.put(2, new HealthHomeFragment());
        sparseArray.put(3, new MeFragment());
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        changeTab(position);
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    //改变tab
    private void changeTab(int position){
        switch (position){
            case 0:
                mToolbar.setVisibility(View.VISIBLE);
                mHomePageText.setTextColor(getResources().getColor(R.color.blue));
                mHomePageImage.setImageDrawable(getResources().getDrawable(R.mipmap.ic_homepage_actived));
                mMallText.setTextColor(getResources().getColor(R.color.gray));
                mMallImage.setImageDrawable(getResources().getDrawable(R.mipmap.ic_mall_normal));
                mHealthHomeText.setTextColor(getResources().getColor(R.color.gray));
                mHealthImage.setImageDrawable(getResources().getDrawable(R.mipmap.ic_health_normal));
                mMeText.setTextColor(getResources().getColor(R.color.gray));
                mMeImage.setImageDrawable(getResources().getDrawable(R.mipmap.ic_me_normal));
                break;

            case 1:
                mToolbar.setVisibility(View.GONE);
                mHomePageText.setTextColor(getResources().getColor(R.color.gray));
                mHomePageImage.setImageDrawable(getResources().getDrawable(R.mipmap.ic_homepage_normal));
                mMallText.setTextColor(getResources().getColor(R.color.blue));
                mMallImage.setImageDrawable(getResources().getDrawable(R.mipmap.ic_mall_actived));
                mHealthHomeText.setTextColor(getResources().getColor(R.color.gray));
                mHealthImage.setImageDrawable(getResources().getDrawable(R.mipmap.ic_health_normal));
                mMeText.setTextColor(getResources().getColor(R.color.gray));
                mMeImage.setImageDrawable(getResources().getDrawable(R.mipmap.ic_me_normal));
                break;


            case 2:
                mToolbar.setVisibility(View.GONE);
                mHomePageText.setTextColor(getResources().getColor(R.color.gray));
                mHomePageImage.setImageDrawable(getResources().getDrawable(R.mipmap.ic_homepage_normal));
                mMallText.setTextColor(getResources().getColor(R.color.gray));
                mMallImage.setImageDrawable(getResources().getDrawable(R.mipmap.ic_mall_normal));
                mHealthHomeText.setTextColor(getResources().getColor(R.color.blue));
                mHealthImage.setImageDrawable(getResources().getDrawable(R.mipmap.ic_health_actived));
                mMeText.setTextColor(getResources().getColor(R.color.gray));
                mMeImage.setImageDrawable(getResources().getDrawable(R.mipmap.ic_me_normal));
                break;


            case 3:
                mToolbar.setVisibility(View.GONE);
                mHomePageText.setTextColor(getResources().getColor(R.color.gray));
                mHomePageImage.setImageDrawable(getResources().getDrawable(R.mipmap.ic_homepage_normal));
                mMallText.setTextColor(getResources().getColor(R.color.gray));
                mMallImage.setImageDrawable(getResources().getDrawable(R.mipmap.ic_mall_normal));
                mHealthHomeText.setTextColor(getResources().getColor(R.color.gray));
                mHealthImage.setImageDrawable(getResources().getDrawable(R.mipmap.ic_health_normal));
                mMeText.setTextColor(getResources().getColor(R.color.blue));
                mMeImage.setImageDrawable(getResources().getDrawable(R.mipmap.ic_me_actived));
                break;
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){

            if (requestCode == SimplebackActivity.RESULT_ADDUSER){
                FamilyUserInfo navUserInfo = (FamilyUserInfo) data.getSerializableExtra("familyUserInfo");
                if (navUserInfo != null){
                    if(navAdapter != null){
                        familyUserInfos.add(navUserInfo);
                        navAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK){
//
//            if (navigationView != null && navigationView.isShown()){
//                navigationView.setVisibility(View.GONE);
//            }
//
//            return true;
//        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void showPromptMessage(int resId) {

    }

    @Override
    public void showPromptMessage(String message) {

    }

    @Override
    public void onBackFamilyListDataSuccess(ResultFamily<ArrayList<FamilyUserInfo>,PageBean> listResultBean) {

        familyUserInfos = listResultBean.getData();
        navAdapter = new NavAdapter(this,familyUserInfos);
        mNavGridView.setAdapter(navAdapter);
//            }
    }

    @Override
    public void onBackFamilyListDataFail(String str) {
        toast(str);
    }
}
