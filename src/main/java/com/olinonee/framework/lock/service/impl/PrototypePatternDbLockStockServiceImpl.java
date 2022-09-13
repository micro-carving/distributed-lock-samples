package com.olinonee.framework.lock.service.impl;

import com.olinonee.framework.lock.mapper.StockMapper;
import com.olinonee.framework.lock.service.IPrototypePatternDbLockStockService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

/**
 * 库存业务 接口实现类（多例模式）
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
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.INTERFACES)
public class PrototypePatternDbLockStockServiceImpl implements IPrototypePatternDbLockStockService {

    private final StockMapper stockMapper;

    /**
     * 使用数据库锁（原子性，行锁或者表锁）减库存，避免jvm锁失效的问题，QPS为 951.837/sec。
     */
    @Override
    public void deductWithDbLock() {
        this.stockMapper.updateStock("1001", 1);
        log.info("[PrototypePatternDbLockStockServiceImpl#deductWithDbLock]");
    }
}
