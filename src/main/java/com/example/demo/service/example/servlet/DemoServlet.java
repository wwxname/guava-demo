package com.example.demo.service.example.servlet;

import com.example.demo.service.AbstractServlet;
import com.example.demo.service.annotation.Method;
import com.example.demo.service.annotation.Path;
import com.example.demo.service.annotation.ServletBean;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;

@ServletBean
@Path(value = "/test")
@Method(value = "GET")
public class DemoServlet extends AbstractServlet {

    public DemoServlet(){

    }
    public DemoServlet(String path, HttpMethod method) {
        super(path, method);
    }

    @Override
    public Object service(FullHttpRequest request) {
        return "wwx";
    }
}
