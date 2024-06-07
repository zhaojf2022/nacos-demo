package cn.zhaojf.demo.nacosconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;

@SpringBootApplication
public class NacosConsumerApplication {

    public static void main(String[] args) {
        /* 在启动目录下生成一个 nacosConsumerApp.id的文件。
         * 停止服务时，在启动目录中执行以下语句：'cat ./nacosConsumerApp.pid | xargs kill'
         */
        SpringApplication application = new SpringApplication(NacosConsumerApplication.class);
        application.addListeners(new ApplicationPidFileWriter("nacosConsumerApp.pid"));
        application.run(args);
    }

}
