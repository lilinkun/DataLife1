package com.datalife.datalife.fragment;

import com.datalife.datalife.R;
import com.datalife.datalife.base.BaseFragment;
import com.datalife.datalife.base.BaseRecordFragment;

/**
 * Created by LG on 2018/2/5.
 */

public class HrRecordFragment extends BaseRecordFragment {
    @Override
    public void showPromptMessage(int resId) {

    }

    @Override
    public void showPromptMessage(String message) {

    }

    @Override
    protected int getlayoutId() {
        return R.layout.fragment_hr_record;
    }

    @Override
    protected void initEventAndData() {

    }

    @Override
    public String getTitle() {
        return "心率";
    }
}
