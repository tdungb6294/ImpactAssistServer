package com.bdserver.impactassist;

import org.springframework.boot.SpringApplication;

public class TestImpactAssistApplication {

    public static void main(String[] args) {
        SpringApplication.from(ImpactAssistApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
