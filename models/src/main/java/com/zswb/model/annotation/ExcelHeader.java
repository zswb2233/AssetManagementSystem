package com.zswb.model.annotation;
import java.lang.annotation.*;

// 该注解用于标注Excel表头的中文名称
@Target({ElementType.FIELD}) // 作用在字段上
@Retention(RetentionPolicy.RUNTIME) // 运行时可获取
@Documented
public @interface ExcelHeader {
    String value(); // 中文表头名称
}