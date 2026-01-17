package com.sky.service;

import com.sky.result.Result;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;

import java.time.LocalDateTime;

/**
 * @author sharkCode
 * @date 2026/1/17 00:33
 */
public interface WorkspaceService {
    BusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end);

    SetmealOverViewVO getSetmealOverview();

    DishOverViewVO getDishOverview();

    OrderOverViewVO getOrderOverview();
}
