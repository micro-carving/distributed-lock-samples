package com.olinonee.framework.lock.controller;

import com.olinonee.framework.lock.service.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 库存 控制器
 *
 * @author olinH, olinone666@gmail.com
 * @version v1.0.0
 * @since 2022-09-09
 */
@RestController
@AllArgsConstructor
@RequestMapping("/stock")
public class StockController {

    private final ISingletonPatternStockService stockService; // 单例
    // private final IPrototypePatternStockService stockService; // 多例
    private final ITransactionalStockService transactionalStockService; // 事务
    private final ISingletonPatternDbLockStockService singletonPatternDbLockStockService; // 单例数据库锁
    private final IPrototypePatternDbLockStockService prototypePatternDbLockStockService; // 多例数据库锁
    private final ITransactionalDbLockStockService transactionalDbLockStockService; // 事务数据库锁

    private final IOptimisticLockStockService optimisticLockStockService; // 乐观锁

    @GetMapping("/deductWithoutLock")
    public String deductWithoutLock() {
        this.stockService.deductWithoutLock();
        return "[deductWithoutLock] - 已经成功执行减库存！！";
    }

    @GetMapping("/deductWithSynchronizedLock")
    public String deductWithSynchronizedLock() {
        this.stockService.deductWithSynchronizedLock();
        return "[deductWithSynchronizedLock] - 已经成功执行减库存！！";
    }

    @GetMapping("/deductWithReentrantLock")
    public String deductWithReentrantLock() {
        this.stockService.deductWithReentrantLock();
        return "[deductWithReentrantLock] - 已经成功执行减库存！！";
    }

    @GetMapping("/baseMysqlDeductWithoutLock")
    public String baseMysqlDeductWithoutLock() {
        this.stockService.baseMysqlDeductWithoutLock();
        return "[baseMysqlDeductWithoutLock] - 已经成功执行减库存！！";
    }

    @GetMapping("/baseMysqlDeductWithSynchronizedLock")
    public String baseMysqlDeductWithSynchronizedLock() {
        this.stockService.baseMysqlDeductWithSynchronizedLock();
        return "[baseMysqlDeductWithSynchronizedLock] - 已经成功执行减库存！！";
    }

    @GetMapping("/baseMysqlDeductWithReentrantLock")
    public String baseMysqlDeductWithReentrantLock() {
        this.stockService.baseMysqlDeductWithReentrantLock();
        return "[baseMysqlDeductWithReentrantLock] - 已经成功执行减库存！！";
    }

    @GetMapping("/transactionalBaseMysqlDeductWithSynchronizedLock")
    public String transactionalBaseMysqlDeductWithSynchronizedLock() {
        this.transactionalStockService.baseMysqlDeductWithSynchronizedLock();
        return "[transactionalBaseMysqlDeductWithSynchronizedLock] - 已经成功执行减库存！！";
    }

    @GetMapping("/transactionalBaseMysqlDeductWithReentrantLock")
    public String transactionalBaseMysqlDeductWithReentrantLock() {
        this.transactionalStockService.baseMysqlDeductWithReentrantLock();
        return "[transactionalBaseMysqlDeductWithReentrantLock] - 已经成功执行减库存！！";
    }

    @GetMapping("/mysqlSingletonPatternDbLock")
    public String mysqlSingletonPatternDbLock() {
        this.singletonPatternDbLockStockService.deductWithDbLock();
        return "[mysqlSingletonPatternDbLock] - 已经成功执行减库存！！";
    }

    @GetMapping("/mysqlPrototypePatternDbLock")
    public String mysqlPrototypePatternDbLock() {
        this.prototypePatternDbLockStockService.deductWithDbLock();
        return "[mysqlPrototypePatternDbLock] - 已经成功执行减库存！！";
    }

    @GetMapping("/mysqlTransactionalDbLock")
    public String mysqlTransactionalDbLock() {
        this.transactionalDbLockStockService.deductWithDbLock();
        return "[mysqlTransactionalDbLock] - 已经成功执行减库存！！";
    }

    @GetMapping("/mysqlTransactionalDbLockForUpdate")
    public String mysqlTransactionalDbLockForUpdate() {
        this.transactionalDbLockStockService.deductWithDbLockForUpdate();
        return "[mysqlTransactionalDbLockForUpdate] - 已经成功执行减库存！！";
    }

    @GetMapping("/optimisticLockStockDeductWithNonAnnotatedVersion")
    public String optimisticLockStockDeductWithNonAnnotatedVersion() {
        this.optimisticLockStockService.deductWithNonAnnotatedVersion();
        return "[optimisticLockStockDeductWithNonAnnotatedVersion] - 已经成功执行减库存！！";
    }

    @GetMapping("/optimisticLockStockDeductWithAnnotatedVersion")
    public String optimisticLockStockDeductWithAnnotatedVersion() {
        this.optimisticLockStockService.deductWithAnnotatedVersion();
        return "[optimisticLockStockDeductWithAnnotatedVersion] - 已经成功执行减库存！！";
    }

    @GetMapping("/optimisticLockStockDeductWithTimestamp")
    public String optimisticLockStockDeductWithTimestamp() {
        this.optimisticLockStockService.deductWithTimestamp();
        return "[optimisticLockStockDeductWithTimestamp] - 已经成功执行减库存！！";
    }
}
