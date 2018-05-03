package com.mk.convention.utils.traditional;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;


public class ProductManager {
	private static ArrayBlockingQueue<String> queue =null;
	public static ArrayBlockingQueue<String> getQueue() {
		return queue;
	}
	public static void setQueue(ArrayBlockingQueue<String> queue) {
		ProductManager.queue = queue;
	}

	private static volatile ProductManager instance;
	public static ProductManager getInstance(int size) {
		if (instance==null) {
			synchronized(ProductManager.class) {
				if (instance==null) {
					instance = new ProductManager(size);
				}
			}
		}
		return instance;
	}
	private ProductManager(int size) {
		queue = new ArrayBlockingQueue<String>(size);
	}
	

	public void setNum(String num){
		try {
			queue.put(num);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public String getNum() throws InterruptedException{
		String num = "";
		num = queue.poll(5,TimeUnit.SECONDS);
		return num;
	}

	
	
}
