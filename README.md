# JaLikeJDBC - 一个用Java包装的JDBC包装器

##概要
JaLikeJDBC思想来源于ScaLikeJDBC,内部对`java.sql.PreparedStatement`和`java.sql.ResultSet`进行包装，只需要写很少的代码便可完成SQL操作。



## Setup

## DB 对象

`jalikejdbc.DB` 从这个开始

`DB`它管理数据库连接，还提供活动的会话和事务操作。

### Connection Management

目前使用DriverManger进行数据库连接

#### DriverManager

例如使用 `java.sq.DriverManager` 进行连接.

```java
Class.forName(driverName);
Connection conn = DriverManager.getConnection(url, username, password);
DB db = new DB(conn);
```


## Operations
所有API操作 可以使用对象作为参数,也可以使用Java 8的Lambda表达式进行操作.

### Query API

JaLikeJDBC有下面几种查询方式

`asOne`, `asList` and `foreach`. 

他们最终使用 `java.sql.PreparedStatement#executeQuery()`进行db操作.


#### 假设db有下面数据(对应表tag,对象Tag)
```$xslt
+------------------+--------------+---------------------+
| id               | tag_name     | created_time        |
+------------------+--------------+---------------------+
| 1561910658515056 | C++          | 2021-07-01 00:04:18 |
| 1561910658515066 | Scala        | 2020-07-01 00:57:01 |
| 1561910658528091 | 算法         | 2019-07-01 00:04:18 |
| 1561910658552046 | 刷题         | 2019-07-01 00:04:18 |
| 1561913712258076 | 二分         | 2019-07-01 00:55:12 |
| 1561913712282076 | 遍历         | 2019-07-01 00:55:12 |
| 1561913821636049 | 树           | 2019-07-01 00:57:01 |
| 1561913821677061 | 二叉树       | 2019-07-01 00:57:01 |
| 1561914017459047 | springboot   | 2019-07-01 01:00:17 |
| 1561914017491098 | 技术         | 2019-07-01 01:00:17 |
| 1561914017524099 | Java         | 2019-07-01 01:00:17 |
| 1570025913174092 | 面试         | 2019-10-02 22:18:33 |
| 1570025913186047 | HR           | 2019-10-02 22:18:33 |
| 1570026120886013 | 动态规划     | 2019-10-02 22:22:00 |
| 1570026826927064 | postman      | 2019-10-02 22:33:46 |
| 1570027246674082 | 情商         | 2019-10-02 22:40:46 |
| 1570200369910086 | spark        | 2019-10-04 22:46:09 |
| 1570200665990090 | 心理         | 2019-10-04 22:51:05 |
| 1570283218111074 | 经济         | 2019-10-05 21:46:58 |
+------------------+--------------+---------------------+

```

#### 使用 asOne 查询 API

`asOne` 返回单个数据.

查询`id=1561914017524099`的数据
```java
Object [] params = {new Long("1561914017524099")};
//查询TagName
Optional<String> tagName = db.readOnly(session -> 
    session.asOne("select * from tag where id=?", params, rs -> rs.getString("tag_name")));

if (tagName.isPresent()) {
    System.out.println(tag1.get()); //输出tagName
}


//定义实体类
class Tag(id: Long, tagName: String, createdTime: String)

方式1:
Optional<Tag> tag = db.readOnly(session -> 
    session.asOne("select * from tag where id=?", params, rs -> 
        new Tag(rs.getLong("id"), rs.getString("tag_name"), rs.getString("created_time"))));

方式2:
Optional<Tag> tag = db.readOnly(session -> 
    session.asOne("select * from tag where id=?", params, Tag.class));
       

if (tag.isPresent()) {
    System.out.println(tag.get().toString()); //输出tag实体信息
}

```
#### asList 查询API

`asList` 返回的是 `java.util.List`.

```java
//查找所有
方式1:
List<Tag> tags = db.readOnly(session -> 
    session.asList("select * from tag",null, rs -> 
        new Tag(rs.getLong("id"), rs.getString("tag_name"), rs.getString("created_time"))));

方式2:
List<Tag> tags = db.readOnly(session -> 
    session.asList("select * from tag",null, Tag.class));

输出: 
tags.stream().forEach(x -> System.out.println(x.toString()));//输出


//模糊查询（如果查询的结果是对象，也可以使用Tag.class作为参数）
Object [] params = {"%2019%"};
List<Tag> tagLikes = db.readOnly(session -> 
    session.asList("select * from tag where created_time like ?",params, rs -> 
        new Tag(rs.getLong("id"), rs.getString("tag_name"), rs.getString("created_time"))));

tagLikes.stream().forEach(x -> System.out.println(x.toString()));
```
### Update API 
下面的update API操作方式不支持事物操作
```java
Object [] params = {"JaikeJDBC",new Long("1561910658515066")};
db.autoCommit(session -> 
    session.update("update tag set tag_name=? where id=?", params));
```


#### asIterator

`asIterator` 返回一个Iterator,你可以使用`java.util.Iterator`进行操作

```java
Iterator<Tag> iter = db.readOnly(session ->
     session.asIterator("select * from tag", null, rs ->
           ew Tag(rs.getLong("id"), rs.getString("tag_name"), rs.getString("created_time"))));

while (iter.hasNext()) { //hashNext
    System.out.println(iter.next().toString());
}
```

#### foreach

`foreach` 你可以在lambda表达式中对数据进行操作,这个API没有返回值.

```java
db.readOnly(dbs ->
    dbs.foreach("select * from tag",null,rs -> {
        System.out.println(new Tag(rs.getLong("id"), rs.getString("tag_name"), rs.getString("created_time")).toString());
}));
```


## Transaction
### Update API and Transaction

你可使用下面withinTx和localTx两种方式进行事物操作

```java
Object [] params = {......}
db.begin()
db withinTx {dbs -> dbs.update("insert into tag (id, tag_name,created_time) values (?, ?, ?)", params) }
db withinTx {dbs -> dbs.update("update tag set tag_name = ? where id = ?", params) }
db withinTx {dbs -> dbs.update("delete tag where id = ?", params) }
db.commit() 或者 db.rollback()
```

```java
Object [] params = {......}
db.localTx(
  // --- transcation start ---
  session -> {
    session.update("update tag set tag_name = ? where id = ?", params);
    session.update("update tag set tag_name = ? where id = ?", params);
    return null;
  } 
  // --- transaction end ---
)
```
注意： localTx发生意外会报异常，自动回滚。


### readOnly block / session

执行只读模式查询

```java
db readOnly (
  session -> session.asList("select * from tag",rs -> rs.getString("name"));
)

val session = db.readOnlySession()
String names = session.asList("select * from tag",rs -> rs.getString("name"));
```