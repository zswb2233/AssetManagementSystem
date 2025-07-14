package com.zswb.changedasset.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zswb.changedasset.convert.Zcbdb2OutputMapper;
import com.zswb.changedasset.dao.ChangedassetDao;
import com.zswb.changedasset.convert.Zcbdb2VoMapper;
import com.zswb.changedasset.dao.DwbDao;
import com.zswb.changedasset.service.ChangedassetService;
import com.zswb.model.dto.IndividualZcbdbTreeNode;
import com.zswb.model.entity.dwb;
import com.zswb.model.entity.zcbdb2;
import com.zswb.model.output.zcbdbOutput;
import com.zswb.model.vo.zcbdbVO;
import com.zswb.util.QueryUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@Slf4j
public class ChangedassetServiceImpl extends ServiceImpl<ChangedassetDao, zcbdb2> implements ChangedassetService {
    @Autowired
    private ChangedassetDao changedassetDao;

    @Autowired
    private Zcbdb2VoMapper zcbdb2VoMapper;

    @Autowired
    private Zcbdb2OutputMapper zcbdb2OutputMapper;

    @Autowired
    private DwbDao dwbDao;

    @Override
    public Map<String, Object> getPageWithQuery(Page<zcbdb2> page, QueryWrapper<zcbdb2> queryWrapper , List<String> sortFields, List<String> sortOrders,List<String> require ) {
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


        // 1. 执行分页查询，获取zcbdb2类型的分页数据
        IPage<zcbdb2> zcbdb2Page = this.page(page, queryWrapper);

        // 2. 将IPage<zcbdb2>转换为IPage<zcbdbVO>
        IPage<zcbdbVO> voPage = zcbdb2Page.convert(zcbdb2VoMapper::toVo);

        // 封装返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("total", voPage.getTotal());
        result.put("pages", voPage.getPages());
        result.put("current", voPage.getCurrent());
        result.put("size", voPage.getSize());
        result.put("records", voPage.getRecords());

        return result;
    }

    @Override
    public Map<String, Object> getPageWithQuerytoOut(Page<zcbdb2> page, QueryWrapper<zcbdb2> queryWrapper, List<String> sortFields, List<String> sortOrders, List<String> require) {
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


        // 1. 执行分页查询，获取zcbdb2类型的分页数据
        IPage<zcbdb2> zcbdb2Page = this.page(page, queryWrapper);

        // 2. 将IPage<zcbdb2>转换为IPage<zcbdbOutput>
        IPage<zcbdbOutput> voPage = zcbdb2Page.convert(zcbdb2OutputMapper::toOutput);

        // 封装返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("total", voPage.getTotal());
        result.put("pages", voPage.getPages());
        result.put("current", voPage.getCurrent());
        result.put("size", voPage.getSize());
        result.put("records", voPage.getRecords());

        return result;
    }

