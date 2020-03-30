package com.datalife.datalife.util;

import android.util.Log;

/**
 * Created by LG on 2018/2/6.
 */

public class MeasureNorm {

    //man=男，woman=女
    //脂肪，<30  >=30
    public static Double[] FATman = new Double[]{17.0, 22.0, 19.0, 24.0};
    public static Double[] FATwoman = new Double[]{23.1, 28.0, 23.1, 30.0};

    //    public static Double[] WETman = new Double[]{57.0, 53.6, 55.6, 52.3};
//    public static Double[] WETwoman = new Double[]{52.9, 49.5, 51.5, 48.1};
    public static Double[] WETman = new Double[]{53.6, 57.0, 52.3, 55.6};
    public static Double[] WETwoman = new Double[]{49.5, 52.9, 48.1, 51.5};

    //全部年龄适应
    public static int[] MUSCLEman = new int[]{31, 34};
    public static int[] MUSCLEwoman = new int[]{25, 27};

    public static Double[] BMI = new Double[]{18.5, 24.0};
    // <39  40-60  >60
//    public static Double[] BONEman = new Double[]{1.93, 2.88, 2.24, 3.36, 2.48, 3.72};
//    public static Double[] BONEwoman = new Double[]{1.3, 2.0, 1.68, 2.52, 1.93, 2.88};
    //体重
    public static Double[] BONEman = new Double[]{2.2, 2.8, 2.6, 3.2, 2.9, 3.5};
    public static Double[] BONEwoman = new Double[]{1.5, 2.1, 1.9, 2.5, 2.2, 2.8};

    //1-2  3-5  6-8 9-11  12-14  15-17  18-29  30-49  50-69 70-  10个
    public static int[] KCALwoman = new int[]{560, 840, 688, 1032, 800, 1200, 944, 1416, 1072, 1608, 1040, 1560, 968, 1450, 936, 1404, 888, 1332, 808, 1212};
    public static int[] KCALman = new int[]{560, 840, 720, 1080, 872, 1308, 1032, 1548, 1184, 1776, 1288, 1932, 1280, 1860, 1200, 1800, 1080, 1620, 976, 1460};

    //全部年龄适应 内脏脂肪 Viscera
    public static int[] VISCERA = new int[]{1, 9};

    //身体年龄
    public static int[] BODYAGE = new int[]{0, 18, 18, 44, 45, 59, 60, 74, 75, 89, 90};
    //    public static int[] BODYAGE = new int[]{0, 18, 18, 35, 35, 65, 65, 120};
    public static Double[] tempValue = new Double[2];
    public static StandardUtils.Standard standard = new StandardUtils.Standard();
    ;

    //获取脂肪的标准
    public static StandardUtils.Standard getFat(int age, int sex) {
        if (sex == 0) {
            if (age < 30) {
                standard.setMin(Double.valueOf(FATwoman[0]));
                standard.setMax(Double.valueOf(FATwoman[1]));
                return standard;
            } else {
                standard.setMin(Double.valueOf(FATwoman[2]));
                standard.setMax(Double.valueOf(FATwoman[3]));
                return standard;
            }
        } else {
            if (age < 30) {
                standard.setMin(Double.valueOf(FATman[0]));
                standard.setMax(Double.valueOf(FATman[1]));
                return standard;
            } else {
                standard.setMin(Double.valueOf(FATman[2]));
                standard.setMax(Double.valueOf(FATman[3]));
                return standard;
            }
        }
    }

    //获取水分的标准
    public static StandardUtils.Standard getWet(int age, int sex) {
        if (sex == 0) {
            if (age < 30) {
                standard.setMin(Double.valueOf(WETwoman[0]));
                standard.setMax(Double.valueOf(WETwoman[1]));
                return standard;
            } else {
                standard.setMin(Double.valueOf(WETwoman[2]));
                standard.setMax(Double.valueOf(WETwoman[3]));
                return standard;
            }
        } else {
            if (age < 30) {
                standard.setMin(Double.valueOf(WETman[0]));
                standard.setMax(Double.valueOf(WETman[1]));
                return standard;
            } else {
                standard.setMin(Double.valueOf(WETman[2]));
                standard.setMax(Double.valueOf(WETman[3]));
                return standard;
            }
        }
    }

