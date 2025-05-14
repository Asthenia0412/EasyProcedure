# EasyProcedure 项目说明文档

## 项目概述

EasyProcedure 是一个基于 Spring Boot 的通用业务处理框架，旨在通过 AOP（面向切面编程）和回调机制简化业务流程的开发。它提供了一个灵活的处理流程，包括校验、预处理、核心逻辑执行、后处理和异常处理等阶段，适用于订单处理、任务调度等场景。

## 功能特点

- **模块化设计**：通过 `Processor` 注解和 `ProcessorCallback` 接口实现业务逻辑的解耦。
- **AOP 切面**：使用 Spring AOP 实现统一的流程控制和日志记录。
- **灵活的回调机制**：支持自定义校验、预处理、核心逻辑、后处理和异常处理。
- **日志支持**：集成 SLF4J 提供详细的日志记录，便于调试和监控。
- **单元测试**：包含 JUnit 测试用例，确保代码质量。

## 项目结构

```
ProcessorTool
├── src
│   ├── main
│   │   └── java
│   │       └── com.xiaoyongcai.io.processortool
│   │           ├── Application.java              # Spring Boot 应用入口
│   │           ├── ProcessorAspect.java          # AOP 切面，处理 Processor 注解
│   │           ├── ProcessorCallback.java        # 回调接口，定义处理流程
│   │           ├── Order.java                    # 订单实体类
│   │           ├── OrderProcessorCallback.java   # 订单处理回调实现
│   │           ├── OrderService.java             # 订单服务接口
│   │           ├── OrderServiceImpl.java         # 订单服务实现
│   │           └── Processor.java                # 自定义注解
│   └── test
│       └── java
│           └── com.xiaoyongcai.io.processortool
│               └── OrderServiceTest.java         # 单元测试
├── target                                        # 编译输出目录
└── .idea                                         # IDE 配置文件
```

## 快速开始

### 环境要求

- Java 17 或以上
- Maven 3.6+
- Spring Boot 3.x

### 安装与运行

1. **克隆项目**

   ```bash
   git clone <repository-url>
   cd ProcessorTool
   ```

2. **构建项目**

   ```bash
   mvn clean install
   ```

3. **运行应用**

   ```bash
   mvn spring-boot:run
   ```

### 使用示例

以下是一个订单处理的示例：

```java
Order order = new Order("ORDER123");
OrderProcessorCallback callback = new OrderProcessorCallback();
OrderService orderService = applicationContext.getBean(OrderService.class);
Object result = orderService.processorOrder(callback, order);
System.out.println(result); // 输出: 订单 ORDER123 处理成功，状态: PENDING
```

## 核心组件说明

### 1. `Processor` 注解

- 用于标记需要切面处理的业务方法。
- 参数：
  - `taskName`：任务名称，用于日志记录。
  - `module`：模块名称，默认值为 `general`。

### 2. `ProcessorCallback` 接口

定义了业务处理的五个阶段：

- `validate`：输入校验。
- `preprocess`：预处理。
- `execute`：核心业务逻辑（必须实现）。
- `postprocess`：后处理。
- `onError`：异常处理。

#### 固定内容

以下内容由框架固定，用户无需修改：

- **切面逻辑**（`ProcessorAspect`）：框架通过 AOP 自动拦截带有 `Processor` 注解的方法，依次调用 `validate`、`preprocess`、`execute`、`postprocess`，并在异常时调用 `onError`。
- **日志记录**：框架在每个阶段自动记录日志，日志前缀为 `[公用业务消息]` 或 `[公用业务默认消息]`。
- **流程控制**：框架确保回调方法的执行顺序和异常处理机制。

#### 开放内容

用户可以通过实现 `ProcessorCallback` 接口自定义以下内容：

