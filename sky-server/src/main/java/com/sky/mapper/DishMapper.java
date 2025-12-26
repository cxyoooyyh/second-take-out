package com.sky.mapper;

import org.apache.ibatis.annotations.Select;

public interface DishMapper {

    @Select("SELECT count(*) FROM dish WHERE category_id = #{categoryId}")
    int getCountByCategoryId(Long categoryId);
}
