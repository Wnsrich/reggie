package com.wnsrich.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 元数据处理器
 * 在entry里@TableField
 */
@Component

public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime",LocalDateTime.now());
        metaObject.setValue("updateUser", BaseContext.getThreadId());
        metaObject.setValue("createUser", BaseContext.getThreadId());

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("updateUser", BaseContext.getThreadId());
        metaObject.setValue("updateTime",LocalDateTime.now());
    }
}
