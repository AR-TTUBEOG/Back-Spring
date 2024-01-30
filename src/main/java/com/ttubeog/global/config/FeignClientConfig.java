package com.ttubeog.global.config;

import com.ttubeog.TtubeogApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackageClasses = TtubeogApplication.class)
public class FeignClientConfig {
}