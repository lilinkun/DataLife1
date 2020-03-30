package com.datalife.datalife.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.datalife.datalife.bean.LoginBean;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by LG on 2018/2/1.
 */

public class DataLifeUtil {

    public static final int PAGE_HOME = 0;
    public static final int PAGE_BP = 1;
    public static final int PAGE_TEMP = 2;
    public static final int PAGE_SPO2H = 3;
    public static final int PAGE_ECG = 4;
    public static final int PAGE_RECORD = 5;

    /**
     * 序列化对象
     * @param loginBean
     * @return
     * @throws IOException
     */
    public static String serialize(LoginBean loginBean) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                byteArrayOutputStream);
        objectOutputStream.writeObject(loginBean);
        String serStr = byteArrayOutputStream.toString("ISO-8859-1");
        serStr = java.net.URLEncoder.encode(serStr, "UTF-8");
        objectOutputStream.close();
        byteArrayOutputStream.close();
        Log.d("serial", "serialize str =" + serStr);
        return serStr;
    }

    /**
     * 序列化对象
     * @return
     * @throws IOException
     */
    public static String serialize(Serializable t) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                byteArrayOutputStream);
        objectOutputStream.writeObject(t);
        String serStr = byteArrayOutputStream.toString("ISO-8859-1");
        serStr = java.net.URLEncoder.encode(serStr, "UTF-8");
        objectOutputStream.close();
        byteArrayOutputStream.close();
        Log.d("serial", "serialize str =" + serStr);
        return serStr;
    }

    /**
     * 反序列化对象
     * @param str
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static LoginBean deSerialization(String str) throws IOException,
            ClassNotFoundException {
        String redStr = java.net.URLDecoder.decode(str, "UTF-8");
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                redStr.getBytes("ISO-8859-1"));
        ObjectInputStream objectInputStream = new ObjectInputStream(
                byteArrayInputStream);
        LoginBean loginBean = (LoginBean) objectInputStream.readObject();
        objectInputStream.close();
        byteArrayInputStream.close();
        return loginBean;
    }

    /**
     * 反序列化对象
     * @param str
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Serializable unSerialization(String str) throws IOException,
            ClassNotFoundException {
        String redStr = java.net.URLDecoder.decode(str, "UTF-8");
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                redStr.getBytes("ISO-8859-1"));
        ObjectInputStream objectInputStream = new ObjectInputStream(
                byteArrayInputStream);
        Serializable loginBean = (Serializable) objectInputStream.readObject();
        objectInputStream.close();
        byteArrayInputStream.close();
        return loginBean;
    }

    /**
     * 保存数据
     * @param context
     * @param str
     */
    public static void saveData(Context context , String str){
        SharedPreferences sharedPreferences = context.getSharedPreferences("login",Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("logininfo",str).commit();
    }

    /**
     * 保存最近一次温度
     */
    public static void saveBtData(Context context , String str){
        SharedPreferences sharedPreferences = context.getSharedPreferences("login",Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("bt",str).commit();
    }

    /**
     * 保存最近一次温度
     */
    public static void saveBpData(Context context , String str){
        SharedPreferences sharedPreferences = context.getSharedPreferences("login",Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("bp",str).commit();
    }

    /**
     * 保存最近一次温度
     */
    public static void saveSpo2hData(Context context , String str){
        SharedPreferences sharedPreferences = context.getSharedPreferences("login",Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("spo2h",str).commit();
    }

    /**
     * 保存最近一次温度
     */
    public static void saveEcgData(Context context , String str){
        SharedPreferences sharedPreferences = context.getSharedPreferences("login",Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("ecg",str).commit();
    }

    /**
     * 获取测量数据
     */
    public  static String getTestData(Context context,String title){
        SharedPreferences sharedPreferences = context.getSharedPreferences("login",Context.MODE_PRIVATE);
        String str = sharedPreferences.getString(title,"");
        return  str;
    }

    /**
     * 获取登录信息
     * @param context
     * @return
     */
    public static LoginBean getLoginData(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("login",Context.MODE_PRIVATE);
        if (sharedPreferences != null) {
            String str = sharedPreferences.getString("logininfo", "");
            try {
                LoginBean loginBean = (LoginBean)DataLifeUtil.deSerialization(str);

                return loginBean;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 得到数据
     * @param context
     * @return
     */
    public static String getData(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("login",Context.MODE_PRIVATE);
        String str = sharedPreferences.getString("logininfo","");
        return  str;
    }

    /**
     * 去除单位的方法
     */
    public static final String getPlusUnit(String str){

        String resultStr = str.substring(0,str.length()-2);
        return resultStr;

    }


    /**
     * 获取头像
     * @param str
     * @param iv
     */
    public static void GetPIC(Context context ,String str , ImageView iv){
        if (str.equals(DefaultPicEnum.PIC1.getNum())){
            iv.setImageDrawable(context.getResources().getDrawable(DefaultPicEnum.PIC1.getResPic()));
        }else if (str.equals(DefaultPicEnum.PIC2.getNum())){
            iv.setImageDrawable(context.getResources().getDrawable(DefaultPicEnum.PIC2.getResPic()));
        }else if (str.equals(DefaultPicEnum.PIC3.getNum())){
            iv.setImageDrawable(context.getResources().getDrawable(DefaultPicEnum.PIC3.getResPic()));
        }else if (str.equals(DefaultPicEnum.PIC4.getNum())){
            iv.setImageDrawable(context.getResources().getDrawable(DefaultPicEnum.PIC4.getResPic()));
        }else if (str.equals(DefaultPicEnum.PIC5.getNum())){
            iv.setImageDrawable(context.getResources().getDrawable(DefaultPicEnum.PIC5.getResPic()));
        }else if (str.equals(DefaultPicEnum.PIC6.getNum())){
            iv.setImageDrawable(context.getResources().getDrawable(DefaultPicEnum.PIC6.getResPic()));
        }
    }

    /**
     *
     * @param context
     */
    public static void startAlarm(Context context) {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (notification == null) return;
        Ringtone r = RingtoneManager.getRingtone(context, notification);
        r.play();
    }

    /**
     * 获取当前时间
     * @return
     */
    public  static String getCurrentTime(){
        Date day=new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return df.format(day);
    }


}
