package com.tc3.sp_mvc.tweet.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.tc3.sp_mvc.tweet.date.USLocalDateFormatter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.util.UrlPathHelper;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDate;

@Configuration
@EnableSwagger2
@EnableConfigurationProperties({PictureUploadProperties.class})
public class WebConfiguration extends WebMvcConfigurerAdapter {

    //解析日期
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatterForFieldType(LocalDate.class, new USLocalDateFormatter());
    }

    // spring 1.*Tomcat 自定义配置全局异常界面
    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {
        EmbeddedServletContainerCustomizer  embeddedServletContainerCustomizer =
        container ->{
            ErrorPage uploadErrorPage =  new ErrorPage(MultipartException.class, "/uploadError");
            container.addErrorPages(uploadErrorPage);
        };
        return embeddedServletContainerCustomizer;
    }


    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        UrlPathHelper urlPathHelper = new UrlPathHelper();
        urlPathHelper.setRemoveSemicolonContent(false);//不移除分号内容
        configurer.setUrlPathHelper(urlPathHelper);
        configurer.setUseRegisteredSuffixPatternMatch(true);
    }

    @Bean
    public Twitter twitter(final @Value("${spring.social.twitter.appId}") String consumerKey,
                           final @Value("${spring.social.twitter.appSecret}") String consumerSecret,
                           final @Value("${spring.social.twitter.accessToken}") String accessToken,
                           final @Value("${spring.social.twitter.accessTokenSecret}") String accessTokenSecret) {

        Twitter twitter = new TwitterTemplate(consumerKey,consumerSecret,accessToken,accessTokenSecret);
        return twitter;
    }

    //日期
    @Bean
    @Primary
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder)
    {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return objectMapper;
    }

//    http://localhost:8080/swagger-ui.html
//    在默认情况下，Springfox 会扫描整个类路径并展示应用中所有声明的请求映射
    @Bean
    public Docket userApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(path -> path.startsWith("/api/"))//
                .build();
    }
}