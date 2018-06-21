package com.example.demo.service;


import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.util.concurrent.AbstractExecutionThreadService;


class ServerService extends AbstractExecutionThreadService {

    private String basePackage = "";

    private static BiMap<String, Servlet> servletCache = HashBiMap.create(100);

    private DispatcherServlet dispatcherServlet = new DispatcherServlet(servletCache);

    private static NettyServer nettyServer;
    private static Integer port;

    public ServerService(Integer port) {
        ServerService.port = port;
    }

    @Override
    protected void startUp() throws Exception {
        nettyServer = new NettyServer(port);
    }

    @Override
    protected void run() throws Exception {
        nettyServer.start(null);
    }

    @Override
    protected void shutDown() throws Exception {
        nettyServer.shutDown();
        this.stopAsync();
    }

    public static NettyServer getNettyServer() {
        return nettyServer;
    }

    public static BiMap getServletCache() {
        return servletCache;
    }

    public DispatcherServlet getDispatcherServlet() {
        return dispatcherServlet;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }
}
