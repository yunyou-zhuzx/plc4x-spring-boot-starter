# plc4x-spring-boot-starter

Spring Boot Starter for Apache PLC4X - 工业 PLC 通信框架

## 简介

`plc4x-spring-boot-starter` 是一个 Spring Boot 集成框架，基于 [Apache PLC4X](https://plc4x.apache.org/) 开发，提供简洁易用的方式来读写工业 PLC（可编程逻辑控制器）数据。

支持多种工业协议：

- **S7** - 西门子 PLC 通信协议
- **Modbus** - 通用工业通信协议
- **OPC UA** - 工业自动化通信标准

## 技术栈

- Java 17
- Spring Boot 3.2.3
- Apache PLC4X 0.13.1
- Lombok
- Hutool

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>com.yunyoucloud</groupId>
    <artifactId>plc4x-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2. 配置 PLC 连接

在 `application.yml` 或 `application.properties` 中配置连接信息：

#### S7 连接配置（西门子 PLC）

```yaml
plc:
  s7:
    address: s7://192.168.1.100/0/1
    connection-config:
      username: admin
      password: password
```

#### Modbus TCP 配置

```yaml
plc:
  modbus:
    address: modbus-tcp://192.168.1.100:502
    connection-config:
      unit-id: 1
```

#### OPC UA 配置

```yaml
plc:
  opcua:
    address: opcua:tcp://192.168.1.100:4840
```

### 3. 启用 PLC 功能

在 Spring Boot 启动类或配置类上添加注解：

```java
@EnablePlc
@SpringBootApplication
public class MainApplication {
    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }
}
```

### 4. 使用示例

#### 读取数据

```java
@Autowired
private PlcManager plcManager;

// 读取单个值
boolean flag = plcManager.getPlc(PlcProtocol.S7).readBoolean("%M0.0");
int counter = plcManager.getPlc(PlcProtocol.S7).readInteger("%DB1.DBD0");
float temperature = plcManager.getPlc(PlcProtocol.S7).readFloat("%DB1.DBD4");

// 批量读取
List<PlcParseData> dataList = new ArrayList<>();
// 添加读取项...
plcManager.getPlc(PlcProtocol.S7).read(dataList, new PlcResolve());
```

#### 写入数据

```java
@Autowired
private PlcManager plcManager;

// 写入单个值
plcManager.getPlc(PlcProtocol.S7).writeBoolean("%M0.0", true);
plcManager.getPlc(PlcProtocol.S7).writeInteger("%DB1.DBD0", 100);

// 批量写入
List<PlcParseData> dataList = new ArrayList<>();
// 添加写入项...
plcManager.getPlc(PlcProtocol.S7).write(dataList);
```

## 核心组件

| 组件 | 说明 |
|------|------|
| `PlcManager` | PLC 连接管理器，负责创建和管理 PLC 连接 |
| `PLC` | PLC 通信接口，提供读写操作 |
| `ConnectionConfig` | 连接配置接口，定义连接参数 |
| `PlcResolve` | 数据解析器，用于批量读取时转换数据 |

## 项目结构

```
plc4x-spring-boot-starter/
├── src/main/java/com/yunyoucloud/plc4x/
│   ├── MainApplication.java          # Spring Boot 启动类
│   ├── client/                       # PLC 客户端
│   │   ├── PLC.java                  # PLC 读写接口
│   │   └── PlcManager.java           # 连接管理器
│   ├── config/                       # 配置类
│   │   ├── PlcConfig.java           # PLC 配置属性
│   │   ├── S7PlcConfig.java         # S7 特定配置
│   │   ├── ModbusPlcConfig.java     # Modbus 特定配置
│   │   ├── OpcuaPlcConfig.java      # OPC UA 特定配置
│   │   └── connection/              # 连接配置
│   ├── exception/                    # 异常定义
│   │   ├── PlcReadExpection.java
│   │   ├── PlcWriteExpection.java
│   │   └── PlcCommExpection.java
│   ├── serializer/                   # 序列化器
│   └── utils/                        # 工具类
└── plc4x-spring-boot-starter-api/   # API 模块
```

## 许可证

本项目基于 [Apache License 2.0](LICENSE) 许可证开源。

## 致谢

- [Apache PLC4X](https://plc4x.apache.org/) - 工业协议通信库
- [Spring Boot](https://spring.io/projects/spring-boot) - Spring Boot 框架
