package cn.zhaojf.demo.cloudconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;

@SpringBootApplication
public class CloudconsumerApplication {

    public static void main(String[] args) {

        /* 在启动目录下生成一个 cloudComsumerApp.id的文件。
         * 停止服务时，在启动目录中执行以下语句：'cat ./cloudComsumerApp.pid | xargs kill'
         */
        SpringApplication application = new SpringApplication(CloudconsumerApplication.class);
        application.addListeners(new ApplicationPidFileWriter("cloudComsumerApp.pid"));
        application.run(args);
    }

}
