package com.zswb.changedasset.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zswb.model.entity.zcbdb;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 设备变动表服务接口
 */

public interface ChangedassetService extends IService<zcbdb> {
    //可以自定义业务方法

    /**
     * 获得按不同条件排序的数据
     * @param page
     * @param queryWrapper
     * @param sortFields
     * @param sortOrders
     * @return
     */
    public Map<String, Object> getPageWithQuery(Page<zcbdb> page, QueryWrapper<zcbdb> queryWrapper ,List<String> sortFields, // 排序字段列表
                                                List<String> sortOrders  );
    /**
     *
     */
    public Map<String, Object> getPageWithCondition(Page<zcbdb> page, QueryWrapper<zcbdb> queryWrapper ,List<String> require);


}
