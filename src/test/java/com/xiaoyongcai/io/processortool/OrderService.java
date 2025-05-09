package com.xiaoyongcai.io.processortool;

import org.springframework.stereotype.Service;


// 接口: 移除 @Service，修正泛型类型
public interface OrderService {
    Object processorOrder(ProcessorCallback<Order, String> callback, Order input);  // 明确泛型类型
}