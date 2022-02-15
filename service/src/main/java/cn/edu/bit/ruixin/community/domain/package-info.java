/**
 * 
 * <p>
 * 数据库字段（Domain或Entity）对象，是JDBC引擎对数据库表的封装
 * </p><p>
 * 原有的字段对象当中，很大一部分使用id来标识其他字段，例如{@link Role}，此类对象对象本身就是视图层对象。
 * 2022年以后创建的对象当中，例如{@link Meeting}，大多数利用JDBC的映射规则，直接将数据库内的id字段转化为Java对象的引用。
 * </p><p>
 * jpa会根据字段定义，自动在数据库中插入不存在的列。
 * </p>
 * 
 * @author jingkaimori
 */
package cn.edu.bit.ruixin.community.domain;
