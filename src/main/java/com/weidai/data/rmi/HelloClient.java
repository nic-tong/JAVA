package com.weidai.data.rmi;

import java.rmi.Naming;


/**
 * @描述
 * @author nic 
 * @time：2018年7月5日 下午8:55:14
 */
public class HelloClient {

	public static void main(String[] args) {
		try {
			ISayHello hello = (ISayHello) Naming.lookup("rmi://localhost:8888/sayHello");
			System.out.println(hello);
			System.out.println(hello.sayHello("张三"));
		} catch(Exception e) {
			
		}
	}
}
