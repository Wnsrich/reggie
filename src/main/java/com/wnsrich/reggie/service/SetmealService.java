package com.wnsrich.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wnsrich.reggie.dto.DishDto;
import com.wnsrich.reggie.dto.SetmealDto;
import com.wnsrich.reggie.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    // 回显数据
    SetmealDto selectById(Long id);
    // 新增菜品
    void save(SetmealDto setmealDto);
    // 修改菜品
    void update(SetmealDto setmealDto);
    // 商品状态
    void status(int status,List<Long> ids);
    // 批量删除套餐
    void deleteBatch(List<Long> ids);
}
