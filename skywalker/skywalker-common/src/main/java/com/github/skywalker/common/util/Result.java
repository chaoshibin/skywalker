package com.github.skywalker.common.util;

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
     * 出现异常时返回该静态变量
     */
    public static final Result SYSTEM_ERROR = Result.of(ResultEnum.INTERNAL_SERVER_ERROR);

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
        return Result.of(ResultEnum.OK.getCode(), ResultEnum.OK.getMsg(), null);
    }

    public static <T> Result<T> ok(T value) {
        return Result.of(ResultEnum.OK.getCode(), ResultEnum.OK.getMsg(), value);
    }

    public static <T> Result<T> ok(T value, String msg) {
        return Result.of(ResultEnum.OK.getCode(), msg, value);
    }

    public static <T> Result<T> error(String msg) {
        return Result.of(ResultEnum.INTERNAL_SERVER_ERROR.getCode(), msg, null);
    }

    public static <T> Result<T> error(T value, String msg) {
        return Result.of(ResultEnum.INTERNAL_SERVER_ERROR.getCode(), msg, value);
    }

    public static <T> Result<T> fail(String msg) {
        return Result.of(ResultEnum.FAIL.getCode(), msg, null);
    }

    public static <T> Result<T> fail(T value, String msg) {
        return Result.of(ResultEnum.FAIL.getCode(), msg, value);
    }

    public static <T> Result<T> of(String code, String msg, T value) {
        return new Result<>(code, msg, value);
    }

    public static <T> Result<T> of(ResultEnum resultEnum) {
        return Result.of(resultEnum.getCode(), resultEnum.getMsg(), null);
    }

    public boolean isOk() {
        return Objects.equals(ResultEnum.OK.getCode(), this.code);
    }

    public boolean isFail() {
        return !isOk() && !isError();
    }

    public boolean isError() {
        return Objects.equals(ResultEnum.INTERNAL_SERVER_ERROR.getCode(), this.code);
    }
}
