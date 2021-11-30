*https://github.com/brianfrankcooper/YCSB/blob/master/redis/README.md*

- 首先按照官方给的提示

```shell
git clone http://github.com/brianfrankcooper/YCSB.git
cd YCSB
mvn -pl site.ycsb:redis-binding -am clean package
```

在*mvn*编译之前需要对一些文件进行修改，从而导出数据。

- 更改文件，需要移植鑫宇师兄给的压缩包中的一些文件。如图所示，主要就是WorkloadWriteFile.java和VariableLength.java这两个文件

  <img src="https://gitee.com/hustdsy/blog-img/raw/master/image-20211129214248338.png" alt="image-20211129214248338" style="zoom:50%;" />

- 之后修改redis目录，/Users/dongshenyu/Applications/YCSB/redis/src/main/java/com/yahoo/ycsb/db 其中更改RedisClient.java中96行和97行，可以更改将load/run文件写到哪个目录如/home/file.在0.17.0版本对应的是/Users/dongshenyu/Applications/YCSB/redis/src/main/java/site/ycsb/db中的RedisClient.java文件。

  <img src="https://gitee.com/hustdsy/blog-img/raw/master/image-20211129215158633.png" alt="image-20211129215158633" style="zoom:50%;" />

- 之后运行命令跑数据，这里我将数据写到了桌面上。修改的位置话，修改RedisClient.java文件中对应的位置，再mvn -pl ...命令编译一遍即可。

```shell
#跑命令之前记得开启redis服务
./bin/ycsb load redis -s -P workloads/workloada -p "redis.host=127.0.0.1" -p "redis.port=6379" > outputLoad.txt
./bin/ycsb run redis -s -P workloads/workloada -p "redis.host=127.0.0.1" -p "redis.port=6379" > outputLoad.txt
```

![image-20211129214918448](https://gitee.com/hustdsy/blog-img/raw/master/image-20211129214918448.png)
