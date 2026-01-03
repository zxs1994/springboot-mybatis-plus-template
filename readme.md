# SpringBoot + MyBatis-Plus 模板项目 📦

## 简介 💡

**这是一个基于 Spring Boot + MyBatis-Plus 的轻量模板**，提供常见 CRUD、分页、统一响应封装、枚举工具、以及代码生成器示例。适合用作新项目起步或学习参考。

---

## 主要特性 ✅

- 基于 Spring Boot 3.x（Java 17）  
- MyBatis-Plus 集成（分页、自动填充）  
- 统一 API 响应封装（`ApiResponse`），可通过 `@NoApiWrap` 取消包装  
- 内置简单的代码生成器（`devtools/CodeGenerator` + Freemarker 模板）  
- Swagger / OpenAPI 文档（springdoc）  

---

## 技术栈 🔧

- Java 17  
- Spring Boot 3.5.5  
- MyBatis-Plus  
- MySQL（示例 SQL 在 `user.sql`）  
- Freemarker（代码生成模板位于 `src/main/resources/templates`）  
- OpenAPI (springdoc) for API 文档

---

## 目录结构（关键文件）

- `src/main/java/com/example/template`：项目主代码  
  - `controller/`：`UserController`, `EnumController`  
  - `config/`：`ApiResponseWrapper`, `MyBatisPlusConfig`, `MyMetaObjectHandler`  
  - `devtools/CodeGenerator.java`：代码生成器（Main 方法）  
  - `util/`：`EnumUtils`, `LoadProperties` 等工具  
- `src/main/resources/application-dev.properties`、`application-prod.properties`：环境配置  
- `user.sql`：建表与样例数据  
- `src/main/resources/templates/`：代码生成 Freemarker 模板（entity、controller）

---

## 快速开始 🚀

### 前置条件
- JDK 17  
- Maven  
- MySQL（或修改 `application-dev.properties` 为你的数据源）

### 克隆 & 构建
```bash
git clone <repo-url>
cd springboot-mybatis-plus-template
mvn clean package
```

### 运行
- 开发（使用 dev 配置）
```bash
mvn spring-boot:run
# 或者
java -jar target/template-1.0.0.jar
```

- 生产运行示例（带 JVM 时区参数，见 `deploy.sh`）：
```bash
# 最简单启动（示例）
java -Duser.timezone=Asia/Shanghai -jar target/template-1.0.0.jar --spring.profiles.active=prod

# 带示例 JVM 内存配置（可选）
java -Duser.timezone=Asia/Shanghai -Xms512m -Xmx1g -jar target/template-1.0.0.jar --spring.profiles.active=prod
```

### 配置
- 默认激活 profile：`application.properties` 中 `spring.profiles.active=dev`  
- 开发环境数据库配置：`src/main/resources/application-dev.properties`（示例已指向 `jdbc:mysql://127.0.0.1:3306/demo?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai&useSSL=false`）

**时区说明：**
- 请确保 MySQL 服务端的时区设置为东八区（`Asia/Shanghai`），可通过 `SET GLOBAL time_zone = "+08:00";` 或在 `my.cnf` 中设置 `default_time_zone = "+08:00"` 后重启数据库。  
- JDBC 连接字符串请包含 `serverTimezone=Asia/Shanghai`（如上例），以确保应用与数据库使用相同的时区，避免 `createdAt/updatedAt` 等时间字段出现偏移。

---

## 数据库 & 样例数据 🗄️

- 建表与样例数据在 `user.sql`，包含 `user` 表结构与三条样例记录。导入后即可直接测试 API。

---

## API 示例（重要端点） 🔎

- 列表：GET /user  
- 获取：GET /user/{id}  
- 新增：POST /user  （JSON body）  
- 更新：PUT /user   （JSON body）  
- 删除：DELETE /user/{id}  
- 分页：GET /user/page?page=1&size=10  
- 枚举：GET /enums/all  （返回项目中枚举的统一列表）

示例 curl（列出所有用户）：
```bash
curl -X GET http://localhost:8088/user
```

> 注意：所有正常响应默认会被 `ApiResponse` 包装；若要跳过包装，在 Controller 或方法上使用 `@NoApiWrap`。

---

## 代码生成器（快速生成实体/Mapper/Controller） 🛠️

- 运行：在 IDE 中以 `devtools.CodeGenerator` 的 `main` 方法运行  
- 配置读取：`src/main/resources/application-dev.properties`（从中读取数据源）  
- 模板：`src/main/resources/templates`（可自定义）

---

## 文档（Swagger / OpenAPI） 📚

- 启动后访问：`/swagger-ui.html` 或 `/swagger-ui/index.html`（springdoc 默认路径）  
- 原始 JSON：`/v3/api-docs`

---

## 开发注意事项 & 约定 ⚠️

- `BaseEntity` 使用 `OffsetDateTime` 存储 `createdAt` / `updatedAt`，已在 `MyMetaObjectHandler` 中使用 `OffsetDateTime.now(ZoneOffset.ofHours(8))` 以写入带 +08:00 的时间。  
- Jackson 已设置 `spring.jackson.time-zone=Asia/Shanghai` 与 `spring.jackson.serialization.write-dates-as-timestamps=false`，确保时间按 ISO 字符串序列化并按东八区解析（尽管 `OffsetDateTime` 自带偏移信息）。  
- Mapper 扫描配置基于 `project.base-package`（位于 `application.properties`）。  
- 推荐开启 Lombok 支持（项目已使用）。  

> 建议：仍可在 JVM 启动参数中设置 `-Duser.timezone=Asia/Shanghai` 以保障第三方库的一致性。

---

## 测试 & 扩展 💡

- 添加集成测试或单元测试（当前仓库无测试示例）  
- 可接入 Actuator（已经添加依赖）用于监控

---

## 贡献 & 联系 ❤️

欢迎提交 Issues / PR，或基于该模板进行定制化改造。

---

## 许可证
请在项目中补充 LICENSE（例如 MIT / Apache-2.0），根据你的需求选择合适的许可证。

---

*需要我把 README 翻译为英文或为 CI/CD、Docker 等添加示例吗？*