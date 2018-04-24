package com.mk.convention;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class MapTester {
	public static void main(String[] args) {
		Student s1 =new Student("1","张三");
		Student s2 =new Student("2","李四");
		
	   Map<Student,String> map = new HashMap<Student,String>();
	   map.put(s1, "s1");
	   map.put(s2, "s2");
	   
	   
	   System.out.println(map);
	   BigDecimal redPackage1 = new BigDecimal(1.235652);
		 BigDecimal redPackagePrice=redPackage1.setScale(4, BigDecimal.ROUND_HALF_UP);
		 System.out.println(redPackage1.doubleValue()+","+redPackagePrice.doubleValue());
		 
		long stratTime = System.nanoTime();  
		 for (int i = 0; i < 1000; i++) {  
		     for (int j = 0; j < 10; j++) {  
		           
		     }  
		 }  
		 long endTime = System.nanoTime();  
		 System.out.println("外大内小耗时："+ (endTime - stratTime)); 
		 
		 stratTime = System.nanoTime();  
		 for (int i = 0; i <10 ; i++) {  
		     for (int j = 0; j < 1000; j++) {  
		           
		     }  
		 }  
		 endTime = System.nanoTime();  
		 System.out.println("外小内大耗时："+(endTime - stratTime));  


	}
}
