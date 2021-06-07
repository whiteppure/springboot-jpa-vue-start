package com.github.springboot.template.service.impl;

import com.github.springboot.template.model.entity.SysWebLogEntity;
import com.github.springboot.template.model.params.WebLogQuery;
import com.github.springboot.template.service.base.AbstractCrudService;
import com.github.springboot.template.utils.BrowseUtils;
import com.github.springboot.template.utils.DateUtils;
import com.github.springboot.template.utils.IPUtils;
import com.github.springboot.template.utils.ObjectUtils;
import com.github.springboot.template.repository.SysWebLogRepository;
import com.github.springboot.template.service.ISysWebLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class SysWebLogServiceImpl extends AbstractCrudService<SysWebLogEntity, Long> implements ISysWebLogService {

    private final SysWebLogRepository logRepository;

    private final HttpServletRequest request;

    protected SysWebLogServiceImpl(SysWebLogRepository logRepository, HttpServletRequest request) {
        super(logRepository);
        this.logRepository = logRepository;
        this.request = request;
    }


    @Override
    @NonNull
    public Page<SysWebLogEntity> findList(Pageable pageable, WebLogQuery query) {
        Assert.notNull(pageable, "Page info must not be null");

        // list by page
        return logRepository.findAll(buildQuery(query), pageable);
    }


    @Override
    @NonNull
    public List<SysWebLogEntity> findList(WebLogQuery query) {
        Assert.notNull(query, "WebLogQuery must not be null");

        // all data
        return logRepository.findAll(buildQuery(query));
    }

    @Override
    public void create(String exMessage) {
        // set param
        final String logLevel = "info";
        final String logType = "3";
        final String oprLoginName = "admin";

        SysWebLogEntity logEntity = new SysWebLogEntity();
        logEntity.setLoginname(oprLoginName);
        logEntity.setBroswer(BrowseUtils.getBrowse(request));
        logEntity.setIp(IPUtils.getSimpleIpAddress());
        logEntity.setLogcontent(exMessage);
        logEntity.setLoglevel(logLevel);
        logEntity.setOperatetype(logType);
        logEntity.setOperatetime(DateUtils.dateToStr(DateUtils.UNSIGNED_DATETIME_PATTERN));

        // save
        logRepository.save(logEntity);
    }

    @NonNull
    private Specification<SysWebLogEntity> buildQuery(WebLogQuery entity) {
        Assert.notNull(entity, "WebLogQuery must not be null");

        return (Specification<SysWebLogEntity>) (root, query, criteriaBuilder) ->
                buildPredicate(root, query, criteriaBuilder, (root1, query1, criteriaBuilder1, predicates) -> {

                            // 用户号码
                            predicates = buildPredicates(
                                    ObjectUtils.isNotEmpty(entity.getLoginName()),
                                    predicates,
                                    buildLikeQuery(root, criteriaBuilder, "%%%s%%", entity.getLoginName(), "loginname")
                            );

                            // 操作时间
                            predicates = buildPredicates(
                                    (ObjectUtils.isNotEmpty(entity.getTimeBegin()) && ObjectUtils.isNotEmpty(entity.getTimeEnd())),
                                    predicates,
                                    buildBetweenQuery(root, criteriaBuilder, entity.getTimeBegin(), entity.getTimeEnd(), "operatetime")
                            );
                            return predicates;
                        }
                );
    }
}
