package com.ttubeog;

import com.ttubeog.global.config.YamlPropertySourceFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@PropertySource(value = {"classpath:database/application-database.yml"}, factory = YamlPropertySourceFactory.class)
@PropertySource(value = { "classpath:oauth2/application-oauth2.yml" }, factory = YamlPropertySourceFactory.class)
@PropertySource(value = { "classpath:swagger/application-springdoc.yml" }, factory = YamlPropertySourceFactory.class)
public class TtubeogApplication {

	public static void main(String[] args) {
		SpringApplication.run(TtubeogApplication.class, args);
	}
}
