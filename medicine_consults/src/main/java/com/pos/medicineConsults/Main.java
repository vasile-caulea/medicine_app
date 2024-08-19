package com.pos.medicineConsults;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

// https://stackoverflow.com/questions/51221777/failed-to-configure-a-datasource-url-attribute-is-not-specified-and-no-embedd

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}