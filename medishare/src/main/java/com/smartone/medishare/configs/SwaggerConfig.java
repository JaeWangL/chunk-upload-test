package com.smartone.medishare.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    private final ExtConfig extConfig;

    public SwaggerConfig(ExtConfig extConfig) {
        this.extConfig = extConfig;
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("MediShare API")
                .description("MediShare 메인 백엔드 서버")
                .version(extConfig.getProjectVersion()));
    }
}
