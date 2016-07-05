package com.nextyu.spring.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * created on 2016-07-05 11:51
 *
 * @author zhouyu
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected Object determineCurrentLookupKey() {
        //logger.info("DynamicDataSource : {}", DataSourceHolder.getDataSource());
        return DataSourceHolder.getDataSource();
    }

}
