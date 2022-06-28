package flab.project.jobfinder.config;

import flab.project.jobfinder.interceptor.AuthInterceptor;
import flab.project.jobfinder.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/job-find/**", "/", "/logout", "/login", "/sign-up", "/error");
        registry.addInterceptor(new LoginInterceptor())
                .order(2)
                .addPathPatterns("/**");
    }
}