    @Override
    public IndividualZcbdbTreeNode showIndividualHouseholdinAndDecrease(String tableType, Integer unitLevel, Integer status, Date formDateFrom, Date formDateTo, String accountSet) {
        //分户增减变动统计表数据返回
        //1.创建根节点。
        //TODO 应该写一个静态类表示，并且该类与数据库里的数据对应（如何设计）
        IndividualZcbdbTreeNode root=new IndividualZcbdbTreeNode("00","北京化工大学");
        //2.找到在formDateFrom之前的数据。需要在在变动账表里找.
        //期初数=期末数−本期增加数+本期减少数
        QueryWrapper<zcbdb2> queryWrapper=new QueryWrapper<>();
        // 按时间范围查询：变动日期在 formDateFrom 和 formDateTo 之间
        // 按时间范围查询：变动日期 >= formDateFrom 且 <= formDateTo
        queryWrapper.ge("change_date", formDateFrom);  // 大于等于起始日期
        queryWrapper.le("change_date", formDateTo);    // 小于等于结束日期

        // 可选：根据状态选择不同业务，但是现在仅仅一个
//        if (status != null && status != -1) {
//
//        }
//
//        // 可选：根据账套选择不同业务，但是现在仅仅一个
//        if (accountSet != null && !accountSet.isEmpty()) {
//
//        }

        // 3. 执行查询
        List<zcbdb2> assetChanges = changedassetDao.selectList(queryWrapper);

        // 4. 根据数据从最小往大求，*开始。
        //
        return buildTreeStructure(assetChanges);

    }
    private IndividualZcbdbTreeNode buildTreeStructure(List<zcbdb2> assetChanges ){
        //建立单位表树状关系
        IndividualZcbdbTreeNode root=buildInitTreeStructure();

        //算所有子节点的值
        for(zcbdb2 temp:assetChanges){
            //先创建完子类，然后往外递归。
            //获得状态，不同状态做不同处理
            String status=temp.getStatus();
            softDifStatus(status,temp,root);
        }
        //倒数第二层处理：现在List<zcbdb2> assetChanges内的所有数据都储存在root里面了
        //递归处理。
        // 3. 从根节点开始递归汇总所有子节点数据到父节点
        root.sumChildrenData();

        return root;

    }
    private void softDifStatus(String status, zcbdb2 temp, IndividualZcbdbTreeNode root) {
        // 获取单位编码，用于查找树节点
        String unitCode = temp.getUsingUnitCode();
        if (unitCode == null || unitCode.isEmpty()) {
            return; // 单位编码为空，无法处理
        }

        // 查找对应的树节点
        IndividualZcbdbTreeNode node = findNodeByUnitCode(root, unitCode);
        if (node == null) {
            return; // 未找到对应节点
        }



        // 根据不同状态处理资产变动
        switch (status) {
            case "1": // 在用
                // 通常在用状态不涉及数量/金额变动，可能仅更新资产状态

                break;

            case "2": // 闲置
                // 闲置状态不涉及数量/金额变动，可能仅更新资产状态

                break;

            case "3": // 待修
                // 待修状态不涉及数量/金额变动，可能仅更新资产状态
                break;

            case "4": // 待报废
                // 待报废状态不涉及数量/金额变动，可能仅更新资产状态
                break;

            case "5": // 丢失
                // 丢失通常作为资产减少处理
                updateIncreaseData(node,temp,7);
                break;

            case "6": // 报废
                // 报废处理，更新减少数据
                updateIncreaseData(node,temp,5);
                break;

            case "7": // 出售
                // 出售处理，更新减少数据
                updateIncreaseData(node,temp,7);
                break;

            case "9": // 其它
                // 其它变动，根据实际情况处理
                //这里只写了减少的
                updateIncreaseData(node,temp,7);
                break;

            case "A": // 调入
                // 调入处理，更新增加数据
                updateIncreaseData(node,temp,3);
                break;

            case "B": // 转入
                // 转入处理，更新增加数据
                updateIncreaseData(node,temp,2);
                break;

            case "C": // 转出
                // 转出处理，更新减少数据
                updateIncreaseData(node,temp,6);
                break;

            case "D": // 注销
                // 注销处理，更新减少数据
                updateIncreaseData(node,temp,7);
                break;

            case "E": // 盘亏
                // 盘亏处理，更新减少数据
                updateIncreaseData(node,temp,7);
                break;

            case "F": // 调剂
                // 调剂处理，可能需要特殊处理，这里简化为增加
                updateIncreaseData(node,temp,3);
                break;

            case "G": // 对外捐赠
                // 对外捐赠处理，更新减少数据
                updateIncreaseData(node,temp,7);
                break;

            default:
                // 未知状态，记录日志或其他处理
                break;
        }
    }
    // 在树中查找节点的方法
    private IndividualZcbdbTreeNode findNodeByUnitCode(IndividualZcbdbTreeNode root, String unitCode) {
        if (root == null || unitCode == null || unitCode.isEmpty()) {
            return null;
        }

        // 如果当前节点匹配，直接返回
        if (root.getUnitCode().equals(unitCode)) {
            return root;
        }

        // 递归查找子节点
        if (root.getChildren() != null && !root.getChildren().isEmpty()) {
            for (IndividualZcbdbTreeNode child : root.getChildren()) {
                IndividualZcbdbTreeNode found = findNodeByUnitCode(child, unitCode);
                if (found != null) {
                    return found;
                }
            }
        }

        return null; // 未找到
    }
    // 更新增加数据的辅助方法
    private void updateIncreaseData(IndividualZcbdbTreeNode node, zcbdb2 temp,int index) {
        BigDecimal before_count=temp.getBeforeCount();
        BigDecimal changed_count=temp.getChangedCount();
        BigDecimal before_amount=temp.getAmount();
        BigDecimal changed_amount=temp.getChangedAmount();
        //0
        node.addNowDeCountWithId(0,before_count);
        node.addNowDeAmountWithId(0,before_amount);
        //index
        node.addNowDeCountWithId(index,changed_count);
        node.addNowDeAmountWithId(index,changed_amount);


        //4 合计
        for(int i=1;i<4;i++){
            node.addNowDeCountWithId(i,node.getNowDeCount()[i]);
            node.addNowDeAmountWithId(i,node.getNowDeAmount()[i]);
        }

        //8 合计
        for(int i=5;i<8;i++){
            node.addNowDeCountWithId(i,node.getNowDeCount()[i]);
            node.addNowDeAmountWithId(i,node.getNowDeAmount()[i]);
        }
        //9 期末
        node.addNowDeCountWithId(9,node.getNowDeCount()[4]);
        node.addNowDeCountWithId(9,node.getNowDeCount()[8]);
        node.addNowDeAmountWithId(9,node.getNowDeCount()[4]);
        node.addNowDeAmountWithId(9,node.getNowDeCount()[8]);

    }



