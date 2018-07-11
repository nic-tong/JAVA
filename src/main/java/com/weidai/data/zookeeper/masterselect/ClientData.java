/**
 * 
 */
package com.weidai.data.zookeeper.masterselect;

import java.io.Serializable;

/**
 * @Description: TODO
 * @ClassName: ClientData
 * @author nic
 * @date: 2018年7月11日 上午10:25:05
 */
public class ClientData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3743852673514077130L;

	private final int clientId;
	
	private final String clientName;
	
	/**
	 * 
	 */
	public ClientData(int clientId, String clientName) {
		this.clientId = clientId;
		this.clientName = clientName;
	}
	/**
	 * @return the clientId
	 */
	public int getClientId() {
		return clientId;
	}

	/**
	 * @return the clientName
	 */
	public String getClientName() {
		return clientName;
	}
}
