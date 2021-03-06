package com.test.crudUser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

// exclude inserido para que a configuração manual seja utilizada
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class CrudUserApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrudUserApplication.class, args);
	}

}
