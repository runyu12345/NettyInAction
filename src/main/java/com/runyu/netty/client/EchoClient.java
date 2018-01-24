package com.runyu.netty.client;

import java.security.acl.Group;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import com.runyu.netty.client.handler.EchoClientHandler;

/**
 * 
 * @author runyu12345@163.com
 *
 */
public class EchoClient {
	
	private final String host;
	private final int port;
	
	public EchoClient(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	public void start() throws Exception {
		
		EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
		
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(eventLoopGroup)
			.channel(NioSocketChannel.class)
			.remoteAddress(host, port)
			.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch) throws Exception{
					ch.pipeline().addLast(new EchoClientHandler());
				}
			});
			
			ChannelFuture channelFuture = bootstrap.connect().sync();
			channelFuture.channel().closeFuture().sync();
		} finally {
			eventLoopGroup.shutdownGracefully().sync();
		}
	}
	
	public static void main(String[] args) throws Exception {
		if(args.length != 2) {
			System.err.println("Usage: " + EchoClient.class.getSimpleName() + "<host><ip>");
			return;
		}
		
		String host = args[0];
		int port = Integer.parseInt(args[1]);
		
		new EchoClient(host, port).start();
	}
}
