package com.mk.convention.utils.jd;

import java.util.concurrent.CountDownLatch;

import com.lmax.disruptor.dsl.Disruptor;

/**
 * 基础消费者模板
 * @author lcc
 *
 */
public abstract class BasePublisher implements Runnable {
	protected final int SIZE = 1;
	protected CountDownLatch countDownLatch;//用于监听初始化操作，等初始化执行完毕后，通知主线程继续工作
	protected Disruptor<JdDataEvent> disruptor;
	protected JdDataEvent jdDataEvent;
	
	public BasePublisher() {
		//this.countDownLatch = countDownLatch;
		this.countDownLatch = new CountDownLatch(SIZE);
	}
	 public CountDownLatch getCountDownLatch() {
	      return countDownLatch;
	 }

	public Disruptor<JdDataEvent> getDisruptor() {
		return disruptor;
	}

	public void setDisruptor(Disruptor<JdDataEvent> disruptor) {
		this.disruptor = disruptor;
	}
	public JdDataEvent getJdDataEvent() {
		return jdDataEvent;
	}
	public void setJdDataEvent(JdDataEvent jdDataEvent) {
		this.jdDataEvent = jdDataEvent;
	}
	
	
	 
}
