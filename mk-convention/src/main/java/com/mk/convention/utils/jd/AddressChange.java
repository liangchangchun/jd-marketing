package com.mk.convention.utils.jd;

import lombok.Data;

@Data
public class AddressChange extends AddressChangeBase {
    private Long id;
    private String areaName;
    private Long jdId;
    private String jdAreaName;
}
