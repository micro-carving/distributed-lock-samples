package com.olinonee.framework.lock.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * mybatisPlus 配置类
 *
 * @author olinH, olinone666@gmail.com
 * @version v1.0.0
 * @since 2022-09-09
 */
@Configuration
@MapperScan("com.olinonee.framework.lock.mapper")
public class MybatisPlusConfiguration {

}
