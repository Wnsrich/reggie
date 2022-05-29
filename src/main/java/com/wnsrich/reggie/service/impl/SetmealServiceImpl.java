package com.wnsrich.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wnsrich.reggie.common.CustomException;
import com.wnsrich.reggie.common.R;
import com.wnsrich.reggie.dto.DishDto;
import com.wnsrich.reggie.dto.SetmealDto;
import com.wnsrich.reggie.entity.Dish;
import com.wnsrich.reggie.entity.DishFlavor;
import com.wnsrich.reggie.entity.Setmeal;
import com.wnsrich.reggie.entity.SetmealDish;
import com.wnsrich.reggie.mapper.SetmealMapper;
import com.wnsrich.reggie.service.SetmealDishService;
import com.wnsrich.reggie.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    SetmealDishService setmealDishService;

    @Override
    public SetmealDto selectById(Long id) {
        Setmeal byId = super.getById(id);
        SetmealDto setmealDto = new SetmealDto();

        BeanUtils.copyProperties(byId,setmealDto);
        LambdaQueryWrapper<SetmealDish> lqw = new LambdaQueryWrapper();
        lqw.eq(id!=null,SetmealDish::getSetmealId,id.toString());
        List<SetmealDish> list = setmealDishService.list(lqw);
        setmealDto.setSetmealDishes(list);
        return setmealDto;
    }


    @Override
    @Transactional
    public void save(SetmealDto setmealDto) {
        super.save(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmealDto.getId());
        }
        setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    @Transactional
    public void update(SetmealDto setmealDto) {
        super.updateById(setmealDto);
        LambdaQueryWrapper<SetmealDish> lqw = new LambdaQueryWrapper();
        lqw.eq(SetmealDish::getSetmealId,setmealDto.getId());
        setmealDishService.remove(lqw);
        List<SetmealDish> list = new ArrayList<>();
        list.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(list);

    }

    /**
     *  菜品状态改变
     * @param status
     * @param ids
     */
    @Override
    @Transactional
    public void status(int status, List<Long> ids) {
        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper();
        lqw.in(Setmeal::getId,ids);
        List<Setmeal> list = super.list(lqw);
        for (Setmeal setmeal : list) {
            setmeal.setStatus(status);
        }
        super.updateBatchById(list);
    }

    @Override
    public void deleteBatch(List<Long> ids) {
        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper();
        lqw.in(Setmeal::getId,ids).eq(Setmeal::getStatus,1);
        if (super.count(lqw)>0) throw new CustomException("套餐正在售卖，不能删除");

        super.removeByIds(ids);
        LambdaQueryWrapper<SetmealDish> lqw2 = new LambdaQueryWrapper<>();
        lqw2.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(lqw2);
    }


}
