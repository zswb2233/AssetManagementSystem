package com.zswb.changedasset.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zswb.changedasset.dao.ChangedassetDao;
import com.zswb.changedasset.service.ChangedassetService;
import com.zswb.model.entity.zcbdb;
import com.zswb.util.QueryUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
public class ChangedassetServiceImpl extends ServiceImpl<ChangedassetDao, zcbdb> implements ChangedassetService {
    @Autowired
    private ChangedassetDao changedassetDao;



    @Override
    public Map<String, Object> getPageWithQuery(Page<zcbdb> page, QueryWrapper<zcbdb> queryWrapper ,List<String> sortFields, List<String> sortOrders) {
        // 动态添加排序条件（安全过滤）
        if (sortFields != null && !sortFields.isEmpty()) {
            for (int i = 0; i < sortFields.size(); i++) {
                String field = sortFields.get(i);
                String order = i < sortOrders.size() ? sortOrders.get(i) : "asc"; // 默认升序

                // 安全校验：仅允许白名单中的字段排序（防止SQL注入）
                if (isValidSortField(field)) {
                    queryWrapper.orderBy(true, "asc".equalsIgnoreCase(order), field);
                } else {
                    log.warn("非法排序字段: {}", field);
                }
            }
        }

        // 执行分页查询
        IPage<zcbdb> equipmentPage = this.page(page, queryWrapper);

        // 封装返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("total", equipmentPage.getTotal());
        result.put("pages", equipmentPage.getPages());
        result.put("current", equipmentPage.getCurrent());
        result.put("size", equipmentPage.getSize());
        result.put("records", equipmentPage.getRecords());

        return result;
    }

    @Override
    public Map<String, Object> getPageWithCondition(Page<zcbdb> page, QueryWrapper<zcbdb> queryWrapper, List<String> require) {
        Map<String, Object> queryParams  = new HashMap<>();
        queryParams = QueryUtils.buildQueryParams(require);
        for (Map.Entry<String, Object> entry : queryParams.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            // 根据键的后缀确定操作符
            if (key.endsWith("_gt")) {
                String field = key.substring(0, key.length() - 3);
                queryWrapper
                        .gt(field, value);
            } else if (key.endsWith("_gte")) {
                String field = key.substring(0, key.length() - 4);
                queryWrapper
                        .ge(field, value);
            } else if (key.endsWith("_lt")) {
                String field = key.substring(0, key.length() - 3);
                queryWrapper
                        .lt(field, value);
            } else if (key.endsWith("_lte")) {
                String field = key.substring(0, key.length() - 4);
                queryWrapper
                        .le(field, value);
            } else {
                // 默认使用等于操作符
                queryWrapper
                        .eq(key, value);
            }
        }

        // 执行分页查询
        IPage<zcbdb> equipmentPage = this.page(page, queryWrapper);

        // 封装返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("total", equipmentPage.getTotal());
        result.put("pages", equipmentPage.getPages());
        result.put("current", equipmentPage.getCurrent());
        result.put("size", equipmentPage.getSize());
        result.put("records", equipmentPage.getRecords());

        return result;



    }

    //可以实现自定义业务逻辑

    private boolean isValidSortField(String field) {
        // 根据你的实体类字段，允许以下字段参与排序
        return Set.of(
                "usingUnitCode", "usingUnitName", "equipmentCode", "categoryCode",
                "equipmentName", "brandModel", "specification", "amount",
                "measurementUnit", "manufacturer", "purchaseDate", "status",
                "fundSubject", "storageLocationCode", "storageLocationName",
                "userCode", "user", "handler", "auditStatus", "businessNumber",
                "accountant", "accountingDate", "remarks", "inputter", "inputDate",
                "changeDate", "changeReason", "transferInUnitCode", "applicantCode", "applicant"
        ).contains(field);
    }

}
