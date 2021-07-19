# 家庭管理威wx-myManage
--------
#### 这是“家庭管理威”小程序App的后台服务端

##### 1. 拦截器：TokenFilter.class
##### 2. 使用DES/ECB/PKCS5Padding对称加、解密EncryptUtil.class
##### 3. 使用POI自定义读写excel文件类，ExcelExportUtil.class 
```
val excel = new ExcelExportUtil();
excel.add(lst); 
byte[] bytes=excel.build().write(); 
```
##### 4. 自定义随机字符串类RandomUtil.class 
##### 5. springboot异步定时任务类TimedTask.class
##### 6. 自定义http返回参数的规范类Result.class和HttpResultEnum.class
##### 7. http发送文件工具HttpUtil.class
##### 8. lombok工具使用@RequiredArgsConstructor + private final IPayPropertyDB iPayPropertyDB可以自动导入Bean
##### 9. java.nio.file.Files中有大量关于文件、inputStream、outputStream的工具，如：
```
+ 文件复制
+ 流到文件
+ 文件到流
+ 输入流到输出流
+ 输出流到输入流
```

