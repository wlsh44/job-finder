package flab.project.jobfinder.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties(prefix = "jobkorea")
public class JobKoreaPropertiesConfig extends PropertiesConfig {
}
