# 🐠 咕噜表达式 (GuluExpression)

[![License](https://img.shields.io/badge/license-Apache%202-blue.svg)](LICENSE)

咕噜表达式 是一个轻量级、高性能的表达式解析与执行引擎。语法树清晰，公开，透明，可扩展性极高。支持复杂的布尔逻辑表达式、嵌套查询、环境变量引用等功能。
> 取名灵感来自于“就一句话能说清的事情，叽里咕噜半天说什么呢！”，咕噜表达式的核心理念，就是“一句话说清楚”

## 🎯 核心痛点：逻辑复用的工程困境 (The Engineering Dilemma)

在风控、CRM、自动化营销、数据资产管理等业务中，普遍存在的一套业务规则同时用于 **“检索 + 判定 + 看板 + ...”** 的混合场景，比如：

| 场景    | 详细流程                                    | 运行平台              | 实现类型                    |
|---------|-------------------------------------------|----------------------|----------------------------|
| 检索场景 | 用户从海量数据中筛选出符合规则的数据集           | Database/ES 查询平台   | 转换为 Database/ES Query   |
| 判定场景 | 对数据进行实时计算，满足规则则打标或染色         | Java 应用层            | Java 代码或则其他脚本实时判断 |
| 看板场景 | 离线筛选出需要关注的数据集，组合规则实现大数据聚合 | 大数据/ETL 平台        | 转换为 HiveSQL 等 ETL任务   |

在这类场景下，同一套规则在不同阶段被复用，且可能需要不同的实现形态（SQL、ES Query、HiveSQL、Java 代码实时判断，SpEL 表达式）。

### 传统方案和 Gulu 表达式对比

| 方案       | 维护方式                  | 维护成本                  |
|-----------|--------------------------|-------------------------|
| 传统方案   | 一种规则，多处同步实现       | 高：多平台多类型需分别维护   |
| Gulu DSL  | 单一规则源，自动生成多种实现  | 低：一次定义自动适配到处运行 |

### 对比成熟方案优势（Why not SpEL/OGNL/MVEL）

在 Java 生态中，SpEL (Spring Expression Language)、OGNL、MVEL 等已经是成熟的表达式引擎。为什么还要开发 GuluExpression？

SpEL/OGNL 是 “执行器 (Executor)”：它们的目标是让 Java 语言更动态，支持方法调用、对象实例化，功能极其强大。但本质上是黑盒，不对外暴露 AST 结构，开发者无法直接访问 AST 节点。

Gulu 表达式是 **“适配器 (Adapter)”**：目标是定义**统一的可移植的业务规则**。Gulu 表达式主动牺牲了语法的灵活性（不支持任意方法调用），仅保留纯粹的布尔逻辑与集合操作。正式这种克制，换取了 Gulu 表达式极高的架构透明性和跨端迁移能力，既能实时执行，也能支持用户自定义转换为 ES DSL 或 SQL。

## 工具特性

拒绝黑盒，语法树透明，暴露简单清晰的 AST。

轻松跨端迁移，自定义扩展，原生支持拜访者模式，支持。

零学习成本，语法设计贴近自然语言与 SQL 习惯，规则清晰可读。

## 📦 快速开始

### Maven 引入

```xml
<dependency>
    <groupId>priv.dawn.lab</groupId>
    <artifactId>gulu-dsl-core</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

### 示例代码

编写简单的表达式：
```java
public class Example {

  public static void main(String[] args) {
    GuluExpression expression = GuluExpressions.parser("name == 'dawn'");
    User user = new User();
    user.setName("dawn");
    GuluContext context = new GuluReflectionContext(user);
    expression.evaluate(context); // assert true
  }

}
```

编写多个表达式并引用
```java
public class Example {
    public static void main(String[] args) {
      User user = new User();
      user.setName("dawn");
      user.setAge(12);
      GuluContext context = new GuluReflectionContext(user);
      GuluExpressions.parserAndRegister("name == 'dawn'","exp_1",context);
      GuluExpressions.parserAndRegister("age == 12","exp_2",context);
      GuluExpression expression = GuluExpressions.parser("#{exp_1} and #{exp_2}");
      expression.evaluate(context); // assert true
    }
}
```

将表达式转义成 ES 查询语句，需要依赖 gulu-dsl-extension 这个 maven 包
```java
public class Example {
  public static void main(String[] args) {
    GuluExpression expression = GuluExpressions.parser("followers[userId == 123 and userRoles[groupId == 'rd_default' or authId[1002,1003,1004]]]");
    GuluReflectionContext context = new GuluReflectionContext(null); // 不需要实体 Object，传入 null 
    QueryBuilder accept = expression.getAstRootNode().accept(new EsQueryTransformerVisitor(context));
  }
}
```
转义成的 ES 查询如下：
```json
{
  "nested" : {
    "path" : "followers",
    "query" : {
      "bool" : {
        "must" : [
          {
            "term" : {
              "followers.userId" : {
                "value" : 123,
                "boost" : 1.0
              }
            }
          },
          {
            "nested" : {
              "path" : "followers.userRoles",
              "query" : {
                "bool" : {
                  "should" : [
                    {
                      "term" : {
                        "followers.userRoles.groupId" : {
                          "value" : "rd_default"
                        }
                      }
                    },
                    {
                      "terms" : {
                        "followers.userRoles.authId" : [
                          1002,
                          1003,
                          1004
                        ]
                      }
                    }
                  ]
                }
              }
            }
          }
        ]
      }
    }
  }
}
```

### DSL 语法介绍

Gulu DSL 是一种类似于 Elasticsearch 查询语法的领域特定语言，专门用于对对象属性进行布尔逻辑判断。以下是详细的语法说明：

#### 基础比较操作

支持标准的关系比较运算符：

| 运算符 | 说明 | 示例 |
|--------|------|------|
| `==` | 等于 | `age == 18` |
| `!=` | 不等于 | `status != "inactive"` |
| `>` | 大于 | `score > 95.5` |
| `<` | 小于 | `price < 100` |
| `>=` | 大于等于 | `level >= 5` |
| `<=` | 小于等于 | `rank <= 10` |

#### 逻辑运算符

支持三种逻辑运算符，均可混合使用：

- **AND**：逻辑与操作（大小写不敏感）
- **OR**：逻辑或操作（大小写不敏感）  
- **NOT/!**：逻辑非操作（NOT 不区分大小写，! 为简写）

```dsl
# 基本逻辑组合
age >= 18 AND score > 90
is_vip == TRUE OR status == "active"

# 复杂嵌套逻辑
NOT (age < 18 AND score < 60)
!(is_deleted == true)

# 多层组合
(age == 18 AND score > 90) OR (is_vip == TRUE AND status == "active")
```

#### 集合操作

提供两种集合检查方式：

**IN 操作符**：检查值是否存在于指定列表中，类似 terms
```dsl
role IN (1,2,3,4)
tag IN ('a','b','c')
status IN ("active","inactive")
type IN (TRUE,FALSE)
```

**[] 操作符**：用于数组/集合的包含检查，要求包含任意值即可，类似 ES 查询中的 terms
```dsl
permissions ["read","write"]
scores [90.5,85.0f,78]
flags [TRUE,FALSE]
```

#### 特殊功能函数

**EXIST 函数**：检查对象属性是否存在
```dsl
EXIST (user.name)
exist (order.payment_time)
```

**嵌套查询**：对复杂对象进行深层查询，类似 ES 查询中的 nested 查询
```dsl
users[age > 18 AND active == TRUE]
orders[status == "paid" OR amount > 1000]
products[category IN ("electronics","books") AND price < 500]
```

#### 变量引用机制

**环境变量引用**：使用 `#` 前缀引用上下文中的环境变量
```dsl
#yesterday >= update_time
```

**表达式引用**：使用 `#{}` 语法引用已注册的表达式
```dsl
#{user_validation_rule} AND #{order_processing_check}
#{security_level_high} OR !#{blacklisted_user}
```

#### 数据类型支持

支持多种数据类型：

- **数值类型**：整数、浮点数、长整型（支持 f/F、d/D、l/L 后缀）
- **字符串类型**：使用单引号或双引号包围，支持转义
- **布尔类型**：TRUE/true 或 FALSE/false（大小写不敏感）
- **路径标识符**：支持多级属性访问，用点号分隔

```dsl
# 数值示例
age == 25
price > 99.99f
timestamp >= 1672502400L
ratio < 0.85d

# 字符串示例
name == '张三'
category == "电子产品"
description != "已删除"

# 布尔示例
is_active == TRUE
is_deleted == false
enabled != FALSE

# 路径示例
user.profile.name == 'admin'
order.payment.details.amount > 1000
product.category.sub_type IN ("手机","平板")
```

#### 重要语法规则

1. **关键字大小写**：AND、OR、NOT、EXIST、IN、TRUE、FALSE 等关键字不区分大小写
2. **运算优先级**：括号 > NOT/! > AND > OR
3. **路径分隔**：使用点号(.)分隔对象层级，如 `user.profile.email`
4. **字符串转义**：在字符串中使用 `\` 转义特殊字符
5. **空白处理**：自动忽略多余的空白字符和换行符

通过这些语法规则，Gulu DSL 能够表达从简单条件判断到复杂业务逻辑的各种场景。

---

## 🧱 架构设计

咕噜表达式的架构主要分为以下几个模块：

### 核心模块 (`gulu-dsl-core`)
- **词法分析器 (GuluLexer)**：负责将输入字符串转换为 Token 流。
- **语法分析器 (GuluParser)**：构建抽象语法树（AST）。
- **节点访问者模式 (GuluNodeVisitor)**：支持对 AST 节点的遍历和处理。
- **上下文管理 (GuluContext)**：提供变量查找、环境变量支持等功能。

### 扩展模块 (`gulu-dsl-extension`)
- **Elasticsearch 查询转换器 (EsQueryTransformerVisitor)**：将表达式转换为 Elasticsearch 查询语句。
- **描述生成器 (DescriptionVisitor)**：将 AST 结构可视化输出。

---

## 🔧 开发指南

### 编译项目

```bash
mvn clean install
```

### 运行测试

```bash
mvn test
```

### 添加新功能

1. 在 GuluNodeVisitor 中添加新的访问方法。
2. 实现对应的 AST 节点类（继承 GuluAstNode）。
3. 更新 GuluParser 和 GuluLexer 以支持新语法。
4. 编写单元测试验证功能正确性。

---

## 🤝 贡献指南

欢迎任何形式的贡献！你可以通过以下方式参与项目开发：

1. 提交 Issue 报告 Bug 或建议新功能。
2. Fork 仓库并提交 Pull Request。
3. 编写文档或改进现有内容。

---

## 📄 许可证

本项目采用 Apache License 2.0 协议开源，详情见 [LICENSE](LICENSE) 文件。

---

## 👥 团队成员

- **Dawn Yang** - 初始作者兼维护者

---

## 💬 社区交流

如有疑问或建议，欢迎讨论区交流
---

## 🌟 Star History

[![Star History Chart](https://api.star-history.com/svg?repos=DAWN0ER/gulu-dsl&type=date&legend=top-left)](https://www.star-history.com/#DAWN0ER/gulu-dsl&type=date&legend=top-left)