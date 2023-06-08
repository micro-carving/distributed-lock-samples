package com.olinonee.framework.lock.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.olinonee.framework.lock.entity.Stock;
import com.olinonee.framework.lock.mapper.StockMapper;
import com.olinonee.framework.lock.service.IOptimisticLockStockService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 库存业务 接口实现类（乐观锁）
 *
 * @author olinH, olinone666@gmail.com
 * @version v1.0.0
 * @since 2022-10-21
 */
@Service
@AllArgsConstructor
@Slf4j
public class OptimisticLockStockServiceImpl implements IOptimisticLockStockService {

    private final StockMapper stockMapper;

    /**
     * 使用 version 版本号（非注解模式），QPS为 150.227/sec，有效解决超卖问题
     * <p>
     * <strong>注意：</strong>使用此方法时，要把 Stock 实体的 version 字段上面的 @Version 注解去掉，同时存在的话，会形成死循环，导致栈溢出
     */
    @Override
    public void deductWithNonAnnotatedVersion() {
        final LambdaQueryWrapper<Stock> stockLambdaQueryWrapper = new LambdaQueryWrapper<>();

        // 1.查询库存信息
        stockLambdaQueryWrapper.eq(Stock::getProductCode, "1001");
        final List<Stock> stockList = this.stockMapper.selectList(stockLambdaQueryWrapper);

        // 取第一个库存
        final Stock stock = Assert.notEmpty(stockList, "查询的数据为空！").get(0);
        // 2.判断库存是否充足
        if (null != stock && stock.getCount() > 0) {
            // 3.扣减库存
            stock.setCount(stock.getCount() - 1);
            final Integer version = stock.getVersion();
            stock.setVersion(version + 1);
            final LambdaUpdateWrapper<Stock> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Stock::getId, stock.getId());
            updateWrapper.eq(Stock::getVersion, version);
            final int updateRows = this.stockMapper.update(stock, updateWrapper);
            // 更新行数是否为 0（更新失败），为 0 则重试，也就是递归调用
            if (updateRows == 0) {
                this.deductWithNonAnnotatedVersion();
            }
        }
    }

    /**
     * 使用 version 版本号（注解模式），QPS为 150.3/sec，有效解决超卖问题
     * <p>
     * <strong>注意：</strong>使用此方法时，要把 Stock 实体的 version 字段上面的 @Version 注解加上
     */
    @Override
    public void deductWithAnnotatedVersion() {
        final LambdaQueryWrapper<Stock> stockLambdaQueryWrapper = new LambdaQueryWrapper<>();

        // 1.查询库存信息
        stockLambdaQueryWrapper.eq(Stock::getProductCode, "1001");
        final List<Stock> stockList = this.stockMapper.selectList(stockLambdaQueryWrapper);

        // 取第一个库存
        final Stock stock = Assert.notEmpty(stockList, "查询的数据为空！").get(0);
        // 2.判断库存是否充足
        if (null != stock && stock.getCount() > 0) {
            // 3.扣减库存
            stock.setCount(stock.getCount() - 1);
            final LambdaUpdateWrapper<Stock> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Stock::getId, stock.getId());
            final int updateRows = this.stockMapper.update(stock, updateWrapper);
            // 更新行数是否为 0（更新失败），为 0 则重试，也就是递归调用
            if (updateRows == 0) {
                this.deductWithAnnotatedVersion();
            }
        }
    }

    /**
     * 使用 timestamp 时间戳
     */
    @Override
    public void deductWithTimestamp() {
        final LambdaQueryWrapper<Stock> stockLambdaQueryWrapper = new LambdaQueryWrapper<>();

        // 1.查询库存信息
        stockLambdaQueryWrapper.eq(Stock::getProductCode, "1001");
        final List<Stock> stockList = this.stockMapper.selectList(stockLambdaQueryWrapper);

        // 取第一个库存
        final Stock stock = Assert.notEmpty(stockList, "查询的数据为空！").get(0);
        // 2.判断库存是否充足
        if (null != stock && stock.getCount() > 0) {
            // 3.扣减库存
            stock.setCount(stock.getCount() - 1);
            final LambdaUpdateWrapper<Stock> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Stock::getId, stock.getId());
            final int updateRows = this.stockMapper.update(stock, updateWrapper);
            // 更新行数是否为 0（更新失败），为 0 则重试，也就是递归调用
            if (updateRows == 0) {
                this.deductWithAnnotatedVersion();
            }
        }
    }
}
