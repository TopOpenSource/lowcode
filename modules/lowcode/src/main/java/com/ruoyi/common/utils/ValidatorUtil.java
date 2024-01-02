package com.ruoyi.common.utils;

import cn.hutool.core.util.ReUtil;

public class ValidatorUtil {
    public static boolean phone(String value) {
        return ReUtil.isMatch("^1[3456789]\\d{9}$", value);
    }

    public static boolean phoneOrMobile(String value) {
        return ReUtil.isMatch("^((0\\d{2,3}-\\d{7,8})|(1[3584]\\d{9}))$", value);
    }

    public static boolean email(String value) {
        return ReUtil.isMatch("^([a-zA-Z]|[0-9])(\\w|\\-)+@[a-zA-Z0-9]+\\.([a-zA-Z]{2,4})$", value);
    }

    public static boolean website(String value) {
        return ReUtil.isMatch("^(https?|ftp|file):\\/\\/[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]$", value);
    }

    public static boolean regex(String regex,String value) {
        return ReUtil.isMatch(regex, value);
    }

    public static boolean integer(String value){
        return ReUtil.isMatch("^-?[1-9]\\d*|[0]{1,1}$", value);
    }
    public static void main(String[] args) {
      System.out.println(ValidatorUtil.phone("15623232d12"));
    }
}
