package com.ruoyi.common.utils;

import cn.hutool.core.util.ReflectUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectUtils {

    /**
     * 获取属性的值
     * @param obj
     * @param fieldName
     * @return
     */
    public static Object getField(Object obj,String fieldName){
       return ReflectUtils.getField(obj,fieldName,Object.class);
    }

    /**
     * 获取属性的值
     * @param obj
     * @param fieldName
     * @return
     */
    public static <T> T getField(Object obj,String fieldName,Class<T> clazz){
        Object result=null;
        Method getMethod = ReflectUtil.getMethod(obj.getClass(), "get"+ StringUtils_.toUpperCase(fieldName));
        try {
            result=getMethod.invoke(obj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }finally {
            return (T) result;
        }
    }

    public static void setField(Object obj,String fieldName,Object val){
        Field field = ReflectUtil.getField(obj.getClass(), fieldName);

        Method setMethod = ReflectUtil.getMethod(obj.getClass(), "set"+ StringUtils_.toUpperCase(fieldName),new Class[]{field.getType()});
        try {
            if(val!=null){
                setMethod.invoke(obj,val);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
