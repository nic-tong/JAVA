package com.weidai.data.zookeeper.curator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;


/**
 * @描述  watcher 事件的案例
 * @author nic 
 * @time：2018年7月7日 下午8:30:48
 */
public class CacheDemo {

	private static final String CONNECT_ADDRESS = "192.168.21.149:2181,192.168.21.151:2181,192.168.21.152:2181";

	public static void main(String[] args) throws Exception {
		
		CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
				.connectString(CONNECT_ADDRESS).sessionTimeoutMs(5000)
				.namespace("wang").retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
		curatorFramework.start();
		System.out.println("success");
//		/**
//		 * nodeCache
//		 */
//		NodeCache nodeCache = new NodeCache(curatorFramework, "/zhang", false);
//		nodeCache.start(true);
//		nodeCache.getListenable().addListener(()->{
//			System.out.println(nodeCache.getCurrentData().getPath() + "节点数据发生变化，变化后结果：" + new String(nodeCache.getCurrentData().getData()));
//		});
//		
//		/**
//		 * pathChildrenCache
//		 */
//		PathChildrenCache paCache = new PathChildrenCache(curatorFramework, "/zhang", true);
//		
//		paCache.start(StartMode.BUILD_INITIAL_CACHE);
//		paCache.getListenable().addListener((curatorFramework1, pahChildrenEvent)->{
//			switch (pahChildrenEvent.getType()) {
//			case CHILD_ADDED:
//				System.out.println("创建子节点");
//				break;
//			case CHILD_REMOVED:
//				System.out.println("删除子节点");
//				break;
//			case CHILD_UPDATED:
//				System.out.println("更新子节点");
//				break;
//			default:
//				break;
//			}
//		});
		
		/**
		 * TreeCache
		 */
		ExecutorService executor = Executors.newFixedThreadPool(10);
		TreeCache treeCache = new TreeCache(curatorFramework, "/zhang");
		treeCache.start();
		treeCache.getListenable().addListener((curatorFramework1, pahChildrenEvent)->{
			switch (pahChildrenEvent.getType()) {
			case NODE_ADDED:
				System.out.println(Thread.currentThread().getName() + "创建节点" + pahChildrenEvent.getData().getPath());
				break;
			case NODE_REMOVED:
				System.out.println(Thread.currentThread().getName() + "删除节点" + pahChildrenEvent.getData().getPath());
				break;
			case NODE_UPDATED:
				System.out.println(Thread.currentThread().getName() + "更新节点" + pahChildrenEvent.getData().getPath());
				break;
			default:
				break;
			}
		},executor);
		curatorFramework.create().withMode(CreateMode.PERSISTENT).forPath("/zhang");
		TimeUnit.SECONDS.sleep(1);
		curatorFramework.setData().forPath("/zhang","112".getBytes());
		TimeUnit.SECONDS.sleep(1);
		
		curatorFramework.create().withMode(CreateMode.EPHEMERAL).forPath("/zhang/event","111".getBytes());
		TimeUnit.SECONDS.sleep(1);
		curatorFramework.setData().forPath("/zhang/event", "222".getBytes());
		TimeUnit.SECONDS.sleep(1);
		curatorFramework.delete().forPath("/zhang/event");
		TimeUnit.SECONDS.sleep(2);
		treeCache.close();
		executor.shutdown();
	}
}
