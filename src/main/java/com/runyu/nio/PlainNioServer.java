package com.runyu.nio;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;


public class PlainNioServer {

	
	public void serve(int port) throws IOException {
		// 打开一个 socketChannel
		ServerSocketChannel serverChannel = ServerSocketChannel.open();
		serverChannel.configureBlocking(false);
		
		// TODO P/40
	}
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
