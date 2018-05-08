package com.mk.convention.utils.jd;

import com.lmax.disruptor.EventTranslator;

public class JdDataEventTranslator implements EventTranslator<JdDataEvent>{
	
	@Override
	public void translateTo(JdDataEvent JdDataEvent, long sequence){
		this.generateData(JdDataEvent);
	}

	private JdDataEvent generateData(JdDataEvent jdDataEvent){
		//填充数据 
		JdTransformTool.published(jdDataEvent);//保存
		//System.out.println("Thread Id " + Thread.currentThread().getId() + " 写完一个event");
		return jdDataEvent;
	}
}