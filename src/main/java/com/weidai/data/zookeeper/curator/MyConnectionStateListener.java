/**
 * 
 */
package com.weidai.data.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.zookeeper.CreateMode;

/**
 * @Description: 断线重连机制
 * @ClassName: MyConnectionStateListener
 * @author nic
 * @date: 2018年7月13日 上午11:22:31
 */
public class MyConnectionStateListener implements ConnectionStateListener {

	private String zkRegPathPrefix;
	private String regContent;
	public MyConnectionStateListener(String zkRegPathPrefix, String regContent) {
		this.zkRegPathPrefix = zkRegPathPrefix;
		this.regContent = regContent;
	}
	/* (non-Javadoc)
	 * @see org.apache.curator.framework.state.ConnectionStateListener#stateChanged(org.apache.curator.framework.CuratorFramework, org.apache.curator.framework.state.ConnectionState)
	 */
	@Override
	public void stateChanged(CuratorFramework client, ConnectionState newState) {
		
		if(newState == ConnectionState.LOST) {
			while(true) {
				try {
					if (client.getZookeeperClient().blockUntilConnectedOrTimedOut()) {
						client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(zkRegPathPrefix, regContent.getBytes());
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
