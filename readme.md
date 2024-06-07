# nacos 的注册和配置功能的demo

一个使用nacos的demo，包括spring boot和spring cloud两种使用方式。

## 1. 踩坑过程：
- nacos的spring boot starter依赖项，和spring cloud的nacos依赖项，配置文件格式不同，调用接口的方式也不同；
- 不指定配置文件名时，无法读取默认的dataId配置文件；
- `@NacosPropertySource `注解放到主类上时无法获取配置，但用其注解一个配置类（NacosConfig）后成功，原因未知；
- `@NacosPropertySource` 注解引入的配置项只支持 properties 格式，其他格式会报错；
- discover配置中，需要添加 `nacos.discovery.auto-register=true`，否则无法注册到Nacos服务中心；
- nacos开头的依赖项和spring cloud的nacos依赖项，其配置文件格式不同；
- 使用服务名称调用接口，需要引入spring cloud的负载均衡依赖项，因为使用了spring cloud的特性，所以必须使用spring cloud的nacos依赖项；
- 使用spring cloud 的nacos config依赖项时，只能使用 @Value 来获取配置值，使用 @NacosValue 无法获取；
- 使用spring cloud 的nacos config依赖项时，若想保持配置自动更新，除了设置 `refresh-enabled: true`，还要在动态刷新的字段上添加 @RefreshScope 注解；

## 2. 项目使用的版本号：
- jdk: 17
- spring boot: 3.3
- nacos server: 2.3.2 

## 3. nacos服务器中的配置
- 本地启动nacos服务器；
- 在nacos服务器中，添加如下测试用的配置项：
  - dataId: nacosproperties
  - group: DEFAULT_GROUP
  - 配置格式：Properties
  - 配置中的内容： 
  ```
  config.info="hello world！"
  config.useLocalCache=true
  ```

## 4. Nacos的依赖

nacos 依赖项的版本号定义在父工程中：
```xml
<properties>
  <nacos.springboot.version>0.3.0-RC</nacos.springboot.version>
  <spring-cloud-nacos.version>2023.0.1.0</spring-cloud-nacos.version>
</properties>
```
如果项目是Spring Boot 应用，且不需要Spring Cloud的特性，那么选择如下两个nacos的spring boot starter依赖项。

```xml
<dependencies>
    <!-- nacos服务注册依赖项 -->
    <dependency>
        <groupId>com.alibaba.boot</groupId>
        <artifactId>nacos-discovery-spring-boot-starter</artifactId>
        <version>${nacos.springboot.version}</version>
    </dependency>

    <!-- nacos配置依赖项 -->
    <dependency>
        <groupId>com.alibaba.boot</groupId>
        <artifactId>nacos-config-spring-boot-starter</artifactId>
        <version>${nacos.springboot.version}</version>
    </dependency>    
</dependencies>
```

如果需要使用Spring Cloud 相关的的特性（如负载均衡），那么选择如下两个spring cloud 的nacos依赖项。
```xml
<dependencies>
    <!-- nacos服务注册依赖项 -->
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
        <version>${spring-cloud-nacos.version}</version>
    </dependency>

    <!-- nacos配置依赖项 -->
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
        <version>${spring-cloud-nacos.version}</version>
    </dependency>
</dependencies>
```

## 5. Nacos的配置
- 使用 nacos的 spring boot starter依赖项，yaml配置如下（示例）：
```yaml
server:
  port: 10463
spring:
  application:
    name: nacos-config
nacos:
  config:
    server-addr: localhost:8848
    username: nacos
    password: nacos
  discovery:
    server-addr: localhost:8848
    username: nacos
    password: nacos
    # 必须添加这一行，否则无法注册到nacos服务中心
    auto-register: true
```

- 使用spring cloud starter 的 nacos 依赖项，yaml配置如下（示例）：
```yaml
server:
  port: 10464
spring:
  application:
    name: cloudconsumer
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
        username: nacos
        password: nacos
```

## 6. nacosconfig 
- 本模块用于测试nacos的配置功能，从nacos服务器获取配置项，通过接口验证是否正确获取到了配置项的值；
- 使用‘nacos config 和 discovery’的spring boot starter 依赖项；
- 配置一个NacosConfig的bean，使用 `@NacosPropertySource`注解引入`configproperties` 配置项（properties 格式）；
  - 可能是nacos服务器本身的bug，使用其他格式的配置项（如yaml, json等）都会报错；
- 使用 `@NacosValue`注解为字段注入配置项中具体配置的值，并添加“autoRefreshed = true”属性以支持自动更新；
  - 使用@Value注解同样可以实现注入，但没有自动更新功能；
- 更高nacos服务器中这个配置项中具体配置的值，可以看到接口返回的结果发生了变化；
- 本模块也作为一个服务提供者，供消费者通过服务发现调用其接口；

## 7. nacosprovider
- 本模块用于测试nacos的服务器注册，并作为一个服务生产者供消费者发现和调用；
- 本模块使用了‘nacos discovery’的 spring boot starter 依赖项；
- 使用 @NacosInjected 注解，注入一个NamingService的实例；
- 实现getService接口，通过NamingService的实例获取在nacos中注册的服务的详情
- 实现一个“sayhi”接口，用于消费者服务通过服务发现方式进行调用；

## 8. nacosconsumer
- 本模块用于测试nacos的服务发现功能，通过服务名称调用服务提供者的接口；使用‘nacos discovery’的Spring boot starter依赖项；
- 注册了一个 `RestTemplate`的bean，用于实现http调用；
- 实现一个“sayhi”接口，调用provider服务的同名接口；
  - 通过NamingService的实例获取在nacos中注册的服务的实例（nacos-provider)，解析其IP和端口号
  - 重组ip、端口和接口名称，生成访问provider服务器接口的url；
  - 访问这个url，实现对 nacos-provider“sayhi”接口的调用；
- 同样的方式，实现一个“getConfig”接口，调用nacos-config服务的同名接口；

## 9. cloudconsumer
- 本模块同样用于测试nacos的服务发现功能，通过服务名称调用服务提供者的接口；使用 spring cloud的 nacos discovery依赖项；
- 引入spring cloud的负载均衡依赖项 `spring-cloud-starter-loadbalancer`，并在`RestTemplate`的bean上添加`@LoadBalanced`注解；
- 在接口中，直接使用服务名称发送请求，负载均衡会自动将服务名（如nacos-provider) 解析为一个可用服务实例的IP 和端口；
  - 实现sayHi接口和getConfig接口，分别调用nacos-provider和nacos-config服务的同名接口；

## 10. cloudconfig
- 本模块用于测试nacos的配置功能，使用 spring cloud 的 nacos config 依赖项；
- 配置文件中，需使用 spring.config.import 来引入 nacos 的配置文件；
  - 引入的配置文件，支持yaml等其他格式；
- 使用@Value注解来获取配置文件的值；使用 @NacosValue 注解无法获取到配置值；
- 在配置文件中，设置 `refresh-enabled: true`，同时在注入配置参数的类前，添加 `@RefreshScope` 注解来保持参数的自动更新；

## 11. 测试
- 测试使用IDEA自带的http客户端，放在根目录的 /http 文件夹下；
- 每个模块下都有一个http文件，用于测试对应模块的接口；