package com.cafe24.network.chat.server;

import java.io.IOException;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class ChatServerApp {
	public static final int SERVER_PORT = 10000;
	
	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		Set<Writer> writerList = new HashSet<Writer>();
		
		try {
			serverSocket = new ServerSocket();
			serverSocket.bind(new InetSocketAddress("0.0.0.0", SERVER_PORT));
			
			while(true) {
				Socket clientSocket = serverSocket.accept();
				new ChatServerThread(clientSocket,writerList).start();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void log(String arg) {
		System.out.println(arg);
	}
}
