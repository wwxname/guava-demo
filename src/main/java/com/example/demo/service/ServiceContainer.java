package com.example.demo.service;

import com.example.demo.core.ClassPathScanUtils;
import com.example.demo.core.PlusMetadataReader;
import com.example.demo.core.ScannedGenericBeanDefinition;
import com.example.demo.service.annotation.Path;
import com.example.demo.service.annotation.ServletBean;
import com.example.demo.service.annotation.ServletScan;
import com.google.common.util.concurrent.Service;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.AnnotationMetadataReadingVisitor;
import org.springframework.core.type.classreading.MetadataReader;

import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;

public class ServiceContainer {
    public static final Log log = LogFactory.getLog(ServiceContainer.class);
    public static ServerService serverService;

    public ServiceContainer(Integer port) {
        serverService = new ServerService(8082);
        serverService.addListener(new Service.Listener() {
            @Override
            public void starting() {
                super.starting();
            }
        }, new Executor() {
            @Override
            public void execute(Runnable command) {
                if (serverService.getBasePackage().equals("")) {
                    return;
                }
                Set<BeanDefinition> bs = ClassPathScanUtils.findCandidateComponents(serverService.getBasePackage(), ServletBean.class);
                DispatcherServlet ds = serverService.getDispatcherServlet();
                for (BeanDefinition b : bs) {
                    Servlet servlet = null;
                    String path = "";
                    ScannedGenericBeanDefinition scannedGenericBeanDefinition = (ScannedGenericBeanDefinition) (b);
                    AnnotationMetadata annotationMetadata = scannedGenericBeanDefinition.getMetadata();
                    Map<String, Object> map = annotationMetadata.getAnnotationAttributes(Path.class.getCanonicalName());
                    path = (String) map.get("value");
                    try {
                        servlet = (Servlet) Class.forName(b.getBeanClassName()).newInstance();
                        if (servlet != null || path.length() > 0) {
                            servlet.setPath(path);
                            ds.register(servlet);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public static ServerService getServerService() {
        return serverService;
    }

    public void run(Class clazz) {
        AnnotationMetadataReadingVisitor annotationMetadata = null;
        String basePackage = "";
        try {
            String simpleName = clazz.getSimpleName();
            URL url = clazz.getResource("");
            URL spec = new URL(url.getProtocol() + ":" + url.getPath() + simpleName + ".class");
            MetadataReader metadataReader = new PlusMetadataReader(new FileUrlResource(spec), clazz.getClassLoader());
            annotationMetadata = (AnnotationMetadataReadingVisitor) new ScannedGenericBeanDefinition(metadataReader).getMetadata();
        } catch (Exception e) {
            log.warn("读取启动文件失败");
            e.printStackTrace();
        }
        try {
            basePackage = (String) annotationMetadata.getAnnotationAttributes(ServletScan.class.getCanonicalName()).get("value");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!"".equals(basePackage)) {
            serverService.setBasePackage(basePackage);
        }
        serverService.startAsync();
    }

}
