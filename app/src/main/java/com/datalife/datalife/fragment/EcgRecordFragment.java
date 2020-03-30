package com.datalife.datalife.fragment;

import com.datalife.datalife.R;
import com.datalife.datalife.base.BaseFragment;
import com.datalife.datalife.base.BaseRecordFragment;

/**
 * Created by LG on 2018/2/5.
 */

public class EcgRecordFragment extends BaseRecordFragment {

    @Override
    protected int getlayoutId() {
        return R.layout.fragment_ecgrecord;
    }

    @Override
    protected void initEventAndData() {

    }

    @Override
    public void showPromptMessage(int resId) {

    }

    @Override
    public void showPromptMessage(String message) {

    }

    @Override
    public String getTitle() {
        return "心电";

    }
}
