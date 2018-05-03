package com.mk.convention.utils.jd;

import com.mk.convention.config.DataSource;

public class JdDataEvent {
	private long id ;
	private DataEvent event ;
	private String command ;
	
	private DataSource dataSource;//jdbc
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public DataEvent getEvent() {
		return event;
	}
	public void setEvent(DataEvent event) {
		this.event = event;
	}
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public DataSource getDataSource() {
		return dataSource;
	}
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	
}
