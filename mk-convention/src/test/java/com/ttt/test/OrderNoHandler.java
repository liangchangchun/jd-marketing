package com.ttt.test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class OrderNoHandler {
	
	private volatile static OrderNoHandler intance =null;
	private volatile String orderprefix = "";
	private volatile Map<Integer,Integer>  orderEnd = new ConcurrentHashMap<Integer,Integer>();
	private volatile Map<String,Integer> map = new ConcurrentHashMap<String,Integer>();
	public static  OrderNoHandler getInstance() {
		if (intance==null) {
			synchronized (OrderNoHandler.class) {
				if (intance==null) {
					intance = new OrderNoHandler();
				}
			}
		}
		return intance;
	}
	
	private OrderNoHandler() {}
	
	public void addNo(String No) {
		if (map.containsKey(No)) {
			Integer num = map.get(No);
			map.put(No, num++);
		} else {
			map.put(No, 1);
		}
	}
	public Integer getOrderNoSize() {
		return map.size();
	}


	public synchronized String getOrderNoSync(int k) {
		 String kk="";
         String orderNo=null;
         SimpleDateFormat sdf=new SimpleDateFormat("yyMMddHHmmss");
         int number;
         //     lock.writeLock().lock();
         //     boolean writeLock = lock.isWriteLocked();
         //     if (writeLock){
         //         try {
         if(k<10){
             kk="0"+k;
         }else{
             kk=k+"";
         }
         number =(int)((Math.random()*9+1)*100000);
         String dateStr=sdf.format(new Date());
         orderNo=dateStr+number+kk;
         
         return orderNo;
	}
	 ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	public String getOrderLock(int k) {
		String orderNo=null;
             lock.writeLock().lock();
             boolean writeLock = lock.isWriteLocked();
             if (writeLock){
            	 String kk="";
                 SimpleDateFormat sdf=new SimpleDateFormat("yyMMddHHmmss");
                 int number;
                 try {
                	 if(k<10){
                		 kk="0"+k;
                	 }else{
                		 kk=k+"";
                	 }
                	 number =(int)((Math.random()*9+1)*100000);
                	 String dateStr=sdf.format(new Date());
                	 orderNo=dateStr+number+kk;
                 } finally {
                	 lock.writeLock().unlock();
				}
             }
        return orderNo;
	}
	
	public synchronized String getOrder(int k) {
		String orderNo=null;
        String kk="";
        SimpleDateFormat sdf=new SimpleDateFormat("yyMMddHHmmss");
        String dateStr=sdf.format(new Date());
        orderprefix = dateStr;
        if(k<10){
               kk="0"+k;
         }else{
               kk=k+"";
        }
        int number = getOrderRNum();
        if (orderEnd.containsKey(number)) {
        		 String now=sdf.format(new Date());
        	if (now.equals(orderprefix)) {
        		number = getOrderRNum();
        	} else {
        		orderEnd.clear();
        		}
        	}
        	
         orderEnd.put(number, 1);
        	orderNo=dateStr+number+kk;
        return orderNo;
	}
	
	public int getOrderRNum() {
		int number = getOrderRd();
		while (orderEnd.containsKey(number)) {
			number = getOrderRd();
		}
		return number;
	}
	
	public  int getOrderRd() {
		return (int)((Math.random()*9+1)*100000);
	}
}
