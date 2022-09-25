package com.olinonee.framework.lock.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.olinonee.framework.lock.entity.Stock;
import com.olinonee.framework.lock.mapper.StockMapper;
import com.olinonee.framework.lock.service.ITransactionalDbLockStockService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    /**
     * 使用 select ... for update 更新减库存，悲观锁（行级锁），QPS为 497.4/sec，有效解决超卖问题
     * <p>
     * 这里通过使用 {@code @Transactional} 事务注解来自动加锁以及释放锁。如果是 SQL 脚本执行下面代码逻辑，流程是这样子的：
     * <pre>
     *     # 命令行窗口1的SQL
     *     1.begin;
     *     2.SELECT * FROM `tb_stock` WHERE product_code = '1001' FOR UPDATE;
     *
     *     # 命令行窗口2的SQL（回车阻塞）
     *     1.UPDATE `tb_stock` SET count = count - 1 WHERE id = 1;
     *
     *     # 命令行窗口1的SQL（命令行窗口2的SQL结束执行，锁释放掉了）
     *     1.commit;或者rollback;
     * </pre>
     */
    @Override
    @Transactional
    public void deductWithDbLockForUpdate() {
        // 1.查询库存信息并锁定库存信息
        final List<Stock> stocks = this.stockMapper.queryStockForUpdate("1001");
        // 实际中这里需要引入仓库自动调配算法，选取最近仓库进行货调配，为了简单，默认取第一条记录
        final Stock stock = CollUtil.getFirst(stocks);

        // 2.判断库存是否充足
        if (ObjUtil.isNotNull(stock) && stock.getCount() > 0) {
            // 3.扣减库存
            stock.setCount(stock.getCount() - 1);
            this.stockMapper.updateById(stock);
        }
    }
}
