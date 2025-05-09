package com.xiaoyongcai.io.processortool;

import org.springframework.stereotype.Service;

// 实现类: 保持 @Service，修正方法参数类型
@Service  // Spring 会自动注册 Bean，名称为 "orderServiceImpl"（首字母小写）
public class OrderServiceImpl implements OrderService {
    @Override
    @Processor(taskName = "orderProcessing", module = "order")
    public Object processorOrder(ProcessorCallback<Order, String> callback, Order input) {
        try {
            callback.validate(input);      // 校验订单
            callback.preprocess(input);    // 预处理
            String result = callback.execute(input);  // 执行核心逻辑
            callback.postprocess(result);  // 后处理
            return result;
        } catch (Exception e) {
            callback.onError(e);           // 异常处理
            throw new RuntimeException(e);
        }
    }
}