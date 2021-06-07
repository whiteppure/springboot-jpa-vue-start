package com.github.springboot.template.controller;

import com.github.springboot.template.annotation.Facade;
import com.github.springboot.template.model.ApiRequest;
import com.github.springboot.template.model.ApiResponse;
import com.github.springboot.template.model.entity.SysWebLogEntity;
import com.github.springboot.template.model.params.WebLogQuery;
import com.github.springboot.template.service.ISysWebLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;



/**
 * 全局异常controller
 */
@Slf4j
@RestController
@RequestMapping("/weblog/")
public class SysWebLogController {

    private final ISysWebLogService sysWebLogService;

    public SysWebLogController(ISysWebLogService sysWebLogService) {
        this.sysWebLogService = sysWebLogService;
    }


    /**
     * 分页条件查询
     *
     * @return ApiResponse
     */
    @Facade
    @PostMapping("/page")
    public ApiResponse findListByPage(@RequestBody ApiRequest<SysWebLogEntity, WebLogQuery> request) {
        return ApiResponse.ofSuccess(sysWebLogService.findList(
                request.getPage(),
                request.getQueryCondition()
        ));
    }


    /**
     * 不分页条件查询
     *
     * @return ApiResponse
     */
    @Facade
    @PostMapping("/list")
    public ApiResponse getListNoPage(@RequestBody ApiRequest<SysWebLogEntity,WebLogQuery> request) {
        return ApiResponse.ofSuccess(sysWebLogService.findList(
                request.getQueryCondition()
        ));
    }



}
