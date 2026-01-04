package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author sharkCode
 * @date 2025/12/24 21:19
 */
@RestController
@RequestMapping("/admin/category")
@Slf4j
@Api(tags = "菜品分类相关接口")
public class CategoryController {
    @Resource
    private CategoryService categoryService;
    @PostMapping
    @ApiOperation("新增菜品分类")
    public Result save(@RequestBody CategoryDTO categoryDTO) {
        categoryService.save(categoryDTO);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("分页查询菜品分类")
    public Result<PageResult> page(CategoryPageQueryDTO queryDTO) {
        PageResult pageResult = categoryService.pageQuery(queryDTO);
        return Result.success(pageResult);
    }

    @PostMapping("/status/{status}")
    @ApiOperation("启动或禁用分类状态")
    public Result startOrStop(@PathVariable("status") Integer status, Long id) {
        categoryService.startOrStop(status, id);
        return Result.success();
    }
    @PutMapping
    @ApiOperation("修改分类数据")
    public Result update(@RequestBody CategoryDTO categoryDTO) {
        categoryService.update(categoryDTO);
        return Result.success();
    }
    @DeleteMapping
    @ApiOperation("根据id删除分类")
    public Result deleteById(Long id) {
        categoryService.deleteById(id);
        return Result.success();
   }

   @GetMapping("/list")
   @ApiOperation("根据类型查询分类")
   public Result<List<Category>> getListByType(Integer type) {
        List<Category> list = categoryService.getListByType(type);
        return Result.success(list);
   }
}



