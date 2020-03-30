package com.datalife.datalife.base;

import android.content.Context;

import com.datalife.datalife.activity.RecordActivity;

/**
 * Created by LG on 2018/2/5.
 */

public abstract class BaseRecordFragment extends BaseFragment {


    protected RecordActivity mActivity;
    public abstract String getTitle();


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (RecordActivity) getActivity();
    }

}
