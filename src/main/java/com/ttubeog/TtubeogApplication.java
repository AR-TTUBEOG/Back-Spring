package com.ttubeog;

import com.ttubeog.global.config.YamlPropertySourceFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TtubeogApplication {

	public static void main(String[] args) {
		SpringApplication.run(TtubeogApplication.class, args);
	}
}
