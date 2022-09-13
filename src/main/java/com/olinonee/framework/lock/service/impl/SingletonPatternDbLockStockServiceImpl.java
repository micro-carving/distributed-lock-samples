package com.olinonee.framework.lock.service.impl;

import com.olinonee.framework.lock.mapper.StockMapper;
import com.olinonee.framework.lock.service.ISingletonPatternDbLockStockService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 库存业务 接口实现类（单例模式）
 * <p>
 * 这里基于数据库层面自带的锁（原子性，行锁或者表锁）来实现并发性，避免jvm锁失效的问题
 *
 * @author olinH, olinone666@gmail.com
 * @version v1.0.0
 * @since 2022-09-13
 */
@Slf4j
@Service
@AllArgsConstructor
public class SingletonPatternDbLockStockServiceImpl implements ISingletonPatternDbLockStockService {

    private final StockMapper stockMapper;

    /**
     * 使用数据库锁（原子性，行锁或者表锁）减库存，避免jvm锁失效的问题，QPS为 897.183/sec。
     */
    @Override
    public void deductWithDbLock() {
        this.stockMapper.updateStock("1001", 1);
        log.info("[DatabaseLockStockServiceImpl#deductWithDbLock]");
    }
}
