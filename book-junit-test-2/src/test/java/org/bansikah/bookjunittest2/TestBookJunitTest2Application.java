package org.bansikah.bookjunittest2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestBookJunitTest2Application {

    public static void main(String[] args) {
        SpringApplication.from(BookJunitTest2Application::main).with(TestBookJunitTest2Application.class).run(args);
    }

}
