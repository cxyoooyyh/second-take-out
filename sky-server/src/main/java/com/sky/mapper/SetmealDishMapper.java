package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.SetmealDish;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    /**
     * 根据菜品ids查询套餐ids
     * @param ids
     * @return
     */
    List<Long> getSetmealIdsByDishIds(List<Long> ids);

    /**
     * 批量插入套餐-菜品关系数据
     * @param dishes
     */
    void insertBatchSetmealDish(List<SetmealDish> dishes);

    /**
     * 根据套餐id查询 套餐-菜品关系
     * @param id
     * @return
     */
    List<SetmealDish> listBySetmealId(Long id);

    /**
     * 根据套餐id删除 套餐-菜品关系
     * @param id
     */
    @Delete("DELETE FROM setmeal_dish WHERE setmeal_id = #{id}")
    void deleteBySetmealId(Long id);

}