    //获取肌肉的标准
    public static StandardUtils.Standard getMuscle(int sex) {
        if (sex == 0) {
            standard.setMin(Double.valueOf(MUSCLEwoman[0]));
            standard.setMax(Double.valueOf(MUSCLEwoman[1]));
            return standard;
        } else {
            standard.setMin(Double.valueOf(MUSCLEman[0]));
            standard.setMax(Double.valueOf(MUSCLEman[1]));
            return standard;
        }
    }

    //获取BMI的标准
    public static StandardUtils.Standard getBMI() {
        standard.setMin(Double.valueOf(BMI[0]));
        standard.setMax(Double.valueOf(BMI[1]));
        return standard;
    }

    //获取骨骼的标准
    public static StandardUtils.Standard getBone(int weight, int sex) {
        if (sex == 0) {
            if (weight < 45) {
                standard.setMin(Double.valueOf(BONEwoman[0]));
                standard.setMax(Double.valueOf(BONEwoman[1]));
                return standard;
            } else if (weight >= 45 && weight < 60) {
                standard.setMin(Double.valueOf(BONEwoman[2]));
                standard.setMax(Double.valueOf(BONEwoman[3]));
                return standard;
            } else {
                standard.setMin(Double.valueOf(BONEwoman[4]));
                standard.setMax(Double.valueOf(BONEwoman[5]));
                return standard;
            }
        } else {
            if (weight < 60) {
                standard.setMin(Double.valueOf(BONEman[0]));
                standard.setMax(Double.valueOf(BONEman[1]));
                return standard;
            } else if (weight >= 60 && weight < 75) {
                standard.setMin(Double.valueOf(BONEman[2]));
                standard.setMax(Double.valueOf(BONEman[3]));
                return standard;
            } else {
                standard.setMin(Double.valueOf(BONEman[4]));
                standard.setMax(Double.valueOf(BONEman[5]));
                return standard;
            }
        }
    }

