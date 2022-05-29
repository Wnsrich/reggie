package com.wnsrich.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wnsrich.reggie.dto.DishDto;
import com.wnsrich.reggie.entity.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {
    // 新增菜品
    List<DishDto> saveWithFlavor(DishDto dishDto);
    // 修改菜品回显数据，根据ID查询口味和菜品
    DishDto getByIdWithFlavors(Long id);
    // 修改菜品保存功能
    void updateWithFlavors(DishDto dishDto);
    // 删除菜品
    void deleteWithFlavors(List<Long> ids);
    // 商品状态
    void status(int status,List<Long> ids);
}
