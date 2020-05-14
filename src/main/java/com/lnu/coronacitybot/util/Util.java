package com.lnu.coronacitybot.util;

public class Util {

    public static void sleep(long sec){
        try {
            Thread.sleep(sec * 1000);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