    //获取卡路里的标准
    public static StandardUtils.Standard getKcal(int age, int sex) {
        if (sex == 0) {
            if (age >= 1 && age <= 2) {
                standard.setMin(Double.valueOf(KCALwoman[0]));
                standard.setMax(Double.valueOf(KCALwoman[1]));
                return standard;
            } else if (age >= 3 && age <= 5) {
                standard.setMin(Double.valueOf(KCALwoman[2]));
                standard.setMax(Double.valueOf(KCALwoman[3]));
                return standard;
            } else if (age >= 6 && age <= 8) {
                standard.setMin(Double.valueOf(KCALwoman[4]));
                standard.setMax(Double.valueOf(KCALwoman[5]));
                return standard;
            } else if (age >= 9 && age <= 11) {
                standard.setMin(Double.valueOf(KCALwoman[6]));
                standard.setMax(Double.valueOf(KCALwoman[7]));
                return standard;
            } else if (age >= 12 && age <= 14) {
                standard.setMin(Double.valueOf(KCALwoman[8]));
                standard.setMax(Double.valueOf(KCALwoman[9]));
                return standard;
            } else if (age >= 15 && age <= 17) {
                standard.setMin(Double.valueOf(KCALwoman[10]));
                standard.setMax(Double.valueOf(KCALwoman[11]));
                return standard;
            } else if (age >= 18 && age <= 29) {
                standard.setMin(Double.valueOf(KCALwoman[12]));
                standard.setMax(Double.valueOf(KCALwoman[13]));
                return standard;
            } else if (age >= 30 && age <= 49) {
                standard.setMin(Double.valueOf(KCALwoman[14]));
                standard.setMax(Double.valueOf(KCALwoman[15]));
                return standard;
            } else if (age >= 50 && age <= 69) {
                standard.setMin(Double.valueOf(KCALwoman[16]));
                standard.setMax(Double.valueOf(KCALwoman[17]));
                return standard;
            } else if (age >= 70) {
                standard.setMin(Double.valueOf(KCALwoman[18]));
                standard.setMax(Double.valueOf(KCALwoman[19]));
                return standard;
            }
        } else {
            if (age >= 1 && age <= 2) {
                standard.setMin(Double.valueOf(KCALman[0]));
                standard.setMax(Double.valueOf(KCALman[1]));
                return standard;
            } else if (age >= 3 && age <= 5) {
                standard.setMin(Double.valueOf(KCALman[2]));
                standard.setMax(Double.valueOf(KCALman[3]));
                return standard;
            } else if (age >= 6 && age <= 8) {
                standard.setMin(Double.valueOf(KCALman[4]));
                standard.setMax(Double.valueOf(KCALman[5]));
                return standard;
            } else if (age >= 9 && age <= 11) {
                standard.setMin(Double.valueOf(KCALman[6]));
                standard.setMax(Double.valueOf(KCALman[7]));
                return standard;
            } else if (age >= 12 && age <= 14) {
                standard.setMin(Double.valueOf(KCALman[8]));
                standard.setMax(Double.valueOf(KCALman[9]));
                return standard;
            } else if (age >= 15 && age <= 17) {
                standard.setMin(Double.valueOf(KCALman[10]));
                standard.setMax(Double.valueOf(KCALman[11]));
                return standard;
            } else if (age >= 18 && age <= 29) {
                standard.setMin(Double.valueOf(KCALman[12]));
                standard.setMax(Double.valueOf(KCALman[13]));
                return standard;
            } else if (age >= 30 && age <= 49) {
                standard.setMin(Double.valueOf(KCALman[14]));
                standard.setMax(Double.valueOf(KCALman[15]));
                return standard;
            } else if (age >= 50 && age <= 69) {
                standard.setMin(Double.valueOf(KCALman[16]));
                standard.setMax(Double.valueOf(KCALman[17]));
                return standard;
            } else if (age >= 70) {
                standard.setMin(Double.valueOf(KCALman[18]));
                standard.setMax(Double.valueOf(KCALman[19]));
                return standard;
            }
        }
        standard.setMin(Double.valueOf(KCALwoman[10]));
        standard.setMax(Double.valueOf(KCALwoman[11]));
        return standard;
    }

    //获取内脏脂肪等级的标准
    public static StandardUtils.Standard getViscera() {
        standard.setMin(Double.valueOf(VISCERA[0]));
        standard.setMax(Double.valueOf(VISCERA[1]));
        return standard;
    }

    //获取身体年龄的标准
    public static StandardUtils.Standard getbodyAge(int age) {
        if (age < 18) {
            standard.setMin(Double.valueOf(BODYAGE[0]));
            standard.setMax(Double.valueOf(BODYAGE[1]));
            return standard;
        } else if (age >= 18 && age < 44) {
            standard.setMin(Double.valueOf(BODYAGE[2]));
            standard.setMax(Double.valueOf(BODYAGE[3]));
            return standard;
        } else if (age >= 44 && age < 60) {
            standard.setMin(Double.valueOf(BODYAGE[4]));
            standard.setMax(Double.valueOf(BODYAGE[5]));
            return standard;
        } else {
            standard.setMin(Double.valueOf(BODYAGE[6]));
            standard.setMax(Double.valueOf(BODYAGE[7]));
            return standard;
        }
    }


