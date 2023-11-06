package com.smartone.medishare.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExtConfig {

    @Value("${ext.appVersion}")
    private String projectVersion;

    public String getProjectVersion() {
        return projectVersion;
    }
}