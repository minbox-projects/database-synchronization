## 数据库同步工具

> 基于pt-archiver命令行生成同步shell脚本文件，执行"SynchronizationShellBuildTest"单元测试类可以在`target`目录下生成"synchronization.sh"脚本文件，
> 将脚本复制到服务器上执行即可。

> 注意：该脚本文件执行之前需要在服务器上安装`pt-archiver`命令，执行之前授权可执行权限`chmod u+x synchronization.sh`。