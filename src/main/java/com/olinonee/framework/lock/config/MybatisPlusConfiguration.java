package com.olinonee.framework.lock.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
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


    /**
     * 乐观锁插件注入
     *
     * @see <a href="https://baomidou.com/pages/0d93c0/#_1-%E9%85%8D%E7%BD%AE%E6%8F%92%E4%BB%B6">乐观锁插件</a>
     * @return MybatisPlusInterceptor
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return interceptor;
    }
}
