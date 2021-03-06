package com.xust;

import com.xust.utils.SendNowData;
import com.xust.utils.TenMessageAPI;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.Timer;

/**
 * Created by lenovo on 2018/5/9.
 */
@RestController
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class}/*,scanBasePackages = {"com.xust.controller"}*/)
@RequestMapping(value = "/indexs")
@ServletComponentScan(basePackages = "com.xust.dao")
@Configuration
public class MySpringBootApplication extends WebMvcConfigurerAdapter implements EmbeddedServletContainerCustomizer {

    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(MySpringBootApplication.class);
    }

    static Logger logger = LoggerFactory.getLogger(MySpringBootApplication.class);
    static Logger log = LoggerFactory.getLogger("Single");

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }

    public static void main(String[] args) {
        SpringApplication.run(MySpringBootApplication.class, args);
        new Timer().schedule(new TenMessageAPI(), 60 * 1000, 60 * 1000);
        new Timer().schedule(new SendNowData(), 30 * 1000, 30 * 1000);
        PropertyConfigurator.configure(System.getProperty("user.dir") + "/config/log4j.properties");
        logger.info("test");
        //SendNowData.config();
    }

    @Override
    public void customize(ConfigurableEmbeddedServletContainer configurableEmbeddedServletContainer) {
        configurableEmbeddedServletContainer.setPort(8081);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/index.html");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
        super.addViewControllers(registry);
    }
}