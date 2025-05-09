package com.xiaoyongcai.io.processortool;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Component
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Processor {
    String taskName() default ""; // 任务名称，用于日志
    String module() default "general"; // 模块类型
}
