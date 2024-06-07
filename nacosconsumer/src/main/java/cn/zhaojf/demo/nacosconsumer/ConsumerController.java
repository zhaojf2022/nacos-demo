package cn.zhaojf.demo.nacosconsumer;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class ConsumerController {

    private final RestTemplate restTemplate;
    @NacosInjected
    private NamingService namingService;

    @Autowired
    public ConsumerController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
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
     * @throws NacosException NacosException
     */
    @GetMapping("/sayhi/{name}")
    public String selectService(@PathVariable String name) throws NacosException {

        // 根据服务名称拼接访问地址后，调用接口
        String url =  getUrl(PROVIDER_SERVICE);
        return restTemplate.getForObject(url + "/sayhi/" + name, String.class);
    }

    /**
     * 调用注册到nacos中的服务的接口
     * @return String
     */
    @RequestMapping("/getConfig")
    public String get() throws NacosException {

        // 根据服务名称拼接访问地址后，调用接口
        String url = getUrl(CONFIG_SERVICE);
        return restTemplate.getForObject( url + "/getConfig", String.class);
    }

    @GetMapping(value = "/getService")
    public List<Instance> get(@RequestParam String serviceName) throws NacosException {
        return namingService.getAllInstances(serviceName);
    }

    private String getUrl(String serviceName) throws NacosException {
        Instance instance = namingService.selectOneHealthyInstance(serviceName, "DEFAULT_GROUP");
        String ip = instance.getIp();
        int port = instance.getPort();
        return String.format("http://%s:%d", ip, port);
    }

}