package com.olinonee.framework.lock.service;

/**
 * 库存业务 接口
 *
 * @author olinH, olinone666@gmail.com
 * @version v1.0.0
 * @since 2022-09-12
 */
public interface ITransactionalStockService {

    /**
     * 减库存（超卖问题），基于 MySQL 数据库，使用 事务 + synchronized 锁
     */
    void baseMysqlDeductWithSynchronizedLock();

    /**
     * 减库存（超卖问题），基于 MySQL 数据库，使用 事务 + ReentrantLock 锁
     */
    void baseMysqlDeductWithReentrantLock();
}
