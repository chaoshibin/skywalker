package com.github.skywalker.common.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Chao Shibin
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public enum ResultEnum {
    /**
     * 错误码
     */
    OK("000000", "成功"),
    FAIL("100001", "失败"),
    ILLEGAL_PARAM("999998", "参数验证不通过"),
    DISTRIBUTE_LOCK_FAIL("999999", "争夺分布式锁失败"),
    INTERNAL_SERVER_ERROR("999999", "服务内部错误");;

    private String code;
    private String msg;
}
