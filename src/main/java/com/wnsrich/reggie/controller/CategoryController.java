package com.wnsrich.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wnsrich.reggie.common.R;
import com.wnsrich.reggie.entity.Category;
import com.wnsrich.reggie.entity.Employee;
import com.wnsrich.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    /**
     * 分页
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> pageR(int page, int pageSize) {
        Page<Category> pageInfo = new Page();
        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper();
        lqw.orderByDesc(Category::getSort);
        categoryService.page(pageInfo, lqw);
        return R.success(pageInfo);
    }

    /**
     * 删除
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> deleteById(Long ids) {
        categoryService.remove(ids);
        return R.success("成功删除");
    }

    /**
     * 添加
     * @param category
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Category category) {
        categoryService.save(category);
        return R.success("成功添加");
    }

    /**
     * 修改
     * @param category
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Category category) {
//        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper();
//        lqw.eq(Category::getId,category.getId());
        categoryService.updateById(category);
        return R.success("成功修改");
    }

    /**
     * 根据category.type查询
     * @param category
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category) {
        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper();
        lqw.eq(category.getType() != null,Category::getType,category.getType())
            .eq(category.getId()!=null,Category::getId,category.getId())
            .orderByAsc(Category::getSort)
            .orderByAsc(Category::getUpdateTime);
        List<Category> categoryList = categoryService.list(lqw);
        return R.success(categoryList);
    }
}
