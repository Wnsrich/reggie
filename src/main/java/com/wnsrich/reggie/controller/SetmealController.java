package com.wnsrich.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wnsrich.reggie.common.R;
import com.wnsrich.reggie.common.Result;
import com.wnsrich.reggie.dto.SetmealDto;
import com.wnsrich.reggie.entity.Category;
import com.wnsrich.reggie.entity.Setmeal;
import com.wnsrich.reggie.service.CategoryService;
import com.wnsrich.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    SetmealService setmealService;
    @Autowired
    CategoryService categoryService;

    @GetMapping("/page")
    public R<Page> pageR(int page,int pageSize,String name){
        Page<Setmeal> pageInfo = new Page<>(page,pageSize);
        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper();
        lqw.like(name != null,Setmeal::getName,name).orderByDesc(Setmeal::getUpdateTime);
        // 查询出来的分页
        setmealService.page(pageInfo, lqw);
        // 解决套餐名称不显示问题
        Page<SetmealDto> pageDtoInfo = new Page<>();
        // 将pageInfo的信息拷贝到pageDtoInfo里面，不包括records
        BeanUtils.copyProperties(pageInfo,pageDtoInfo,"records");
        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> newRecords  = new ArrayList<>();
        for (Setmeal record : records) {
            long s = record.getCategoryId();
            Category byId = categoryService.getById(s);
            SetmealDto setmealDto = new SetmealDto();
            // 拷贝资源
            BeanUtils.copyProperties(record,setmealDto);
            setmealDto.setCategoryName(byId.getName());
            newRecords.add(setmealDto);
        }
        pageDtoInfo.setRecords(newRecords);
        return R.success(pageDtoInfo);
    }

    /**
     *  修改页面回显数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<SetmealDto> selectById(@PathVariable Long id){
        SetmealDto setmealDto = setmealService.selectById(id);
        return R.success(setmealDto);
    }
    /**
     * 新增套餐
     * @param setmealDto
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        setmealService.save(setmealDto);
        return R.success("新增成功");
    }

    /**
     *  修改套餐
     * @param setmealDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto){
        setmealService.update(setmealDto);
        return R.success(Result.SAVE_SUCCESS);
    }
    /**
     * 套餐status 停售
     * @param ids
     * @return
     */
    @PostMapping("/status/0")
    public R<String> status0(@RequestParam List<Long> ids){
        setmealService.status(0,ids);
        return R.success("操作成功");
    }
    /**
     * 套餐status 启售
     * @param ids
     * @return
     */
    @PostMapping("/status/1")
    public R<String> status1(@RequestParam List<Long> ids){
        setmealService.status(1,ids);
        return R.success("操作成功");
    }

    /**
     * 删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> deleteBath(@RequestParam List<Long> ids){
        setmealService.deleteBatch(ids);
        return R.success("删除成功");
    }
    /**
     * 根据条件查询套餐数据
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId() != null,Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus() != null,Setmeal::getStatus,setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        List<Setmeal> list = setmealService.list(queryWrapper);

        return R.success(list);
    }
}
