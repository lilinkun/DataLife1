package com.datalife.datalife.fragment;

import com.datalife.datalife.R;
import com.datalife.datalife.base.BaseFragment;
import com.datalife.datalife.base.BaseRecordFragment;

/**
 * Created by LG on 2018/2/5.
 */

public class BpRecordFragment extends BaseRecordFragment {



    @Override
    protected int getlayoutId() {
        return R.layout.fragment_bprecord;
    }

    @Override
    protected void initEventAndData() {

    }

    @Override
    public String getTitle() {
        return "血压";
    }


    @Override
    public void showPromptMessage(int resId) {

    }

    @Override
    public void showPromptMessage(String message) {

    }
}
