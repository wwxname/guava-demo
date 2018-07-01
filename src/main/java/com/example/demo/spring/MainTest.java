package com.example.demo.spring;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.action.ConfigurationAction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.spi.LocationAwareLogger;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

public class MainTest {
    private static final Log logger = LogFactory.getLog(MainTest.class);
    public static void main(String args[]){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("com.example.demo.spring");
        LoggerContext loggerContext;
        ConfigurationAction configurationAction;
        LocationAwareLogger locationAwareLogger;
       // ExtendedLogger extendedLogger;
        LoggerContext loggerContext1;

        StandardEnvironment standardEnvironment;

        ClassPathBeanDefinitionScanner classPathBeanDefinitionScanner;
        PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver;

        context.getBean("demoService");
        logger.info("laji");
        if(logger.isDebugEnabled()){
            logger.debug("laji");
        }
    }
}
