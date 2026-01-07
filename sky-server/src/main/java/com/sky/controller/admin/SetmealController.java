package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author sharkCode
 * @date 2025/12/26 22:42
 */
@Slf4j
@RestController
@RequestMapping("/admin/setmeal")
@Api(tags = "套餐相关接口")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    @ApiOperation("新增套餐接口")
    @PostMapping
    public Result<String> save(@RequestBody SetmealDTO setmealDTO) {
        setmealService.saveWithDishes(setmealDTO);
        return Result.success();
    }

    @ApiOperation("分页查询套餐")
    @GetMapping("/page")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageResult result = setmealService.pageQuery(setmealPageQueryDTO);
        return Result.success(result);
    }

    @ApiOperation("根据id查询套餐详情")
    @GetMapping("/{id}")
    public Result<SetmealVO> getById(@PathVariable Long id) {
        SetmealVO setmealVO = setmealService.getById(id);
        return Result.success(setmealVO);
    }

    @ApiOperation("根据 (1,2,3)ids 批量删除套餐")
    @DeleteMapping
    public Result<String> delete(@RequestParam List<Long> ids) {
        log.info("参数{}", ids);
        setmealService.deleteBatch(ids);
        return Result.success();
    }

    @ApiOperation("修改套餐数据")
    @PutMapping
    public Result<String> update(@RequestBody SetmealDTO setmealDTO) {
        log.info("套餐dto{}", setmealDTO);
        setmealService.updateWithDish(setmealDTO);
        return Result.success();
    }

    @ApiOperation("套餐起售或停售")
    @PostMapping("/status/{status}")
    public Result<String> startOrStop(@PathVariable Integer status, @RequestParam Long id) {
        setmealService.startOrStop(status, id);
        return Result.success();
    }
}
