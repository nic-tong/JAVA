# 利用CuratorLock 实现分布式锁


final InterProcessLock lock = new InterProcessMutex(client, lockPath);

跟踪到源码可以看出 curator 利用Zookeeper的临时有序节点特性，每个客户端都去注册一个临时有序节点，选取最小的节点获取锁，其他节点创建一个watcher 监听比自己小的节点删除动作， 一旦触发 即获取锁。