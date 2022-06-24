package flab.project.jobfinder.config.jobkorea;

import flab.project.jobfinder.config.PropertiesConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix = "jobkorea")
@PropertySource(value = "classpath:parsing.properties", encoding = "UTF-8")
public class JobKoreaPropertiesConfig extends PropertiesConfig {
}
