package com.weidai.data.rmi;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;


/**
 * @描述
 * @author nic 
 * @time：2018年7月5日 下午8:49:46
 */
public class HelloServer {

	public static void main(String[] args) {
		try  {
			ISayHello hello = new SayHelloImpl();
			LocateRegistry.createRegistry(8888);
			Naming.bind("rmi://localhost:8888/sayHello", hello);
			System.out.println("server start success");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
