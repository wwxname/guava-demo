package com.example.demo.main;



import com.example.demo.core.ClassPathScanningCandidateComponentProvider;
import com.example.demo.core.ScannedGenericBeanDefinition;
import com.example.demo.service.Servlet;
import com.example.demo.service.annotation.Path;
import com.example.demo.service.annotation.ServletBean;
import com.google.common.collect.Table;
import org.springframework.beans.factory.config.BeanDefinition;


import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;

import javax.swing.text.html.parser.Entity;
import java.util.Map;
import java.util.Set;

public class SpringTest {

    public static void main(String args[]) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        boolean useDefaultFilters = false;//是否使用默认的filter，使用默认的filter意味着只扫描那些类上拥有Component、Service、Repository或Controller注解的类。
        String basePackage = "com.example.demo.service.example.servlet";
        ClassPathScanningCandidateComponentProvider beanScanner = new ClassPathScanningCandidateComponentProvider(useDefaultFilters);
        TypeFilter includeFilter1 = new AnnotationTypeFilter(ServletBean.class);
        beanScanner.addIncludeFilter(includeFilter1);
        // beanScanner.ad
        Set<BeanDefinition> beanDefinitions = beanScanner.findCandidateComponents(basePackage);

        for (BeanDefinition beanDefinition : beanDefinitions) {
            String beanName = beanDefinition.getBeanClassName();
            String s = beanDefinition.getBeanClassName();
            //beanDefinition.me
            ScannedGenericBeanDefinition scannedGenericBeanDefinition = (ScannedGenericBeanDefinition)(beanDefinition);
            AnnotationMetadata annotationMetadata = scannedGenericBeanDefinition.getMetadata();
            Map<String, Object> map =  annotationMetadata.getAnnotationAttributes(Path.class.getCanonicalName());
             String p = (String) map.get("value");
            System.err.println(p);

            //annotationMetadata.getAnnotationAttributes()
            beanDefinition.getBeanClassName();
            Class.forName(s).newInstance();

            System.err.println(beanName+s);

        }
    }
}
