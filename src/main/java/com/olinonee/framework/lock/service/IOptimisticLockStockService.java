package com.olinonee.framework.lock.service;

/**
 * 库存业务 接口（乐观锁）
 *
 * @author olinH, olinone666@gmail.com
 * @version v1.0.0
 * @since 2022-10-21
 */
public interface IOptimisticLockStockService {
    /**
     * 使用 version 版本号（非注解模式）
     */
    void deductWithNonAnnotatedVersion();

    /**
     * 使用 version 版本号（注解模式）
     */
    void deductWithAnnotatedVersion();

    /**
     * 使用 timestamp 时间戳
     */
    void deductWithTimestamp();
}
