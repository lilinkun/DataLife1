package com.datalife.datalife.util;

import android.content.Context;

/**
 * Created by LG on 2018/1/18.
 */

public class StandardUtils {

    public static Standard comparsionWithStandard(Context context, int week, double bmi) {
        if (bmi < 18.5) {
            bmi = 18.5;
        } else if (bmi < 25) {
            bmi = 25;
        } else if (bmi < 30) {
            bmi = 30;
        } else {
            bmi = 100;
        }
//        AssetsDatabaseManager.initManager(context);
//        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase(
//                "standard.db");
//        if (db != null) {
//            Cursor cursor = db.query("com_example_test_WeightStandard",
//                    new String[]{"minWeight", "maxWeight"}, "bmi=" + bmi
//                            + " and weekNum=" + week, null, null, null, null);
//            if (cursor != null && cursor.moveToNext()) {
//                double min = cursor.getDouble(0);
//                double max = cursor.getDouble(1);
//                cursor.close();
//                return new Standard(min, max,max);
//            }
//            cursor.close();
//        }
        return new Standard(49, 1,1);
//        return null;

    }

    public static class Standard {
        private double min;
        private double max;
        private double middle;

        public Standard() {

        }

        public Standard(double min, double max) {
            super();
            this.min = min;
            this.max = max;
        }

        public Standard(double min, double max, double middle) {
            super();
            this.min = min;
            this.max = max;
            this.middle = middle;
        }

        public double getMin() {
            return min;
        }

        public void setMin(double min) {
            this.min = min;
        }

        public double getMax() {
            return max;
        }

        public void setMax(double max) {
            this.max = max;
        }

        public double getMiddle() {
            return middle;
        }

        public void setMiddle(double middle) {
            this.middle = middle;
        }
    }

}

