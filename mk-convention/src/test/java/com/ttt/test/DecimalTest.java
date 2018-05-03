package com.ttt.test;


import com.alibaba.druid.support.json.JSONUtils;
import com.mk.convention.meta.JsonResult;
import com.mk.convention.service.JDOpenApiService;
import com.mk.convention.service.impl.JDOpenApiServiceImpl;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class DecimalTest {

    @Autowired
    JDOpenApiService jdOpenApiService;

    @Test
    public void testcase1() {
//        Assert.assertTrue(true);
        System.out.println("testcase1");
    }

    @Test
    public void testcase2() {
//        Assert.assertTrue(true);
        System.out.println("testcase1");
    }

    @Test
    public void getSellPrice() {
       // JsonResult sellPrice = jdOpenApiService.getSellPrice("4099139,4110748,4112338,4120319");
       // System.out.println(JSONUtils.toJSONString(sellPrice));
    }
}
