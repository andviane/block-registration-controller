package com.smartvalor.hl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import com.smartvalor.hl.controllers.ContractController;
import com.smartvalor.hl.controllers.WebSecurityConfig;
import com.smartvalor.hl.logic.UniqueUUIDGenerator;

@SpringBootApplication
@ComponentScan(basePackageClasses={ContractController.class, UniqueUUIDGenerator.class})
@Import(WebSecurityConfig.class)
public class Application {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

}