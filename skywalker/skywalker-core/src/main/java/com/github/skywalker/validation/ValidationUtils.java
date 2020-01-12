package com.github.skywalker.validation;

import com.github.skywalker.common.utils.Result;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

/**
 * @author Chao Shibin 2019/4/17
 */
@Slf4j
@UtilityClass
public class ValidationUtils {

    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    public static <T> Result<String> validate(List<T> params) {
        for (T obj : params) {
            Set<ConstraintViolation<T>> validateSet = VALIDATOR.validate(obj);
            if (CollectionUtils.isNotEmpty(validateSet)) {
                StringBuilder builder = new StringBuilder();
                for (ConstraintViolation<T> v : validateSet) {
                    log.debug("前置参数校验-校验到异常参数 {}={}", v.getPropertyPath().toString(), v.getMessage());
                    if (builder.length() > 0) {
                        builder.append("|");
                    }
                    builder.append(v.getMessage());
                }
                return Result.fail(builder.toString());
            }
        }
        return Result.ok(StringUtils.EMPTY);
    }

}
