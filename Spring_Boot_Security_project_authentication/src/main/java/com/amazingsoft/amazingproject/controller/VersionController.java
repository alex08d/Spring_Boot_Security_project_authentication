package com.amazingsoft.amazingproject.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.util.Properties;

@RestController
@RequestMapping("/version")
public class VersionController {

    @GetMapping
    public String getCurrentVersion() throws Exception {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("properties-from-pom.properties");
        Properties properties = new Properties();
        properties.load(inputStream);

        return properties.getProperty("api.version");
    }
}
