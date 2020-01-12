package com.github.skywalker.common.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.skywalker.common.enums.ResultEnum;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * <p>
 * 没有无参构造器，无法使用反射构造对象
 * ok(...)  请求处理成功使用ok方法构造返回结果，错误码 “000000”
 * error(...) 请求处理结果不确定使用error方法构造返回结果，e.g.发生异常、获取分布式锁失败...，错误码 “999999”
 * fail(...) 请求处理失败使用fail方法构造返回结果，错误码使用除“000000”、“999999”之外
 * </p>
 *
 * @author Chao Shibin
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 错误码
     */
    private String code;

    /**
     * 错误信息
     */
    private String msg;

    /**
     * 结果对象
     */
    private T value;

    public static <T> Result<T> ok() {
        return Result.of(ResultEnum.OK.getCode(), ResultEnum.OK.getMsg());
    }

    public static <T> Result<T> ok(T value) {
        return Result.of(ResultEnum.OK.getCode(), ResultEnum.OK.getMsg(), value);
    }

    public static <T> Result<T> ok(String msg, T value) {
        return Result.of(ResultEnum.OK.getCode(), msg, value);
    }

    public static <T> Result<T> fail(String msg) {
        return Result.of(ResultEnum.FAIL.getCode(), msg);
    }

    public static <T> Result<T> fail(String msg, T value) {
        return Result.of(ResultEnum.FAIL.getCode(), msg, value);
    }

    public static <T> Result<T> of(String code, String msg, T value) {
        return new Result<>(code, msg, value);
    }

    public static <T> Result<T> of(String code, String msg) {
        return new Result<>(code, msg, null);
    }


    @JsonIgnore
    public boolean isOk() {
        if (isError()) {
            throw new IllegalStateException("断言错误失败，不对系统异常做业务处理,result=" + this);
        }
        return Objects.equals(ResultEnum.OK.getCode(), this.code);
    }

    @JsonIgnore
    public boolean isFail() {
        return !isOk();
    }

    @JsonIgnore
    private boolean isError() {
        return Objects.equals(ResultEnum.INTERNAL_SERVER_ERROR.getCode(), this.code);
    }
}
