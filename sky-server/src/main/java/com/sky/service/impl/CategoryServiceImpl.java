package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.mapper.CategoryMapper;
import com.sky.result.PageResult;
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

    /**
     * 新增菜品分类方法
     * @param categoryDTO
     */
    @Override
    public void save(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);

        // TODO 设置其余属性
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
     * @param id
     */
    @Override
    public void deleteById(Long id) {
        // TODO 查询分类是否含有套餐或菜品


        categoryMapper.deleteById(id);
    }
}
