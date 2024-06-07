package cn.zhaojf.demo.nacosprovider;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 * @author jianfengzhao
 */
@RestController
public class ProviderController {

    @NacosInjected
    private NamingService namingService;

    @GetMapping(value = "/getService")
    public List<Instance> get(@RequestParam String serviceName) throws NacosException {
        return namingService.getAllInstances(serviceName);
    }

    @RequestMapping("/sayhi/{name}")
    public String sayHi(@PathVariable String name) {
        return "Hi Nacos Discovery " + name;
    }

    @GetMapping(value = "/test")
    public String get() {
        return "test";
    }

}
