package com.olinonee.framework.lock.service;

/**
 * 库存业务 接口
 *
 * @author olinH, olinone666@gmail.com
 * @version v1.0.0
 * @since 2022-09-09
 */
public interface ISinglePatternStockService {

    /**
     * 减库存（超卖问题），未使用锁的情况
     */
    void deductWithoutLock();

    /**
     * 减库存（超卖问题），使用 synchronized 锁
     */
    void deductWithSynchronizedLock();

    /**
     * 减库存（超卖问题），使用 ReentrantLock 锁
     */
    void deductWithReentrantLock();

    /**
     * 减库存（超卖问题），基于 MySQL 数据库，未使用锁情况
     */
    void baseMysqlDeductWithoutLock();

    /**
     * 减库存（超卖问题），基于 MySQL 数据库，使用 synchronized 锁
     */
    void baseMysqlDeductWithSynchronizedLock();

    /**
     * 减库存（超卖问题），基于 MySQL 数据库，使用 ReentrantLock 锁
     */
    void baseMysqlDeductWithReentrantLock();
}
