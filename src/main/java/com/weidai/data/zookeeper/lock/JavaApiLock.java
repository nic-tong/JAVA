/**
 * 
 */
package com.weidai.data.zookeeper.lock;

import java.io.IOException;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

/**
 * @Description:  通过javaapi实现分布式锁
 * @ClassName: JavaApiLock
 * @author nic
 * @date: 2018年7月10日 下午2:37:39
 */
public class JavaApiLock {

	private ZooKeeper zooKeeper;
	
	private String lockId;
	
	private static final  byte [] LOCK_VALUE = new byte[]{1};
	private static final String LOCK_PATH = "/LOCKS";
	
	private CountDownLatch latch = new CountDownLatch(1);
	
	/**
	 * @throws InterruptedException 
	 * @throws IOException 
	 * 
	 */
	public JavaApiLock() throws IOException, InterruptedException {
		this.zooKeeper = ZookeeperClient.getInstance();
	}
	
	public boolean lock() {
		try {
			lockId = zooKeeper.create(LOCK_PATH + "/", LOCK_VALUE, Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
			List<String> locks =  zooKeeper.getChildren(LOCK_PATH, true);
			TreeSet<String> treeSet = new TreeSet<>();
			for (String lock : locks) {
				treeSet.add(LOCK_PATH + "/" + lock);
			}
			if (lockId.equals(treeSet.first())) {
				System.out.println(Thread.currentThread().getName() + " 获取到了锁，id为->" + lockId);
				return true;
			}
			
			SortedSet<String> lessThanLockId = treeSet.headSet(lockId);
			if (!lessThanLockId.isEmpty()) {
				String preLockId = lessThanLockId.last();
				zooKeeper.exists(preLockId, new LockWatcher(latch));
				latch.await();
				System.out.println(Thread.currentThread().getName() + " 等待获取到了锁，id为->" + lockId);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean unlock() {
		
		try {
			zooKeeper.delete(lockId, -1);
			System.out.println(Thread.currentThread().getName() + "成功删除锁->" +lockId);
		}catch (Exception e) {
			e.printStackTrace();	
		}
		return true;
	}
}
