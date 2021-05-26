package com.example.websock;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author Ido
 * @date 2020/8/18 10:00
 */
public class HttpServerChannelInitializer extends ChannelInitializer {
    @Override
    protected void initChannel(Channel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        // websocket是基于http协议的，所以需要使用http编解码器
        pipeline.addLast(new HttpServerCodec())
                // 对写大数据流的支持
                .addLast(new ChunkedWriteHandler())
                // 对http消息的聚合，聚合成FullHttpRequest或FullHttpResponse
                // 在Netty的编程中，几乎都会使用到这个handler
                .addLast(new HttpObjectAggregator(1024 * 64));
        // 以上三个处理器是对http协议的支持

        // 自定义的处理器
        pipeline.addLast(new WsServerHandler());
    }

}
