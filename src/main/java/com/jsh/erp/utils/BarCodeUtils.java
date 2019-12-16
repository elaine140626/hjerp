package com.jsh.erp.utils;


public class BarCodeUtils {
    public static   int CheckBarCode(String s) {
                int a = 0, b = 0, c = 0, d = 0, e = 0;
        for (int i = 1; i <= 12; i++) {
            int sc = Integer.parseInt(s.substring(i-1,i));
            if (i <= 12 && i % 2 == 0) {
                a += sc;
            } else if (i <= 11 && i % 2 == 1) {
                b += sc;
            }
        }
        c = a * 3;
        d = b + c;
        if (d % 10 == 0) e = d - d;
        else e = d + (10 - d % 10) - d;
        return e;
    }

}
