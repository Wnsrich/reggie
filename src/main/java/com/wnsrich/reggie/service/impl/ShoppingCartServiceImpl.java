package com.wnsrich.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wnsrich.reggie.entity.ShoppingCart;
import com.wnsrich.reggie.mapper.ShoppingCartMapper;
import com.wnsrich.reggie.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
