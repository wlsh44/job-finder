package flab.project.jobfinder.config.rocketpunch;

import flab.project.jobfinder.config.PropertiesConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "rocket-punch")
public class RocketPunchPropertiesConfig extends PropertiesConfig {
}
