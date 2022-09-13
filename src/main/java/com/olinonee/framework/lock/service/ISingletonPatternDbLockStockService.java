package com.olinonee.framework.lock.service;

/**
 * 库存业务 接口（单例模式）
 *
 * @author olinH, olinone666@gmail.com
 * @version v1.0.0
 * @since 2022-09-13
 */
public interface ISingletonPatternDbLockStockService {

    /**
     * 使用数据库锁（原子性，行锁或者表锁）减库存
     */
    void deductWithDbLock();
}
