/**
 * <p>
 * jpa存储库相关的函数，定义了数据库查询方法。
 * </p><p>
 * 这些函数定义于接口内，不需要函数体。spring框架会根据其函数定义来生成访问数据库的代码。
 * 如果某个字段没有定义，spring会根据该字段的定义，自动创建对应的数据库列，
 * 但是如果某个数据库表没有创建，spring不会创建数据库表。
 * {@link https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-2.5-Release-Notes#sql-script-datasource-initialization}
 * </p><p>
 * 如果某个函数定义上方有{@link org.springframework.data.jpa.repository.Query Query}注解，
 * 则该函数将执行注解定义的sql语句。
 * </p><p>
 * 否则，该函数将根据spring的DSL（领域特定语言）语法，执行数据库查询
 * </p><p>
 * {@link org.springframework.data.jpa.repository.JpaRepository JpaRepository}
 *  封装了基本CRUD，
 * {@link org.springframework.data.jpa.repository.JpaSpecificationExecutor JpaSpecificationExecutor}
 *  封装了分页等复杂查询
 * </p><p>
 * 对于大部分字段对象而言，SQL语句只接受整数ID作为参数，而不接受整个数据对象。
 * </p><p>
 * SQL statements only accept id, not entire data field.
 * </p>
 * 
 * @author jingkaimori
 * @author 78165
 */
package cn.edu.bit.ruixin.community.repository;
