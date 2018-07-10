/**
 * 
 */
package com.weidai.data.zookeeper.lock;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * @Description: TODO
 * @ClassName: JavaApiLockTest
 * @author nic
 * @date: 2018年7月10日 下午3:02:51
 */
public class JavaApiLockTest {

	public static void main(String[] args)  {
		CountDownLatch countDownLatch  = new CountDownLatch(10);
		
		for (int i=0; i< 10 ; i++) {
			new Thread(()-> {
				JavaApiLock lock = null;
				try {
					lock = new JavaApiLock();
					countDownLatch.countDown();
					countDownLatch.await();
					lock.lock();
					Thread.sleep(new Random().nextInt(500));
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (lock != null)
						lock.unlock();
				}
			}).start();
		}
	}
}
