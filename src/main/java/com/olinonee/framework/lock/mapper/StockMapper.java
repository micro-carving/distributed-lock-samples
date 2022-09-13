package com.olinonee.framework.lock.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.olinonee.framework.lock.entity.Stock;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 库存 mapper接口
 *
 * @author olinH, olinone666@gmail.com
 * @version v1.0.0
 * @since 2022-09-09
 */
@Mapper
public interface StockMapper extends BaseMapper<Stock> {

    int updateStock(@Param("productCode") String productCode, @Param("count") Integer count);

}
