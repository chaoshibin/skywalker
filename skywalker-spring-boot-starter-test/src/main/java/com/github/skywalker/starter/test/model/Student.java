package com.github.skywalker.starter.test.model;

import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

/**
 * @author Chao Shibin 2019/5/15 0:39
 */
@ToString
public class Student {
    private String name;

    @Max(message = "年龄最小25", value = 25)
    private int age;
    @NotNull(message = "地址不能为空")
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
