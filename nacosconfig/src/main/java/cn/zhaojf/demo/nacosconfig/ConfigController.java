package cn.zhaojf.demo.nacosconfig;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 从nacos中提取配置项
 */
@RestController
public class ConfigController {

    @NacosInjected
    private NamingService namingService;

    @GetMapping("/test")
    public String test(){
        return "test";
    }

    @NacosValue(value = "${config.info}", autoRefreshed = true)
    private String configInfo;

    @GetMapping("/getConfig")
    public String getConfigInfo(){
        return "获取config.info属性值：" + configInfo;
    }

    @GetMapping(value = "/getService")
    public List<Instance> get(@RequestParam String serviceName) throws NacosException {
        return namingService.getAllInstances(serviceName);
    }

}
