package com.github.springboot.template.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionUtils {

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private TransactionDefinition transactionDefinition;


    /**
     * 手动操作事物测试
     */
    @Test
    public void context() {
        TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);
        try {
            // 事务操作
            // 事务提交
            transactionManager.commit(transactionStatus);
        } catch (DataAccessException e) {
            // 事务提交
            transactionManager.rollback(transactionStatus);
            throw e;
        }
    }
}
