package com.weidai.data.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;


/**
 * @描述
 * @author nic 
 * @time：2018年8月15日 下午2:52:35
 */
public class NIOServer {

	private int port = 8080;
	private InetSocketAddress address;
	
	private Selector selector;
	
	private NIOServer(int port) {
		this.port = port;
		this.address = new InetSocketAddress(port);
		try {
		// 首先创建socketChannel
			ServerSocketChannel server = ServerSocketChannel.open();
			server.bind(address);
			// 设定为非阻塞，默认为阻塞
			server.configureBlocking(false);
			
			// 开启select选择器
			selector = Selector.open();
			
			server.register(selector, SelectionKey.OP_ACCEPT);
			System.out.println("NIO服务已开启.... 端口号：8080");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void listen() {
		// select 开始轮训  接收请求
		while(true) {
			try {
				// 查看有多少服务在等在操作
				int wait = this.selector.select();
				if(wait == 0) continue;
				
				Set<SelectionKey> keys = this.selector.selectedKeys();
				// 开始处理各个注册的channel
				Iterator<SelectionKey> iterator = keys.iterator();
				while(iterator.hasNext()) {
					SelectionKey key = iterator.next();
					processSelectKey(key);
					iterator.remove();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void processSelectKey(SelectionKey key) throws IOException {
		// 每一个key 创建一个缓冲区
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		
		if (key.isAcceptable()) {
			ServerSocketChannel server = (ServerSocketChannel) key.channel();
			// 拿出client连接的客户端，开始处理并标记为可读
			SocketChannel client = server.accept();
			client.configureBlocking(false);
			client.register(selector, SelectionKey.OP_READ);
		} else if (key.isReadable()) {
			
			SocketChannel client = (SocketChannel) key.channel();
			int len  = client.read(buffer);
			if (len > 0) {
				buffer.flip();
				String content = new String(buffer.array(), 0, len);
				System.out.println(content);
				client.configureBlocking(false);
				client.register(selector, SelectionKey.OP_WRITE);
			}
			buffer.clear();
		} else if (key.isWritable()) {
			SocketChannel client = (SocketChannel) key.channel();
			buffer.wrap("hello world".getBytes());
			client.write(buffer);
			client.close();
		}
	}


	public static void main(String[] args) {
		
		new NIOServer(8080).listen();
	}
}
