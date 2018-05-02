package com.mk.convention.utils;

import java.util.concurrent.Executors;

import com.alibaba.fastjson.JSONObject;
import com.mk.convention.config.DataSource;

public class ServiceCategory {
	private Integer number;
	private DataSource dataSource;
	
	public ServiceCategory(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public DataSource getDataSource() {
		return dataSource;
	}
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public void start(){
		Executors.newSingleThreadExecutor().execute(
				new Runnable(){
					public void run(){
						//下面这种写法的运行效率低，最好是把while放在case下面
						while(true){
							commonService();
						}
					}
				}
		);
	}
	
	private void commonService(){
		String windowName = "第" + number + "号服务";		
		System.out.println(windowName + "开始获取保存商品!");
		String sql = "insert into product_other_info(sku_id,brand_name,name,product_area,upc,sale_unit,category,introduction,param,wareQD,image_path,weight) values (?,?,?,?,?,?,?,?,?,?,?,?)";
		for (int i=0;i<100;i++) {
			JSONObject jsonObject = CategoryMachine.getInstance().getCategoryManager().fetchCategory();	
		//System.out.println("jsonObject:" + jsonObject.getString("sku"));
			if(jsonObject != null ){
				String sql2 = "";
			 	String[] parameters = null;
			 	parameters = new String[12];
		        parameters[0] = jsonObject.getString("sku");
                parameters[1] = jsonObject.getString("brandName");
                parameters[2] = jsonObject.getString("name");
                parameters[3] = jsonObject.getString("productArea");
                parameters[4] = jsonObject.getString("upc");
                parameters[5] = jsonObject.getString("saleUnit");
                parameters[6] = jsonObject.getString("category");
                parameters[7] = jsonObject.getString("introduction");
                parameters[8] = jsonObject.getString("param");
                parameters[9] = jsonObject.getString("wareQD");
                parameters[10] = jsonObject.getString("imagePath");
                parameters[11] = jsonObject.getString("weight");
                dataSource.executeUpdate(sql,parameters);
                System.out.println(windowName + "保存商品成功!");
		}else{
			System.out.println(windowName + "没有取到普通任务，正在空闲一秒");		
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}				
		}
			
		}
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}
	
	
}
