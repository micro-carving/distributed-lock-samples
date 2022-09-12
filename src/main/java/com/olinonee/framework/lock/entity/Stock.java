package com.olinonee.framework.lock.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 库存
 *
 * @author olinH, olinone666@gmail.com
 * @version v1.0.0
 * @since 2022-09-09
 */
@Data
@TableName("tb_stock")
public class Stock {

    // 库存量 5000
    @TableField(exist = false)
    private Integer stockNum = 5000;

    private Long id;
    private String productCode;
    private String warehouse;
    private Integer count;
}
