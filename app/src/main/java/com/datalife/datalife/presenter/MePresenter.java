package com.datalife.datalife.presenter;

import android.view.View;

import com.datalife.datalife.base.BaseFragment;
import com.datalife.datalife.base.BasePresenter;
import com.datalife.datalife.contract.MeContract;
import com.datalife.datalife.fragment.MeFragment;
import com.datalife.datalife.mvp.IModel;

import java.util.HashMap;

/**
 * Created by LG on 2018/1/17.
 */

public class MePresenter extends BasePresenter<MeFragment> implements MeContract.MePresenter {

    @Override
    public HashMap<String, IModel> getiModelMap() {
        return null;
    }

    @Override
    public HashMap<String, IModel> loadModelMap(IModel... models) {
        return null;
    }

    @Override
    public void ordermanager() {

    }

    @Override
    public void shoppingcarmanager() {

    }

    @Override
    public void equitManager() {

    }

    @Override
    public void systemsetting() {

    }
}
