package com.weidai.data.zookeeper.lock;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;


/**
 * @描述
 * @author nic 
 * @time：2018年7月9日 下午8:27:01
 */
public class ZookeeperClient {
	private static final String CONNECT_ADDRESS = "192.168.21.149:2181,192.168.21.151:2181,192.168.21.152:2181";

	public static ZooKeeper getInstance() throws IOException, InterruptedException {
		CountDownLatch countDownLatch = new CountDownLatch(1);
		ZooKeeper zookeeper = new ZooKeeper(CONNECT_ADDRESS, 5000, new Watcher() {
			
			@Override
			public void process(WatchedEvent event) {
				if (event.getState() == KeeperState.SyncConnected) {
					countDownLatch.countDown();
					System.out.println("zookeeper 连接建立.");
				}
			}
		});
		countDownLatch.await();
		return zookeeper;
	}
	
	public static void main(String[] args) throws IOException, InterruptedException {
		getInstance();
	}
}