    /**
     * 构建单位树形结构
     * @return 根节点
     */
    private IndividualZcbdbTreeNode buildInitTreeStructure() {
        // 获取所有单位数据
        List<dwb> dwbList = dwbDao.selectList(null);

        // 用于快速查找节点的映射表
        Map<String, IndividualZcbdbTreeNode> nodeMap = new HashMap<>();

        // 根节点（默认为空或根据业务规则设置）
        IndividualZcbdbTreeNode rootNode = null;

        // 第一遍遍历：创建所有节点并放入映射表
        for (dwb unit : dwbList) {
            IndividualZcbdbTreeNode node = new IndividualZcbdbTreeNode(unit.getUnitCode(), unit.getUnitName());
            nodeMap.put(unit.getUnitCode(), node);
        }

        // 第二遍遍历：构建父子关系
        for (dwb unit : dwbList) {
            IndividualZcbdbTreeNode currentNode = nodeMap.get(unit.getUnitCode());

            // 如果当前节点不存在（理论上不会发生）
            if (currentNode == null) {
                continue;
            }

            // 处理父节点
            String parentCode = unit.getParentUnitCode(); // 上级单位代码

            // 如果父节点为空或为自身，则视为根节点
            if (parentCode == null || parentCode.isEmpty() || parentCode.equals(unit.getUnitCode())) {
                if (rootNode == null) {
                    //理论上只有根：大学才是根节点
                    rootNode = currentNode;
                }
                continue;
            }

            // 获取父节点
            IndividualZcbdbTreeNode parentNode = nodeMap.get(parentCode);

            // 如果父节点存在，建立父子关系
            if (parentNode != null) {
                // 设置子节点的父引用
                currentNode.setParent(parentNode);

                // 初始化父节点的子列表（如果不含该条）
                if (parentNode.getChildren() == null) {
                    parentNode.setChildren(new ArrayList<>());
                }

                // 将当前节点添加为子节点
                parentNode.getChildren().add(currentNode);
            } else {
                // 如果父节点不存在，将当前节点作为根节点
                if (rootNode == null) {
                    rootNode = currentNode;
                }
            }
        }

        // 如果没有找到根节点，创建一个默认根节点
        if (rootNode == null) {
            rootNode = new IndividualZcbdbTreeNode("0000", "顶级单位");
        }

        return rootNode;
    }

    //可以实现自定义业务逻辑
    private boolean isValidSortField(String field) {
        // 允许参与排序的字段（对应数据库表中的下划线字段）
        return Set.of(
                "using_unit_code", "using_unit_name", "equipment_code", "category_code",
                "equipment_name", "brand_model", "specification", "amount",
                "jldw", "Cj", "Ggrq", "status",
                "jfkm", "storage_location_code", "storage_location_name",  // 注：storage_location_name在实体类中未定义，需确认是否存在
                "user_code", "user", "handler", "audit_status", "business_number",
                "accountant", "accounting_date", "remarks", "inputter", "input_date",  // 注：inputter、input_date在实体类中未定义，需确认是否存在
                "change_date", "change_reason", "transfer_in_unit_code", "applicant_code", "applicant"
        ).contains(field);
    }

}
