package br.com.mvp.maonamassa;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "br.com.mvp.controller" })
public class MaoNaMassaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MaoNaMassaApplication.class, args);
	}

}
