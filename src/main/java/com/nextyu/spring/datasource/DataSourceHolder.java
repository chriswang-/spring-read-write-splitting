package com.nextyu.spring.datasource;

/**
 * created on 2016-07-05 12:01
 *
 * @author zhouyu
 */
public class DataSourceHolder {

    private static final String MASTER = "master";

    private static final String SLAVE = "slave";

    /**
     * dataSource master or slave
     */
    private static final ThreadLocal<String> dataSource = new ThreadLocal<String>();


    public static void setDataSource(String dataSourceKey) {
        dataSource.set(dataSourceKey);
    }

    public static String getDataSource() {
        return dataSource.get();
    }

    /**
     * 标志为master
     */
    public static void setMaster() {
        setDataSource(MASTER);
    }

    /**
     * 标志为slave
     */
    public static void setSlave() {
        setDataSource(SLAVE);
    }

    /**
     * 清除thread local中的数据源
     */
    public static void clearDataSource() {
        dataSource.remove();
    }

}