    //判断脂肪的类型
    public static int NormFat(int age, int sex, double value) {
        Log.e("age=", +age + "   sex=" + sex + "   value=" + value);
        if (sex == 0)//女
        {
            if (age < 30) {
                if (value < FATwoman[0]) {
                    return 0;
                }
                if (value >= FATwoman[0] && value < FATwoman[1]) {
                    return 1;
                }
                if (value >= FATwoman[1]) {
                    return 2;
                }
            } else {
                if (value < FATwoman[2]) {
                    return 0;
                }
                if (value >= FATwoman[2] && value < FATwoman[3]) {
                    return 1;
                }
                if (value >= FATwoman[3]) {
                    return 2;
                }
            }
        }
        if (sex == 1) {
            if (age < 30) {
                if (value < FATman[0]) {
                    return 0;
                }
                if (value >= FATman[0] && value < FATman[1]) {
                    return 1;
                }
                if (value >= FATman[1]) {
                    return 2;
                }
            } else {
                if (value < FATman[2]) {
                    return 0;
                }
                if (value >= FATman[2] && value < FATman[3]) {
                    return 1;
                }
                if (value >= FATman[3]) {
                    return 2;
                }
            }
        }
        return 0;
    }

    //判断水分的类型
    public static int NormWet(int age, int sex, double value) {
        if (sex == 0)//女
        {
            if (age < 30) {
                if (value < WETwoman[0]) {
                    return 0;
                }
                if (value >= WETwoman[0] && value < WETwoman[1]) {
                    return 1;
                }
                if (value > WETwoman[1]) {
                    return 2;
                }
            } else {
                if (value < WETwoman[2]) {
                    return 0;
                }
                if (value >= WETwoman[2] && value < WETwoman[3]) {
                    return 1;
                }
                if (value >= WETwoman[3]) {
                    return 2;
                }
            }
        }
        if (sex == 1) {
            if (age < 30) {
                if (value < WETman[0]) {
                    return 0;
                }
                if (value >= WETman[0] && value < WETman[1]) {
                    return 1;
                }
                if (value > WETman[1]) {
                    return 2;
                }
            } else {
                if (value < WETman[2]) {
                    return 0;
                }
                if (value >= WETman[2] && value < WETman[3]) {
                    return 1;
                }
                if (value >= WETman[3]) {
                    return 2;
                }
            }
        }
        return 0;
    }

    //判断肌肉的类型
    public static int NormMUSCL(int sex, double value) {
        if (sex == 0)//女
        {
            if (value < MUSCLEwoman[0]) {
                return 0;
            }
            if (value >= MUSCLEwoman[0] && value < MUSCLEwoman[1]) {
                return 1;
            }
            if (value >= MUSCLEwoman[1]) {
                return 2;
            }
        }
        if (sex == 1) {
            if (value < MUSCLEman[0]) {
                return 0;
            }
            if (value >= MUSCLEman[0] && value < MUSCLEman[1]) {
                return 1;
            }
            if (value >= MUSCLEman[1]) {
                return 2;
            }
        }
        return 0;
    }

    //判断BMI的类型
    public static int NormBMI(double value) {
        if (value < BMI[0]) {
            return 0;
        }
        if (value >= BMI[0] && value < BMI[1]) {
            return 1;
        }
        if (value >= BMI[1]) {
            return 2;
        }
        return 0;
    }

