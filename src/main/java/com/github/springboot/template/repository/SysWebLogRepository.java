package com.github.springboot.template.repository;

import com.github.springboot.template.model.entity.SysWebLogEntity;
import com.github.springboot.template.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SysWebLogRepository extends BaseRepository<SysWebLogEntity,Long>, JpaSpecificationExecutor<SysWebLogEntity> {
}
