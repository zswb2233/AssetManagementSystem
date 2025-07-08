package com.zswb.changedasset.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage; // 新增：IPage 接口导入
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zswb.model.entity.zcbdb;
import com.zswb.changedasset.service.ChangedassetService;
// 新增：导入 models 模块的实体类
import com.zswb.util.QueryUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/api/changedasset")
public class ChangedassetController {

    @Autowired
    ChangedassetService changedassetService;
    //排序查询以及默认查询
    @GetMapping("/page")
    public Map<String, Object> getEquipmentPage(
            @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) List<String> sortFields, // 排序字段列表
            @RequestParam(required = false) List<String> sortOrders   // 排序方向列表
    ) {
        // 创建分页对象
        Page<zcbdb> page = new Page<>(pageNum, pageSize);
        QueryWrapper<zcbdb> queryWrapper = new QueryWrapper<>();

       return  changedassetService.getPageWithQuery(page, queryWrapper, sortFields, sortOrders);
    }

    // 安全校验：排序字段白名单（防止SQL注入）

    //条件语句查询
    @GetMapping("/require")
    public Map<String,Object> getEquipmentbyRequire(
            @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) List<String> require
    ){

        //分页
        // 创建分页对象
        Page<zcbdb> page = new Page<>(pageNum, pageSize);
        QueryWrapper<zcbdb> queryWrapper = new QueryWrapper<>();

        return  changedassetService.getPageWithCondition(page, queryWrapper, require);


    }
    //TODO 前端展示的数据可以双击进入详细页面

    //ToDo 明细表:报废明细表 校内调拨明细表 分户增减表 分类增减表 需要使用itext,已下载依赖

}