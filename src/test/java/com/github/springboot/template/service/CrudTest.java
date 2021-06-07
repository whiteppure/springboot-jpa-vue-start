package com.github.springboot.template.service;

import com.github.springboot.template.model.entity.SysWebLogEntity;
import com.github.springboot.template.model.params.WebLogQuery;
import com.github.springboot.template.utils.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CrudTest {

    @Autowired
    private ISysWebLogService logService;

    /**
     * 创建一条异常日志
     */
    @Test
    public void context0() {
        SysWebLogEntity sysWebLogEntity = new SysWebLogEntity();
        sysWebLogEntity.setBroswer("ie");
        sysWebLogEntity.setIp("127.0.0.1");
        sysWebLogEntity.setLoglevel("info");
        sysWebLogEntity.setLogcontent("java.lang.NullPointerException");
        sysWebLogEntity.setLoginname("admin");
        sysWebLogEntity.setOperatetype("3");
        sysWebLogEntity.setOperatetime(DateUtils.dateToStr("yyyyMMddHHmmss"));
        logService.create(sysWebLogEntity);
    }


    /**
     * 分页查询异常日志
     */
    @Test
    public void context1() {
        // 如果page=1 要-1
        PageRequest pageRequest = PageRequest.of(0, 30, Sort.Direction.DESC,"id");
        WebLogQuery webLogQuery = new WebLogQuery();
        Page<SysWebLogEntity> byPage = logService.findList(pageRequest, webLogQuery);

        System.out.println("共" + byPage.getTotalPages() + "页");
        System.out.println("共"+ byPage.getTotalElements() +"条数据");
        System.out.println("当前第" + byPage.getPageable().getPageNumber() + "页");
        System.out.println("是否是最后一页: " + byPage.isLast());
        System.out.println("当前每页" + byPage.getPageable().getPageSize() + "条");
        System.out.println("当前页共" + byPage.getNumberOfElements() + "条");

        List<SysWebLogEntity> content = byPage.getContent();
        System.out.println("==>"+content.size());
        content.forEach(System.out::println);

    }

}
