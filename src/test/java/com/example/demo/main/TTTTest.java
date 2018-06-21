package com.example.demo.main;

import org.springframework.aop.Advisor;
import org.springframework.aop.AfterAdvice;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.framework.*;
import org.springframework.aop.framework.adapter.AdvisorAdapter;
import org.springframework.aop.framework.adapter.AfterReturningAdviceInterceptor;
import org.springframework.aop.framework.adapter.DefaultAdvisorAdapterRegistry;
import org.springframework.aop.support.DefaultIntroductionAdvisor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;

import org.springframework.aop.framework.Advised;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;

class Dog {
    private String name = "dog";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

public class TTTTest {

    public static void main(String args[]) throws NoSuchMethodException {

        DefaultListableBeanFactory defaultListableBeanFactory = new DefaultListableBeanFactory();
        BeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClassName("com.example.demo.test.proxy.Dog");

        AfterReturningAdvice afterReturningAdvice = new AfterReturningAdvice() {
            @Override
            public void afterReturning(@Nullable Object returnValue, Method method, Object[] args, @Nullable Object target) throws Throwable {
                System.err.println(method.getName());
                System.err.println("wwx");
            }
        };
        AfterReturningAdviceInterceptor advice = new AfterReturningAdviceInterceptor(afterReturningAdvice);

        defaultListableBeanFactory.registerBeanDefinition("dog", beanDefinition);


        DefaultAdvisorChainFactory defaultAdvisorChainFactory = new DefaultAdvisorChainFactory();


        AopProxyFactory aopProxyFactory = new DefaultAopProxyFactory();


        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
        proxyFactoryBean.setBeanClassLoader(Dog.class.getClassLoader());
        proxyFactoryBean.setProxyClassLoader(Dog.class.getClassLoader());
        proxyFactoryBean.setTargetName("dog");

        proxyFactoryBean.setBeanFactory(defaultListableBeanFactory);
        proxyFactoryBean.setAopProxyFactory(aopProxyFactory);
        proxyFactoryBean.setAdvisorChainFactory(defaultAdvisorChainFactory);
        AdvisedSupport advisedSupport = proxyFactoryBean;
        advisedSupport.setTarget(Dog.class);
        advisedSupport.addAdvice(advice);
        //或
        //DefaultIntroductionAdvisor advisor = new DefaultIntroductionAdvisor(advice);
        // advisedSupport.addAdvisor(advisor);

        Dog dog = (Dog) proxyFactoryBean.getObject();
      /* * interface org.springframework.aop.scope.ScopedObject
        interface java.io.Serializable
        interface org.springframework.aop.framework.AopInfrastructureBean
        interface org.springframework.aop.framework.Advised
        interface org.springframework.cglib.proxy.Factory
*/

        System.err.println(dog.getName());
        Class[] cs = dog.getClass().getInterfaces();
        for (Class c : cs) {
            System.err.println(c.getCanonicalName());
        }
    }
}

