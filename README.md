### 安装YCSB

主要更改的地方有：

1. core目录，/usr/local/YCSB/core
2. redis目录，/usr/local/YCSB/redis/src/main/java/com/yahoo/ycsb/db 其中更改RedisClient.java中96行和97行，可以更改将load/run文件写到哪个目录如/home/file

### 安装redis

### 更改负载

- $ cd /user/local/YCSB/workloads ** 该目录下有很多类型的负载，可以自行更改

### 编译命令

注意需要先开启redis服务： $ redis-server, 之后的命令将负载写入到redis中

1. 编译

- mvn -pl com.yahoo.ycsb:redis-binding -am clean package

1. load

- ./bin/ycsb run redis -P workloads/workloada -p redis.host=localhost -p redis.port=6379 > outputrun.txt
- 可以在/home/file目录下得到write.txt文件，执行以下命令
  - mv write.txt load.txt，得到load文件

1. run

- ./bin/ycsb run redis -P workloads/workload_test -p redis.host=localhost -p redis.port=6379 > outputrun.txt
- 可以在/home/file目录下得到write.txt文件，执行以下命令
  - mv write.txt run.txt，得到run文件

### 更改db_bench.cc文件

1. 解析write.txt/read.txt文件，获取操作，对leveldb进行读写
2. 主要分为如下几个部分：可以在db_bench.cc中搜索meggie,进行相应更改
