package com.olinonee.framework.lock.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.olinonee.framework.lock.entity.Stock;
import com.olinonee.framework.lock.mapper.StockMapper;
import com.olinonee.framework.lock.service.ITransactionalStockService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 库存业务 接口实现类（事务模式）
 * 基于 spring 事务来处理
 *
 * @author olinH, olinone666@gmail.com
 * @version v1.0.0
 * @since 2022-09-12
 */
@Service
@Slf4j
@AllArgsConstructor
public class TransactionalStockServiceImpl implements ITransactionalStockService {

    private final ReentrantLock reentrantLock = new ReentrantLock();
    private final StockMapper stockMapper;

    /**
     * 减库存（超卖问题），基于 MySQL 数据库，使用 事务 + synchronized 锁，QPS为 852.1/sec，spring 事务模式下，此方式会失效
     * <p>
     * 为什么会失效呢？因为 MySQL 默认事务隔离级别为 “可重复读”（repeatable read 简称 rr）
     * <p>
     * 要想解决这个问题，可以参考 {@link TransactionalStockServiceImpl#baseMysqlDeductWithReentrantLock} 方法事务的使用，
     * 其实就是 {@code @Transactional} 注解将隔离级别从 “默认”（aop 底层默认采用数据库的隔离级别） 改为 “读未提交”（read uncommitted 简称 ru），
     * 这样一来就可以读取还未提交的数据，但是也有缺点，会带来脏读等问题。这里只在当前示例中这样使用，但是实际互联网项目中采用的是 “读已提交”（read committed 简称 rc）事务隔离级别
     */
    @Override
    @Transactional
    public synchronized void baseMysqlDeductWithSynchronizedLock() {
        LambdaQueryWrapper<Stock> stockLambdaQueryWrapper = new LambdaQueryWrapper<>();
        stockLambdaQueryWrapper.eq(Stock::getProductCode, "1001");
        final Stock stk = this.stockMapper.selectOne(stockLambdaQueryWrapper);

        if (null != stk && stk.getCount() > 0) {
            stk.setCount(stk.getCount() - 1);
            log.info("[TransactionalStockServiceImpl#baseMysqlDeductWithSynchronizedLock] - 库存余量为：{}", stk.getCount());
            this.stockMapper.updateById(stk);
        }
    }

    /**
     * 减库存（超卖问题），基于 MySQL 数据库，使用 事务 + ReentrantLock 锁，QPS为 814.7/sec，使用 spring 下的 “读未提交” 事务隔离级别来解决，并发失效的问题
     * <p>
     * 注意：这里只在当前示例中这样使用，但是实际互联网项目中采用的是 “读已提交”（read committed 简称 rc）事务隔离级别
     */
    @Override
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public void baseMysqlDeductWithReentrantLock() {
        reentrantLock.lock();
        try {
            LambdaQueryWrapper<Stock> stockLambdaQueryWrapper = new LambdaQueryWrapper<>();
            stockLambdaQueryWrapper.eq(Stock::getProductCode, "1001");
            final Stock stk = this.stockMapper.selectOne(stockLambdaQueryWrapper);

            if (null != stk && stk.getCount() > 0) {
                stk.setCount(stk.getCount() - 1);
                log.info("[TransactionalStockServiceImpl#baseMysqlDeductWithReentrantLock] - 库存余量为：{}", stk.getCount());
                this.stockMapper.updateById(stk);
            }
        } finally {
            reentrantLock.unlock();
        }
    }
}
