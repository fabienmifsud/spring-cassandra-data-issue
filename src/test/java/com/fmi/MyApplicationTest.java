package com.fmi;


import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;
import com.datastax.oss.driver.api.core.config.DriverExecutionProfile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.cassandra.SessionFactory;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class MyApplicationTest {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private SessionFactory sessionFactory;

    @Test
    public void test() {

        DriverExecutionProfile config = context.getBean(DriverConfigLoader.class).getInitialConfig()
                .getDefaultProfile();

        assertThat(config.getDuration(DefaultDriverOption.CONNECTION_INIT_QUERY_TIMEOUT)).isEqualTo(Duration.ofSeconds(5));

        assertThat(sessionFactory.getSession().getContext().getConfigLoader().getInitialConfig().getDefaultProfile().getDuration(DefaultDriverOption.CONNECTION_INIT_QUERY_TIMEOUT))
                .isEqualTo(Duration.ofSeconds(5));

    }

}
