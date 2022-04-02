/**
 * <p>
 * 在spring框架的语义中，服务（service）指的是一类可以访问数据库和其他后端组件的代码。
 * 不同于一些工具函数，服务可以不经过调用方的授权，访问持久化存储仓库以及其他服务。
 * </p><p>
 * 服务的各个函数有可能同时访问和修改某个字段，因此可以访问数据库的函数必须由事务调度系统管理。
 * 在函数上添加{@link org.springframework.transaction.annotation.Transactional}注解，
 * 让框架去适配其事务特性。注意注解的包名，必须使用spring框架提供的注解，而非javax包的注解。
 * </p><p>
 * 添加注解时要先查看注解依附的函数，如果函数向数据库插入或删除行，则应该将隔离级别
 *   {@link org.springframework.transaction.annotation.Transactional#isolation()}
 * 设置为
 *   {@link org.springframework.transaction.annotation.Isolation#SERIALIZABLE}；
 * 如果函数更改数据库内已有的行，则应该将隔离级别设置为
 *   {@link org.springframework.transaction.annotation.Isolation#REPEATABLE_READ}或更低；
 * 如果函数只进行查询，没有更改数据库内的数据，那么不应该设置隔离级别，而需要置位只读属性
 *   {@link org.springframework.transaction.annotation.Transactional#readOnly()}；
 * 
 * </p><p>
 * 大多数情况下，service类的对象不能由调用方初始化，而需要采用依赖注入的方式，
 * 由spring框架将全局唯一的服务对象注入到调用方的上下文当中。
 * 此时服务必须具有{@link org.springframework.stereotype.Service}注解，才能被框架发现。
 * </p><p>
 * 划分服务与控制器，主要是防止一块代码同时依赖网络组件和数据库组件，
 * 因而服务的调用方（本项目中大多数为控制器，也有一些过滤器）不能直接访问仓库。
 * 当网络组件或网络接口发生变化时，适配数据库的代码就不需要更改。
 * </p><p>
 * 由于项目的需求和数据库结构难以分开（强耦合）；而且受限于关系型数据库的设计，
 * 调整后端的数据结构较为困难，往往是代码削足适履去适配数据库定义；
 * 因此业务代码和数据库查询纠缠在一起，像清真食堂吃了一半的拉面一样。
 * </p><p>
 * 由于json格式的便利，利用胶水代码转换前后端接口的数据结构较为容易；
 * 同时http协议设计的缺陷导致了，前后端对接的代码中，有一些和业务无关的部分，
 * 因此前后端对接的代码改动较为频繁。
 * </p>
 */
package cn.edu.bit.ruixin.community.service;
