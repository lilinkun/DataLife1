package com.datalife.datalife.util;

import com.datalife.datalife.R;

/**
 * Created by LG on 2018/2/4.
 */
public enum DefaultPicEnum {
    PIC1(R.mipmap.ic_mother,"1"),
    PIC2(R.mipmap.ic_father,"2"),
    PIC3(R.mipmap.ic_boy,"3"),
    PIC4(R.mipmap.ic_girl,"4"),
    PIC5(R.mipmap.ic_grandma,"5"),
    PIC6(R.mipmap.ic_grandpa,"6");


    private int resPic;
    private String num;

    DefaultPicEnum(int resPic, String num) {
        this.resPic = resPic;
        this.num = num;
    }

    public int getResPic() {
        return resPic;
    }

    public void setResPic(int resPic) {
        this.resPic = resPic;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public static DefaultPicEnum getPageByValue(String val) {
        for (DefaultPicEnum p : values()) {
            if (p.getNum().equals(val))
                return p;
        }
        return null;
    }
}
