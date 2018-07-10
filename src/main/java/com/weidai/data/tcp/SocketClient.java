package com.weidai.data.tcp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketAddress;


/**
 * @描述
 * @author nic 
 * @time：2018年7月1日 下午4:23:39
 */
public class SocketClient {

	public static void main(String[] args) {
		try {
			Socket socket = new Socket("localhost", 8123);
			PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer.write("hello wang!");
			writer.print("hello zhang!");
			writer.println("hello li!");
			writer.println("hello zhao!");
			while(true) {
				String input = reader.readLine();
				if (input == null)
					break;
				System.out.println(input);
			}
			writer.close();
			socket.close();
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			
		}
		
	}
}
