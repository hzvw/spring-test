package com.example.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Idempotent {
    /**
     * 幂等键的SpEL表达式，用于唯一标识一个业务请求
     * 例如: "'orderModify:' + #orderId"
     */
    String key();
    /**
     * 锁的持有时间（秒），超过此时间自动释放，防止死锁
     */
    long timeout() default 30L;
}