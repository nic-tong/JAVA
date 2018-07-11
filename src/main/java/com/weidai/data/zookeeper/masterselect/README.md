# 利用zkClient 实现Master 选举

## 定义一个ClientData 
 node节点 包含id和name
 
## 定义一个master选举器操作
主要流程
1. 每个服务端启动一个master竞选器，监听master节点的删除操作
2. master节点是一个临时节点，每个client 尝试注册创建mater节点，其中只有一个能够注册成功。
3. 注册成功后，模拟5s后删除节点制造异常操作