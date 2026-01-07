package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * @author sharkCode
 * @date 2025/12/26 22:44
 */
@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    /**
     * 新增菜品
     * @param dishDTO
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {

        // 新增菜品数据
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        // 默认关闭菜单起售
        dish.setStatus(StatusConstant.DISABLE);

        // 插入一条菜品数据
        dishMapper.insert(dish);

        Long dishId = dish.getId();

        // 新增菜品口味数据
        // 批量插入菜品口味数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
            flavors.forEach(flavor -> flavor.setDishId(dishId));
            //批量插入口味数据
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    /**
     * 根据起售状态，菜品名称，分类id 分页查询菜品
     * @param dishPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());

        Page<DishVO> result = dishMapper.pageQuery(dishPageQueryDTO);

        // 封装返回对象
        PageResult pageResult = new PageResult();
        pageResult.setRecords(result.getResult());
        pageResult.setTotal(result.getTotal());
        return pageResult;
    }

    /**
     * 根据分类id查询 菜品列表
     * @param categoryId
     * @return
     */
    @Override
    public List<Dish> getByCategoryId(Long categoryId) {

        List<Dish> list = dishMapper.getByCategoryId(categoryId);

        return list;
    }

    /**
     * 根据菜品id查询菜品详情和口味数据
     * @param id
     * @return
     */
    @Override
    public DishVO getByIdWithFlavor(Long id) {
        // 查询菜品
        Dish dish = dishMapper.getById(id);

        // 查询菜品关联的口味
        List<DishFlavor> list = dishFlavorMapper.getByDishId(id);

        // 将查询到的数据封装到 VO
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish ,dishVO);
        dishVO.setFlavors(list);
        return dishVO;
    }

    /**
     * 格局status起售或者停售菜品
     * @param status
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        Dish dish = new Dish();
        dish.setStatus(status);
        dish.setId(id);
        dishMapper.update(dish);
    }

    /**
     * 更新菜品数据
     * @param dishDTO
     */
    @Override
    @Transactional // 开启事务
    public void updateWithFlavor(DishDTO dishDTO) {
        // 更新菜品数据
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        // 修改菜品表基本数据
        dishMapper.update(dish);

        // 删除原有口味数据
        Long id = dishDTO.getId();
        dishFlavorMapper.deleteByDishId(id);
        // 重新插入口味数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(id);
            });
            // 向口味表插入n条数据
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    /**
     * 批量删除菜品
     * @param ids
     */
    @Override
    @Transactional //开启事务
    public void deleteBatch(List<Long> ids) {
        // 1. 校验菜品是否处于起售状态
        for (Long id : ids) {
            Dish dish = dishMapper.getById(id);
            if (dish != null && dish.getStatus().equals(StatusConstant.ENABLE)) { // 假设状态常量ENABLE=1
                //当前菜品处于起售中，不能删除
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        // 2. 校验菜品是否被套餐关联
        List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(ids);
        if (setmealIds != null && !setmealIds.isEmpty()) {
            // 当前菜品已经被套餐关联，无法删除
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        for (Long id : ids) {
            // 根据菜品id删除
            dishMapper.deleteById(id);

            dishFlavorMapper.deleteByDishId(id);
        }


//        // 3. 批量删除菜品数据
//        dishMapper.deleteByIds(ids);
//
//        // 4. 批量删除关联的口味数据
//        dishFlavorMapper.deleteByDishIds(ids);
    }
}
