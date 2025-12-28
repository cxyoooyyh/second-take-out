package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;

import java.util.List;

public interface CategoryService {
    void save(CategoryDTO categoryDTO);

    PageResult pageQuery(CategoryPageQueryDTO queryDTO);

    void startOrStop(Integer status, Long id);

    void update(CategoryDTO categoryDTO);

    void deleteById(Long id);

    List<Category> getListByType(Integer type);
}
