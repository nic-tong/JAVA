/**
 * 
 */
package com.weidai.data.zookeeper.masterselect;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkException;
import org.I0Itec.zkclient.exception.ZkInterruptedException;
import org.I0Itec.zkclient.exception.ZkNoNodeException;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;

/**
 * @Description: TODO
 * @ClassName: MasterSelector
 * @author nic
 * @date: 2018年7月10日 下午4:30:37
 */
public class MasterSelector {

	
	/**
	 * 
	 */
	private ZkClient zkClient;
	
	/**
	 * master 节点路径
	 */
	private final String MASTER_PATH = "/master";
	
	/**
	 * 争抢到的master节点
	 */
	private ClientData masterNode;
	
	private ClientData serverNode;
	
	/**
	 * 服务是否启动
	 */
	private volatile boolean isRuning = false;
	
	IZkDataListener dataListener;
	
	private ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(1);
	
	
	public MasterSelector(ZkClient zkClient, ClientData clientData) {
		this.zkClient = zkClient;
		this.serverNode = clientData;
		this.dataListener = new IZkDataListener() {
			
			@Override
			public void handleDataDeleted(String dataPath) throws Exception {
				vieMaster();
				//监听节点删除
//				scheduledExecutor.schedule(new Runnable() {
//					
//					@Override
//					public void run() {
//						vieMaster();
//					}
//				}, 5, TimeUnit.SECONDS);				
			}
			
			@Override
			public void handleDataChange(String dataPath, Object data) throws Exception {

				
			}
		};
	}
	
	/**
	 * 启动选举master
	 */
	public void start() {
		if(isRuning) 
			throw new RuntimeException("服务已经启动...");
		isRuning = true;
		zkClient.subscribeDataChanges(MASTER_PATH, dataListener);
		vieMaster();
	}
	
	
	/**
	 * 停止服务并释放master权
	 */
	public void stop() {
		if (!isRuning)
			throw new RuntimeException("服务已经停止...");
		isRuning = false;
		
		zkClient.subscribeDataChanges(MASTER_PATH, dataListener);
		releaseMaster();
	}
	/**
	 * 
	 */
	private void releaseMaster() {
        if (checkIsMaster()) {
            zkClient.delete(MASTER_PATH);
        }
	}

	/**
	 * 竞争master节点
	 */
	private void vieMaster() {
		if (!isRuning)
			return;
		
		try {
			System.out.println(serverNode.getClientName() + "来争抢master节点.");
			zkClient.createEphemeral(MASTER_PATH, serverNode);
			masterNode = serverNode;
			System.out.println(serverNode.getClientName() + "竞选成功！ 成为了master节点.");
			scheduledExecutor.schedule(new Runnable() {
				
				@Override
				public void run() {
					releaseMaster();
					
				}
			}, 5, TimeUnit.SECONDS);
		} catch (ZkNodeExistsException e) {
			
		}
	}

	/**
	 * @return 检查当前节点是否为maste 节点
	 */
	protected boolean checkIsMaster() {

		try {
			ClientData cNode = zkClient.readData(MASTER_PATH);
			masterNode = cNode;
			if(serverNode.getClientName().equals(masterNode.getClientName())) {
				return true;
			}
			return false;
		} catch (ZkNoNodeException e) {
			return false;
		} catch (ZkInterruptedException e) {
			return checkIsMaster();
		} catch(ZkException e) {
			return false;
		}
	}
}
