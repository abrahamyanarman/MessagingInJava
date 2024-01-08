package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.jms.annotation.EnableJms;

@EnableJms
@SpringBootApplication
public class PublisherApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(PublisherApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(PublisherApplication.class, args);
    }
}
