package com.runyu.oio;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

public class PlainOioServer {
	
	public void serve(int port) throws IOException {
		// 创建一个 ServerSocket
		final ServerSocket serverSocket = new ServerSocket();
		
		try {
			for(;;) {
				// 准备接受链接
				final Socket clientSocket = serverSocket.accept();
				//
				System.out.println("Accept connection from " + clientSocket);
				
				// 每次都创建一个新线程  处理连接
				new Thread(new Runnable() {
					
					public void run() {
						OutputStream out;
						try {
							out = clientSocket.getOutputStream();
							out.write("Hello World~ \r\n".getBytes(Charset.forName("UTF-8")));
							out.flush();
							clientSocket.close();
						} catch (IOException e) {
							e.printStackTrace();
						} finally {
							try {
								clientSocket.close();
							} catch (Exception e2) {
								// 关闭连接异常时 忽略
							}
						}
					}
				}).start();
				
			}
		}catch (IOException e) {
			e.printStackTrace();
		} 
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
