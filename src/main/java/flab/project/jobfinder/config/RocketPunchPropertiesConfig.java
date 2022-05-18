package flab.project.jobfinder.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "rocket-punch")
public class RocketPunchPropertiesConfig {
    private String url;
    private String searchUrl;
    private String delimiter;
    private String selector;
    private String totalPageSelector;
}
