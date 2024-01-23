package com.ttubeog.domain.auth.config;

import com.ttubeog.TtubeogApplication;
import feign.Retryer;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackageClasses = TtubeogApplication.class)
public class FeignClientConfig {
    @Bean
    public Retryer retryer() {
        return new Retryer.Default(1000, 1500, 1);
    }
}
