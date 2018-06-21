package com.example.demo.service;

import io.netty.handler.codec.http.HttpMethod;

public abstract class AbstractServlet implements Servlet {

    private String path;

    private HttpMethod method;

    public AbstractServlet(){

    }


   public AbstractServlet(String path, HttpMethod method) {
        this.path = path;
        this.method = method;
    }

    @Override
    public String getPath() {
        return this.path;
    }

    @Override
    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public HttpMethod getMethod() {
        return method;
    }

    @Override
    public void setHttpMethod(HttpMethod method) {
        this.method = method;
    }
}
