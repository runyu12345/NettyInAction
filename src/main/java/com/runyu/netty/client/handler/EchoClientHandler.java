package com.runyu.netty.client.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * 
 * 
 * @author runyu12345@163.com
 *
 */
@Sharable
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

	/**
	 * 当被通知的channel活跃的时候,发送一条消息
	 * 
	 * channelActive在连接建立时调用
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks! ", CharsetUtil.UTF_8));
	}

	/**
	 * 记录已接收消息的转储
	 * 
	 * 每当接收数据时被调用
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg)
			throws Exception {
			
		System.out.println("Client received: " + msg.toString(CharsetUtil.UTF_8));
			
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
