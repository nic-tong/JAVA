package com.weidai.data.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


/**
 * @描述
 * @author nic 
 * @time：2018年7月5日 下午8:23:12
 */
public class SayHelloImpl extends UnicastRemoteObject implements ISayHello {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6490909599089987949L;

	protected SayHelloImpl() throws RemoteException {
		super();
	}

	@Override
	public String sayHello(String name) throws RemoteException {
		String res = "hi, " + name;
		System.out.println(res);
		return res;
	}

	
}
