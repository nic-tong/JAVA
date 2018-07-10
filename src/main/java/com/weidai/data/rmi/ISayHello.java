package com.weidai.data.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;


/**
 * @描述
 * @author nic 
 * @time：2018年7月5日 下午8:22:13
 */
public interface ISayHello extends Remote {

	public String sayHello(String name) throws RemoteException;
}
