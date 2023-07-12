package com.gateway.sample.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "r2dbc")
public class R2DBCProperties {
    @NotEmpty
    private String url;

    private String user;
    private String password;
}
