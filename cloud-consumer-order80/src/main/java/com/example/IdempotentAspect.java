package com.example;

import com.example.anno.Idempotent;
import com.example.exp.BusinessException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class IdempotentAspect {
    @Autowired
    private RedissonClient redissonClient;
    /**
     * 环绕通知：在方法执行前尝试获取锁，执行后释放锁
     */
    @Around("@annotation(idempotent)")
    public Object around(ProceedingJoinPoint joinPoint, Idempotent idempotent) throws Throwable {
        // 1. 解析SpEL表达式，生成分布式锁的Key（核心：确保唯一性）
        String lockKey = parseKey(joinPoint, idempotent.key());
        RLock lock = redissonClient.getLock(lockKey);
        boolean isLocked = false;
        try {
            // 2. 尝试获取锁，如果获取失败（false），说明正在处理相同业务的请求
            isLocked = lock.tryLock(0, idempotent.timeout(), TimeUnit.SECONDS); // waitTime=0，表示不等待
            if (!isLocked) {
                throw new BusinessException("请求正在处理中，请勿重复操作");
            }
            // 3. 获取锁成功，执行原方法
            return joinPoint.proceed();
        } finally {
            // 4. 释放锁
            if (isLocked && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
    private String parseKey(ProceedingJoinPoint joinPoint, String keyExpression) {
        // 1. 获取方法签名
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        // 2. 获取方法参数名
        String[] parameterNames = methodSignature.getParameterNames();
        // 3. 获取方法参数值
        Object[] arguments = joinPoint.getArgs();

        // 4. 创建解析器和上下文
        ExpressionParser parser = new SpelExpressionParser();
        EvaluationContext context = new StandardEvaluationContext();

        // 5. 将方法参数名和值设置到上下文中
        if (parameterNames != null) {
            for (int i = 0; i < parameterNames.length; i++) {
                context.setVariable(parameterNames[i], arguments[i]);
            }
        }

        // 6. 解析SpEL表达式
        try {
            Expression expression = parser.parseExpression(keyExpression);
            return expression.getValue(context, String.class);
        } catch (Exception e) {
            // 可选：处理解析异常，例如记录日志或抛出自定义异常
            throw new IllegalArgumentException("Failed to parse SpEL expression: " + keyExpression, e);
        }
    }
}