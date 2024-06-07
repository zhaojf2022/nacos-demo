package cn.zhaojf.demo.nacosconfig;

import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.springframework.context.annotation.Configuration;

/**
 * 从nacos中提取配置项
 * 只有 properties格式的配置项可以成功，其他格式均会出现如下错误：
 *     org.yaml.snakeyaml.constructor.SafeConstructor: method 'void <init>()' not found
 * @author jianfengzhao
 */
@Configuration
@NacosPropertySource(dataId = "configproperties", autoRefreshed = true)
public class NacosConfig {
    // 这个类负责从 Nacos 配置中心获取配置
}