package com.github.springboot.template.service.base;

import com.github.springboot.template.utils.ObjectUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;

/**
 * 封装基础查询条件
 */
public interface BaseQueryCondition {


    /**
     * 左模糊查询
     */
    default Predicate buildLeftLikeQuery(@NotNull Root<?> root,
                                         @NotNull CriteriaBuilder criteriaBuilder,
                                         @NotNull String likeCondition,
                                         @NotNull String filed) {
        final String formatter = "%%%s";
        return buildLikeQuery(root, criteriaBuilder, formatter, likeCondition, filed);
    }

    /**
     * 右模糊查询
     */
    default Predicate buildRightLikeQuery(@NotNull Root<?> root,
                                          @NotNull CriteriaBuilder criteriaBuilder,
                                          @NotNull String likeCondition,
                                          @NotNull String filed) {
        final String formatter = "%s%%";
        return buildLikeQuery(root, criteriaBuilder, formatter, likeCondition, filed);
    }


    /**
     * <p>
     * 构造模糊查询条件
     * 全表搜索
     * </p>
     *
     * @param root            实体类型；<?> 为任意实体类型不做限制
     * @param criteriaBuilder 用于构造条件查询，此处为模糊查询
     * @param likeCondition   模糊查询条件
     * @param likeFormatter   模糊查询格式:%like%、%like、like%；默认 %like% 全表搜索。
     *                        调用{@link java.lang.String}#format方法，如果格式有问题，则抛出`java.util.IllegalFormatException`异常。
     * @param filed           数据库对应模糊查询字段，要注意jpa,大小写敏感驼峰命名规则等问题。此处应该填写Javabean中名称，而不是数据库字段名称。
     *                        例如，表中：login_name，Javabean中：，loginName；在查询时应填写loginName，而不是login_name；
     *                        否则会抛出
     *                        <code>
     *                        IllegalArgumentException,Unable to locate Attribute  with the the given name [login_name] on this ManagedType [com.lilian.ticket.image.exchange.model.entity.SysWebLogEntity]
     *                        </code>
     * @return Predicate 查询条件
     */
    default Predicate buildLikeQuery(@NotNull Root<?> root,
                                     @NotNull CriteriaBuilder criteriaBuilder,
                                     String likeFormatter,
                                     final String likeCondition,
                                     @NotNull final String filed
    ) {
        Assert.notNull(filed, "build  query, 'filed' must not be null");

        if (StringUtils.isEmpty(likeCondition)) {
            return null;
        }

        return buildQuery(root, criteriaBuilder, () -> {
            final String formatter = "%%%s%%";
            String finalFormatter = ObjectUtils.isEmpty(likeFormatter) ? formatter : likeFormatter;

            // 使用 %% 对'%'进行转义
            String finalLikeCondition = String.format(finalFormatter, likeCondition);

            // 模糊构造模糊查询
            return criteriaBuilder.like(root.get(filed), finalLikeCondition);
        });
    }

    /**
     * between and 查询
     *
     * @param root            实体类型；<?> 为任意实体类型不做限制
     * @param criteriaBuilder 用于构造条件查询，此处为between and
     * @param from            between {@param from} and '{@param to}'
     * @param to              between {@param from} and '{@param to}'
     * @param filed           数据库字段
     * @return Predicate 查询条件
     */
    default Predicate buildBetweenQuery(@NotNull Root<?> root,
                                        @NotNull CriteriaBuilder criteriaBuilder,
                                        final String from,
                                        final String to,
                                        @NotNull final String filed) {

        Assert.notNull(filed, "build  query, 'filed' must not be null");
        if (StringUtils.isEmpty(from) || StringUtils.isEmpty(to)) {
            return null;
        }

        return buildQuery(root, criteriaBuilder, () -> criteriaBuilder.between(root.get(filed), from, to));
    }


    /**
     * 构建查询条件
     *
     * @param root              实体类型
     * @param criteriaBuilder   用于构造条件查询
     * @param builderConditions 自定义查询条件接口
     * @return Predicate 条件
     */
    default Predicate buildQuery(@NotNull Root<?> root,
                                 @NotNull CriteriaBuilder criteriaBuilder,
                                 BuilderConditions builderConditions) {
        Assert.notNull(root, "build query, 'root' must not be null");
        Assert.notNull(criteriaBuilder, "build  query, 'criteriaBuilder' must not be null");

        Predicate predicate = builderConditions.accept();
        return criteriaBuilder.or(predicate);
    }



    /**
     * 构造查询条件列表；防止大量if……else……出现，影响程序可读性
     *
     * @param flag       判空标志
     * @param predicates 查询拼接条件列表
     * @param predicate  查询条件
     * @return 拼接条件列表
     */
    default List<Predicate> buildPredicates(boolean flag, List<Predicate> predicates, Predicate predicate) {
        Assert.notNull(predicates, "'predicates' must not be null");
        if (flag) {
            predicates.add(predicate);
            return predicates;
        }
        return predicates;
    }


    /**
     * 构造查询条件列表；防止大量if……else……出现，影响程序可读性
     *
     * @param root 由上级传入
     * @param query 由上级传入
     * @param criteriaBuilder  由上级传入
     * @param builder 自定义查询条件
     * @return 返回构造好的一条查询条件
     */
    default Predicate buildPredicate(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder,BuilderPredicate builder){
        List<Predicate> predicates = new LinkedList<>();
        return query.where(builder.accept(root,query,criteriaBuilder,predicates).toArray(new Predicate[0])).getRestriction();
    }


}
