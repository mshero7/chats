package com.cafe24.network.chat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

import com.cafe24.network.chat.server.ChatServerApp;

public class ChatClientApp {
	private static final String SERVER_IP = "192.168.1.25";

	public static void main(String[] args) {
		String name = null;
		Scanner scanner = new Scanner(System.in);
		BufferedReader br = null;
		PrintWriter pw = null;
		Socket clientSocket = null;

		while (true) {
			System.out.println("대화명을 입력하세요.");
			System.out.print(">>> ");
			name = scanner.nextLine();

			if (name.isEmpty() == false) {
				break;
			}

			System.out.println("대화명은 한글자 이상 입력해야 합니다.\n");
		}

		try {
			scanner.close();
			// 1. 소켓 만들고 서버와 연결.
			clientSocket = new Socket();
			clientSocket.connect(new InetSocketAddress(SERVER_IP, ChatServerApp.SERVER_PORT));

			// 2. iostream
			br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
			pw = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"), true);
			
			pw.println("JOIN:"+name);
			
			// 3. join protocol 2개
			new ChatWindow(name, clientSocket, br, pw).show();
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	public static void log(String arg) {
		System.out.println(arg);
	}
}
