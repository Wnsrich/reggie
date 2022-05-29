package com.wnsrich.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wnsrich.reggie.common.R;
import com.wnsrich.reggie.dto.DishDto;
import com.wnsrich.reggie.dto.SetmealDto;
import com.wnsrich.reggie.entity.*;
import com.wnsrich.reggie.service.CategoryService;
import com.wnsrich.reggie.service.DishFlavorService;
import com.wnsrich.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Autowired
    DishService dishService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    DishFlavorService dishFlavorService;

    /**
     * 菜品分页
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> pageR(int page,int pageSize,String name){
        Page<Dish> pageInfo = new Page(page,pageSize);
        // 创建Page<DishDto> ，DishDto继承了Dto，用于解决菜品分类不显示名称问题
        Page<DishDto> pageDto = new Page<>();

        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper();
        lqw.like(name != null,Dish::getName, name).orderByDesc(Dish::getUpdateTime);
        dishService.page(pageInfo,lqw);
        // Page拷贝
        BeanUtils.copyProperties(pageInfo,pageDto,"records");
        // 处理CategoryId
        List<Dish> records = pageInfo.getRecords();
        List<DishDto> dishDtos = new ArrayList<>();
        for (Dish record : records) {
            DishDto dishDto = new DishDto();
            //拷贝单独的dishDto对象
            BeanUtils.copyProperties(record,dishDto);
            // 获取菜品ID
            Long categoryId = record.getCategoryId();
            // 根据id查询出byId对象
            Category byId = categoryService.getById(categoryId);
            // 设置名称
            dishDto.setCategoryName(byId.getName());
            //被每个设置好名称的dishDto添加回pageDto里
            dishDtos.add(dishDto);
        }
        pageDto.setRecords(dishDtos);
        return R.success(pageDto);
    }

    /**
     * 新增菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        dishService.saveWithFlavor(dishDto);
        return R.success("新增成功");
    }

    /**
     * 在修改菜品时根据id回显数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> selectById(@PathVariable Long id){
        DishDto one = dishService.getByIdWithFlavors(id);
        return R.success(one);
    }

    /**
     * 菜品修改
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        dishService.updateWithFlavors(dishDto);
        return R.success("成功修改");
    }


    /**
     * 删除菜品
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> deleteBath(@RequestParam List<Long> ids){
        dishService.deleteWithFlavors(ids);
        return R.success("删除成功");
    }

    /**
     * 商品status 停售
     * @param ids
     * @return
     */
    @PostMapping("/status/0")
    public R<String> status0(@RequestParam List<Long> ids){
        dishService.status(0,ids);
        return R.success("操作成功");
    }
    /**
     * 商品status 启售
     * @param ids
     * @return
     */
    @PostMapping("/status/1")
    public R<String> status1(@RequestParam List<Long> ids){
        dishService.status(1,ids);
        return R.success("操作成功");
    }

    /**
     * 根据categoryId查询
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish) {
        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper();
        lqw.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId())
                .eq(Dish::getStatus,1);
        List<Dish> list = dishService.list(lqw);

        List<DishDto> listDtos = new ArrayList<>();
        for (Dish record : list) {
            DishDto dishDto = new DishDto();
            //拷贝单独的dishDto对象
            BeanUtils.copyProperties(record,dishDto);
            // 获取菜品ID
            Long categoryId = record.getCategoryId();
            // 根据id查询出byId对象
            Category byId = categoryService.getById(categoryId);
            // 设置名称
            dishDto.setCategoryName(byId.getName());

            Long id = dishDto.getId();
            // 查询口味信息
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper();
            lambdaQueryWrapper.eq(DishFlavor::getDishId,id);
            List<DishFlavor> dishFlavors = dishFlavorService.list(lambdaQueryWrapper);
            dishDto.setFlavors(dishFlavors);
            // 追加到listDto里面
            listDtos.add(dishDto);
        }
        return R.success(listDtos);
    }



}
