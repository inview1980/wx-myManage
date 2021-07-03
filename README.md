# 家庭管理威wx-myManage
--------
#### 这是“家庭管理威”小程序App的后台服务端

##### 1. 拦截器：[TokenFilter.class](file:///src/main/java/com/example/mymanage/filter/TokenFilter.java)
##### 2. 使用DES/ECB/PKCS5Padding对称加、解密 
##### 3. 使用POI自定义读写excel文件类，ExcelExportUtil.class 
```java
val excel = new ExcelExportUtil();
excel.add(lst); 
byte[] bytes=excel.build().write(); 
```
##### 4. 自定义随机字符串类RandomUtil.class 
##### 5. springboot异步定时任务类TimedTask.class
##### 6. 自定义http返回参数的规范类Result.class和HttpResultEnum.class
##### 7. http发送文件工具HttpUtil.class
##### 8. lombok工具使用@RequiredArgsConstructor+private final IPayPropertyDB iPayPropertyDB可以自动导入Bean

