package com.example.websock;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

/**
 * @author Ido
 * @date 2020/8/18 10:01
 */

@Slf4j
// TextWebSocketFrame: 在Netty中，专门用于websocket处理文本消息的对象，frame是消息的载体
public class WsServerHandler extends SimpleChannelInboundHandler<HttpRequest> {
    /**
     * 全局websocket
     */
    private WebSocketServerHandshaker handshaker;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress socketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        log.info("ip : {} , port :{} ", socketAddress.getHostName(), socketAddress.getPort());
        super.channelActive(ctx);
    }

    /**
     * 用于记录和管理所有客户端的channel
     */
    private ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpRequest msg) throws Exception {

        //普通HTTP接入
        if (msg instanceof FullHttpRequest) {
            if (msg.getUri().equals("/favicon.ico")) {
                ctx.close();
            }
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        } else if (msg instanceof WebSocketFrame) { //websocket帧类型 已连接
            //BinaryWebSocketFrame CloseWebSocketFrame ContinuationWebSocketFrame
            //PingWebSocketFrame PongWebSocketFrame TextWebScoketFrame

            handleWebSocketFrame(ctx, (WebSocketFrame) msg);
        }

    }

    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest request) {
        CompletableFuture fsRsp = CompletableFuture.supplyAsync(() -> {
            try {
                HttpClient client = HttpClientBuilder.create().build();
                org.apache.http.HttpResponse clientRsp = client.execute(new HttpGet("http://localhost:18080/home"));

                byte[] bs = new byte[clientRsp.getEntity().getContent().available()];
                clientRsp.getEntity().getContent().read(bs);
                ByteBuf data = Unpooled.copiedBuffer(bs);
                HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, data);
                response.headers().add("content-length", data.readableBytes());
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
        fsRsp.thenAcceptAsync((response) -> {
            ctx.writeAndFlush(response);
            ctx.close();
        });


        System.out.println("先返回了");
    }


    /**
     * response
     *
     * @param ctx
     * @param request
     * @param response
     */
    private static void sendHttpResponse(ChannelHandlerContext ctx,
                                         FullHttpRequest request, FullHttpResponse response) {
        //返回给客户端
        if (response.status().code() != HttpResponseStatus.OK.code()) {
            ByteBuf buf = Unpooled.copiedBuffer(response.status().toString(), CharsetUtil.UTF_8);
            response.content().writeBytes(buf);
            buf.release();
        }
        //如果不是keepalive那么就关闭连接
        ChannelFuture f = ctx.channel().writeAndFlush(response);
        if (response.status().code() != HttpResponseStatus.OK.code()) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    /**
     * websocket帧
     *
     * @param ctx
     * @param frame
     */
    private void handleWebSocketFrame(final ChannelHandlerContext ctx, WebSocketFrame frame) {
        //判断是否关闭链路指令
        if (frame instanceof CloseWebSocketFrame) {
            CloseWebSocketFrame close = (CloseWebSocketFrame) frame.retain();//这行
            handshaker.close(ctx.channel(), close);
            return;
        }
        //判断是否Ping消息 -- ping/pong心跳包
        if (frame instanceof PingWebSocketFrame) {
            log.info("ping");
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        //本程序仅支持文本消息
        if (frame instanceof TextWebSocketFrame) {
            TextWebSocketFrame msg = (TextWebSocketFrame) frame.copy();
            log.info(msg.text());
            ctx.channel().writeAndFlush(msg);
        }

        //支持二进制消息
        if (frame instanceof BinaryWebSocketFrame) {
            BinaryWebSocketFrame img = (BinaryWebSocketFrame) frame.copy();
            ctx.channel().writeAndFlush(img);
        }


    }


    /**
     * 当客户端连接服务端(打开连接)后
     * 获取客户端的channel，并放到ChannelGroup中进行管理
     *
     * @param ctx ChannelHandlerContext
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        // 不能在这里做关联，因为这里不能接受客户端的消息，是没法绑定的
        clients.add(ctx.channel());
    }

    /**
     * 当触发当前方法时，ChannelGroup会自动移除对应客户端的channel
     *
     * @param ctx ChannelHandlerContext
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        log.info("客户端断开连接，channel的长ID：[{}]", ctx.channel().id().asLongText());
        log.info("客户端断开连接，channel的短ID：[{}]", ctx.channel().id().asShortText());
    }
}

