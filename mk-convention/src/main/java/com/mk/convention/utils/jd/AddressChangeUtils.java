package com.mk.convention.utils.jd;


import com.mk.convention.config.DataSource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class AddressChangeUtils {
    private static final Logger log = LoggerFactory.getLogger(AddressChangeUtils.class);
    //    @Autowired
//    private AddressChangeMapper addressChangeMapper;
    private DataSource dataSource = new DataSource();
    BufferedWriter bw = null;

    public AddressChange getAddressChange(AddressChange addressChange) throws Exception {

        Field[] fields = addressChange.getClass().getDeclaredFields();
        for (Field field : fields) {
            if ("areaName".equals(field.getName())) {
                getAddressChangeByName(addressChange, field);
            }
            if ("jdAreaName".equals(field.getName())) {
                getAddressChangeByName(addressChange, field);
            }
        }
        return null;
    }

    private AddressChange getAddressChangeByName(AddressChange addressChange, Field field) throws IllegalAccessException {
        field.setAccessible(true);
        String addressName ;
        addressName = (String) field.get(addressChange);
        if (StringUtils.isNotEmpty(addressName)) {
            if ("areaName".equals(field.getName())) {
                //查询本地的编码
//                   AddressChange addressChange1 = addressChangeMapper.queryLocalAreaCodeByAddressName(addressName);
                //通过名称来获取京东id
                ArrayList addressChangeList = this.dataSource.executeQueryByAddress(" select" +
                        "        id as id," +
                        "        area_name as areaName" +
                        "        from jd_base_area" +
                        "        where area_name = ?", new String[]{addressChange.getAreaName()});
                AddressChange addressChange1 = (AddressChange) addressChangeList.get(0);
                System.out.println(addressChange1.getId() + addressChange1.getAreaName());
                return addressChange1;
            } else if ("jdAreaName".equals(field.getName())) {
//                  AddressChange addressChange1 = addressChangeMapper.queryjdAreaCodeByAddressName(addressName);
                //通过京东名称来获取本地id
                ArrayList addressChangeList = this.dataSource.executeQueryByAddress(" select" +
                        "        id as id," +
                        "        area_name as areaName" +
                        "        from base_area" +
                        "        where area_name = ?", new String[]{addressChange.getJdAreaName()});
                AddressChange addressChange1 = (AddressChange) addressChangeList.get(0);
                System.out.println(addressChange1.getId() + addressChange1.getAreaName());
                return addressChange1;
            }
        }
        return null;
    }

    public void show() throws Exception {
        File file = new File("D:\\2.txt");
        Writer fw = new FileWriter(file, true);
        bw = new BufferedWriter(fw);
//        AddressChangeMapper addressChangeMapper;
//        addressChangeMapper = SpringBeanFactoryUtils.getBean(AddressChangeMapper.class);
        // addressChangeMapper = SpringBeanFactoryUtils.getBean(AddressChangeMapper.class);

        ArrayList<AddressChange> addressChanges = this.dataSource.executeQueryByAddress("  select id as id,area_name as areaName from jd_base_area", null);
//        List<AddressChange> addressChanges = addressChangeMapper.queryBaseArea();
        for (AddressChange addressChange : addressChanges) {

            AddressChange addressChange2 = new AddressChange();
            addressChange2.setJdAreaName(addressChange.getAreaName());
            getAddressChange(addressChange2);
        }
        bw.close();
        fw.close();
    }


}
