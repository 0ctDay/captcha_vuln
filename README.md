# 图形验证码靶场

## 目的
训练学生面对验证码的渗透思路

## build

##### 修改
1. 手动修改每个控制器中的密码
2. 手动修改flag
##### 启动
```docker
mvn clean package
docker-compose up -d
```

## 章节
### 1. 图形验证码失效的情况
用户提交成功或失败后的验证码均不刷新
### 2. 验证码的绑定关系失效
验证码如果不是和session绑定, 那么必然存在其他的绑定关系, 有时删除绑定关系, 可能会直接bypass
### 3. 正常验证码的暴力破解
使用工具或手写脚本识别验证码然后爆破密码