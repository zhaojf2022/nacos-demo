package cn.zhaojf.demo.cloudconsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    private final RestTemplateBuilder builder;

    @Autowired
    public RestTemplateConfig(RestTemplateBuilder builder) {
        this.builder = builder;
    }

    /**
     * 没有实例化RestTemplate时，初始化RestTemplate
     */
    @ConditionalOnMissingBean(RestTemplate.class)
    @LoadBalanced
    @Bean
    public RestTemplate restTemplate(){
        return builder.build();
    }
}

