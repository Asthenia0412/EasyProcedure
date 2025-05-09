package com.xiaoyongcai.io.processortool;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@Aspect
@Component
@EnableAspectJAutoProxy
public class ProcessorAspect {
    private static final Logger logger = LoggerFactory.getLogger(ProcessorAspect.class);

    @Around("@annotation(processor)")
    public Object process(ProceedingJoinPoint joinPoint, Processor processor) throws Throwable {
        String taskName = processor.taskName();
        String module = processor.module();
        logger.info("[公用业务消息]：切面类正在处理任务: {}, 模块: {}", taskName, module);

        Object[] args = joinPoint.getArgs();
        if (args.length == 0 || !(args[0] instanceof ProcessorCallback)) {
            throw new IllegalArgumentException("ProcessorCallback is required");
        }

        ProcessorCallback<Object, Object> callback = (ProcessorCallback<Object, Object>) args[0];
        Object input = args.length > 1 ? args[1] : null;

        try {
            // 执行回调的各个阶段
            callback.validate(input);
            callback.preprocess(input);
            Object result = callback.execute(input);
            callback.postprocess(result);
            return result;
        } catch (Exception e) {
            callback.onError(e);
            logger.warn("[公用业务消息]：任务 {} 执行失败: {}", taskName, e.getMessage());
            throw e;
        }
    }
}