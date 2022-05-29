package com.wnsrich.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wnsrich.reggie.common.BaseContext;
import com.wnsrich.reggie.common.R;
import com.wnsrich.reggie.entity.ShoppingCart;
import com.wnsrich.reggie.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    ShoppingCartService shoppingCartService;

    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper();
        lqw.eq(ShoppingCart::getUserId,BaseContext.getThreadId()).orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(lqw);
        return R.success(list);

    }

    /**
     * 添加购物车
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    @Transactional
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        // 设置用户ID
        shoppingCart.setUserId((Long) BaseContext.getThreadId());
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper();
        lqw.eq(ShoppingCart::getUserId,BaseContext.getThreadId());
        Long dishId = shoppingCart.getDishId();
        if (dishId != null){
            // 添加的是菜品
            lqw.eq(ShoppingCart::getDishId,dishId);
        }else {
            // 是套餐
            lqw.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }

        ShoppingCart one = shoppingCartService.getOne(lqw);
        if (one != null){
            // 如果存在
            one.setNumber(one.getNumber() + 1);
            shoppingCartService.updateById(one);
        }else {
            // 不存在则添加
            shoppingCart.setNumber(1);
            shoppingCartService.save(shoppingCart);
            one = shoppingCart;
        }
        return R.success(one);
    }


    /**
     * 购物车删除
     */
    @DeleteMapping("/clean")
    @Transactional
    public R<String> clean(){
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper();
        lqw.eq(ShoppingCart::getUserId,BaseContext.getThreadId());
        shoppingCartService.remove(lqw);
        return R.success("删除成功");
    }
    /**
     * 购物车数量减少
     */
    @PostMapping("/sub")
    @Transactional
    public R<String> sub(@RequestBody ShoppingCart shoppingCart){
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId,BaseContext.getThreadId());
        Long dishId = shoppingCart.getDishId();
        if ( dishId != null){
            // 是菜品
            lqw.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
        }else{
            // 是套餐
            lqw.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        ShoppingCart one = shoppingCartService.getOne(lqw);

        if (one.getNumber() > 1){
            one.setNumber(one.getNumber() - 1);
            shoppingCartService.updateById(one);
        }else {
            shoppingCartService.removeById(one);
        }

        return R.success("删除成功");
    }
}
