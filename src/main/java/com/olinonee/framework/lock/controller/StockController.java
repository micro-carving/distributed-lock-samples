package com.olinonee.framework.lock.controller;

import com.olinonee.framework.lock.service.IPrototypePatternStockService;
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

    // private ISinglePatternStockService singlePatternStockService;
    private IPrototypePatternStockService singlePatternStockService;


    @GetMapping("/deductWithoutLock")
    public String deductWithoutLock() {
        this.singlePatternStockService.deductWithoutLock();
        return "[deductWithoutLock] - 已经成功执行减库存！！";
    }

    @GetMapping("/deductWithSynchronizedLock")
    public String deductWithSynchronizedLock() {
        this.singlePatternStockService.deductWithSynchronizedLock();
        return "[deductWithSynchronizedLock] - 已经成功执行减库存！！";
    }

    @GetMapping("/deductWithReentrantLock")
    public String deductWithReentrantLock() {
        this.singlePatternStockService.deductWithReentrantLock();
        return "[deductWithReentrantLock] - 已经成功执行减库存！！";
    }

    @GetMapping("/baseMysqlDeductWithoutLock")
    public String baseMysqlDeductWithoutLock() {
        this.singlePatternStockService.baseMysqlDeductWithoutLock();
        return "[baseMysqlDeductWithoutLock] - 已经成功执行减库存！！";
    }

    @GetMapping("/baseMysqlDeductSynchronizedLock")
    public String baseMysqlDeductSynchronizedLock() {
        this.singlePatternStockService.baseMysqlDeductSynchronizedLock();
        return "[baseMysqlDeductSynchronizedLock] - 已经成功执行减库存！！";
    }

    @GetMapping("/baseMysqlDeductWithReentrantLock")
    public String baseMysqlDeductWithReentrantLock() {
        this.singlePatternStockService.baseMysqlDeductWithReentrantLock();
        return "[baseMysqlDeductWithReentrantLock] - 已经成功执行减库存！！";
    }
}
