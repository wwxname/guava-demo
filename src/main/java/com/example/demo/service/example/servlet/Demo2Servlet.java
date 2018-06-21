package com.example.demo.service.example.servlet;

import com.example.demo.service.AbstractServlet;
import com.example.demo.service.annotation.Method;
import com.example.demo.service.annotation.Path;
import com.example.demo.service.annotation.ServletBean;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;

@ServletBean
@Path(value = "/test2")
@Method(value = "GET")
public class Demo2Servlet extends AbstractServlet {
    public Demo2Servlet(){

    }
    public Demo2Servlet(String path, HttpMethod method) {
        super(path, method);
    }

    @Override
    public Object service(FullHttpRequest request) {
        return new DemoServlet("/23",null);
    }
}
