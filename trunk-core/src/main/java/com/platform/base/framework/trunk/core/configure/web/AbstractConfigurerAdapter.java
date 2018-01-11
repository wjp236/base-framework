package com.platform.base.framework.trunk.core.configure.web;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.servlet.view.XmlViewResolver;

/**
 * 提供基础配置加载功能 web使用
 */
public abstract class AbstractConfigurerAdapter extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**/*.js")
                .addResourceLocations("/").setCachePeriod(0);
        registry.addResourceHandler("/**/*.css")
                .addResourceLocations("/").setCachePeriod(0);
        registry.addResourceHandler("/**/*.png")
                .addResourceLocations("/").setCachePeriod(0);
        registry.addResourceHandler("/**/*.jpg")
                .addResourceLocations("/").setCachePeriod(0);
        registry.addResourceHandler("/**/*.gif")
                .addResourceLocations("/").setCachePeriod(0);
        registry.addResourceHandler("/**/*.ico")
                .addResourceLocations("/").setCachePeriod(0);
        registry.addResourceHandler("/**/*.html")
                .addResourceLocations("/").setCachePeriod(0);
        registry.addResourceHandler("/**/*.rpc")
                .addResourceLocations("/").setCachePeriod(0);
    }

    @Bean
    public UrlBasedViewResolver viewResolver() {
        UrlBasedViewResolver resolver = new UrlBasedViewResolver();
        resolver.setViewClass(JstlView.class);
        resolver.setPrefix("/");
        resolver.setOrder(2);
        return resolver;
    }

    @Bean
    public XmlViewResolver xmlViewResolver() {
        XmlViewResolver xmlViewResolver = new XmlViewResolver();
        ApplicationContext context = new FileSystemXmlApplicationContext("classpath:/views.xml");
        xmlViewResolver.setLocation(context.getResource("classpath:/views.xml"));
        xmlViewResolver.setOrder(1);
        return xmlViewResolver;
    }


}
