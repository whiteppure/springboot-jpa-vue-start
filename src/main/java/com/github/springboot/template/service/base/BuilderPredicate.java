package com.github.springboot.template.service.base;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public interface BuilderPredicate {

    /**
     * 构造多条查询条件
     * @return 多条查询条件
     */
    List<Predicate> accept(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder, List<Predicate> predicates);
}
