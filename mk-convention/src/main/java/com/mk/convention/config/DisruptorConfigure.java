package com.mk.convention.config;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.mk.convention.utils.jd.JDExceptionHandler;
import com.mk.convention.utils.jd.JdDataEvent;
import com.mk.convention.utils.jd.JdDataEventFactory;
import com.mk.convention.utils.jd.JdDataPublisher;
import com.mk.convention.utils.jd.JdEventHandler;

@Component
public class DisruptorConfigure {
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Bean(name="disruptor")   
    public Disruptor<JdDataEvent> disruptor() {   
		// 创建线程池，负责处理Disruptor的四个消费者  
		//ExecutorService executor=Executors.newFixedThreadPool(4);
		//ExecutorService executor = Executors.newCachedThreadPool();
		ExecutorService executor = executorService();
		JdDataEventFactory factory = new JdDataEventFactory();
    	 Disruptor<JdDataEvent> disruptor = new Disruptor<JdDataEvent>(factory, 1024, executor, ProducerType.SINGLE , new YieldingWaitStrategy());
    	 disruptor.setDefaultExceptionHandler(new JDExceptionHandler());
    	 disruptor.handleEventsWith(new JdEventHandler());
    	 disruptor.start();
    	/*
    	 CountDownLatch countDownLatch = new CountDownLatch(1);// 一个生产者线程准备好了就可以通知主线程继续工作了
    	 //生产者生成数据
    	 executor.submit(new JdDataPublisher(countDownLatch, disruptor));
    	 try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//等待生产者结束
		
    	 //disruptor.shutdown();
    	 //executor.shutdown();
    	  * 
    	  */
         return disruptor;   
    }  
	
	@Bean(name="executor")
	public ExecutorService executorService() {
		ExecutorService executor = Executors.newCachedThreadPool();
		return executor;
	}
}
