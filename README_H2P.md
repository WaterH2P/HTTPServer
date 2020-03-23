## 发送 POST 请求

1. 在 httpserver/src/main/java（该路径针对本目录） 下创建文件夹 Controller。

2. 在 Controller 创建 Class，添加 @Controller 注解。

3. 有 @Controller 注解的类的处理 POST 请求的方法需要添加 @RequestMapping 注解，并指定 value。
    - @RequestMapping(value = "/test")
    
4. POST 请求 response 返回还不完善，需要手动完成。

5. 如果本框架作为 jar 包导入，Controller 需要创建在项目运行时的根目录（具体我也不太清楚）。