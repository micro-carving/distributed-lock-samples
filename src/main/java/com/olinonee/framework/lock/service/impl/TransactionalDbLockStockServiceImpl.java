package com.olinonee.framework.lock.service.impl;

import com.olinonee.framework.lock.mapper.StockMapper;
import com.olinonee.framework.lock.service.ITransactionalDbLockStockService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 库存业务 接口实现类（事务）
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
public class TransactionalDbLockStockServiceImpl implements ITransactionalDbLockStockService {

    private final StockMapper stockMapper;

    /**
     * 使用数据库锁（原子性，行锁或者表锁）减库存，避免jvm锁失效的问题，QPS为 869.565/sec，有效解决超卖问题
     */
    @Override
    @Transactional
    public void deductWithDbLock() {
        this.stockMapper.updateStock("1001", 1);
        log.info("[TransactionalDbLockStockServiceImpl#deductWithDbLock]");
    }
}
