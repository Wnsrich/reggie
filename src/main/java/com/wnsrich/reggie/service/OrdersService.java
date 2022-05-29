package com.wnsrich.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wnsrich.reggie.entity.Orders;

public interface OrdersService extends IService<Orders> {
    void submit(Orders orders);
}
