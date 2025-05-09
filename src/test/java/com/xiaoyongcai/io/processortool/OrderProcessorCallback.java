package com.xiaoyongcai.io.processortool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class OrderProcessorCallback implements ProcessorCallback<Order,String> {
    private static final Logger logger = LoggerFactory.getLogger(OrderProcessorCallback.class);
    @Override
    public void validate(Order input) throws IllegalArgumentException {
        if(input==null||input.getOrderId()==null){
            throw new IllegalArgumentException("订单不能为空且必须包含订单ID");
        }
        logger.info("订单校验通过: {}", input.getOrderId());
    }

    @Override
    public void preprocess(Order input) {
        logger.info("预处理订单: {}", input.getOrderId());
        input.setStatus("PENDING");
    }

    @Override
    public String execute(Order input) throws Exception {
        logger.info("执行订单核心逻辑: {}", input.getOrderId());
        return "订单 " + input.getOrderId() + " 处理成功，状态: " + input.getStatus();
    }

    @Override
    public void postprocess(String result) {
        logger.info("后处理结果: {}", result);
    }

    @Override
    public void onError(Exception e) {
        logger.error("订单处理异常: {}", e.getMessage());
    }
}
