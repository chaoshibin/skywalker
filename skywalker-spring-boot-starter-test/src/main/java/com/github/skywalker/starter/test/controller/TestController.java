package com.github.skywalker.starter.test.controller;

import com.github.skywalker.annotation.EasyLog;
import com.github.skywalker.annotation.EasyValidation;
import com.github.skywalker.common.util.Result;
import com.github.skywalker.starter.test.model.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @author Chao Shibin 2019/5/15 0:30
 */
@Controller
@RequestMapping("")
@Slf4j
public class TestController {

    @RequestMapping("/test")
    @ResponseBody
    public String test(Student student) {
        log.info("/test");
        return "";
    }

    @RequestMapping("/testLog")
    @ResponseBody
    @EasyLog(title = "测试EasyLog")
    @EasyValidation
    public Result<Student> testLog(Student student) {
        student.setAddress("在哪里");
        return Result.ok(student);
    }

    @RequestMapping("/testComponent")
    @ResponseBody
    @EasyLog(title = "测试日志")
    @EasyValidation
    public Result<Student> testComponent(Student s) {
        s.setAddress("在哪里");
        return Result.ok(s);
    }

    @RequestMapping("/testLock")
    @ResponseBody
    public Result<Student> testLock(Student s) {
        s.setAddress("在哪里");
        return Result.ok(s);
    }
}
