package com.ttubeog.domain.auth.config;

import com.ttubeog.TtubeogApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackageClasses = TtubeogApplication.class)
public class FeignClientConfig {
}
