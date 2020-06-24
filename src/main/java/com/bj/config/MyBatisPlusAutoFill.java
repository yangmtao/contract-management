package com.bj.config;

import com.baomidou.mybatisplus.mapper.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
@Slf4j
public class MyBatisPlusAutoFill extends MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("自动填充插入时间===================");
        this.setFieldValByName("createDate", new Date(),metaObject);

    }

    @Override
    public void updateFill(MetaObject metaObject) {

    }
}
