package com.neostudy.creditBank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class CreditBankApplication {

	public static void main(String[] args) {
		SpringApplication.run(CreditBankApplication.class, args);
	}

}
