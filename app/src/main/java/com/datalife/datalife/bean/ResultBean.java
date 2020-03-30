package com.datalife.datalife.bean;

import java.io.Serializable;

/**
 * Created by LG on 2018/1/17.
 */

public class ResultBean<T> implements Serializable {

    private String Status;
    private T Data;
    private String Code;
    private String Page;
    private String StartTime;
    private String LengTime;
    private String Desc;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getPage() {
        return Page;
    }

    public void setPage(String page) {
        Page = page;
    }

    public String getStartTime() {
        return StartTime;
    }

    public T getData() {
        return Data;
    }

    public void setData(T data) {
        Data = data;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getLengTime() {
        return LengTime;
    }

    public void setLengTime(String lengTime) {
        LengTime = lengTime;
    }

    @Override
    public String toString() {
        return "RegisterBean{" +
                "Status='" + Status + '\'' +
                ", Data='" + Data + '\'' +
                ", Code='" + Code + '\'' +
                ", Page='" + Page + '\'' +
                ", StartTime='" + StartTime + '\'' +
                ", LengTime='" + LengTime + '\'' +
                ", Desc='" + Desc + '\'' +
                '}';
    }
}
