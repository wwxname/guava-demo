package com.example.demo.service;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.netty.handler.codec.http.FullHttpRequest;


public class DispatcherServlet {

    private BiMap<String, Servlet> servletCache;

    DispatcherServlet(BiMap servletCache) {
        this.servletCache = servletCache;
    }

    public Object execute(FullHttpRequest request) {
        Servlet servlet = servletCache.get(request.uri());
        if (servlet == null) {
            return "路径无效";
        }
        return servlet.service(request);
    }

    public void register(Servlet servlet) {
        servletCache.put("/plus" + servlet.getPath(), servlet);
    }


}
