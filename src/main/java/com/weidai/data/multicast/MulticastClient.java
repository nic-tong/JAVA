package com.weidai.data.multicast;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;


/**
 * @描述   在虚拟机运行参数中加上  -Djava.net.preferIPv4Stack=true 
 * 		否则会报  java.net.SocketException: Can't assign requested address 异常
 * @author nic  
 * @time：2018年7月1日 下午4:56:55
 */
public class MulticastClient {

	public static void main(String[] args) throws Exception {
 		InetAddress group = InetAddress.getByName("224.5.6.7");
		MulticastSocket socket = new MulticastSocket(8888);
		socket.joinGroup(group);
		byte[] buff = new byte[256];
		while(true) {
			DatagramPacket message = new DatagramPacket(buff, buff.length);
			socket.receive(message);
			System.out.println(new String(message.getData()));
		}
	}
}
