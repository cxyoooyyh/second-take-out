package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.exception.BaseException;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * @author sharkCode
 * @date 2025/12/24 21:33
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private DishMapper dishMapper;

    @Resource
    private SetmealMapper setmealMapper;

    /**
     * 新增菜品分类方法
     * @param categoryDTO
     */
    @Override
    public void save(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);

        // 默认禁用菜品分类状态
        category.setStatus(StatusConstant.DISABLE);

        // 调用 Mapper 映射层方法添加
        categoryMapper.insert(category);
    }

    /**
     * 分页查询菜品分类
     * @param queryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(CategoryPageQueryDTO queryDTO) {
        PageHelper.startPage(queryDTO.getPage(), queryDTO.getPageSize());

        // 调用Mapper层分页查询方法
        Page<Category> page = categoryMapper.pageQuery(queryDTO);

        //封装PageResult对象
        List<Category> result = page.getResult();
        long total = page.getTotal();
        return new PageResult(total, result);
    }

    /**
     * 启动或禁用分类
     * @param status
     * @param id
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        Category category = Category.builder()
                .status(status)
                .id(id)
                .build();
        // 更新 菜品分类 数据
        categoryMapper.update(category);
    }

    /**
     * 更新分类
     * @param categoryDTO
     */
    @Override
    public void update(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);


        // 更新 菜品分类 数据
        categoryMapper.update(category);
    }

    /**
     * 根据分类id删除分类
     * @param id
     */
    @Override
    public void deleteById(Long id) {

        // 查询当前分类是否关联了菜品，如果关联则删除失败
        Integer count = dishMapper.countByCategoryId(id);

        if (count > 0) {
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        }

        // 查询当前分类是否关联了套餐
        count = setmealMapper.countByCategoryId(id);

        if (count > 0) {
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        }

        // 根据 id 删除分类
        categoryMapper.deleteById(id);
    }

    /**
     * 根据类型获取分类列表
     * @param type
     * @return
     */
    @Override
    public List<Category> getListByType(Integer type) {
        return categoryMapper.getListByType(type);
    }
}











