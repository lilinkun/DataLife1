package com.datalife.datalife.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by LG on 2018/2/6.
 */

public class Utils {

    /**
     * 全屏
     *
     * @param act
     */
    public static void fullScreen(Activity act) {
        act.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // act.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
        // ,WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    /**
     * 用来判断服务是否运行.        
     *
     * @param mContext          
     * @param className 判断的服务名字
     * @return true 在运行 false 不在运行         
     */
    public static boolean isServiceRunning(Context mContext, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager
                .getRunningServices(30);
        if (!(serviceList.size() > 0)) {
            return false;
        }
        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    /**
     * dip转换px
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    //设置发送给脂肪秤的数据
    public static void setSendData() {
        String tempStr = "a5";
        int strDir = 0;
        int sex = 0;
        String wc = "";
        String hc = "";
        if (Variate.getUser() != null) {
            if (Variate.getUser().getSex().equals("0")) {
                sex = 1;
            } else {
                sex = 0;
            }
            wc = Variate.getUser().getWc();
            hc = Variate.getUser().getHc();
            if (wc == null || wc.equals("0") || wc.length() > 0)
                wc = "80";
//            if (wc.length() <= 1)

            if (hc == null || hc.equals("0") || hc.length() > 0)
                hc = "90";
//            if (hc.length() <= 1)

            Log.e("tempStr  wc=", wc + "   hc=" + hc);
            strDir = 165 ^ sex;
            strDir = strDir ^ Integer.parseInt(Variate.getUser().getAge());
            strDir = strDir ^ (int) Double.parseDouble(Variate.getUser().getHeight());
//            strDir = strDir ^ 25;
//            strDir = strDir ^ 175;
            strDir = strDir ^ (int) Double.parseDouble(wc);
            strDir = strDir ^ (int) Double.parseDouble(hc);
            tempStr += ("0" + Integer.toHexString(sex));
//            tempStr += Integer.toHexString(25);
//            tempStr += Integer.toHexString(175);
            String tempAge = Integer.toHexString(Integer.parseInt(Variate.getUser().getAge()));
            if (tempAge.length() == 1) {
                tempAge = "0" + tempAge;
            }
            tempStr += tempAge;
            tempStr += Integer.toHexString((int) Double.parseDouble(Variate.getUser().getHeight()));
            tempStr += Integer.toHexString((int) Double.parseDouble(wc));
            tempStr += Integer.toHexString((int) Double.parseDouble(hc));
            if (Integer.toHexString(strDir).length() == 1) {
                tempStr = tempStr + "0" + Integer.toHexString(strDir);
//                tempStr += Integer.toHexString(strDir);
            } else {
                tempStr += Integer.toHexString(strDir);
            }

//            Variate.user_sendDivData = "A50019AF505A19";
//            Variate.user_sendDivData = "a50019af505a19";
            Variate.user_sendDivData = tempStr;
            Log.e("tempStr=", tempStr);
        } else {
            Variate.user_sendDivData = "a50019af505a19";
        }
    }

    /**
     * 调用相册
     *
     * @param activity
     * @param resultCode 结果
     */
    public static void callCallery(Activity activity, int resultCode) {
        // Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        Intent i = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // intent.addCategory(Intent.CATEGORY_OPENABLE);
        // intent.setType("image/.jpg*");
        activity.startActivityForResult(i, resultCode);
    }

    /**
     * 调用相机
     *
     * @param activity
     * @param imageName  拍照保存的图片名
     * @param resultCode 结果
     */
    public static void callPhoto(Activity activity, int resultCode,
                                 String imageName) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File path = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);// 获取相机图片的路�?
        File file = new File(path, imageName);// 拍照过后图片的默�?路径以及名称
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));// 保存
        activity.startActivityForResult(intent, resultCode);
    }

    /**
     * 将�?择的图片进行缩放
     *
     * @param imageUri   选中图片的Uri
     * @param activity
     * @param resultCode 返回的结果码1
     */
    public static void imageScale(Uri imageUri, Activity activity,
                                  int resultCode, int width, int height) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(imageUri, "image/*");// 按照片的Uri选中图片进行手动缩放

        int i = 100;
        int j = 100;
        if (width != 0) {
            i = width;
        }
        if (height != 0) {
            j = height;
        }

        intent.putExtra("crop", "true");

        intent.putExtra("aspectX", 1);

        intent.putExtra("aspectY", 1);

        intent.putExtra("outputX", i);

        intent.putExtra("outputY", j);

        intent.putExtra("scale", true);
        //
        intent.putExtra("return-data", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        activity.startActivityForResult(intent, resultCode);
        // }

        // intent.putExtra("return-data", true);
        // activity.startActivityForResult(intent, resultCode);
    }

    /**
     * 强制打开EditText的键盘
     *
     * @param
     * @param edit
     */
    public static void openEditText(EditText edit) {
        edit.requestFocus();
        InputMethodManager imm = (InputMethodManager) edit.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 强制隐藏EditText的键盘
     *
     * @param
     * @param edit
     */
    public static void closeEditText(EditText edit) {
        edit.clearFocus();
        InputMethodManager imm = (InputMethodManager) edit.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edit.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * toast提示
     *
     * @param context
     * @param txt
     */
    public static void makeToast(Context context, String txt) {
        Toast.makeText(context, txt, Toast.LENGTH_SHORT).show();
    }

    private static final String TAG = "data";

    public static void LogE(String txt) {
        Log.e(TAG, txt);
    }

    /**
     * 遍历当前类以及父类去查找方法，例子，写的比较简单
     *
     * @param object
     * @param methodName
     * @param params
     * @param paramTypes
     * @return
     */
    public static Object invokeMethod(Object object, String methodName, Object[] params, Class[] paramTypes) {
        Object returnObj = null;
        if (object == null) {
            return null;
        }
        Class cls = object.getClass();
        Method method = null;
        for (; cls != Object.class; cls = cls.getSuperclass()) { //因为取的是父类的默认修饰符的方法，所以需要循环找到该方法
            try {
                method = cls.getDeclaredMethod(methodName, paramTypes);
                break;
            } catch (NoSuchMethodException e) {
//					e.printStackTrace();
            } catch (SecurityException e) {
//					e.printStackTrace();
            }
        }
        if (method != null) {
            method.setAccessible(true);
            try {
                returnObj = method.invoke(object, params);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return returnObj;
    }

    /*
    展开全部list
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() + 1));
        listView.setLayoutParams(params);
    }

    private static DecimalFormat df = new DecimalFormat("0.0");
    private static DecimalFormat df2 = new DecimalFormat("0.00");

    /*
     * */
    public static String getUnit(String weight, String unit) {
        if (unit.equals("lb")) {
            double temp = Double.parseDouble(weight) * 2.2046 / 2;
            BigDecimal b = new BigDecimal(temp);
            double f1 = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
            f1 = f1 * 2;
            if (f1 > 10000)
                f1 = 0;
            return df.format(f1).replace(',', '.');

//            return df.format(Double.parseDouble(weight) * 10 * 22046 / 10000 / 2 * 2 / 10).replace(',', '.');
        } else {
            return df2.format(Double.parseDouble(weight)).replace(',', '.');
        }
    }

    /*
       * */
    public static String getLbUnit(String weight, String unit) {
        if (unit.equals("lb")) {
            return df.format(Double.parseDouble(weight) * 0.453592).replace(',', '.');
        } else {
            return df2.format(Double.parseDouble(weight)).replace(',', '.');
        }
    }

    //切换到德语荷兰语的时候，小数变成的逗号
    public static String getReplace(String string) {
        return string.replace(',', '.');
    }



}
