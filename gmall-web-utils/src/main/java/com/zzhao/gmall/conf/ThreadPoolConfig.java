package com.zzhao.gmall.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 * @date 2019/11/27 0027上午 10:13
 */
@Configuration
public class ThreadPoolConfig {

    @Value("${pool.coreSize}")
    private Integer coreSize;
    @Value("${pool.maximumPoolSize}")
    private Integer maximumPoolSize;
    @Value("${pool.queueSize}")
    private Integer queueSize;

    @Bean("mainThreadPoolExecutor")
    public ThreadPoolExecutor mainThreadPoolExecutor(){
        /**
         *   public ThreadPoolExecutor(int corePoolSize,
         *                               int maximumPoolSize,
         *                               long keepAliveTime,
         *                               TimeUnit unit,
         *                               BlockingQueue<Runnable> workQueue,
         *                               RejectedExecutionHandler handler) {
         */
        LinkedBlockingDeque<Runnable> deque = new LinkedBlockingDeque<>(queueSize);

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(coreSize,
                maximumPoolSize, 10,
                TimeUnit.MINUTES, deque);

        return threadPoolExecutor;
    }
}
