package com.wnsrich.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wnsrich.reggie.common.CustomException;
import com.wnsrich.reggie.entity.Category;
import com.wnsrich.reggie.entity.Dish;
import com.wnsrich.reggie.entity.Setmeal;
import com.wnsrich.reggie.mapper.CategoryMapper;
import com.wnsrich.reggie.service.CategoryService;
import com.wnsrich.reggie.service.DishService;
import com.wnsrich.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    DishService dishService;
    @Autowired
    SetmealService setmealService;


    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> DishLqw = new LambdaQueryWrapper();
        DishLqw.eq(Dish::getCategoryId,id);
        if (dishService.count(DishLqw) > 0)  throw new CustomException("该分类已经关联菜品，不能删除");

        LambdaQueryWrapper<Setmeal> SetmealLqw = new LambdaQueryWrapper();
        SetmealLqw.eq(Setmeal::getCategoryId,id);
        if (setmealService.count(SetmealLqw) > 0)  throw new CustomException("该分类已经关联套餐，不能删除");

        super.removeById(id);
    }
}
