package com.datalife.datalife.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.datalife.datalife.R;
import com.datalife.datalife.activity.SimplebackActivity;
import com.datalife.datalife.adapter.AddMemberAdapter;
import com.datalife.datalife.adapter.MemberItemAdapter;
import com.datalife.datalife.base.BaseActivity;
import com.datalife.datalife.base.BaseFragment;
import com.datalife.datalife.bean.FamilyUserInfo;
import com.datalife.datalife.bean.NavUserInfo;
import com.datalife.datalife.bean.ResultBean;
import com.datalife.datalife.contract.AddFamilyUserContract;
import com.datalife.datalife.interf.FragmentCallBack;
import com.datalife.datalife.interf.OnTitleBarClickListener;
import com.datalife.datalife.interf.onTitleBarSetListener;
import com.datalife.datalife.presenter.AddUserPresener;
import com.datalife.datalife.presenter.LoginPresenter1;
import com.datalife.datalife.util.DataLifeUtil;
import com.datalife.datalife.util.DefaultPicEnum;
import com.datalife.datalife.util.DeviceData;
import com.datalife.datalife.util.MyCalendar;
import com.datalife.datalife.widget.MyDateLinear;
import com.datalife.datalife.widget.MyHeightAndWeight;
import com.datalife.datalife.widget.PopWindowSexSelectBy;
import com.datalife.datalife.widget.RoundImageView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;

/**
 * Created by LG on 2018/1/29.
 */

public class AddUserFragment extends BaseFragment implements MyDateLinear.MyDateLinearListener,OnTitleBarClickListener,AddFamilyUserContract.AddFamilyView{


    private PopWindowSexSelectBy popWindowSexSelectBy;
    private MyCalendar calendar;

    private ArrayList<String> height_arrayList = new ArrayList<String>();
    private ArrayList<String> height_arrayList_right = new ArrayList<String>();
    private ArrayList<String> weight_arrayList = new ArrayList<String>();
    NavUserInfo navUserInfo = null;
    FamilyUserInfo familyUserInfo = null;

    private int state;
    private final int DEFAULT = 0;
    private final int MYHEIGHT = 1;
    private final int MYWEIGHT = 2;
    private final int MYDATE = 4;
    private String last_time, my_height,my_weight;
    private int years;
    private int wm_width, wm_height;

    //sex 0代表男 1代表女
    private String Gender = "0";

    @BindView(R.id.myWeight)
    MyHeightAndWeight myWeight;
    @BindView(R.id.myheight)
    MyHeightAndWeight myHeight;
    @BindView(R.id.mydate)
    MyDateLinear myDateLinear;
    @BindView(R.id.text_sex)
    TextView mAddSex;
    @BindView(R.id.text_age)
    TextView mAddAge;
    @BindView(R.id.text_height)
    TextView mAddHeight;
    @BindView(R.id.text_weight)
    TextView mAddWeight;
    @BindView(R.id.et_username)
    EditText mAddUserName;
    @BindView(R.id.ic_adduserhead)
    RoundImageView mRoundImageView;
    private onTitleBarSetListener mListener;
    private FragmentCallBack listener;
    private int clickItem = 1;

    AddUserPresener mAddUserPresener = new AddUserPresener(getActivity());

    @Override
    protected int getlayoutId() {
        return R.layout.fragment_adduser;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (onTitleBarSetListener) context;
        mListener.setRightText("保存");
        mListener.setRightTextVisibility(View.VISIBLE);
        listener = (SimplebackActivity) context;
    }

    @Override
    protected void initEventAndData() {
        mAddUserPresener.onCreate();
        mAddUserPresener.attachView(this);

        WindowManager wm = (WindowManager) getActivity().getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);

        wm_width = wm.getDefaultDisplay().getWidth();
        wm_height = wm.getDefaultDisplay().getHeight();
        initHeightArrayList();

        calendar = calendar.getInstance();
        myDateLinear.setMyDateLinearListener(this);
        Calendar c = Calendar.getInstance();
        myDateLinear.setMaxYear(calendar.get(Calendar.YEAR));
//        myDateLinear.setMinYear(1950);
        myDateLinear.init();
        myDateLinear.init(1990,
                1,
                1);

        myHeight.setMyDateLinearListener(this);
        myHeight.setArrayListYear(height_arrayList);
        myHeight.setArrayListMonth(height_arrayList_right);
        myHeight.setSelectLeftId(141);
        myHeight.setSelectRightId(1);
        myHeight.setTitle_one(getResources().getString(R.string.analyze_project_9));
        myHeight.setTitle_two("cm");
        myHeight.init();

