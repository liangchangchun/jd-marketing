package com.mk.convention.utils.jd;

import com.lmax.disruptor.ExceptionHandler;

public class JDExceptionHandler implements ExceptionHandler<Object>{

	@Override
	public void handleEventException(Throwable ex, long arg1, Object arg2) {
		ex.printStackTrace();
	}

	@Override
	public void handleOnShutdownException(Throwable ex) {
		
	}

	@Override
	public void handleOnStartException(Throwable ex) {
		// TODO Auto-generated method stub
		
	}

}