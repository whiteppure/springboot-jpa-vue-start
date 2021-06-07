package com.github.springboot.template.model.params;

import lombok.Data;

import javax.validation.constraints.Max;

/**
 * 全局异常日志查询条件
 */
@Data
public class WebLogQuery {

    /**
     * 登陆用户号码 模糊查询
     */
    @Max(value = 5,message = "用户号码最大为5位")
    private String loginName;

    /**
     * 日志内容 模糊查询
     */
    @Max(value = 20,message = "日志内容最大为20位")
    private String logContent;

    /**
     * 日志级别
     */
    private String logLevel;

    /**
     * 操作起始日期
     */
    private String timeBegin;

    /**
     * 操作结束日期
     */
    private String timeEnd;

}
