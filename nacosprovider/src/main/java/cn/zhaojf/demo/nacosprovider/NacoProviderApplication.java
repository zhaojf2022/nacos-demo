package cn.zhaojf.demo.nacosprovider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;

@SpringBootApplication
//@EnableDiscoveryClient
public class NacoProviderApplication {

    public static void main(String[] args) {

        /* 在启动目录下生成一个 nacosProviderApp.id的文件。
         * 停止服务时，在启动目录中执行以下语句：'cat ./nacosProviderApp.pid | xargs kill'
         */
        SpringApplication application = new SpringApplication(NacoProviderApplication.class);
        application.addListeners(new ApplicationPidFileWriter("nacosProviderApp.pid"));
        application.run(args);

    }

}
