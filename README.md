# NTP
ntp client in android device
1. Make the Android device work as a NTP client, to achieve the NTP time from the NTP server
2. Need to set the NTP server IP first
3. Make sure the connection with the NTP server
4. The NTP server port and the NTP client port are 123 default, and they are set in the code already
5. NTP time checking interval is set 30min default
6. The local Time zone is set default as 27 which indicates "(GMT+08:00)" timezone

1	概述
    网络时间协议（Network Time Protocol， NTP）是用来在Internet上使不同的机器能维持相同时间的一种通讯协定。NTP是一个跨越广域网或局域网的复杂的同步时间协议，它通常可获得毫秒级的精度。时间服务器（time server）是利用NTP的一种服务器，通常让局域网上的若干台主机通过网络与NTP服务器同步时钟，使网络中的机器维持时间同步。
2	实现原理
2.1	环境
系统：Red Hat Enterprise Linux 6.0
2.2	安装
不管是服务器端，还是客户端都需要安装ntp的软件。
3	实现方法
为了简便使用，特定制了NTP Server和NTP Client的安装包。
3.1	NTP Server的配置
NTP Server安装包：ntp_server_install_v1.0.0.tgz
将NTP Server安装包拷贝至硬盘，在root用户的命令行提示符下输入命令如下：
[root@localhost ~]#tar xzf  ntp_server_install_v1.0.0.tgz
[root@localhost ~]#cd ntp_server_install
[root@localhost pxe_install]# ./install

第一行命令是执行安装包的解压操作
第二行命令是进入到安装包的目录
第三行命令是进行安装操作
执行如下命令将系统重新启动，系统重新启动后，则可以提供NTP服务。
[root@localhost ntp_server_install]# reboot
如果需要NTP Server与上一级的NTP Server校时，需要修改/etc/ntp.conf文件，添加一行，如下图所示，编辑完成后，保存退出，重新启动系统。
![image](https://github.com/hubinjisu/images/blob/master/images/image1.png)


卸载操作
如果不再让设备作为NTP Server，则执行卸载操作即可
[root@localhost ntp_server_install]# ./uninstall

【注意】NTP服务端软件与NTP客户端软件不能安装在同一台服务器上。
3.2	NTP Client配置
定制的NTP Client安装包：ntp_client_install_v1.0.0.tgz
将NTP Client安装包拷贝至硬盘，在root用户的命令行提示符下输入命令如下：
[root@localhost ~]#tar xzf  ntp_client_install_v1.0.0.tgz
[root@localhost ~]#cd ntp_client_install
[root@localhost ntp_client_install]# ./install

安装完毕后，需要使用setntp命令进行定时校时的配置
[root@localhost ~]#setntp 192.168.1.7
注意192.168.1.7是NTP Server的IP地址，设置成功后，则如下图所示。
![image](https://github.com/hubinjisu/images/blob/master/images/image2.png)

[root@localhost ~]#setntp 
注意：setntp命令后面没有参数时，则会输出已设置的NTP Server的IP地址，如下图所示
![image](https://github.com/hubinjisu/images/blob/master/images/image3.png)

[root@localhost ~]#setntp 0.0.0.0
注意：setntp 0.0.0.0 表示删除已有的NTP Server的IP地址。这样配置以后，该设备不再进行校时。如下图所示。
![image](https://github.com/hubinjisu/images/blob/master/images/image4.png)
【注意】客户端与服务端的时间不能相差太多，如相差太多，则无法完成时间同步。应使用date命令将时间调整后，再进行配置。
4	结束语
如果网络中设备时间差别太大，会产生很多的问题： 
1、系统时间旧于软件创建的时间，造成软件无法安装
2、系统时间不稳定，造成数据同步将出现问题
3、多台计划任务不能统一准时执行
4、对时间依赖较大的软件会出现混乱，
因此，设置时间服务器进行时间同步是是必不可少的。使所有的服务器时间保持一致，既有利我们管理，更有利于我们维护。

