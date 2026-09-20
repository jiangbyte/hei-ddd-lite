# hei-ddd-lite

最基础的 **DDD 单体模板脚手架**：完整核心抽象 + 六层工程模型 + 账户体系最小竖切 + 可选 MinIO。  
坐标：`io.github.jiangbyte` / `io.github.jiangbyte.hei.*`。

**做齐：** 按限界上下文分包（`domain.user`）、仓储端口 `adapter.repository`、DAO 在 `infrastructure.dao`、依赖倒置、`I*Service` 契约与 Assembler；用户注册登录（PORTAL / ADMIN）。  
**刻意不做：** Event Sourcing、Saga、强制 CQRS 总线、细粒度 RBAC、Spring AI / 向量库 / MCP；不按 BC 拆独立 Maven 工程。

架构图源文件见 [`docs/diagrams/*.drawio`](docs/diagrams/)（可用 [diagrams.net](https://app.diagrams.net/) 打开编辑）。前端说明见 [`web/README.md`](web/README.md)。

---

## 1. 模块结构（六层工程模型）

```text
hei-ddd-lite/
├── hei-ddd-lite-types/             # 类型层：异常、错误码
├── hei-ddd-lite-api/               # 契约层：I*Service + Request/Response + R
├── hei-ddd-lite-trigger/           # 触发器层：HTTP / JWT / Assembler（可依赖 infra）
├── hei-ddd-lite-domain/            # 领域层：按 BC 分包 + application 用例（账户）
├── hei-ddd-lite-infrastructure/    # 基础设施：dao / adapter.repository / 事件 / 中间件
├── hei-ddd-lite-app/               # 启动层：根包入口、application.yml
├── web/                            # Vue3 pnpm monorepo（portal / admin / shared）
└── docs/                           # SQL、架构图
```

Maven 坐标统一为 `hei-ddd-lite-*`；Java 包为 `io.github.jiangbyte.hei.*`。

领域按限界上下文分包：

```text
domain.user.{adapter,model,factory,event,specification,service}
domain.core          # 通用底座（AggregateRoot / Repository / ValueObject …）
application          # 账户用例编排（ApplicationService）
```

---

## 2. 六层职责与依赖

| 层 | 模块 | 放什么 | 不放什么 |
|----|------|--------|----------|
| types | `hei-ddd-lite-types` | 业务异常、错误码 | 领域模型、HTTP |
| api | `hei-ddd-lite-api` | 对外契约接口、Request/Response、`R` | Controller、JWT、领域服务 |
| trigger | `hei-ddd-lite-trigger` | Controller（implements `I*Service`）、Assembler、JWT；admin 可直调 DAO | 领域规则 |
| domain | `hei-ddd-lite-domain` | BC 模型 / 端口 / 领域服务；账户 `application` 用例 | HTTP、SQL、对外 DTO |
| infrastructure | `hei-ddd-lite-infrastructure` | `dao` + `dao.po`、`adapter.repository`、事件、中间件配置 | 领域规则 |
| app | `hei-ddd-lite-app` | 根包启动类、`application.yml`、组件扫描 | 业务逻辑 |

依赖方向：

```text
hei-ddd-lite-app → hei-ddd-lite-trigger → hei-ddd-lite-api → hei-ddd-lite-types
                        ↘ hei-ddd-lite-domain ─────────────→ hei-ddd-lite-types
                        ↘ hei-ddd-lite-infrastructure
hei-ddd-lite-app → hei-ddd-lite-infrastructure → hei-ddd-lite-domain
```

`trigger` 依赖 `infrastructure`，便于后台 CRUD 直接使用 `I*Dao`。  
JWT / CORS 属于 **trigger**；MySQL / Redis 默认集成；MinIO 可选。

---

## 3. DDD 核心概念与包约定

| 抽象 | 包位置 | 扩展时怎么用 |
|------|--------|----------------|
| `Entity` / `ValueObject` / `AggregateRoot` | `domain.core` | 通用底座；BC 内实体放 `domain.<bc>.model.entity`，VO 放 `model.valobj` |
| `DomainEvent` | `domain.<bc>.event` | 不可变事实；参考 `UserCreatedEvent` |
| `DomainEventPublisher` | `domain.core` | 端口在 domain；infra 提供 `SpringDomainEventPublisher` |
| 仓储端口 | `domain.<bc>.adapter.repository.IXxxRepository` | 实现在 `infrastructure.adapter.repository` |
| 外部端口 | `domain.<bc>.adapter.port` | 如 `PasswordHasher`；实现在 infra |
| DAO / PO | `infrastructure.dao` / `dao.po` | `IXxxDao` + 表映射对象 |
| `Factory` / `Specification` / 领域服务 | `domain.<bc>.factory` 等 | 参考 `UserFactory`、`UserClientAccessPolicy` |
| 账户用例 | `application` | `*ApplicationService` + Command/Query |

包级约定见各模块 `package-info.java`。

---
## 4. 账户体系（最小示例竖切）

| 类型 | 说明 |
|------|------|
| `PORTAL` | 前台用户；公开注册创建；只能登录 Portal |
| `ADMIN` | 后台管理员；种子或后台创建；只能登录 Admin |

主要接口：

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/auth/register` | 注册 PORTAL 用户 |
| POST | `/auth/login` | 登录（body 需 `clientType`: `PORTAL` / `ADMIN`） |
| POST | `/auth/logout` | 登出（JWT 进 Redis 黑名单） |
| GET | `/auth/me` | 当前登录用户（需登录） |
| GET | `/users/public?userId=` | 公开资料（启用中的 PORTAL） |
| GET | `/admin/users` | 后台用户分页（需 ADMIN） |
| POST | `/admin/users` | 后台创建用户（需 ADMIN） |
| POST | `/admin/users/change-enabled` | 启用/禁用（需 ADMIN） |

建表脚本：[`docs/sql/schema-user.sql`](docs/sql/schema-user.sql)  
默认管理员：`admin` / `admin123`

---

## 5. 基于本脚手架开发

按限界上下文扩展。账户参考 `domain.user` + `application`：

1. **领域模型**（`domain.<bc>.model.entity` / `valobj`）：聚合继承 `AggregateRoot`，值对象实现 `ValueObject`。
2. **Factory / Event / Spec / 领域服务**（`domain.<bc>.factory` / `event` / `specification` / `service`）。
3. **仓储端口**（`domain.<bc>.adapter.repository.IXxxRepository`）。
4. **编排**：账户用 `application.*ApplicationService` + Command/Query。
5. **基础设施**：`infrastructure.dao.IXxxDao` + `dao.po`；仓储实现 `infrastructure.adapter.repository.XxxRepository`；事件监听参考 `UserDomainEventListener`。
6. **触发器**（`trigger.web`）：Controller + Assembler；契约在 `api`；后台管理可直调 `I*Dao`。
7. **启动**：业务组件放在 `io.github.jiangbyte.hei.*` 下即可被根包启动类扫描；可选中间件用 `@AutoConfiguration` + `AutoConfiguration.imports`，无需改 `scanBasePackages` 白名单。

### 领域事件约定

1. 聚合行为内 `registerEvent(...)`。
2. 应用服务 `repository.save(aggregate)`。
3. `aggregate.pullDomainEvents()` 取出并清空。
4. `domainEventPublisher.publish(events)`。
5. 基础设施 `@EventListener` 消费（参考 `UserDomainEventListener`）。

事务边界在**应用服务**；默认同步 Spring 事件发布，可替换为 Outbox / MQ。

### 何时用 DomainService / Factory / Specification

- **Factory**：构造复杂或必须保证不变式的聚合创建。
- **Specification**：多处复用的业务判定，避免散落 if。
- **DomainService**：规则不属于单一聚合（如端类型访问策略），且仍是纯领域逻辑；由 `application` 包 `@Bean` 注册。

---
## 6. 快速启动

### 前置

- **JDK 21**
- **MySQL**（库名 `hei`，默认账号见 `application.yml`）
- **Redis**（默认密码见 `application.yml`）
- 执行 [`docs/sql/schema-user.sql`](docs/sql/schema-user.sql)

本地依赖可参考：

```bash
chmod +x start-infra.sh && ./start-infra.sh
```

默认密码示例：`infra123!`（与 `hei-ddd-lite-app/src/main/resources/application.yml` 一致）。

### 后端

```bash
export JAVA_HOME=/home/charlie/Workspace/sdks/jdk-21
export PATH="$JAVA_HOME/bin:$PATH"
mvn install -DskipTests
mvn -pl hei-ddd-lite-app spring-boot:run
```

注册 / 登录示例：

```bash
curl -X POST http://localhost:8080/auth/register \
  -H 'Content-Type: application/json' \
  -d '{"username":"alice","password":"alice123"}'

TOKEN=$(curl -s -X POST http://localhost:8080/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"username":"admin","password":"admin123","clientType":"ADMIN"}' \
  | sed -n 's/.*"token":"\([^"]*\)".*/\1/p')

curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/auth/me
```

Knife4j：`http://localhost:8080/doc.html`

### 前端

```bash
cd web
pnpm install
pnpm dev:portal   # http://127.0.0.1:5173
pnpm dev:admin    # http://127.0.0.1:5174
```

详见 [`web/README.md`](web/README.md)。

---

## 7. 可选基础设施

| 能力 | 说明 |
|------|------|
| MySQL + Druid + MyBatis-Plus | 默认集成；用户仓储落库 |
| Redis | 默认集成；JWT 登出黑名单 |
| JWT | `trigger` 内置；`@RequireLogin` / `@RequireAdmin` |
| MinIO | 可选依赖 + `hei.ddd.minio.enabled` |

版本在父 POM `<properties>` 中用 `${xxx.version}` 统一管理。

---

## 8. 「不做」清单

- 不做 Event Sourcing / Saga / 强制 CQRS 总线
- 不做 ACL / 多限界上下文工程拆分 / 细粒度 RBAC
- 不做 Spring AI / Milvus / MCP / 向量检索

---

## 技术栈

- Java 21 / Spring Boot 4.1.x / Maven 多模块
- Lombok / Hutool / MapStruct / JJWT / Knife4j
- Druid / MyBatis-Plus / Redis / BCrypt（默认）；MinIO（可选）
- Vue 3 / Vite / TypeScript / Naive UI（`web/`：portal + admin）
