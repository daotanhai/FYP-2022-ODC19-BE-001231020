package com.odc19.baseEntity.CustomIdGenerator;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Properties;
import java.util.stream.Stream;

@Slf4j
public class CustomIdGenerator implements IdentifierGenerator, Configurable {
    private String prefix;

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object obj) throws HibernateException {
//        select "table_primary_key" from "table_name"
        String query = String.format("select %s from %s",
                session.getEntityPersister(obj.getClass().getName(), obj).getIdentifierPropertyName(),
                obj.getClass().getSimpleName());

        Stream<String> ids = session.createQuery(query).stream();
//        get total id of entity then split the number,then, parse to long for increase the count
        Long max = ids.map(o -> o.replace(prefix, ""))
                .mapToLong(Long::parseLong)
                .max()
                .orElse(0L);
//        return id for entity
        return prefix + (max + 1);
    }

    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
//        to pass param from entity which use this custom generator
        prefix = params.getProperty("prefix");
    }
}
