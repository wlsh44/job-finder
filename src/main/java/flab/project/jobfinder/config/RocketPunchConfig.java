package flab.project.jobfinder.config;

import flab.project.jobfinder.service.crawler.generator.QueryParamGenerator;
import flab.project.jobfinder.service.crawler.generator.RocketPunchQueryParamGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class RocketPunchConfig {

    @Bean
    public QueryParamGenerator rocketPunchQueryParamGenerator() {
        return new RocketPunchQueryParamGenerator();
    }

    @Bean
    public WebClient webClient(RocketPunchPropertiesConfig config) {
        String url = config.getSearchUrl();
        return WebClient.builder()
                .baseUrl(url)
                .build();
    }
}
