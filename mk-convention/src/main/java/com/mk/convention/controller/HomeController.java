package com.mk.convention.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mk.convention.persistence.model.Order;
import com.mk.convention.respository.OrderRepository;

@RestController
@RequestMapping(value = "/home")
public class HomeController {

	  	@Autowired
	    private OrderRepository orderRepository;
	    /**
	     * 处理日期类型
	     *
	     * @param binder
	     */
	    @InitBinder
	    public void initBinder(WebDataBinder binder) {
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	        dateFormat.setLenient(false);
	        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	    }

	    @PostMapping("/save")
	    @ResponseBody
	    public  Map<String, Object> save( Order entity) throws Exception {
	        Map<String, Object> result = new HashMap<>();
	        entity.setDate(new Date());
	        entity = orderRepository.save(entity);
	        result.put("id", entity.id);
	        return result;
	    }

	    /**
	     * 获取对象
	     *
	     * @param id
	     * @return
	     */
	    @PostMapping("/get")
	    @ResponseBody
	    public  Object get(String id) {
	        return orderRepository.findOne(id);
	    }

	    /**
	     * 获取全部
	     *
	     * @return
	     */
	    @PostMapping("/findAll")
	    @ResponseBody
	    public Object findAll() {
	        return orderRepository.findAll();
	    }

	    /**
	     * 删除
	     *
	     * @param id
	     * @return
	     */
	    @PostMapping("/delete")
	    @ResponseBody
	    public  Map<String, Object> delete(String id) {
	        Map<String, Object> result = new HashMap<>();
	        orderRepository.delete(id);
	        result.put("id", id);
	        return result;
	    }
}
