package com.olinonee.framework.lock.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.olinonee.framework.lock.entity.Stock;
import com.olinonee.framework.lock.mapper.StockMapper;
import com.olinonee.framework.lock.service.ISingletonPatternStockService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.concurrent.locks.ReentrantLock;


/**
 * 库存业务 接口实现类（单例模式）
 * （单例模式，spring 初始化 bean 默认使用的是单例模式，可以使用 @Scope 注解加以区分，也可以不用加，这里为了区分多例，加上了【有点画蛇添足的感觉】）
 *
 * @author olinH, olinone666@gmail.com
 * @version v1.0.0
 * @since 2022-09-09
 */
@Service
@Slf4j
@AllArgsConstructor
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class SingletonPatternStockServiceImpl implements ISingletonPatternStockService {

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
        log.info("[SinglePatternStockServiceImpl#deductWithoutLock] - 总库存为：{}，库存余量为：{}", total, stock.getStockNum());
    }

    /**
     * 减库存（超卖问题），使用 synchronized 锁
     */
    @Override
    public synchronized void deductWithSynchronizedLock() {
        stock.setStockNum(stock.getStockNum() - 1);
        log.info("[SinglePatternStockServiceImpl#deductWithSynchronizedLock] - 总库存为：{}，库存余量为：{}", total, stock.getStockNum());
    }

    /**
     * 减库存（超卖问题），使用 ReentrantLock 锁
     */
    @Override
    public void deductWithReentrantLock() {
        reentrantLock.lock();
        try {
            stock.setStockNum(stock.getStockNum() - 1);
            log.info("[SinglePatternStockServiceImpl#deductWithReentrantLock] - 总库存为：{}，库存余量为：{}", total, stock.getStockNum());
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
            log.info("[SinglePatternStockServiceImpl#baseMysqlDeductWithoutLock] - 库存余量为：{}", stk.getCount());
            this.stockMapper.updateById(stk);
        }
    }

    /**
     * 减库存（超卖问题），基于 MySQL 数据库，使用 synchronized 锁，QPS为 496.968/sec
     */
    @Override
    public synchronized void baseMysqlDeductWithSynchronizedLock() {
        LambdaQueryWrapper<Stock> stockLambdaQueryWrapper = new LambdaQueryWrapper<>();
        stockLambdaQueryWrapper.eq(Stock::getProductCode, "1001");
        final Stock stk = this.stockMapper.selectOne(stockLambdaQueryWrapper);

        if (null != stk && stk.getCount() > 0) {
            stk.setCount(stk.getCount() - 1);
            log.info("[SinglePatternStockServiceImpl#baseMysqlDeductWithSynchronizedLock] - 库存余量为：{}", stk.getCount());
            this.stockMapper.updateById(stk);
        }
    }

    /**
     * 减库存（超卖问题），基于 MySQL 数据库，使用 ReentrantLock 锁，QPS为 496.574/sec
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
                log.info("[SinglePatternStockServiceImpl#baseMysqlDeductWithReentrantLock] - 库存余量为：{}", stk.getCount());
                this.stockMapper.updateById(stk);
            }
        } finally {
            reentrantLock.unlock();
        }
    }
}