        myWeight.setMyDateLinearListener(this);
        myWeight.setArrayListYear(weight_arrayList);
        myWeight.setArrayListMonth(height_arrayList_right);
        myWeight.setSelectLeftId(30);
        myWeight.setSelectRightId(1);
        myWeight.setTitle_one(getResources().getString(R.string.my_weight));
        myWeight.setTitle_two("kg");
        myWeight.init();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAddUserPresener.onStop();
    }

    @OnClick({R.id.ll_sex,R.id.ll_age,R.id.ll_weight,R.id.ll_height,R.id.ic_adduserhead})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_sex:
                hideLinear();
                popWindowSexSelectBy = new PopWindowSexSelectBy(getActivity(), view);
                popWindowSexSelectBy.setByWhatListener(new PopWindowSexSelectBy.ByWhatListener() {
                @Override
                public void onByWhat(Boolean isByCamera) {
                    // TODO Auto-generated method stub
                    if (!isByCamera) {
                        mAddSex.setText(R.string.my_sex_nan);
                        Gender = "0";
//                        User_sex = "1";
                    } else {
                        mAddSex.setText(R.string.my_sex_nv);
                        Gender = "1";
//                        User_sex = "0";
                    }
                }
            });
                popWindowSexSelectBy.show();
                break;

            case R.id.ll_age:
                hideLinear();
                myDateLinear.init(calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH) + 1,
                        calendar.get(Calendar.DAY_OF_MONTH));
                setMyVisible(myDateLinear, MYDATE);
                break;

            case R.id.ll_weight:
                hideLinear();

                setMyVisible(myWeight, MYWEIGHT);

                break;

            case R.id.ll_height:
                hideLinear();
                setMyVisible(myHeight, MYHEIGHT);
                break;

            case R.id.ic_adduserhead:
                View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.member_popup_listview, null);

                ArrayList<String> arrayList = new ArrayList<String>();

                for (int i =1;i < 7;i++){
                    arrayList.add(String.valueOf(i));
            }
                final GridView gridView = (GridView) contentView.findViewById(R.id.lv_add_member);
                gridView.setVisibility(View.VISIBLE);
                gridView.setAdapter(new AddMemberAdapter(getActivity(),arrayList));
                final PopupWindow popupWindow = new PopupWindow(contentView,
                        LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.WRAP_CONTENT, true);
                popupWindow.setContentView(contentView);
                popupWindow.showAsDropDown(mRoundImageView);

                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        gridView.setVisibility(View.GONE);
                        clickItem = position + 1;
                        mRoundImageView.setImageResource(DefaultPicEnum.getPageByValue(String.valueOf(clickItem)).getResPic());
                    }
                });
                myDateLinear.setVisibility(View.GONE);
                myWeight.setVisibility(View.GONE);
                myHeight.setVisibility(View.GONE);

                break;
        }
    }


    /**
     * edittext值
     * @param textView
     * @return
     */
    public String dataStr(TextView textView){
        if (textView == null  || textView.getText().toString().trim().length() == 0){
            return "";
        }else{
            return textView.getText().toString();
        }
    }

    public void initHeightArrayList() {
        for (int i = 30; i <= 220; i++) {
            String str = i + "";
            height_arrayList.add(str);
        }
        for (int i = 0; i <= 9; i++) {
            String str = "." + i;
            height_arrayList_right.add(str);
        }
        for (int i = 30; i <= 130; i++) {
            String str = i + "";
            weight_arrayList.add(str);
        }
    }


    /**
     * edittext是否为空
     * @param editText
     * @return
     */
    public boolean isEmpty(EditText editText){
       if(editText == null  || editText.getText().toString().trim().length() == 0){
           return true;
       }else {
           return false;
       }
    }

    /**
     * testview是否为空
     * @param textView
     * @return
     */
    public boolean isTextEmpty(TextView textView) {
        if (textView == null || textView.getText().toString().trim().length() == 0) {
        return true;
        } else {
        return false;
        }
    }

    public void setMyVisible(View view, int state) {
        if (myDateLinear.getVisibility() == View.VISIBLE || myHeight.getVisibility() == View.VISIBLE ) {
            return;
        }
        this.state = state;
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.fenxiang_weiyi_input);
        view.setAnimation(animation);
        view.setVisibility(View.VISIBLE);
    }

    @Override
    public void showPromptMessage(int resId) {

    }

    @Override
    public void showPromptMessage(String message) {

    }

    @Override
    public int getscreenWidth() {
        return wm_width;
    }

    @Override
    public int getscreenHeight() {
        return wm_height;
    }

    @Override
    public void cancle() {
        if (state == MYDATE) {
            setMyInvisible(myDateLinear);
            state = DEFAULT;
        } else if (state == MYHEIGHT) {
            setMyInvisible(myHeight);
            state = DEFAULT;
        } else if (state == MYWEIGHT) {
            setMyInvisible(myWeight);
            state = DEFAULT;
        }
    }

    @Override
    public void sure() {
        if (state == MYDATE) {
            setMyInvisible(myDateLinear);
            state = DEFAULT;
            String after_date = myDateLinear.getSelcet_my_year() + "-"
                    + myDateLinear.getSelcet_my_month() + "-"
                    + myDateLinear.getSelcet_my_day();
            String str_date = after_date;
            last_time = str_date;
            calendar.set(Calendar.YEAR, myDateLinear.getSelcet_my_year());
            calendar.set(Calendar.MONTH, myDateLinear.getSelcet_my_month() - 1);
            calendar.set(Calendar.DAY_OF_MONTH, myDateLinear.getSelcet_my_day());

            Date now = new Date();
            Date born = new Date();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            try {
                born = df.parse(str_date);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            String str = df.format(born);
            long days = (now.getTime() - born.getTime()) / (1000 * 60 * 60 * 24);//得到总天数
            years = (int) days / 365;
            Log.e("years:", years + "");
            mAddAge.setText(str);

        } else if (state == MYHEIGHT) {
            setMyInvisible(myHeight);
            state = DEFAULT;
            my_height = myHeight.getData();
            mAddHeight.setText((int) Double.parseDouble(my_height) + "cm");
        } else if (state == MYWEIGHT) {
            setMyInvisible(myWeight);
            state = DEFAULT;
            my_weight = myWeight.getData();
            mAddWeight.setText((int) Double.parseDouble(my_weight) + "kg");
        }
    }


    private void hideLinear() {
        if (state == MYHEIGHT) {
            setMyInvisible(myHeight);
            state = DEFAULT;
        } else if (state == MYDATE) {
            setMyInvisible(myDateLinear);
            state = DEFAULT;
        } else if (state == MYWEIGHT) {
            setMyInvisible(myWeight);
            state = DEFAULT;
        }
    }

    @Override
    public void realTime(String data) {
        if (state == MYDATE) {
            String after_date = myDateLinear.getSelcet_my_year() + "-"
                    + myDateLinear.getSelcet_my_month() + "-"
                    + myDateLinear.getSelcet_my_day();
            String str_date = after_date;
            last_time = str_date;
            calendar.set(Calendar.YEAR, myDateLinear.getSelcet_my_year());
            calendar.set(Calendar.MONTH, myDateLinear.getSelcet_my_month() - 1);
            calendar.set(Calendar.DAY_OF_MONTH, myDateLinear.getSelcet_my_day());

            Date now = new Date();
            Date born = new Date();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            try {
                born = df.parse(str_date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            long days = (now.getTime() - born.getTime()) / (1000 * 60 * 60 * 24);//得到总天数
            years = (int) days / 365;
            Log.e("years:", years + "");
            mAddAge.setText(years + " ");

        } else if (state == MYHEIGHT) {
            my_height = myHeight.getData();
            mAddHeight.setText((int) Double.parseDouble(my_height) + "cm");
        } else if (state == MYWEIGHT) {
            my_weight = myWeight.getData();
            mAddWeight.setText((int) Double.parseDouble(my_weight) + "kg");
        }
    }

    public void setMyInvisible(View view) {
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.fenxiang_weiyi_output);
        view.setAnimation(animation);
        view.setVisibility(View.GONE);
    }

    @Override
    public void onBackClick() {
        getActivity().finish();
    }

    @Override
    public void onMoreClick() {
        navUserInfo = new NavUserInfo();
        familyUserInfo = new FamilyUserInfo();
        if (isEmpty(mAddUserName)){
            toast("请输入用户名");
            return;
        }else if (isTextEmpty(mAddAge)) {
            toast("请输入年龄");
            return;
        }else if(isTextEmpty(mAddHeight)) {
            toast("请输入身高");
            return;
        } else if(isTextEmpty(mAddSex)) {
            toast("请输入性别");
            return;
        }else if(isTextEmpty(mAddWeight)) {
            toast("请输入体重");
            return;
        }else{
            navUserInfo.setAge(dataStr(mAddAge));
            navUserInfo.setUsername(mAddUserName.getText().toString());
            navUserInfo.setHeight(DataLifeUtil.getPlusUnit(dataStr(mAddHeight)));
            navUserInfo.setSex(dataStr(mAddSex));
            navUserInfo.setWeight(DataLifeUtil.getPlusUnit(dataStr(mAddWeight)));

            familyUserInfo.setCreateDate(dataStr(mAddAge));
            familyUserInfo.setMember_Sex(Gender);
            familyUserInfo.setMember_Name(mAddUserName.getText().toString());
//            familyUserInfo.setMember_Sex(dataStr(mAddSex));
            familyUserInfo.setMember_Stature(Double.parseDouble(DataLifeUtil.getPlusUnit(dataStr(mAddHeight))));
//            familyUserInfo.setSex(dataStr(mAddSex));
            familyUserInfo.setMember_Weight(Double.parseDouble(DataLifeUtil.getPlusUnit(dataStr(mAddWeight))));
            familyUserInfo.setMember_Portrait(String.valueOf(clickItem));
        }

        String sessionid = DeviceData.getUniqueId(getActivity());

        mAddUserPresener.addFamilyUser(familyUserInfo.getMember_Name(),familyUserInfo.getMember_Portrait(),DataLifeUtil.getPlusUnit(dataStr(mAddHeight)),DataLifeUtil.getPlusUnit(dataStr(mAddWeight)),familyUserInfo.getCreateDate(),familyUserInfo.getMember_Sex(),"0","1",sessionid );

    }

    @Override
    public void onsuccess(ResultBean resultBean) {
        Intent intent = new Intent();
        intent.putExtra("familyUserInfo",familyUserInfo);
        getActivity().setResult(Activity.RESULT_OK,intent);
        getActivity().finish();
    }

    @Override
    public void onfail(String failMsg) {
        toast(failMsg + "");
    }
}
