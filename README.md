## spring mybatis database read write splitting

基于spring事务，用于mybatis，实现读写分离。完全基于配置文件，无侵入。

- 只读事务到从库
- 读写事务到主库
- 如果没有配置事务，全部走主库

### 引入maven依赖

### 数据源配置
```
<!-- master -->
<bean id="masterDataSource" class="com.alibaba.druid.pool.DruidDataSource" init-method="init"
          destroy-method="close">
</bean>

<!-- slave2 -->
<bean id="slaveDataSource1" class="com.alibaba.druid.pool.DruidDataSource" init-method="init"
          destroy-method="close">
</bean>

<!-- slave2 -->
<bean id="slaveDataSource2" class="com.alibaba.druid.pool.DruidDataSource" init-method="init"
          destroy-method="close">
</bean>

<!-- 自定义数据源 -->
<bean id="dataSource" class="com.nextyu.spring.datasource.DynamicDataSource">
        <!-- 默认数据源 -->
        <property name="defaultTargetDataSource" ref="masterDataSource"/>
        <!-- 目标数据源 -->
        <property name="targetDataSources">
            <map>
                <entry key="master" value-ref="masterDataSource"/>
                <entry key="slave1" value-ref="slaveDataSource1"/>
                <entry key="slave2" value-ref="slaveDataSource2"/>
            </map>
        </property>
</bean>

```

### 事务管理器配置
```
<!--自定义事务管理器-->
<bean id="transactionManager" class="com.nextyu.spring.datasource.DynamicDataSourceTransactionManager">
        <property name="dataSource" ref="dataSource"/>
        <property name="masterDataSourceKey" value="master"/>
        <property name="slaveDataSourceKeys">
            <list>
                <value>slave1</value>
                <value>slave2</value>
            </list>
        </property>
</bean>
```

### 其他的配置还是按照原来的方式配置


