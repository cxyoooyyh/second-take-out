package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author sharkCode
 * @date 2026/1/5 11:31
 */
@Mapper
public interface DishFlavorMapper {
    /**
     * 批量插入菜品口味数据
     * @param flavors
     */
    void insertBatch(List<DishFlavor> flavors);

    /**
     * 根据 菜品id 查询 口味数据
     * @param dishId
     * @return
     */
    @Select("SELECT * FROM dish_flavor WHERE dish_id = #{dishId}")
    List<DishFlavor> getByDishId(Long dishId);

    /**
     * 根据菜品ids 批量删除菜品口味数据
     * @param ids
     */
    void deleteByDishIds(List<Long> ids);

    /**
     * 根据 菜品id删除 口味数据
     * @param dishId
     */
    @Delete("DELETE FROM dish_flavor WHERE dish_id = #{dishId}")
    void deleteByDishId(Long dishId);
}