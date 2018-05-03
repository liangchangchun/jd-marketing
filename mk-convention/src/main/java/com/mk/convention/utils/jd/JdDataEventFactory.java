package com.mk.convention.utils.jd;

import com.lmax.disruptor.EventFactory;

public class JdDataEventFactory implements EventFactory<JdDataEvent>{

	@Override
	public JdDataEvent newInstance() {
		return new JdDataEvent();
	}
}
