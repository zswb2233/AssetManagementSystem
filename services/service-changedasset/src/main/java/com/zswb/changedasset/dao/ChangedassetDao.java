package com.zswb.changedasset.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zswb.model.entity.zcbdb;
import com.zswb.model.entity.zcbdb2;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChangedassetDao extends BaseMapper<zcbdb2> {
    // 自定义 SQL 方法
}
