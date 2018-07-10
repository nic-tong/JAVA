package com.weidai.data.zookeeper.curator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;


/**
 * @描述
 * @author nic 
 * @time：2018年7月7日 下午5:43:03
 */
public class CuratorSessionDemo {

	private static final String CONNECT_ADDRESS = "192.168.21.149:2181,192.168.21.151:2181,192.168.21.152:2181";

	
	public static void main(String[] args) throws Exception {
		
		CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
				.connectString(CONNECT_ADDRESS).sessionTimeoutMs(5000)
				.namespace("wang").retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
		curatorFramework.start();
		System.out.println("success");
		NodeCache nodeCache = new NodeCache(curatorFramework, "/zhang", false);
		nodeCache.start(true);
		nodeCache.getListenable().addListener(()->{
			System.out.println(nodeCache.getCurrentData().getPath() + "节点数据发生变化，变化后结果：" + new String(nodeCache.getCurrentData().getData()));
		});
		ExecutorService executor = Executors.newFixedThreadPool(10);
		try {
			/**
			 * 创建节点
			 */
			String res = curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT)
					.inBackground(new BackgroundCallback() {
						
						@Override
						public void processResult(CuratorFramework client, CuratorEvent event)
								throws Exception {
							System.out.println(Thread.currentThread().getName() + "-->result:" + event.getType());
						}
					},executor).forPath("/zhang","123".getBytes());
			System.out.println(Thread.currentThread().getName() + "  "+res);
			TimeUnit.SECONDS.sleep(1);
			/**
			 * 更新节点
			 */
			Stat stat = curatorFramework.setData().forPath("/zhang", "333".getBytes());
			System.out.println(stat);
			
			/**
			 * 创建子节点
			 */
			res = curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath("/zhang/zhang1","111".getBytes());
			System.out.println(res);
			stat = curatorFramework.setData().forPath("/zhang/zhang1", "333".getBytes());
			System.out.println(stat);
			curatorFramework.delete().deletingChildrenIfNeeded().forPath("/zhang");
			System.out.println("删除完成");
//			/**
//			 * 事务操作
//			 */
//			Collection<CuratorTransactionResult> results  = curatorFramework.inTransaction()
//					.create().forPath("/zhang", "111".getBytes()).and().setData().forPath("/zhang", "1231".getBytes())
//					.and().commit();
//			TimeUnit.SECONDS.sleep(1);
//			for (CuratorTransactionResult result : results) {
//				System.out.println(result.getType());
//			}
			executor.shutdown();
			nodeCache.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
