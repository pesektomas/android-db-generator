package org.tom.database.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {

    int INTEGER = 0;
    int TEXT = 1;
    int NUMERIC = 2;
    int REAL = 3;
    int BLOB = 4;

    String name();

    int type();

    boolean primaryKey() default false;

    boolean autoIncrement() default false;
}
