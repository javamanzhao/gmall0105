package com.zzhao.gmall.conf;

import io.shardingjdbc.core.api.MasterSlaveDataSourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

/**
 * @author Administrator
 * @date 2019/11/28 0028上午 10:15
 */
@Configuration
public class GmallShardingJdbcConfig {

    @Bean
    public DataSource dataSource() throws IOException, SQLException {
        //使用sharding-jdbc创建出具有主从库的数据源
        DataSource dataSource = MasterSlaveDataSourceFactory
                .createDataSource(ResourceUtils.getFile("classpath:sharding-jdbc.yml"));
        return  dataSource;
    }

}
