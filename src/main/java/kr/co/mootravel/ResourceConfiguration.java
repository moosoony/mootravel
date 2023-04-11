package kr.co.mootravel;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ResourceConfiguration implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/img/**")
                .addResourceLocations("file:///C:/moo/");

                // 접근 파일 캐싱 시간
//                .setCacheControl(CacheControl.maxAge(1, TimeUnit.MINUTES));
    }
}