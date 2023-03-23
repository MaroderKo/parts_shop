package com.autosale.shop.configuration;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;

public class TestBeanFactory {

    public static DSLContext testDSLContext()
    {
        return DSL.using("jdbc:postgresql://127.0.0.1:5432/parts_shop","postgres","postgres");
    }
}
