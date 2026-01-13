package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealMapper {
    /**
     * 根据分类id查询套餐数量
     * @param categoryId
     * @return
     */
    @Select("SELECT count(id) FROM setmeal WHERE category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 插入套餐数据
     * @param setmeal
     */
    @AutoFill(OperationType.INSERT)
    void insert(Setmeal setmeal);

    /**
     * 分页查询套餐和数据
     * @param pageDTO
     * @return
     */
    Page<SetmealVO> pageQuery(SetmealPageQueryDTO pageDTO);

    /**
     * 根据套餐id查询 套餐数据
     * @param id
     * @return
     */
    Setmeal getById(Long id);

    /**
     * 根据套餐id删除 套餐
     * @param id
     */
    @Delete("DELETE FROM setmeal WHERE id = #{id}")
    void deleteById(Long id);

    /**
     * 根据套餐id更新套餐数据
     * @param setmeal
     */
    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);

    /**
     * 条件查询套餐列表
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 根据套餐id查询 菜品列表项目
     * @param setmealId
     * @return
     */
    @Select("SELECT sd.name, sd.copies, d.image, d.description FROM setmeal_dish sd LEFT JOIN dish d ON sd.dish_id = d.id " +
            "WHERE sd.setmeal_id = #{setmealId}")
    List<DishItemVO> getDishItemBySetmealId(Long setmealId);
}
