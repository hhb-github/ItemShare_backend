package com.itemshare.config;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自定义Web服务器配置，确保服务器在正确的端口上运行
 */
@Configuration
public class WebServerConfiguration {
    
    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> webServerFactoryCustomizer() {
        return factory -> {
            System.out.println("=== WebServerFactoryCustomizer被调用 ===");
            System.out.println("正在配置服务器端口为8081");
            factory.setPort(8081);
        };
    }
}