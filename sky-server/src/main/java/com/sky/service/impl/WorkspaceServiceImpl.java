package com.sky.service.impl;

import com.sky.constant.StatusConstant;
import com.sky.entity.Orders;
import com.sky.mapper.DishMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sharkCode
 * @date 2026/1/17 00:34
 */
@Service
public class WorkspaceServiceImpl implements WorkspaceService {

    @Autowired
    private OrderMapper orderMapper;


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private DishMapper dishMapper;

    /**
     * 获取今日营业数据
     * @param begin
     * @param end
     * @return
     */
    @Override
    public BusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end) {

        // 新增用户数
        Map map = new HashMap<>();
        map.put("begin", begin);
        map.put("end", end);
        Integer newUsers = userMapper.countByMap(map);

        // 当日营业额
        map.put("status", Orders.COMPLETED);
        Double turnover = orderMapper.sumByMap(map);
        turnover = turnover == null ? 0.0 : turnover;

        // 总订单数
        Integer allOrderCount = orderMapper.countByMap(map);
        // 有效订单数
        Integer validOrderCount = orderMapper.countByMap(map);

        // 订单完成率 平均客单价
        Double orderCompletionRate = 0.0;
        Double unitPrice = 0.0;
        if (allOrderCount != 0 && validOrderCount != 0) {
            // 订单完成率
            orderCompletionRate = validOrderCount.doubleValue() / allOrderCount;

            // 平均客单价
            unitPrice = turnover / validOrderCount;
        }

        return BusinessDataVO.builder()
                .newUsers(newUsers)
                .turnover(turnover)
                .orderCompletionRate(orderCompletionRate)
                .validOrderCount(validOrderCount)
                .unitPrice(unitPrice)
                .build();
    }

    /**
     * 获取菜品总览
     * @return
     */
    @Override
    public SetmealOverViewVO getSetmealOverview() {
        Map map = new HashMap();
        map.put("status", StatusConstant.ENABLE); // 起售
        Integer soldCount = setmealMapper.countByMap(map);
        map.put("status", StatusConstant.DISABLE); // 停售
        Integer discontinuedCount = setmealMapper.countByMap(map);
        return SetmealOverViewVO.builder()
                .discontinued(discontinuedCount)
                .sold(soldCount)
                .build();
    }

    /**
     * 获取菜品总览
     * @return
     */
    @Override
    public DishOverViewVO getDishOverview() {
        Map map = new HashMap();
        map.put("status", StatusConstant.ENABLE); // 起售
        Integer soldCount = dishMapper.countByMap(map);
        map.put("status", StatusConstant.DISABLE); // 停售
        Integer discontinuedCount = dishMapper.countByMap(map);
        return DishOverViewVO.builder()
                .discontinued(discontinuedCount)
                .sold(soldCount)
                .build();
    }

    /**
     * 获取订单总览
     * @return
     */
    @Override
    public OrderOverViewVO getOrderOverview() {

        Map map = new HashMap();
        map.put("start", LocalDateTime.now().with(LocalTime.MIN));
        Integer allCount = orderMapper.countByMap(map);
        map.put("status", Orders.TO_BE_CONFIRMED);
        Integer waitingCount = orderMapper.countByMap(map);
        map.put("status", Orders.CONFIRMED);
        Integer deliveredCount = orderMapper.countByMap(map);
        map.put("status", Orders.COMPLETED);
        Integer completedCount = orderMapper.countByMap(map);
        map.put("status", Orders.CANCELLED);
        Integer cancelledCount = orderMapper.countByMap(map);


        return OrderOverViewVO.builder()
                .waitingOrders(waitingCount)
                .deliveredOrders(deliveredCount)
                .completedOrders(completedCount)
                .cancelledOrders(cancelledCount)
                .allOrders(allCount)
                .build();
    }
}



























