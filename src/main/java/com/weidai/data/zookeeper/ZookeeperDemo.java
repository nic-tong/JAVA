package com.weidai.data.zookeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.jboss.netty.handler.codec.http.multipart.HttpPostRequestEncoder.EncoderMode;


/**
 * @描述
 * @author nic 
 * @time：2018年7月7日 下午1:21:58
 */
public class ZookeeperDemo {

	private static final String CONNECT_ADDRESS = "192.168.21.149:2181,192.168.21.151:2181,192.168.21.152:2181";
	
	private static final CountDownLatch COUNT_DOWN_LATCH = new CountDownLatch(1);
	
	private static ZooKeeper zookeeper;
	private static Stat stat= new Stat();
	public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
		
		zookeeper = new ZooKeeper(CONNECT_ADDRESS, 2000, new MyWatcher());
		
		// zookeeper 客户端和服务端建立连接是一个异步的过程，所以在初始化的时候要进行一个线程阻塞
		System.out.println(zookeeper.getState());
		
		COUNT_DOWN_LATCH.await();
		// 打印连接状态
		System.out.println(zookeeper.getState());
		zookeeper.addAuthInfo("digest", "root:root".getBytes());
		// 创建一个节点
		String path = "/node4";
		String childPath = "/childnode1";
		String result = zookeeper.create("/node4", "123".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		zookeeper.getData(path,new MyWatcher(),stat); //增加一个监听
		System.out.println(result);
		// 创建一个节点
		Thread.sleep(5000);
		zookeeper.setData(path, "3333".getBytes(), -1);
		Thread.sleep(5000);
		Stat stat=zookeeper.exists(path + childPath, true);
		if (stat == null) {
			System.out.println("创建子节点" + path + childPath);
			result = zookeeper.create(path + childPath, "123".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		}
		Thread.sleep(5000);
		System.out.println("修改子节点值" + path + childPath);
		zookeeper.setData(path + childPath, "222".getBytes(), -1);
		Thread.sleep(5000);
		System.out.println("删除子节点" + path + childPath);
		zookeeper.delete(path + childPath, -1);
		Thread.sleep(5000);
		System.out.println("删除节点" + path);
		zookeeper.delete(path, -1);
		Thread.sleep(5000);
	}
	
	
	private static class MyWatcher implements Watcher {

		@Override
		public void process(WatchedEvent watchedEvent) {
			if (watchedEvent.getState() == KeeperState.SyncConnected) {
				if (watchedEvent.getType() == EventType.None && null == watchedEvent.getPath()) {
					COUNT_DOWN_LATCH.countDown();
					System.out.println("zookeeper 已建立连接");
				} else if (watchedEvent.getType() == EventType.NodeCreated) {
					System.out.println("创建节点状态监听 ——>" + watchedEvent.getPath());
					try {
						zookeeper.getData(watchedEvent.getPath(), true, stat);
					} catch (KeeperException | InterruptedException e) {
						e.printStackTrace();
					}
				}  else if (watchedEvent.getType() == EventType.NodeChildrenChanged) {
					try {
					System.out.println("子节点值变化监听 ——>" + watchedEvent.getPath() +
							" 变化后的值为：" + zookeeper.getData(watchedEvent.getPath(), true, stat));
					} catch (KeeperException | InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}  else if (watchedEvent.getType() == EventType.NodeDataChanged) {
					try {
					System.out.println("节点值变化监听 ——>" + watchedEvent.getPath() +
							" 变化后的值为：" + zookeeper.getData(watchedEvent.getPath(), true, stat));
					} catch (KeeperException | InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}   else if (watchedEvent.getType() == EventType.NodeDeleted) {
					System.out.println("节点删除监听 ——>" + watchedEvent.getPath());
				}
				
			}
		}
		
		
	}
}
