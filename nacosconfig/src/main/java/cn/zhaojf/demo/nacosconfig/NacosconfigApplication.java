package cn.zhaojf.demo.nacosconfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;

@SpringBootApplication
public class NacosconfigApplication {

    public static void main(String[] args) {

        /* 在启动目录下生成一个 nacosConfigApp.id的文件。
         * 停止服务时，在启动目录中执行以下语句：'cat ./nacosConfigApp.pid | xargs kill'
         */
        SpringApplication application = new SpringApplication(NacosconfigApplication.class);
        application.addListeners(new ApplicationPidFileWriter("nacosConfigApp.pid"));
        application.run(args);
    }

}
