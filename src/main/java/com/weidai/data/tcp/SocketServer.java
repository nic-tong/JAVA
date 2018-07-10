package com.weidai.data.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * @描述
 * @author nic 
 * @time：2018年7月1日 下午4:23:26
 */
public class SocketServer {

	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		try {
			serverSocket=  new ServerSocket(8123);
			Socket socket = serverSocket.accept();
			new Thread(() ->{
				try {
					BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
					writer.println("hi , zhao !");
					while(true) {
						String input = reader.readLine();
						if (input == null)
							break;
						System.out.println(input);
						writer.flush();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}).start();
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (serverSocket != null)
				try {
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		
	}
}
