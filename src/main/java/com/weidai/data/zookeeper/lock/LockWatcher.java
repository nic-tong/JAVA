/**
 * 
 */
package com.weidai.data.zookeeper.lock;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;

/**
 * @Description: TODO
 * @ClassName: LockWatcher
 * @author nic
 * @date: 2018年7月10日 下午2:56:42
 */
public class LockWatcher implements Watcher {

	CountDownLatch latch;
	
	/**
	 * 
	 */
	public LockWatcher(CountDownLatch latch) {
		this.latch = latch;
	}
	/* (non-Javadoc)
	 * @see org.apache.zookeeper.Watcher#process(org.apache.zookeeper.WatchedEvent)
	 */
	@Override
	public void process(WatchedEvent event) {
		if (event.getType() == EventType.NodeDeleted) {
			latch.countDown();
		}
	}

}
