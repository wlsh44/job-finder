package flab.project.jobfinder.config;

import flab.project.jobfinder.service.crawler.generator.QueryParamGenerator;
import flab.project.jobfinder.service.crawler.generator.RocketPunchQueryParamGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RocketPunchConfig {

    @Bean
    public QueryParamGenerator rocketPunchQueryParamGenerator(RocketPunchPropertiesConfig config) {
        return new RocketPunchQueryParamGenerator(config);
    }
}
