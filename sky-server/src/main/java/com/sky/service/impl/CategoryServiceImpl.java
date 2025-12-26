package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.exception.BaseException;
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
        // 初始化创建时间和更新时间
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        // 初始化创建人和修改人
        Long currentId = BaseContext.getCurrentId();
        category.setCreateUser(currentId);
        category.setUpdateUser(currentId);


        // 调用 Mapper 映射层方法添加
        categoryMapper.save(category);
    }

    /**
     * 分页查询菜品分类
     * @param queryDTO
     * @return
     */
    @Override
    public PageResult page(CategoryPageQueryDTO queryDTO) {
        PageHelper.startPage(queryDTO.getPage(), queryDTO.getPageSize());

        // 调用Mapper层分页查询方法
        Page<Category> page = categoryMapper.page(queryDTO);

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
                .id(id).build();
        category.setUpdateUser(BaseContext.getCurrentId());
        category.setUpdateTime(LocalDateTime.now());
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

        category.setUpdateUser(BaseContext.getCurrentId());
        category.setUpdateTime(LocalDateTime.now());

        // 更新 菜品分类 数据
        categoryMapper.update(category);
    }

    /**
     * 根据分类id删除分类
     * @param id
     */
    @Override
    public Result deleteById(Long id) {

        Category category = categoryMapper.getById(id);
        if (category == null) {
            throw new BaseException("分类id为空");
        }
        Long categoryId = category.getId();
        int count = 0;
        if (categoryId == 1) {
            // 查询菜品数量
            count = dishMapper.getCountByCategoryId(categoryId);
        } else if (categoryId == 2) {
            // 差评套餐数量
            count = setmealMapper.getCountByCategoryId(categoryId);
        }
        if (count != 0) {
            return Result.error("删除失败，分类下不为空");
        }
        categoryMapper.deleteById(id);
        return Result.success();
    }
}