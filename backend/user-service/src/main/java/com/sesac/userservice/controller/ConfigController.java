package com.sesac.userservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
@RequiredArgsConstructor
@RequestMapping("/config")
@Tag(name = "Config Test", description = "check config api")
public class ConfigController {

    @Value("${user.service.name}")
    private String serviceName;

    @Value("${user.service.version}")
    private String serviceVersion;

    @Value("${user.service.description}")
    private String serviceDescription;

    @GetMapping
    @Operation(summary = "get setting value", description = "check setting value in config server")
    public Map<String, String > getConfig() {
        Map<String, String> config = new HashMap<>();
        config.put("serviceName", serviceName);
        config.put("serviceVersion", serviceVersion);
        config.put("serviceDescription", serviceDescription);
        return config;
    }
}
