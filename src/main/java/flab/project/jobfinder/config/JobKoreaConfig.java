package flab.project.jobfinder.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "jobkorea")
public class JobKoreaConfig {
    private String url;
    private String delimiter;
}
