package com.ruoyi.common.utils;

import com.alibaba.fastjson2.JSON;

import java.util.List;

public class JSONUtil {
    public static String parseObject(Object obj){
        return JSON.toJSONString(obj);
    }

    public static <T> List<T>  parseListJson(String json,Class<T> clazz){
        return  JSON.parseArray(json, clazz);
    }

}