- **`validate`**：定义输入数据的校验逻辑，例如检查字段是否为空或格式是否正确。
- **`preprocess`**：定义数据预处理逻辑，例如设置默认值或转换数据格式。
- **`execute`**：实现核心业务逻辑，处理输入并返回结果（必须实现）。
- **`postprocess`**：定义处理结果的后处理逻辑，例如记录日志或触发通知。
- **`onError`**：定义异常处理逻辑，例如记录错误或发送警报。

### 3. `ProcessorAspect` 切面

- 拦截带有 `Processor` 注解的方法，执行回调的各个阶段。
- 记录日志并处理异常。

### 4. `OrderService` 和 `OrderServiceImpl`

- 定义和实现订单处理的服务逻辑。
- 使用 `Processor` 注解触发切面处理。

### 5. `OrderProcessorCallback`

- 订单处理的回调实现，包含具体的校验、预处理等逻辑。

## 如何定义自定义 Callback 类

要创建自己的 `ProcessorCallback` 实现类，请按照以下步骤操作：

1. **创建实现类**
   新建一个类，实现 `Processor.fasterxml

Callback<T, R>`接口，其中`T` 是输入类型，`R` 是输出类型。例如：

```java
package com.xiaoyongcai.io.processortool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomProcessorCallback implements ProcessorCallback<String, Integer> {
    private static final Logger logger = LoggerFactory.getLogger(CustomProcessorCallback.class);

    @Override
    public void validate(String input) throws IllegalArgumentException {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("输入字符串不能为空");
        }
        logger.info("校验通过: {}", input);
    }

    @Override
    public void preprocess(String input) {
        logger.info("预处理输入: {}", input);
        // 例如：转换输入格式
    }

    @Override
    public Integer execute(String input) throws Exception {
        logger.info("执行核心逻辑: {}", input);
        // 核心业务逻辑，例如计算字符串长度
        return input.length();
    }

    @Override
    public void postprocess(Integer result) {
        logger.info("后处理结果: {}", result);
        // 例如：记录结果
    }

    @Override
    public void onError(Exception e) {
        logger.error("处理异常: {}", e.getMessage());
        // 例如：发送错误通知
    }
}
```

1. **使用自定义 Callback**
   在服务方法中调用自定义的 Callback：

   ```java
   CustomProcessorCallback callback = new CustomProcessorCallback();
   OrderService orderService = applicationContext.getBean(OrderService.class);
   Object result = orderService.processorOrder(callback, "test input");
   System.out.println(result); // 输出: 4（字符串长度）
   ```

2. **注意事项**

   - **日志**：建议使用 `Logger` 记录关键信息，便于调试。
   - **异常处理**：在 `validate` 中抛出 `IllegalArgumentException`，在 `execute` 中抛出 `Exception`，并在 `onError` 中处理。
   - **类型安全**：确保输入和输出类型与 `ProcessorCallback<T, R>` 定义一致。

## 测试

项目包含单元测试，位于 `src/test/java/com/xiaoyongcai/io/processortool/OrderServiceTest.java`。运行测试：

```bash
mvn test
```

测试用例包括：

- 正常订单处理流程。
- 无效输入的异常处理。

## 日志

- 使用 SLF4J 记录日志，日志级别包括 `INFO`（正常流程）和 `WARN`（异常情况）。

- 日志格式示例：

  ```
  [公用业务消息]：切面类正在处理任务: orderProcessing, 模块: order
  [公用业务默认消息]：您尚未配置Validate
  ```

## 常见问题

1. **Q：如何扩展新的业务逻辑？**
   A：实现 `ProcessorCallback` 接口，定义新的回调逻辑，并在服务方法上使用 `Processor` 注解。
2. **Q：为什么收到 `ProcessorCallback is required` 异常？**
   A：确保 `processorOrder` 方法的第一个参数是 `ProcessorCallback` 类型的对象。

## 贡献

欢迎提交 Issue 或 Pull Request！请确保代码符合项目风格，并附带单元测试。

## 许可证

本项目采用 MIT 许可证，详情见 `LICENSE` 文件（待添加）。
