package com.sky.mapper;

import com.sky.entity.AddressBook;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AddressBookMapper {

    /**
     * 插入一条地址数据
     * @param addressBook
     */
    @Insert("INSERT INTO address_book (user_id, consignee, sex, phone, province_code, province_name, city_code, " +
            "city_name, district_code, district_name, detail, label, is_default) VALUES " +
            "(#{userId}, #{consignee}, #{sex}, #{phone}, #{provinceCode}, #{provinceName}, #{cityCode}, #{cityName}," +
            "#{districtCode}, #{districtName}, #{detail}, #{label}, #{isDefault})")
    void insert(AddressBook addressBook);

    @Select("SELECT * FROM address_book WHERE id = #{id}")
    AddressBook getById(Long id);

    @Delete("DELETE FROM address_book WHERE id = #{id}")
    void deleteById(Long id);

    /**
     * 根据id更新地址
     * @param addressBook
     */
    void update(AddressBook addressBook);

    /**
     * 根据用户id设置默认地址
     * @param addressBook
     */
    @Update("UPDATE address_book SET is_default = #{isDefault} WHERE user_id = #{userId}")
    void updateIsDefaultByUserId(AddressBook addressBook);

    /**
     * 获取默认地址
     * @param addressBook
     * @return
     */
    @Select("SELECT * FROM address_book WHERE user_id = #{userId} AND is_default = #{isDefault}")
    AddressBook getDefault(AddressBook addressBook);

    /**
     * 根据用户id获取所有地址
     * @param currentId
     * @return
     */
    @Select("SELECT * FROM address_book WHERE user_id = #{userId}")
    List<AddressBook> listByUserId(Long currentId);
}
