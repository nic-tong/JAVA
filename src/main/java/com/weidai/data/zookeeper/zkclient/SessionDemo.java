package com.weidai.data.zookeeper.zkclient;

import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;


/**
 * @描述
 * @author nic 
 * @time：2018年7月7日 下午3:09:32
 */
public class SessionDemo {

	private static final String CONNECT_ADDRESS = "192.168.21.149:2181,192.168.21.151:2181,192.168.21.152:2181";
	
	public static void main(String[] args) {
		ZkClient zkClient = new ZkClient(CONNECT_ADDRESS, 5000);
		System.out.println(zkClient + "-->success");
		String path = zkClient.create("/node3", "123", CreateMode.PERSISTENT);
		System.out.println(path);
	}
}
