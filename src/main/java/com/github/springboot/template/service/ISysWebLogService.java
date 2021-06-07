package com.github.springboot.template.service;

import com.github.springboot.template.model.entity.SysWebLogEntity;
import com.github.springboot.template.model.params.WebLogQuery;
import com.github.springboot.template.service.base.CrudService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 全局异常日志接口
 */
public interface ISysWebLogService extends CrudService<SysWebLogEntity, Long> {


    /**
     * 分页查询
     *
     * @param pageable 分页对象
     * @param query    查询条件
     * @return 分页对象
     */
    Page<SysWebLogEntity> findList(Pageable pageable, WebLogQuery query);


    /**
     * 不分页条件查询 查询全部
     *
     * @param query 查询条件
     * @return list
     */
    List<SysWebLogEntity> findList(WebLogQuery query);


    /**
     * 保存一条异常日志信息
     * @param exMessage 错误信息，其他信息默认
     */
    void create(String exMessage);


}
