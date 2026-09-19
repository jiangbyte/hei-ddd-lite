# hei-ddd-lite

最基础的 **DDD 单体模板脚手架**：完整核心抽象 + 常见六层工程模型 + 账户体系最小竖切 + 可选 MinIO。  
坐标：`io.github.jiangbyte` / `io.github.jiangbyte.hei.*`。

**做齐：** Entity、ValueObject、AggregateRoot（含事件登记）、DomainEvent、DomainEventPublisher、Repository、DomainService、Factory、Specification、用例编排（Command / Query）、分层依赖倒置、`I*Service` 契约与 Assembler；用户注册登录（PORTAL / ADMIN）。  
**刻意不做：** Event Sourcing、Saga、强制 CQRS 总线、ACL、多限界上下文拆分、细粒度 RBAC、Spring AI / 向量库 / MCP。

架构图源文件见 [`docs/diagrams/*.drawio`](docs/diagrams/)（可用 [diagrams.net](https://app.diagrams.net/) 打开编辑）；README 嵌入对应 SVG。

---

## 1. 模块结构（常见六层工程模型）

```text
hei-ddd-lite/
├── hei-ddd-lite-types/             # 类型层：跨层异常、错误码
├── hei-ddd-lite-api/               # 契约层：I*Service + Request/Response + R
├── hei-ddd-lite-trigger/           # 触发器层：HTTP / JWT / Assembler
├── hei-ddd-lite-domain/            # 领域层：领域模型 + 用例编排（application 包）
├── hei-ddd-lite-infrastructure/    # 基础设施层：仓储实现 / 事件 / MySQL·Redis 等
├── hei-ddd-lite-app/               # 应用启动层：入口与 application.yml
├── web/                            # Vue3 pnpm monorepo（portal / admin / shared）
└── docs/                           # SQL、架构图
```

Maven 坐标统一为 `hei-ddd-lite-*`；Java 包为 `io.github.jiangbyte.hei.*`。  
用例编排以 `application` 包放在 domain 模块内。前端说明见 [`web/README.md`](web/README.md)。

---

## 2. 六层职责与依赖

![分层架构](docs/diagrams/01-layered-architecture.svg)

| 层 | 模块 | 放什么 | 不放什么 |
|----|------|--------|----------|
| types | `hei-ddd-lite-types` | 业务异常、错误码 | 领域模型、HTTP |
| api | `hei-ddd-lite-api` | 对外契约接口、Request/Response、`R` | Controller、JWT、领域服务 |
| trigger | `hei-ddd-lite-trigger` | Controller（implements `I*Service`）、Assembler、JWT | 业务规则、持久化、契约 DTO 定义 |
| domain | `hei-ddd-lite-domain` | 聚合/VO/事件/规约/领域服务 + 用例编排（Command/Query/事务） | HTTP、SQL、对外 DTO |
| infrastructure | `hei-ddd-lite-infrastructure` | 仓储实现、事件发布、Druid / MyBatis-Plus / Redis 等 | 领域规则、trigger/api |
| app | `hei-ddd-lite-app` | 启动类、`application.yml`、组件扫描 | 业务逻辑 |

依赖方向（**不可反向**）：

```text
hei-ddd-lite-app → hei-ddd-lite-trigger → hei-ddd-lite-api → hei-ddd-lite-types
                        ↘ hei-ddd-lite-domain ─────────────→ hei-ddd-lite-types
hei-ddd-lite-app → hei-ddd-lite-infrastructure → hei-ddd-lite-domain
```

JWT / CORS 属于 **trigger**；MySQL / Redis 默认集成；MinIO 可选。

---

## 3. DDD 核心概念

![领域概念](docs/diagrams/02-domain-concepts.svg)

| 抽象 | 包位置 | 扩展时怎么用 |
|------|--------|----------------|
| `Entity` | `domain.core` | 有标识、可变；按 ID 相等 |
| `ValueObject` | `domain.core` | 不可变、按值相等；参考 `Username` |
| `AggregateRoot` | `domain.core` | 继承它；行为内 `registerEvent` |
| `DomainEvent` | `domain.core` | 不可变事实；参考 `UserCreatedEvent` |
| `DomainEventPublisher` | `domain.core` | 领域端口；infra 提供 `SpringDomainEventPublisher` |
| `Repository` | `domain.core` | 以聚合为粒度；端口在 domain，实现在 infra |
| `Factory` | `domain.core` | 创建合法聚合；参考 `UserFactory` |
| `Specification` | `domain.core` | 可复用判定；参考 `UsernameFormatSpecification` |
| `DomainService` | `domain.core` / `domain.service` | 跨聚合无状态规则；参考 `UserClientAccessPolicy` |
| `ApplicationService` | `application.core` | 编排用例 + `@Transactional`（位于 domain 模块） |
| `Command` / `Query` | `application.core` | 写/读用例入参 |

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

![用例扩展](docs/diagrams/03-usecase-extension.svg)

按**用户管理**竖切复制扩展即可：

1. **领域模型**（`domain.model`）：新建聚合继承 `AggregateRoot`，值对象实现 `ValueObject`（参考 `Username`）。
2. **Factory / Event / Spec / DomainService**：参考 `UserFactory`、`UserCreatedEvent`、`UsernameFormatSpecification`、`UserClientAccessPolicy`。
3. **Repository 端口**（`domain.repository`）：`XxxRepository extends Repository<Xxx, ID>`。
4. **用例编排**（domain 模块 `application` 包）：Command/Query + ApplicationService；无 Spring 的领域服务用 `@Bean`（参考 `UserDomainConfiguration`）。
5. **基础设施**（`infrastructure.persistence` / `event` / `auth`）：仓储、事件监听、`PasswordHasher` / `TokenDenylist` 实现。
6. **契约 + 触发器**：`api` 定义 `I*Service`；`trigger` Controller implements + Assembler。
7. **启动**：新增需扫描的 infra 包时，更新 `HeiDddLiteApplication` 的 `scanBasePackages`。

### 领域事件约定

![事件链路](docs/diagrams/04-domain-event-flow.svg)

1. 聚合行为内 `registerEvent(...)`。
2. 应用服务 `repository.save(aggregate)`。
3. `aggregate.pullDomainEvents()` 取出并清空。
4. `domainEventPublisher.publish(events)`。
5. 基础设施 `@EventListener` 消费（参考 `UserDomainEventListener`）。

事务边界在**应用服务**；默认同步 Spring 事件发布，可替换为 Outbox / MQ。

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
