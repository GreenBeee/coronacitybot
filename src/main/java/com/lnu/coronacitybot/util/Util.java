package com.lnu.coronacitybot.util;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public class Util {

    public final static Set<String> ALL_COUNTRIES = Arrays.stream(Locale.getISOCountries())
            .map(x -> new Locale("en", x).getDisplayCountry().toLowerCase())
            .collect(Collectors.toSet());

    public static void sleep(long sec){
        try {
            Thread.sleep(sec * 1000);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
