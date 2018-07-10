package com.weidai.data.multicast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.util.concurrent.TimeUnit;


/**
 * @描述
 * @author nic 
 * @time：2018年7月1日 下午4:56:44
 */
public class MulticastServer {

	public static void main(String[] args) throws IOException {
		try {
			InetAddress group = InetAddress.getByName("224.5.6.7");
			MulticastSocket socket = new MulticastSocket();
			for (int i=0; i<10; i++) {
				String data = "hello zhangsan " + i;
				byte[] bytes = data.getBytes();
				socket.send(new DatagramPacket(bytes, bytes.length, group, 8888));
				TimeUnit.SECONDS.sleep(2);
			}
		}catch(Exception e ) {
			e.printStackTrace();
		}
	}
}
