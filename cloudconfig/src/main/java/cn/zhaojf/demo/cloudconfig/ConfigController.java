package cn.zhaojf.demo.cloudconfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 从nacos中提取配置项
 */
@RefreshScope
@RestController
public class ConfigController {

    @GetMapping("/test")
    public String test(){
        return "test";
    }

    @Value(value = "${config.info}")
    private String configInfo;

    @GetMapping("/getConfig")
    public String getConfigInfo(){
        return "获取config.info属性值：" + configInfo;
    }

}
