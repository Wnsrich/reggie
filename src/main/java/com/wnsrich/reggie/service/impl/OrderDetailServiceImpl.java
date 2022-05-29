package com.wnsrich.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wnsrich.reggie.entity.OrderDetail;
import com.wnsrich.reggie.mapper.OrderDetailMapper;
import com.wnsrich.reggie.service.OrderDetailService;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
