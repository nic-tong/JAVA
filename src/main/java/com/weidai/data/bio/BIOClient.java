package com.weidai.data.bio;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;


/**
 * @描述
 * @author nic 
 * @time：2018年8月15日 下午3:23:26
 */
public class BIOClient {

	public static void main(String[] args) {
		int count = 1;
		final CountDownLatch latch = new CountDownLatch(count);
		for (int i=0; i< count; i++) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						latch.await();
						Socket client = new Socket("localhost", 8080);
						OutputStream oStream = client.getOutputStream();
						String name = UUID.randomUUID().toString();
						oStream.write(name.getBytes());
						System.out.println("写入完成");
//						InputStream iStream = client.getInputStream();
//						BufferedReader reader = new BufferedReader(new InputStreamReader(iStream));
//						System.out.println(reader.readLine());
						String name2 = UUID.randomUUID().toString();
						oStream.write(name2.getBytes());
						oStream.close();
						client.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
			latch.countDown();
		}
	}
}
