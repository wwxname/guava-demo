package com.example.demo.service;

import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.json.JsonObjectDecoder;
import io.netty.util.CharsetUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.ws.Service;
import java.net.InetAddress;


public class NettyServerHandler extends ChannelHandlerAdapter {

    public static final Log log = LogFactory.getLog(NettyServer.class);

    DispatcherServlet dispatcherServlet = ServiceContainer.getServerService().getDispatcherServlet();

    /*
     * 收到消息时，返回信息
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String result = "";
        if (!(msg instanceof FullHttpRequest)) {
            send(ctx,"未知请求",HttpResponseStatus.BAD_REQUEST);
            return;
        }
        FullHttpRequest httpRequest = (FullHttpRequest) msg;
        try {
            String path = httpRequest.uri();    //获取路径
            String body = getBody(httpRequest);    //获取参数
            HttpMethod method = httpRequest.method();//获取请求方法
            //如果不是这个路径，就直接返回错误
            if (!path.startsWith("/plus")){
                send(ctx,"非法求情", HttpResponseStatus.BAD_REQUEST);
                return;
            }
            //log method
            logRequestMethod(method);
            Object object = dispatcherServlet.execute(httpRequest);
            send(ctx, JSONObject.toJSONString(object), HttpResponseStatus.OK);
        } catch (Exception e) {
            log.warn("处理请求失败!");
            send(ctx, "系统错误", HttpResponseStatus.INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        } finally {
            //释放请求
            httpRequest.release();
        }
    }

    private void logRequestMethod(HttpMethod method) {
        log.info("获得-" + method.toString() + "-请求");
    }

    /**
     * 获取body参数
     *
     * @param request
     * @return
     */
    private String getBody(FullHttpRequest request) {
        ByteBuf buf = request.content();
        return buf.toString(CharsetUtil.UTF_8);
    }

    /**
     * 发送的返回值
     *
     * @param ctx     返回
     * @param context 消息
     * @param status  状态
     */
    private void send(ChannelHandlerContext ctx, String context, HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, Unpooled.copiedBuffer(context, CharsetUtil.UTF_8));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/json; charset=UTF-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    /*
     * 建立连接时，返回消息
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("连接的客户端地址:" + ctx.channel().remoteAddress());
        ctx.writeAndFlush("客户端" + InetAddress.getLocalHost().getHostName() + "成功与服务端建立连接！ ");
        super.channelActive(ctx);
    }
}
