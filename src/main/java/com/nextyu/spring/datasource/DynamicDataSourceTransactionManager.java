package com.nextyu.spring.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * created on 2016-07-05 13:39
 *
 * @author zhouyu
 */
public class DynamicDataSourceTransactionManager extends DataSourceTransactionManager {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private String masterDataSourceKey;

    private List<String> slaveDataSourceKeys;

    private AtomicInteger counter = new AtomicInteger();

    public void setMasterDataSourceKey(String masterDataSourceKey) {
        this.masterDataSourceKey = masterDataSourceKey;
    }

    public void setSlaveDataSourceKeys(List<String> slaveDataSourceKeys) {
        this.slaveDataSourceKeys = slaveDataSourceKeys;
    }

    /**
     * 只读事务到从库
     * 读写事务到主库
     *
     * @param transaction
     * @param definition
     */
    @Override
    protected void doBegin(Object transaction, TransactionDefinition definition) {
        boolean readOnly = definition.isReadOnly();
        //logger.info("readOnly: {}", readOnly);
        if (readOnly) {
            int count = counter.incrementAndGet();
            if (count > 1000000) {
                counter.set(0);
            }
            int size = slaveDataSourceKeys.size();
            int index = count % size;
            //logger.info("slave: {}", slaveDataSourceKeys.get(index));
            DataSourceHolder.setDataSource(slaveDataSourceKeys.get(index));

        } else {
            DataSourceHolder.setDataSource(masterDataSourceKey);
        }
        super.doBegin(transaction, definition);
    }

    /**
     * 清理本地线程的数据源
     *
     * @param transaction
     */
    @Override
    protected void doCleanupAfterCompletion(Object transaction) {
        DataSourceHolder.clearDataSource();
        super.doCleanupAfterCompletion(transaction);
    }
}
