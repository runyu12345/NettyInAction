package com.runyu.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

import com.runyu.netty.server.handler.EchoServerHandler;

/**
 * Server端完整类
 * @author runyu
 *
 */
public class EchoServer {
	
	private final int port;
	
	public EchoServer(int port) {
		this.port = port;
	}
	
	public static void main(String[] args) throws Exception {
		if(args.length != 1) {
			System.err.println("Usage: " + EchoServer.class.getSimpleName() + " <port>");
		}
		
		int port = Integer.parseInt(args[0]);
		
		EchoServer echoServer = new EchoServer(port);
		
		// 调用启动方法 
		echoServer.start();
		
	}
	
	/**
	 * 服务端启动方法
	 * @throws Exception
	 */
	public void start() throws Exception {
		
		final EchoServerHandler echoServerHandler = new EchoServerHandler();
		
		// 这里用来证明一下, final修饰的对象
		System.out.println(echoServerHandler.hashCode());
		
		// 创建 EventLoopGroup 
		EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
		
		try {
			// 创建 ServerBootstrap
			ServerBootstrap bootstrap = new ServerBootstrap();
		
			bootstrap.group(eventLoopGroup)
			// 指定NIOchannel
			.channel(NioServerSocketChannel.class)
			// 指定端口socket地址
			.localAddress(new InetSocketAddress(port))
			// 添加一个  EchoServerHandler到子的 Channel 的 ChannelPipeline
			.childHandler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {

						ch.pipeline().addLast(echoServerHandler);
				
					}
			
				});
			
			// 异步绑定服务器,调用sync()阻塞等待,直到绑定完成
			ChannelFuture channelFuture = bootstrap.bind().sync();
			// 获取Channel的CloseFuture,并且阻塞当前线程直到他完成
			channelFuture.channel().closeFuture().sync();
			
			} finally {
				// 关闭 EventLoopGroup 释放所有资源
				eventLoopGroup.shutdownGracefully().sync();
			}
	}
	
}
