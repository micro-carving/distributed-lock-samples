package com.olinonee.framework.lock.service;

/**
 * 库存业务 接口
 *
 * @author olinH, olinone666@gmail.com
 * @version v1.0.0
 * @since 2022-09-09
 */
public interface IPrototypePatternStockService {

    /**
     * 减库存，未使用锁的情况
     */
    void deductWithoutLock();

    /**
     * 减库存，使用 synchronized 锁
     */
    void deductWithSynchronizedLock();

    /**
     * 减库存，使用 ReentrantLock 锁
     */
    void deductWithReentrantLock();

    /**
     * 减库存，基于 MySQL 数据库，未使用锁情况
     */
    void baseMysqlDeductWithoutLock();

    /**
     * 减库存，基于 MySQL 数据库，使用 synchronized 锁
     */
    void baseMysqlDeductSynchronizedLock();

    /**
     * 减库存，基于 MySQL 数据库，使用 ReentrantLock 锁
     */
    void baseMysqlDeductWithReentrantLock();
}