    //判断骨骼的类型
    public static int NormBONE(int weight, int sex, double value) {
        if (sex == 0)//女
        {
            if (weight < 45) {
                if (value < BONEwoman[0]) {
                    return 0;
                }
                if (value >= BONEwoman[0] && value < BONEwoman[1]) {
                    return 1;
                }
                if (value >= BONEwoman[1]) {
                    return 2;
                }
            } else if (weight >= 45 && weight < 60) {
                if (value < BONEwoman[2]) {
                    return 0;
                }
                if (value >= BONEwoman[2] && value < BONEwoman[3]) {
                    return 1;
                }
                if (value >= BONEwoman[3]) {
                    return 2;
                }
            } else {
                if (value < BONEwoman[4]) {
                    return 0;
                }
                if (value >= BONEwoman[4] && value < BONEwoman[5]) {
                    return 1;
                }
                if (value >= BONEwoman[5]) {
                    return 2;
                }
            }
        }
        if (sex == 1) {
            if (weight < 60) {
                if (value < BONEman[0]) {
                    return 0;
                }
                if (value >= BONEman[0] && value < BONEman[1]) {
                    return 1;
                }
                if (value >= BONEman[1]) {
                    return 2;
                }
            } else if (weight >= 60 && weight < 75) {
                if (value < BONEman[2]) {
                    return 0;
                }
                if (value >= BONEman[2] && value < BONEman[3]) {
                    return 1;
                }
                if (value >= BONEman[3]) {
                    return 2;
                }
            } else {
                if (value < BONEman[4]) {
                    return 0;
                }
                if (value >= BONEman[4] && value < BONEman[5]) {
                    return 1;
                }
                if (value >= BONEman[5]) {
                    return 2;
                }
            }
        }
        return 0;
    }

