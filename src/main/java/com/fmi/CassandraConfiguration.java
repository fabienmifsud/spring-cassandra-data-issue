package com.fmi;

import com.datastax.oss.driver.api.core.CqlSession;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.SessionFactory;
import org.springframework.data.cassandra.config.CqlSessionFactoryBean;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.config.SessionFactoryFactoryBean;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.convert.CassandraConverter;
import org.springframework.data.cassandra.core.convert.MappingCassandraConverter;
import org.springframework.data.cassandra.core.mapping.CassandraMappingContext;
import org.springframework.data.cassandra.core.mapping.SimpleUserTypeResolver;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@Configuration
@EnableCassandraRepositories(basePackages = {"com.fmi"})
public class CassandraConfiguration {

    @Value("${spring.data.cassandra.port}")
    private Integer port;

    @Value("${spring.data.cassandra.contact-points}")
    private String hosts;

    @Value("${spring.data.cassandra.keyspace}")
    private String keySpace;

    @Value("${spring.data.cassandra.username}")
    private String username;

    @Value("${spring.data.cassandra.password}")
    private String password;

    @Value("${spring.data.cassandra.local-datacenter}")
    private String datacenterName;

    @Bean
    public CqlSessionFactoryBean session() {

        CqlSessionFactoryBean session = new CqlSessionFactoryBean();
        session.setContactPoints(this.hosts);
        session.setPort(this.port);
        session.setKeyspaceName(this.keySpace);

        if(StringUtils.isNotBlank(this.datacenterName)) {
            session.setLocalDatacenter(this.datacenterName);
        }
        if(StringUtils.isNotBlank(this.username)) {
            session.setUsername(this.username);
        }
        if(StringUtils.isNotBlank(this.password)) {
            session.setPassword(this.password);
        }

        return session;
    }

    @Bean
    public SessionFactoryFactoryBean sessionFactory(CqlSession session, CassandraConverter converter) {

        SessionFactoryFactoryBean sessionFactory = new SessionFactoryFactoryBean();
        sessionFactory.setSession(session);
        sessionFactory.setConverter(converter);
        sessionFactory.setSchemaAction(SchemaAction.NONE);

        return sessionFactory;
    }

    @Bean
    public CassandraMappingContext mappingContext() {

        CassandraMappingContext mappingContext = new CassandraMappingContext();
        return mappingContext;
    }

    @Bean
    public CassandraConverter converter(CassandraMappingContext mappingContext, CqlSession session) {
        MappingCassandraConverter mappingCassandraConverter = new MappingCassandraConverter(mappingContext);
        mappingCassandraConverter.setUserTypeResolver(new SimpleUserTypeResolver(session));
        return mappingCassandraConverter;
    }

    @Bean
    public CassandraOperations cassandraTemplate(SessionFactory sessionFactory, CassandraConverter converter) {
        return new CassandraTemplate(sessionFactory, converter);
    }
}
