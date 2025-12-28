package com.sky.mapper;

import org.apache.ibatis.annotations.Select;

public interface SetmealMapper {

    @Select("SELECT count(*) FROM setmeal WHERE category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);
}
