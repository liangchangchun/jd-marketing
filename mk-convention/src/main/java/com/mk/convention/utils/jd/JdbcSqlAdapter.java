package com.mk.convention.utils.jd;

import lombok.Data;

@Data
public class JdbcSqlAdapter {
	private String sql;
	private String[] parameters;
	
}
