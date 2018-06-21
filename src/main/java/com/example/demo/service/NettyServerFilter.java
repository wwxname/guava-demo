package com.example.demo.service;


import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;



public class NettyServerFilter extends ChannelInitializer<SocketChannel> {
 
     @Override
     protected void initChannel(SocketChannel ch) throws Exception {
         ChannelPipeline ph = ch.pipeline();
         //处理http服务的关键handler
         ph.addLast("encoder",new HttpResponseEncoder());
         ph.addLast("decoder",new HttpRequestDecoder());
         ph.addLast("aggregator", new HttpObjectAggregator(10*1024*1024));
         // 服务端业务逻辑
         ph.addLast("handler", new NettyServerHandler());
     }
 }
