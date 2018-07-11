/**
 * 
 */
package com.weidai.data.zookeeper.lock;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @Description: TODO
 * @ClassName: CuratorLock
 * @author nic
 * @date: 2018年7月11日 下午8:55:37
 */
public class CuratorLock {
	
	private static final String CONNECT_ADDRESS = "192.168.21.149:2181,192.168.21.151:2181,192.168.21.152:2181";

	public static final String lockPath = "/curator_recipes_lock";
	
	
	public static void main(String[] args) {
		
		
		
		final CountDownLatch latch = new CountDownLatch(1);
		
		for (int i=0; i< 10; i++) {
			new Thread(()-> {
				CuratorFramework client = CuratorFrameworkFactory.builder().connectString(CONNECT_ADDRESS)
						.sessionTimeoutMs(5000).retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
				client.start();
				final InterProcessLock lock = new InterProcessMutex(client, lockPath);
				try {
					latch.await();
					lock.acquire();
					SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss,SSS");

					String orderNo = sdf.format(new Date());
					System.out.println(client.getZookeeperClient().getZooKeeper().getSessionId() + "获取了锁" + orderNo);


				} catch (Exception e) {}
				finally {
					try {
						lock.release();
						System.out.println(client.getZookeeperClient().getZooKeeper().getSessionId() + "释放了锁");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}).start();
		}
		
		latch.countDown();
	}
}
