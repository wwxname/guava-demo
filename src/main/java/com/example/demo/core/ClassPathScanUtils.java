package com.example.demo.core;

import com.example.demo.service.annotation.ServletBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;

import java.lang.annotation.Annotation;
import java.util.Set;

public abstract class ClassPathScanUtils {

    public static Set<BeanDefinition> findCandidateComponents(String basePackage,Class<? extends Annotation> ... annotations) {
        boolean useDefaultFilters = false;//是否使用默认的filter，使用默认的filter意味着只扫描那些类上拥有Component、Service、Repository或Controller注解的类。
        ClassPathScanningCandidateComponentProvider beanScanner = new ClassPathScanningCandidateComponentProvider(useDefaultFilters);
        for (Class annotation : annotations) {
            beanScanner.addIncludeFilter(new AnnotationTypeFilter(annotation));
        }
        return beanScanner.findCandidateComponents(basePackage);
    }

}
