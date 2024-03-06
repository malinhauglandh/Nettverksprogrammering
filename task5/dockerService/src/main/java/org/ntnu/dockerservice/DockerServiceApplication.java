package org.ntnu.dockerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class DockerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DockerServiceApplication.class, args);
    }

}
