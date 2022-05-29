package com.wnsrich.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wnsrich.reggie.entity.AddressBook;
import com.wnsrich.reggie.mapper.AddressBookMapper;
import com.wnsrich.reggie.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

}
