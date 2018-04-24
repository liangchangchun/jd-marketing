package com.stylefeng.guns.core.base.warpper;

import java.util.List;
import java.util.Map;

/**
 * 控制器查询结果的包装类基类
 *
 * @author fengshuonan
 * @date 2017年2月13日 下午10:49:36
 */
public abstract class ModelControllerWarpper<T>{

    public Object obj = null;

    public ModelControllerWarpper(Object obj) {
        this.obj = obj;
    }

    @SuppressWarnings("unchecked")
    public Object warp() {
        if (this.obj instanceof List) {
            List<T> list = (List<T>) this.obj;
            for (T map : list) {
            	warpTheModel(map);
            }
            return list;
        } else {
            T map = (T) this.obj;
            warpTheModel(map);
            return map;
        } 
    }

    protected abstract void warpTheModel(T model);
}
