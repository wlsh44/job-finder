package flab.project.jobfinder.config.rocketpunch;

import flab.project.jobfinder.config.JobFinderPropertiesConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix = "rocket-punch")
@PropertySource(value = "classpath:application.properties", encoding = "UTF-8")
public class RocketPunchPropertiesConfig extends JobFinderPropertiesConfig {
}
