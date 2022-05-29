package com.wnsrich.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wnsrich.reggie.common.CustomException;
import com.wnsrich.reggie.dto.DishDto;
import com.wnsrich.reggie.entity.Dish;
import com.wnsrich.reggie.entity.DishFlavor;
import com.wnsrich.reggie.mapper.DishMapper;
import com.wnsrich.reggie.service.DishFlavorService;
import com.wnsrich.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 新增菜品
     * @param dishDto
     * @return
     */
    @Transactional  // 开启事务
    public List<DishDto> saveWithFlavor(DishDto dishDto) {
        // 保存dish表
        super.save(dishDto);
        // 从dto提取flavors
        List<DishFlavor> flavors = dishDto.getFlavors();
        // 提取菜单id
        Long dishId = dishDto.getId();
        // 用stream流的方式遍历flavors的DishId
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);

        return null;
    }


    public DishDto getByIdWithFlavors(Long id) {
        DishDto dishDto = new DishDto();
        Dish dish = this.getById(id);
        // 拷贝资源
        BeanUtils.copyProperties(dish,dishDto);
//        查询构造器
        LambdaQueryWrapper<DishFlavor> lqw = new LambdaQueryWrapper();
        lqw.eq(DishFlavor::getDishId,id);
        List<DishFlavor> dishFlavors = dishFlavorService.list(lqw);
//        把查出来的Flavors设置到dishDto里
        dishDto.setFlavors(dishFlavors);
        return dishDto;
    }

    /**
     *  修改菜品保存功能
     * @param dishDto
     * @return
     */
    @Override
    @Transactional
    public void updateWithFlavors(DishDto dishDto) {
        // 更新dish表基本信息
        this.updateById(dishDto);
        // 清理口味数据
        LambdaQueryWrapper<DishFlavor> lqw = new LambdaQueryWrapper<>();
        lqw.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(lqw);
        // 新增口味数据
        List<DishFlavor> flavors = dishDto.getFlavors();
        // 提取菜单id
        Long dishId = dishDto.getId();
        // 用stream流的方式遍历flavors的DishId
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }

    /**
     * 删除菜品功能
     * @param ids
     */
    @Override
    @Transactional
    public void deleteWithFlavors(List<Long> ids) {
        // 查询是否启售
        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper();
        lqw.in(Dish::getId,ids).eq(Dish::getStatus,1);
        if (this.count(lqw)>0) throw new CustomException("该菜品启售中，不能删除");
        // 删除菜品信息
        super.removeByIds(ids);
        // 删除口味信息
        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.in(DishFlavor::getDishId,ids);
        dishFlavorService.remove(lambdaQueryWrapper);
    }

    /**
     *  菜品状态改变
     * @param status
     * @param ids
     */
    @Override
    @Transactional
    public void status(int status, List<Long> ids) {
        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper();
        lqw.in(Dish::getId,ids);
        List<Dish> list = super.list(lqw);
        for (Dish dish : list) {
            dish.setStatus(status);
        }
        super.updateBatchById(list);
    }
}
