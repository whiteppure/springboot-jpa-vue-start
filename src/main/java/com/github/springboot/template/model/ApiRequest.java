package com.github.springboot.template.model;

import com.github.springboot.template.utils.ObjectUtils;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * 公共请求对象
 * @param <E> 实体参数 entity 类型
 * @param <Q> 请求参数查询条件  queryCondition 查询条件
 */
@Slf4j
@ToString
@Data
public class ApiRequest<E,Q> {


    /**
     * 分页查询条件 当前页
     */
    @Setter
    @Min(value = 0, message = "查询条件当前页最小为0")
    private Integer current;

    /**
     * 每页数量
     */
    @Setter
    @Max(value = 50, message = "查询条件每页数量最大为50条")
    private Integer size;

    /**
     * 排序方式
     * - ASC 正序
     * - DESC 倒序
     */
    @Setter
//    @Pattern(regexp = "/(ASC|DESC)/", message = "排序方式可选值：ASC、DESC")
    private String sortWay;

    /**
     * 排序字段 根据什么排序
     */
    @Setter
    @Getter
    private String[] sortFile;
    /**
     * 实体 保存、修改 时使用
     */
    @Nullable
    private E entity;
    /**
     * 修改、删除 时使用
     */
    private String primaryKey;
    /**
     *
     */
    @Nullable
    private Q queryCondition;
    /**
     * 保留字段
     */
    @Nullable
    private String recv;

    public String getSortWay() {
        return ObjectUtils.isEmpty(sortWay) ? "DESC" : sortWay;
    }

    /**
     * 获取当前页
     *
     * @return current
     */
    public Integer getCurrent() {
        final Integer firstPage = 0;
        if (ObjectUtils.isEmpty(current)) {
            return firstPage;
        }
        //  首页为0，oracle 数据库 rowNum = 0 为第一页
        return firstPage >= current ? firstPage : (current - 1);
    }

    /**
     * 获取每页数量
     *
     * @return size
     */
    public Integer getSize() {
        return ObjectUtils.isEmpty(size) ? 10 : size;
    }


    /**
     * 获取分页对象
     *
     * @return page
     */
    public PageRequest getPage() {
        Sort sort = ObjectUtils.isEmpty(getSortWay()) || ("DESC".equals(getSortWay())) ?
                Sort.by(Sort.Direction.DESC, getSortFile()) :
                Sort.by(Sort.Direction.ASC, getSortFile());
        return PageRequest.of(getCurrent(), getSize(), sort);
    }


}
