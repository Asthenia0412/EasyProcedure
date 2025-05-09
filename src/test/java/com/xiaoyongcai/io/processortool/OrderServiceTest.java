package com.xiaoyongcai.io.processortool;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest()
public class OrderServiceTest {

    @Autowired
    private OrderService OrderServiceImpl;

    @Test
    public void testOrderProcessingSuccess() {
        // 准备测试数据
        Order order = new Order("ORDER123");
        OrderProcessorCallback callback = new OrderProcessorCallback();

        // 执行订单处理
        Object result = OrderServiceImpl.processorOrder(callback, order);

        // 验证结果
        assertNotNull(result);
        assertTrue(result instanceof String);
        assertEquals("订单 ORDER123 处理成功，状态: PENDING", result);
    }

    @Test
    public void testOrderProcessingWithInvalidInput() {
        // 准备无效输入
        Order order = null;
        OrderProcessorCallback callback = new OrderProcessorCallback();

        // 验证异常
        assertThrows(IllegalArgumentException.class, () -> {
            OrderServiceImpl.processorOrder(callback, order);
        });
    }
}