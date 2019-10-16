package com.github.skywalker.common.util;

import com.github.skywalker.common.enums.ResultEnum;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author Chao Shibin 2019/4/16
 */
@UtilityClass
public class AspectUtil {

    /**
     * 获取注解方法
     */
    public static Method getMethod(JoinPoint jp) {
        MethodSignature methodSignature = (MethodSignature) jp.getSignature();
        return methodSignature.getMethod();
    }

    /**
     * 获取方法注解
     *
     * @param jp              链接点
     * @param annotationClass 注解类
     * @param <T>             注解
     * @return 注解
     */
    public static <T extends Annotation> T getAnnotationOnMethod(JoinPoint jp, Class<T> annotationClass) {
        MethodSignature methodSignature = (MethodSignature) jp.getSignature();
        return methodSignature.getMethod().getAnnotation(annotationClass);
    }

    private static MethodSignature getMethodSignature(JoinPoint jp) {
        return (MethodSignature) jp.getSignature();
    }

    /**
     * 获取方法名
     */
    public static String getMethodName(JoinPoint jp) {
        MethodSignature signature = (MethodSignature) jp.getSignature();
        return signature.getDeclaringTypeName() + "." + signature.getName();
    }

    /**
     * 获取返回类型
     */
    public static Class<?> getReturnType(JoinPoint jp) {
        MethodSignature methodSignature = (MethodSignature) jp.getSignature();
        return methodSignature.getReturnType();
    }


    /**
     * 构建返回结果
     *
     * @param returnType    返回类型
     * @param codeFieldName 错误码域
     * @param msgFieldName  错误信息域
     * @param <T>           返回结果
     * @return 构造的异常结果
     */
    @SneakyThrows
    public static <T> T buildErrorResult(Class<T> returnType, String codeFieldName, String msgFieldName) {
        return buildResult(returnType, codeFieldName, msgFieldName, ResultEnum.INTERNAL_SERVER_ERROR.getCode(), ResultEnum.INTERNAL_SERVER_ERROR.getMsg());
    }

    /**
     * 构建返回结果
     *
     * @param returnType    返回类型
     * @param codeFieldName 错误码域
     * @param msgFieldName  错误信息域
     * @param code          错误码
     * @param msg           错误信息
     * @param <T>           返回结果
     * @return 构造的异常结果
     */
    @SneakyThrows
    public static <T> T buildResult(Class<T> returnType, String codeFieldName, String msgFieldName, String code, String msg) {
        Constructor<T> constructor = returnType.getDeclaredConstructor();
        constructor.setAccessible(true);
        T obj = constructor.newInstance();
        Field codeField = getField(returnType, codeFieldName);
        codeField.setAccessible(true);
        codeField.set(obj, code);

        Field msgField = getField(returnType, msgFieldName);
        msgField.setAccessible(true);
        msgField.set(obj, msg);
        return obj;
    }

    private static Field getField(Class clazz, String fieldName) throws NoSuchFieldException {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            return clazz.getSuperclass().getDeclaredField(fieldName);
        }
    }

    /**
     * 获取接口名
     */
    public static String getInterfaceName(JoinPoint jp) {
        return jp.getSignature().getDeclaringTypeName();
    }
}
