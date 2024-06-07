package cn.zhaojf.demo.cloudconsumer;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class ConsumerController {

    private final RestTemplate restTemplate;


    // 只有配置了discovery并引入spring cloud的nacos discovery依赖项，才能使用discoveryClient
    private final DiscoveryClient discoveryClient;

    @Autowired
    public ConsumerController(RestTemplate restTemplate, DiscoveryClient discoveryClient) {
        this.restTemplate = restTemplate;
        this.discoveryClient = discoveryClient;
    }

    private static final String PROVIDER_SERVICE = "nacos-provider";
    private static final String CONFIG_SERVICE = "nacos-config";

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    /**
     * 调用注册到nacos中的服务的接口
     * @return String
     */
    @RequestMapping("/getConfig")
    public String get() {
        // 直接使用注册到nacos中的服务名称调用其接口
        return restTemplate.getForObject("http://"+ CONFIG_SERVICE + "/getConfig", String.class);
    }


    /**
     * 调用注册到nacos中的服务的接口
     * @return String
     * @throws NacosException NacosException
     */
    @GetMapping("/sayhi/{name}")
    public String sayHi(@PathVariable String name) throws NacosException {

        String url =  String.format("http://%s/sayhi/%s", PROVIDER_SERVICE, name);
        return restTemplate.getForObject(url, String.class);
    }

    @GetMapping(value = "/getService")
    public List<ServiceInstance> get(@RequestParam String serviceName)  {
        return discoveryClient.getInstances(serviceName);
    }

}