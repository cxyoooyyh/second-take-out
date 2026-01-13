package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * @author sharkCode
 * @date 2025/12/26 22:44
 */
@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Autowired
    private DishMapper dishMapper;

    /**
     * 保存 套餐和套餐菜品接口
     * @param setmealDTO
     */
    @Override
    @Transactional
    public void saveWithDishes(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        // 保存套餐表
        setmealMapper.insert(setmeal);
        // 保存套餐-菜品关系表
        List<SetmealDish> dishes = setmealDTO.getSetmealDishes();
        Long setmealId = setmeal.getId();
        if (dishes != null && !dishes.isEmpty()) {
            dishes.forEach(dish -> {
                dish.setSetmealId(setmealId);
            });
            // 批量插入 套餐-菜品关系数据
            setmealDishMapper.insertBatchSetmealDish(dishes);
        }

    }

    /**
     * 分页查询套餐接口
     * @param pageDTO
     * @return
     */
    @Override
    public PageResult pageQuery(SetmealPageQueryDTO pageDTO) {
        PageHelper.startPage(pageDTO.getPage(), pageDTO.getPageSize());

        // 分页查询套餐数据
        Page<SetmealVO> result = setmealMapper.pageQuery(pageDTO);
        return new PageResult(result.getTotal(), result.getResult());
    }

    /**
     * 根据套餐id查询套餐详情
     * @param id
     * @return
     */
    @Override
    public SetmealVO getById(Long id) {
        // 根据套餐id查询套餐
        Setmeal setmeal = setmealMapper.getById(id);

        // 根据套餐id查询套餐-菜品关系
        List<SetmealDish> setmealDishes = setmealDishMapper.listBySetmealId(id);

        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal, setmealVO);
        setmealVO.setSetmealDishes(setmealDishes);
        return setmealVO;
    }

    /**
     * 批量删除套餐
     * @param ids
     */
    @Override
    @Transactional //添加事务
    public void deleteBatch(List<Long> ids) {
        // 套餐是否起售中，起售中无法删除
        for (Long id : ids) {
            Setmeal setmeal = setmealMapper.getById(id);
            if (setmeal != null && setmeal.getStatus().equals(StatusConstant.ENABLE)) {
                // 起售中的套餐无法删除
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }

        for (Long id : ids) {
            // 删除套餐
            setmealMapper.deleteById(id);
            // 删除 套餐-菜品数据
            setmealDishMapper.deleteBySetmealId(id);
        }
    }

    /**
     * 修改套餐和套餐-菜品关系数据
     * @param setmealDTO
     */
    @Override
    @Transactional
    public void updateWithDish(SetmealDTO setmealDTO) {
        // 修改菜品数据
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.update(setmeal);

        // 根据套餐id 删除套餐-菜品数据
        Long setmealId = setmealDTO.getId();
        setmealDishMapper.deleteBySetmealId(setmealId);

        // 添加套餐-菜品数据
        List<SetmealDish> dishes = setmealDTO.getSetmealDishes();
        dishes.forEach(dish-> {
            dish.setSetmealId(setmealId);
        });
        setmealDishMapper.insertBatchSetmealDish(dishes);
    }

    /**
     * 起售或者停售 套餐
     * @param status
     * @param id
     */
    @Override
    @Transactional
    public void startOrStop(Integer status, Long id) {
        log.info("状态:{}，id:{}",status, id);
        // 1.如果是启售操作，校验套餐内是否有停售的菜品
        if (status == StatusConstant.ENABLE) {
            // 通过套餐id查询 菜品
            List<Dish> dishes = dishMapper.getBySetmealId(id);
            for (Dish dish : dishes) {
                if(dish.getStatus().equals(StatusConstant.DISABLE)) {
                    throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                }
            }
        }
        // 2.更新套餐状态
        Setmeal setmeal = Setmeal.builder()
                .id(id)
                .status(status)
                .build();
        setmealMapper.update(setmeal);
    }

    /**
     * 条件查询套餐
     * @param setmeal
     * @return
     */
    @Override
    public List<Setmeal> list(Setmeal setmeal) {
        return setmealMapper.list(setmeal);
    }

    /**
     * 根据套餐id查询 菜品列表项
     * @param id
     * @return
     */
    @Override
    public List<DishItemVO> getDishItemById(Long id) {
        return setmealMapper.getDishItemBySetmealId(id);
    }
}















