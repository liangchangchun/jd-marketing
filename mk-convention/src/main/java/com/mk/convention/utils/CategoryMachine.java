package com.mk.convention.utils;

public class CategoryMachine {
	private volatile static CategoryMachine instance = null;
	public static CategoryMachine getInstance(){
		if (instance==null) {
			synchronized (CategoryMachine.class) {
				if (instance==null) {
					instance = new CategoryMachine();
				}
			}
		}
		return instance;
	}
	private CategoryMachine(){
		
	}
	private CategoryManager categoryManager = new CategoryManager();
	
	public CategoryManager getCategoryManager() {
		return categoryManager;
	}
	public void setCategoryManager(CategoryManager categoryManager) {
		this.categoryManager = categoryManager;
	}
	
	
}
