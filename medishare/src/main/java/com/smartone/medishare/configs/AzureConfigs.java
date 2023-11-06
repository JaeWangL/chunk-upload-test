package com.smartone.medishare.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
@ConfigurationProperties(prefix = "azure.blob")
@Validated
@Getter
@Setter
public class AzureConfigs {
    private String accountName;
    private String accountKey;
    private String sasDuration;
}
