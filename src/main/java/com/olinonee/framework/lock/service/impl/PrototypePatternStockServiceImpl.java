package com.olinonee.framework.lock.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.olinonee.framework.lock.entity.Stock;
import com.olinonee.framework.lock.mapper.StockMapper;
import com.olinonee.framework.lock.service.IPrototypePatternStockService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 库存业务 接口实现类（多例模式）
 * （多例模式，注意和单例模式的区别）
 * <p>
 * 注意：当使用多例模式情况下，要保证 proxyMode 的值不为 ScopedProxyMode.DEFAULT(与ScopedProxyMode.NO等同)，
 * 否则只改变 value 的值为 “prototype”，此时还是按照单例模式来处理
 * <p>
 * 扩展：原生 spring 默认使用 jdk 动态代理（实现接口），而 SpringBoot 2.X 使用 CGLIB 代理（基于类代理），由于这里采用的是实现类方式，所以使用 jdk 动态代理方式
 *
 * @author olinH, olinone666@gmail.com
 * @version v1.0.0
 * @since 2022-09-09
 */
@Service
@Slf4j
@AllArgsConstructor
// @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.INTERFACES)
public class PrototypePatternStockServiceImpl implements IPrototypePatternStockService {

    private final Stock stock = new Stock();
    private final int total = stock.getStockNum();
    private final ReentrantLock reentrantLock = new ReentrantLock();
    private final StockMapper stockMapper;


    /**
     * 减库存（超卖问题），未使用锁的情况，经过使用 JMeter 测试（100用户，循环50次，测试并发量 3494/sec），会出现超卖问题（理想已售完，但实际库存还有）
     */
    @Override
    public void deductWithoutLock() {
        stock.setStockNum(stock.getStockNum() - 1);
        log.info("[PrototypePatternStockServiceImpl#deductWithoutLock] - 总库存为：{}，库存余量为：{}", total, stock.getStockNum());
    }

    /**
     * 减库存（超卖问题），使用 synchronized 锁，多例模式下，此方式会失效
     */
    @Override
    public synchronized void deductWithSynchronizedLock() {
        stock.setStockNum(stock.getStockNum() - 1);
        log.info("[PrototypePatternStockServiceImpl#deductWithSynchronizedLock] - 总库存为：{}，库存余量为：{}", total, stock.getStockNum());
    }

    /**
     * 减库存（超卖问题），使用 ReentrantLock 锁，多例模式下，此方式会失效
     */
    @Override
    public void deductWithReentrantLock() {
        reentrantLock.lock();
        try {
            stock.setStockNum(stock.getStockNum() - 1);
            log.info("[PrototypePatternStockServiceImpl#deductWithReentrantLock] - 总库存为：{}，库存余量为：{}", total, stock.getStockNum());
        } finally {
            reentrantLock.unlock();
        }
    }

    /**
     * 减库存（超卖问题），基于 MySQL 数据库，未使用锁情况，经过使用 JMeter 测试（100用户，循环50次，测试并发量 1193.602/sec），会出现超卖问题（理想已售完，但实际库存还有）
     */
    @Override
    public void baseMysqlDeductWithoutLock() {
        LambdaQueryWrapper<Stock> stockLambdaQueryWrapper = new LambdaQueryWrapper<>();
        stockLambdaQueryWrapper.eq(Stock::getProductCode, "1001");
        final Stock stk = this.stockMapper.selectOne(stockLambdaQueryWrapper);

        if (null != stk && stk.getCount() > 0) {
            stk.setCount(stk.getCount() - 1);
            log.info("[PrototypePatternStockServiceImpl#baseMysqlDeductWithoutLock] - 库存余量为：{}", stk.getCount());
            this.stockMapper.updateById(stk);
        }
    }

    /**
     * 减库存（超卖问题），基于 MySQL 数据库，使用 synchronized 锁，QPS为 496.968/sec，多例模式下，此方式会失效
     */
    @Override
    public synchronized void baseMysqlDeductWithSynchronizedLock() {
        LambdaQueryWrapper<Stock> stockLambdaQueryWrapper = new LambdaQueryWrapper<>();
        stockLambdaQueryWrapper.eq(Stock::getProductCode, "1001");
        final Stock stk = this.stockMapper.selectOne(stockLambdaQueryWrapper);

        if (null != stk && stk.getCount() > 0) {
            stk.setCount(stk.getCount() - 1);
            log.info("[PrototypePatternStockServiceImpl#baseMysqlDeductWithSynchronizedLock] - 库存余量为：{}", stk.getCount());
            this.stockMapper.updateById(stk);
        }
    }

    /**
     * 减库存（超卖问题），基于 MySQL 数据库，使用 ReentrantLock 锁，QPS为 496.574/sec，多例模式下，此方式会失效
     */
    @Override
    public void baseMysqlDeductWithReentrantLock() {
        reentrantLock.lock();
        try {
            LambdaQueryWrapper<Stock> stockLambdaQueryWrapper = new LambdaQueryWrapper<>();
            stockLambdaQueryWrapper.eq(Stock::getProductCode, "1001");
            final Stock stk = this.stockMapper.selectOne(stockLambdaQueryWrapper);

            if (null != stk && stk.getCount() > 0) {
                stk.setCount(stk.getCount() - 1);
                log.info("[PrototypePatternStockServiceImpl#baseMysqlDeductWithReentrantLock] - 库存余量为：{}", stk.getCount());
                this.stockMapper.updateById(stk);
            }
        } finally {
            reentrantLock.unlock();
        }
    }
}

