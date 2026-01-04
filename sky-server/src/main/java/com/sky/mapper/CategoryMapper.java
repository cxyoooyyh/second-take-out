package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

/**
 * @author sharkCode
 * @date 2025/12/25 20:43
 */
@Mapper
public interface CategoryMapper {
    /**
     * 插入一条分类数据
     * @param category
     */
    @Insert("INSERT INTO category (type, name, sort, status, create_time, update_time, create_user, update_user) VALUES " +
            "(#{type}, #{name}, #{sort}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    @AutoFill(OperationType.INSERT)
    void insert(Category category);

    /**
     * 分页查询 分类
     * @param queryDTO
     * @return
     */
    Page<Category> pageQuery(CategoryPageQueryDTO queryDTO);

    /**
     * 更新 分类数据
     * @param category
     */
    @AutoFill(OperationType.UPDATE)
    void update(Category category);

    /*
     * 根据id删除分类
     * @param id
     */
    @Delete("DELETE FROM category WHERE id = #{id}")
    void deleteById(Long id);

    /**
     * 根据id查询分类
     * @param id
     * @return
     */
    @Select("SELECT * FROM category where id = #{id}")
    Category getById(Long id);

    /**
     * 根据类型查询分类列表
     * @param type
     * @return
     */
    List<Category> getListByType(Integer type);
}
