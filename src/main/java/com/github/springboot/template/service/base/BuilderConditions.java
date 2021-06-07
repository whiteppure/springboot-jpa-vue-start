package com.github.springboot.template.service.base;

import javax.persistence.criteria.Predicate;

public interface BuilderConditions {

    /**
     * 构建查询条件
     * @return 查询条件
     */
    Predicate accept();

}
