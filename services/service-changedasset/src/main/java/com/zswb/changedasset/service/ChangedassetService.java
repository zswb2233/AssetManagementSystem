package com.zswb.changedasset.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zswb.model.dto.IndividualZcbdbTreeNode;
import com.zswb.model.dto.sbflbDTO;
import com.zswb.model.entity.zcbdb;
import com.zswb.model.entity.zcbdb2;
import com.zswb.model.vo.zcbdbVO;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 设备变动表服务接口
 */

public interface ChangedassetService extends IService<zcbdb2> {
    //可以自定义业务方法

    /**
     * 获得按不同条件排序的数据
     * @param page
     * @param queryWrapper
     * @param sortFields
     * @param sortOrders
     * @return
     */
    public Map<String, Object> getPageWithQuery(Page<zcbdb2> page, QueryWrapper<zcbdb2> queryWrapper , List<String> sortFields, // 排序字段列表
                                                List<String> sortOrders ,List<String> require  );
    /**
     *
     */


    public Map<String, Object> getPageWithQuerytoOut(Page<zcbdb2> page, QueryWrapper<zcbdb2> queryWrapper , List<String> sortFields, // 排序字段列表
                                                List<String> sortOrders ,List<String> require  );


    IndividualZcbdbTreeNode showIndividualHouseholdinAndDecrease(String tableType, Integer unitLevel, Integer status, Date formDateFrom, Date formDateTo, String accountSet);

    List<sbflbDTO> showIndividualClassificationinAndDecrease(String tableType,  Integer status, Date formDateFrom, Date formDateTo, String accountSet);
}
