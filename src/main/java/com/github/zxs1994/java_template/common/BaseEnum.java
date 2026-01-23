package com.github.zxs1994.java_template.common;

public interface BaseEnum<T> {
    T getCode();
    String getDesc();

    // 默认实现，旧枚举不受影响
    default String getColor() {
        return null;
    }
}
