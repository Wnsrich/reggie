package com.wnsrich.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wnsrich.reggie.common.BaseContext;
import com.wnsrich.reggie.common.R;
import com.wnsrich.reggie.dto.OrdersDto;
import com.wnsrich.reggie.entity.Orders;
import com.wnsrich.reggie.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrdersService ordersService;

    /**
     * 提交订单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        ordersService.submit(orders);
        return R.success("下单成功");
    }
    @GetMapping("/userPage")
    public R<Page> userPage(@RequestParam int page,@RequestParam int pageSize){
        Page<Orders> pageInfo = new Page(page,pageSize);
        LambdaQueryWrapper<Orders> lqw = new LambdaQueryWrapper();
        lqw.eq(Orders::getUserId, BaseContext.getThreadId()).orderByDesc(Orders::getOrderTime);
        ordersService.page(pageInfo,lqw);
        return R.success(pageInfo);

    }
}
