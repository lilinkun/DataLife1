package com.datalife.datalife.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.datalife.datalife.activity.SimplebackActivity;
import com.datalife.datalife.bean.SimpleBackPage;

/**
 * Created by LG on 2018/1/17.
 * 界面帮助类
 */

public class UIHelper {

    /**
     * 不带有参的跳转
     */
    public static void showSimpleBackForResult(Activity context,
                                               int requestCode, SimpleBackPage page) {
        Intent intent = new Intent(context, SimplebackActivity.class);
        intent.putExtra(SimplebackActivity.BUNDLE_KEY_PAGE, page.getValue());
        context.startActivityForResult(intent, requestCode);
    }

    /**
     *带有参的跳转
     */
    public static void showSimpleBackBundleForResult(Activity context,
                                                     int requestCode, SimpleBackPage page, Bundle bundle) {
        Intent intent = new Intent(context, SimplebackActivity.class);
        intent.putExtra(SimplebackActivity.BUNDLE_KEY_PAGE, page.getValue());
        intent.putExtra(SimplebackActivity.BUNDLE_KEY_ARGS,bundle);
        context.startActivityForResult(intent, requestCode);
    }

    public static void launcherForResult(Activity activity, Class<?> targetActivity, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(activity, targetActivity);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void launcher(Context context, Class<?> targetActivity) {
        Intent intent = new Intent();
        intent.setClass(context, targetActivity);
        context.startActivity(intent);
    }

    public static void launcher(Activity activity, Class<?> targetActivity) {
        Intent intent = new Intent();
        intent.setClass(activity, targetActivity);
        activity.startActivity(intent);
    }

}
