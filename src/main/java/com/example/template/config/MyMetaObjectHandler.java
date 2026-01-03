package com.example.template.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    private static final ZoneOffset ZONE_OFFSET = ZoneOffset.ofHours(8);

    @Override
    public void insertFill(MetaObject metaObject) {
        OffsetDateTime now = OffsetDateTime.now(ZONE_OFFSET);
        // 无论是否传值，都覆盖
        this.setFieldValByName("createdAt", now, metaObject);
        this.setFieldValByName("updatedAt", now, metaObject);
        this.setFieldValByName("deleted", false, metaObject);

        // 强制覆盖 id 为 null
        if (metaObject.hasGetter("id")) {
            metaObject.setValue("id", null); // ⚡ 强制覆盖
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        OffsetDateTime now = OffsetDateTime.now(ZONE_OFFSET);
        // 无论是否传值，都覆盖
        this.setFieldValByName("updatedAt", now, metaObject);
    }
}
