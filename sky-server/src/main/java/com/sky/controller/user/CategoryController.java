package com.sky.controller.user;

import com.sky.entity.Category;
import com.sky.mapper.CategoryMapper;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author sharkCode
 * @date 2026/1/13 14:28
 */
@RestController("userCategoryController")
@RequestMapping("/user/category")
@Api(tags = "分类相关接口")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @ApiOperation("根据类型查询分类列表")
    @GetMapping("/list")
    public Result<List<Category>> list(Integer type) {
        log.info("查询分类列表: {}", type);
        List<Category> list = categoryService.getListByType(type);
        return Result.success(list);
    }
}
