/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.ats.web.config;

import javax.servlet.MultipartConfigElement;
import net.acesinc.ats.web.request.RequestContextInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 *
 * @author andrewserff
 */
@Configuration
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class,ThymeleafAutoConfiguration.class})
//@EnableWebMvc
@EnableAsync
@ComponentScan(basePackages = "net.acesinc.ats.web")
@PropertySource({"classpath:${spring.profiles.active}/application.properties"})
@Import(ThymeleafConfig.class)
public class MVCConfig extends WebMvcConfigurationSupport {

    @Value("${upload.maxFileSize}")
    private String maxFileUploadSize;

//    @Override
//    public Executor getAsyncExecutor() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(2);
//        executor.setMaxPoolSize(3);
//        executor.setQueueCapacity(50);
//        executor.setThreadNamePrefix("MyExecutor-");
//        executor.initialize();
//        return executor;
//    }
    @Autowired
    private RequestContextInterceptor interceptor;

    @Bean
    public InternalResourceViewResolver configureInternalResourceViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        return resolver;
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(maxFileUploadSize);
        factory.setMaxRequestSize(maxFileUploadSize);
        return factory.createMultipartConfig();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("/static/");
        registry.addResourceHandler("/assets/**").addResourceLocations("/assets/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor);
    }

    /**
     * Setup a simple strategy: use all the defaults and return XML by default
     * when not sure.
     */
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(true).
                ignoreAcceptHeader(true).
                useJaf(false).
                defaultContentType(MediaType.TEXT_HTML).
                mediaType("html", MediaType.TEXT_HTML).
                mediaType("xml", MediaType.APPLICATION_XML).
                mediaType("json", MediaType.APPLICATION_JSON);
    }

//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        
//        addDefaultHttpMessageConverters(converters);
//    }
}
