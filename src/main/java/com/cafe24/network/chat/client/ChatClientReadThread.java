package com.cafe24.network.chat.client;

import java.awt.TextArea;
import java.io.BufferedReader;
import java.io.IOException;

public class ChatClientReadThread implements Runnable {
	BufferedReader br = null;
	TextArea textArea = null;
	
	

	public ChatClientReadThread(BufferedReader br, TextArea textArea) {
		this.br = br;
		this.textArea = textArea;
	}

	@Override
	public void run() {
		try {
			while(true) {
				String data = br.readLine();
				
				if("quit!".equals(data)) {
					break;
				}
				
				if (data == null) {
					log("Closed By Server");
					return;
				}
				
				textArea.append(data+"\n");				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	public static void log(String arg) {
		System.out.println(arg);
	}
}
