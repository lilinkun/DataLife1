package com.datalife.datalife.bean;

import com.datalife.datalife.R;
import com.datalife.datalife.fragment.AddUserFragment;
import com.datalife.datalife.fragment.EquipmanagerFragment;
import com.datalife.datalife.fragment.MeFragment;
import com.datalife.datalife.fragment.SettingFragment;
import com.datalife.datalife.fragment.UserinfoFragment;

/**
 * Created by LG on 2018/1/17.
 */

public enum SimpleBackPage {

    MY_INFORMATION(1, R.string.apptab_me, MeFragment.class),
    EQUIPMANAGER(2, R.string.equipmanager, EquipmanagerFragment.class),
    SYSTEMSETTING(3,R.string.systemsetting, SettingFragment.class),
    USERINFO(4,R.string.user_info, UserinfoFragment.class),
    ADDUSER(5,R.string.add_user, AddUserFragment.class);

    private int title;
    private Class<?> clz;
    private int value;

    private SimpleBackPage(int value, int title, Class<?> clz) {
        this.value = value;
        this.title = title;
        this.clz = clz;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public Class<?> getClz() {
        return clz;
    }

    public void setClz(Class<?> clz) {
        this.clz = clz;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public static SimpleBackPage getPageByValue(int val) {
        for (SimpleBackPage p : values()) {
            if (p.getValue() == val)
                return p;
        }
        return null;

    }
}

