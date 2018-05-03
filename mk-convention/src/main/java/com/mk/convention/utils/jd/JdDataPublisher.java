package com.mk.convention.utils.jd;

public class JdDataPublisher extends BasePublisher implements Runnable{

	public JdDataPublisher() {
	}

	@Override
	public void run() {
	
		JdDataEventTranslator eventTranslator = new JdDataEventTranslator();
		JdDataEvent jdDataEvent = this.getJdDataEvent();
		BaseDataEvent req=(BaseDataEvent)jdDataEvent.getEvent();
	   try {
		   req.ApiRequest(jdDataEvent, disruptor, eventTranslator);
		   
				/*
				for(int i = 0; i<NUM;i++){
					disruptor.publishEvent(eventTranslator);
					Thread.sleep(1000); // 假设一秒钟进一辆车
				}*/
			} finally{
				countDownLatch.countDown();//执行完毕后通知 await()方法
				//System.out.println("+==========+获取商品池编号:"+page_num+",名称:【"+name+"】,数量:【"+skus+"】数据都获取到!");
			}
		}

	}

