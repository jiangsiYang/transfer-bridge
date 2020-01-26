package com.yjs.bridge;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.SpringApplication;


@ComponentScan(value = {"com.yjs"})
@SpringBootApplication
public class BridgeBootApplication {
    public static void main(String args[]) {
        SpringApplication springBootApplication = new SpringApplication(BridgeBootApplication.class);
        springBootApplication.addListeners(new ApplicationPidFileWriter());
        springBootApplication.run(args);
    }
}
