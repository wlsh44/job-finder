package flab.project.jobfinder.config.jobkorea;

import flab.project.jobfinder.service.crawler.generator.JobKoreaQueryParamGenerator;
import flab.project.jobfinder.service.crawler.generator.QueryParamGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobKoreaConfig {

    @Bean
    public QueryParamGenerator jobKoreaQueryParamGenerator() {
        return new JobKoreaQueryParamGenerator();
    }
}
