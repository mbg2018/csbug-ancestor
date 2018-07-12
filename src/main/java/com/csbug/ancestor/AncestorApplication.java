package com.csbug.ancestor;

import com.csbug.ancestor.config.MyBatisConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Slf4j
@ServletComponentScan
@ComponentScan(basePackages = {"com.csbug.ancestor"})
@Import({MyBatisConfiguration.class})
public class AncestorApplication {

	public static void main(String[] args) {
		SpringApplication.run(AncestorApplication.class, args);
	}
}
