/**
 * 
 */
package com.weidai.data.zookeeper.masterselect;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

/**
 * @Description: TODO
 * @ClassName: MaterSelectTest
 * @author nic
 * @date: 2018年7月11日 上午11:12:39
 */
public class MaterSelectTest {

	
	private static final String CONNECT_ADDRESS = "192.168.21.149:2181,192.168.21.151:2181,192.168.21.152:2181";

	private static final int SESSTION_TIMEOUT = 5000;
	
	private static final int CONNECT_TIMEOUT = 5000;
	
	public static void main(String[] args) {
		ExecutorService service = Executors.newCachedThreadPool();
		
		for (int i = 0; i < 10; i++) {
			final int idx =i;
			Runnable run = new Runnable() {
				
				@Override
				public void run() {
					ZkClient zkClient =new ZkClient(CONNECT_ADDRESS, SESSTION_TIMEOUT, CONNECT_TIMEOUT, new SerializableSerializer());
					ClientData cNode = new ClientData(idx, "client-" + idx);
					MasterSelector masterSelector = new MasterSelector(zkClient, cNode);
					masterSelector.start();
				}
			};
			service.execute(run);
		}
		
	}
}
