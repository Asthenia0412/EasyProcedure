package com.xiaoyongcai.io.processortool;

import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public interface ProcessorCallback<T,R> {
    Logger log = LoggerFactory.getLogger(ProcessorCallback.class);
    default void validate(T input) throws IllegalArgumentException{
        // 默认校验逻辑
        log.info("[公用业务默认消息]：您尚未配置Validate");
    }
    default void preprocess(T input){
        //默认预处理
        log.info("[公用业务默认消息]：您尚未配置preprocess");
    }

    R execute(T input) throws  Exception; // 核心业务逻辑

    default void postprocess(R result){
        // 默认后置处理
        log.info("[公用业务默认消息]：您尚未配置postprocess");
    }

    default void onError(Exception e){
        //默认异常处理
        log.warn("[公用业务默认消息]：您尚未配置Error");
    }
}
