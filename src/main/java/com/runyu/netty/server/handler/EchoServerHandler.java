package com.runyu.netty.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * netty 服务端handler
 *
 * @author runyu12345@163.com
 */
// 这个handler可以安全的被多个channel共享
@Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 每个传入的消息都要调用
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        ByteBuf in = (ByteBuf) msg;

        // 丢弃接收的消息
//		ReferenceCountUtil.release(in);

        System.out.println("Server received: " + in.toString(CharsetUtil.UTF_8));

        // Bytebuf的堆缓冲区
        if (in.hasArray()) {
            byte[] array = in.array();
            int offset = in.arrayOffset() + in.readerIndex();
            // 获取可读字节数
            int length = in.readableBytes();

        }
        // Bytebuf的直接缓冲区
        else if (!in.hasArray()) {

            int length = in.readableBytes();
            System.out.println(length);
            byte[] array = new byte[length];

            in.getBytes(in.readerIndex(), array);

        }
        // 将接收到的消息写给发送者,但不冲刷出站消息
        ctx.write(in);
    }

    /**
     * 通知ChannelInboundHandler最后一次对channelRead()的调用,是当前批量读取中的最后一条信息
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        // 将 "未决消息" 冲刷到远程节点, 并且关闭该channel
        // 未决消息(pending message): 目前暂时存在于 ChannelOutboundBuffer中的消息, 在下一次调用flush()或者wirteAndFlush()时写出到socket.
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * 读取消息有异常时调用
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 打印异常,关闭channel
        cause.printStackTrace();
        ctx.close();
    }
}
