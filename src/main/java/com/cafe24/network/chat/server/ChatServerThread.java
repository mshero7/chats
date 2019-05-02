package com.cafe24.network.chat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Set;

public class ChatServerThread extends Thread {
	private String nickname;
	private Socket socket;
	BufferedReader br = null;
	PrintWriter pw = null;
	Set<Writer> writerSet = null;

	public ChatServerThread(Socket socket, Set<Writer> writerSet) {
		this.socket = socket;
		this.writerSet = writerSet;
	}

	@Override
	public void run() {
		try {
			br = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
			pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);

			System.out.println(socket + "정보");

			while (true) {
				String request = br.readLine();

				if (request == null) {
					ChatServerApp.log("클라이언트로 부터 연결 끊김!!");
					doQuit(pw);
					break;
				}
				
				log("request log: " + request);

				String[] tokens = request.split(":");

				if ("join".equals(tokens[0].toLowerCase())) {
					doJoin(tokens[1], pw);
				} else if ("quit".equals(tokens[0].toLowerCase())) {
					System.out.println("QUIT");
					doQuit(pw);
				} else if ("message".equals(tokens[0].toLowerCase())) {
					doMessage(tokens[1]);
					System.out.println("message");
				} else {
					ChatServerApp.log("에러:알수 없는 요청(" + tokens[0] + ") => " + socket);
				}

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				br.close();
				pw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private void doQuit(PrintWriter writer) {
		boolean result = removeWriter(writer);
		if (result) {
			String data = nickname + "님이 퇴장 하였습니다.";
			broadcast(data);
		}
	}

	private boolean removeWriter(Writer writer) {
		boolean flag = false;

		if (writerSet.contains(writer)) {
			writerSet.remove(writer);
			flag = true;
		}

		return flag;
	}

	private void doMessage(String message) {
		broadcast(nickname + " : " + message);
	}

	private void doJoin(String nickname, PrintWriter pw) {
		this.nickname = nickname;
		String data = nickname + "님이 참여하였습니다.";

		/* writer pool에 저장 */
		addWriter(pw);

		broadcast(data);
		System.out.println("nickname : " + nickname);
	}

	private void addWriter(Writer pw) {
		synchronized (writerSet) {
			writerSet.add(pw);
			System.out.println("writerSet length : " + writerSet.size());
		}
	}

	private void broadcast(String data) {
		synchronized (writerSet) {
			for (Writer writer : writerSet) {
				PrintWriter printWriter = (PrintWriter) writer;
				printWriter.println(data);
				printWriter.flush();
			}

		}
	}

	public static void log(String arg) {
		System.out.println(arg);
	}
}