    //判断卡路里的类型
    public static int NormKCAL(int age, int sex, double value) {
        if (sex == 0)//女
        {
            if (age >= 1 && age <= 2) {
                if (value < KCALwoman[0]) {
                    return 0;
                }
                if (value >= KCALwoman[0] && value < KCALwoman[1]) {
                    return 1;
                }
                if (value >= KCALwoman[1]) {
                    return 2;
                }
            } else if (age >= 3 && age <= 5) {
                if (value < KCALwoman[2]) {
                    return 0;
                }
                if (value >= KCALwoman[2] && value < KCALwoman[3]) {
                    return 1;
                }
                if (value >= KCALwoman[3]) {
                    return 2;
                }
            } else if (age >= 6 && age <= 8) {
                if (value < KCALwoman[4]) {
                    return 0;
                }
                if (value >= KCALwoman[4] && value < KCALwoman[5]) {
                    return 1;
                }
                if (value >= KCALwoman[5]) {
                    return 2;
                }

            } else if (age >= 9 && age <= 11) {
                if (value < KCALwoman[6]) {
                    return 0;
                }
                if (value >= KCALwoman[6] && value < KCALwoman[7]) {
                    return 1;
                }
                if (value >= KCALwoman[7]) {
                    return 2;
                }
            } else if (age >= 12 && age <= 14) {
                if (value < KCALwoman[8]) {
                    return 0;
                }
                if (value >= KCALwoman[8] && value < KCALwoman[9]) {
                    return 1;
                }
                if (value >= KCALwoman[9]) {
                    return 2;
                }

            } else if (age >= 15 && age <= 17) {
                if (value < KCALwoman[10]) {
                    return 0;
                }
                if (value >= KCALwoman[10] && value < KCALwoman[11]) {
                    return 1;
                }
                if (value >= KCALwoman[11]) {
                    return 2;
                }
            } else if (age >= 18 && age <= 29) {
                if (value < KCALwoman[12]) {
                    return 0;
                }
                if (value >= KCALwoman[12] && value < KCALwoman[13]) {
                    return 1;
                }
                if (value >= KCALwoman[13]) {
                    return 2;
                }
            } else if (age >= 30 && age <= 49) {
                if (value < KCALwoman[14]) {
                    return 0;
                }
                if (value >= KCALwoman[14] && value < KCALwoman[15]) {
                    return 1;
                }
                if (value >= KCALwoman[15]) {
                    return 2;
                }
            } else if (age >= 50 && age <= 69) {
                if (value < KCALwoman[16]) {
                    return 0;
                }
                if (value >= KCALwoman[16] && value < KCALwoman[17]) {
                    return 1;
                }
                if (value >= KCALwoman[17]) {
                    return 2;
                }
            } else if (age >= 70) {
                if (value < KCALwoman[18]) {
                    return 0;
                }
                if (value >= KCALwoman[18] && value < KCALwoman[19]) {
                    return 1;
                }
                if (value >= KCALwoman[19]) {
                    return 2;
                }
            }
        }
        if (sex == 1) {
            if (age >= 1 && age <= 2) {
                if (value < KCALman[0]) {
                    return 0;
                }
                if (value >= KCALman[0] && value < KCALman[1]) {
                    return 1;
                }
                if (value >= KCALman[1]) {
                    return 2;
                }
            } else if (age >= 3 && age <= 5) {
                if (value < KCALman[2]) {
                    return 0;
                }
                if (value >= KCALman[2] && value < KCALman[3]) {
                    return 1;
                }
                if (value >= KCALman[3]) {
                    return 2;
                }
            } else if (age >= 6 && age <= 8) {
                if (value < KCALman[4]) {
                    return 0;
                }
                if (value >= KCALman[4] && value < KCALman[5]) {
                    return 1;
                }
                if (value >= KCALman[5]) {
                    return 2;
                }

            } else if (age >= 9 && age <= 11) {
                if (value < KCALman[6]) {
                    return 0;
                }
                if (value >= KCALman[6] && value < KCALman[7]) {
                    return 1;
                }
                if (value >= KCALman[7]) {
                    return 2;
                }
            } else if (age >= 12 && age <= 14) {
                if (value < KCALman[8]) {
                    return 0;
                }
                if (value >= KCALman[8] && value < KCALman[9]) {
                    return 1;
                }
                if (value >= KCALman[9]) {
                    return 2;
                }

            } else if (age >= 15 && age <= 17) {
                if (value < KCALman[10]) {
                    return 0;
                }
                if (value >= KCALman[10] && value < KCALman[11]) {
                    return 1;
                }
                if (value >= KCALman[11]) {
                    return 2;
                }
            } else if (age >= 18 && age <= 29) {
                if (value < KCALman[12]) {
                    return 0;
                }
                if (value >= KCALman[12] && value < KCALman[13]) {
                    return 1;
                }
                if (value >= KCALman[13]) {
                    return 2;
                }
            } else if (age >= 30 && age <= 49) {
                if (value < KCALman[14]) {
                    return 0;
                }
                if (value >= KCALman[14] && value < KCALman[15]) {
                    return 1;
                }
                if (value >= KCALman[15]) {
                    return 2;
                }
            } else if (age >= 50 && age <= 69) {
                if (value < KCALman[16]) {
                    return 0;
                }
                if (value >= KCALman[16] && value < KCALman[17]) {
                    return 1;
                }
                if (value >= KCALman[17]) {
                    return 2;
                }
            } else if (age >= 70) {
                if (value < KCALman[18]) {
                    return 0;
                }
                if (value >= KCALman[18] && value < KCALman[19]) {
                    return 1;
                }
                if (value >= KCALman[19]) {
                    return 2;
                }
            }
        }
        return 0;
    }

    //判断内脏脂肪等级的类型
    public static int NormVISCERA(double value) {
        if (value < VISCERA[0]) {
            return 0;
        }
        if (value >= VISCERA[0] && value < VISCERA[1]) {
            return 1;
        }
        if (value >= VISCERA[1]) {
            return 2;
        }
        return 0;
    }

    //     public static int[] BODYAGE = new int[]{0, 18, 18, 44, 45, 59, 60, 74, 75, 89, 90};
    //判断身体年龄的类型
    public static int NormBODYAGE(double value) {
        if (value <= BODYAGE[2]) {
            return 0;
        } else if (value >= BODYAGE[2] && value <= BODYAGE[3]) {
            return 1;
        } else if (value >= BODYAGE[4] && value <= BODYAGE[5]) {
            return 2;
        } else if (value >= BODYAGE[6]) {
            return 3;
        }
        return 0;
    }

}

