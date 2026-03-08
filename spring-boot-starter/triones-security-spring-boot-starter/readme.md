# Triones Security Spring Boot Starter

`triones-security-spring-boot-starter` 是一个基于 Spring Security 封装的 Spring Boot Starter，旨在简化安全认证和鉴权的配置。它提供了灵活的 Token 管理（支持 JWT）、请求授权配置以及多种认证方式的支持。

## 特性

- **简化配置**：通过配置文件即可完成大部分安全设置。
- **多种认证方式**：支持 JWT, API Key, Bearer Token 等认证类型。
- **Token 管理**：内置 JWT 支持，可配置过期时间、刷新机制等。
- **灵活的鉴权**：支持基于路径的细粒度权限控制（permitAll, authenticated, hasRole 等）。
- **忽略路径**：轻松配置不需要认证的路径。

## 快速开始

### 1. 引入依赖

在你的 `pom.xml` 中添加以下依赖：

```xml
<dependency>
    <groupId>com.trionesdev.springboot</groupId>
    <artifactId>triones-security-spring-boot-starter</artifactId>
    <version>3.0.1-SNAPSHOT</version>
</dependency>
```

### 2. 配置参数

在 `application.yml` 或 `application.properties` 中配置安全策略。

#### 基础配置示例

```yaml
triones:
  security:
    # Token 所在的 Header key，默认为 Authorization
    header-key: Authorization
    # Token 所在的查询参数 key，默认为 token
    query-param-key: token
    # 认证类型: jwt (默认), apiKey, bearerToken
    auth-type: jwt
    # Token 类型: jwt (默认)
    token-type: jwt
    # Token 风格: uuid (默认)
    token-style: uuid
    # 是否开启刷新 Token
    enable-refresh: false
    # JWT 密钥
    secret: your_secret_key_here
    # Token 过期时间（秒），默认 86400 (1天)
    expires: 86400
    # 刷新 Token 过期时间（秒），默认 2592000 (30天)
    refresh-expires: 2592000
    # 全局忽略的路径（不需要认证）
    ignore-matchers:
      - /public/**
      - /login
```

#### 权限控制配置示例

你可以通过 `authorize-request` 配置具体的请求权限：

```yaml
triones:
  security:
    authorize-request:
      # 默认的请求授权类型，默认为 authenticated (需要认证)
      authorize-type: authenticated
      # 自定义请求匹配规则
      request-matchers:
        # 允许所有访问
        - patterns: 
            - /api/public/**
            - /register
          authorize-type: permitAll
        
        # 仅允许特定角色访问
        - patterns:
            - /api/admin/**
          authorize-type: hasRole
          role: 
            - ADMIN
            
        # 仅允许匿名访问
        - patterns:
            - /api/anonymous/**
          authorize-type: anonymous
```

## 配置项说明

| 配置项 | 说明 | 默认值 |
| --- | --- | --- |
| `triones.security.header-key` | 获取 Token 的 HTTP Header 名称 | `Authorization` |
| `triones.security.query-param-key` | 获取 Token 的 URL 参数名称 | `token` |
| `triones.security.auth-type` | 认证方式 (`jwt`, `apiKey`, `bearerToken`) | `jwt` |
| `triones.security.token-type` | Token 类型 | `jwt` |
| `triones.security.token-style` | Token 生成风格 | `uuid` |
| `triones.security.enable-refresh` | 是否启用 Token 刷新 | `false` |
| `triones.security.secret` | JWT 签名密钥 | `trionesdev_secret` |
| `triones.security.expires` | Token 有效期(秒) | `86400` |
| `triones.security.refresh-expires` | Refresh Token 有效期(秒) | `2592000` |
| `triones.security.ignore-matchers` | 忽略认证的路径列表 | `[]` |
| `triones.security.authorize-request.authorize-type` | 默认请求授权策略 | `authenticated` |
| `triones.security.authorize-request.request-matchers` | 详细的请求授权规则列表 | `[]` |

### AuthorizeType 支持类型

- `permitAll`: 允许所有请求
- `denyAll`: 拒绝所有请求
- `authenticated`: 需要认证
- `anonymous`: 仅允许匿名访问
- `hasVariable`: 需要特定变量（需结合代码逻辑）
- `hasRole`: 需要特定角色
- `hasAnyRole`: 需要任一角色
- `hasAuthority`: 需要特定权限
- `hasAnyAuthority`: 需要任一权限
