package com.github.skywalker.validation;

import com.github.skywalker.common.utils.AspectUtils;
import com.github.skywalker.common.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;

import java.util.Arrays;

/**
 * @author Chao Shibin 2019/4/16
 */
@Slf4j
@Aspect
@Order(Integer.MIN_VALUE + 1)
public class EasyValidationAspect {

    @Pointcut("@annotation(com.github.skywalker.validation.EasyValidation)")
    public void pointCut() {
        //pointcut
    }

    @Around("pointCut()")
    public Object methodProcess(ProceedingJoinPoint joinPoint) throws Throwable {
        Result<String> result = ValidationUtils.validate(Arrays.asList(joinPoint.getArgs()));
        //验证失败
        if (result.isFail()) {
            //返回类型
            Class<?> returnType = AspectUtils.getReturnType(joinPoint);
            EasyValidation easyValidationAnnotation = AspectUtils.getMethod(joinPoint).getAnnotation(EasyValidation.class);
            //错误码域
            String codeField = easyValidationAnnotation.codeField();
            //错误信息域
            String msgField = easyValidationAnnotation.msgField();
            //验证失败错误码
            String code = easyValidationAnnotation.code();
            return AspectUtils.buildResult(returnType, codeField, msgField, code, result.getMsg());
        }
        return joinPoint.proceed();
    }
}
