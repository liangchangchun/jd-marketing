package com.mk.convention.utils.jd;

import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.dsl.Disruptor;

public class JDExceptionHandler implements ExceptionHandler<Object>{
	
	private final Disruptor<JdDataEvent> disruptor;
	
	public JDExceptionHandler(Disruptor<JdDataEvent> disruptor) {
        this.disruptor = disruptor;
    }

	@Override
	public void handleEventException(Throwable ex, long arg1, Object arg2) {
		ex.printStackTrace();
		disruptor.shutdown();
	}

	@Override
	public void handleOnShutdownException(Throwable ex) {
		disruptor.shutdown();
	}

	@Override
	public void handleOnStartException(Throwable ex) {
		// TODO Auto-generated method stub
		disruptor.shutdown();
	}

}