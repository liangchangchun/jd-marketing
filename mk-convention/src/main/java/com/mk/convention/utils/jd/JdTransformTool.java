package com.mk.convention.utils.jd;


import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lmax.disruptor.dsl.Disruptor;
import com.mk.convention.config.DataSource;
import com.mk.convention.utils.SpringUtil;
import com.mk.convention.utils.disruptor.MyEventProduce;

public class JdTransformTool {
	public static DataSource dataSource;

	/**
	 * 表数据类型
	 * @author lovegp
	 *
	 */
	public enum JdDataEventType{
		JDBC_SAVE,ES_SAVE;
		public String toString(){
			return super.toString().toLowerCase() ;
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void published(JdDataEvent jdDataEvent){
		published(jdDataEvent.getEvent(), dataSource ,jdDataEvent.getCommand());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void published(DataEvent event, DataSource dataSource , String command){
		Disruptor<JdDataEvent> disruptor = (Disruptor<JdDataEvent>) SpringUtil.getApplicationContext().getBean("disruptor") ;
		long seq = disruptor.getRingBuffer().next();
		JdDataEvent jdDataEvent = disruptor.getRingBuffer().get(seq) ;
		jdDataEvent.setEvent(event);
		jdDataEvent.setDataSource(dataSource);
		jdDataEvent.setCommand(command);
		disruptor.getRingBuffer().publish(seq);
	}
	/**
	 * 生产者类型
	 * @param event
	 * @param dataSource
	 * @param command
	 * @param publisher
	 * @throws InterruptedException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void produce(DataEvent event, DataSource dataSource ,String command,BasePublisher publisher) throws InterruptedException {
		ExecutorService executor = (ExecutorService) SpringUtil.getApplicationContext().getBean("executor");
		Disruptor<JdDataEvent> disruptor = (Disruptor<JdDataEvent>) SpringUtil.getApplicationContext().getBean("disruptor");
		//JdDataPublisher ep = new JdDataPublisher();//生产者
		publisher.setDisruptor(disruptor);
		JdDataEvent jdDataEvent = new JdDataEvent();
		jdDataEvent.setEvent(event);
		jdDataEvent.setDataSource(dataSource);
		jdDataEvent.setCommand(command);
		publisher.setJdDataEvent(jdDataEvent);
		CountDownLatch countDownLatch = publisher.getCountDownLatch();
		executor.execute(publisher);
		countDownLatch.await();
		//disruptor.shutdown();
	}
	/**
	 * 
	 * @param event
	 * @param dataSource
	 * @param commandType  类型
	 * @param publisher
	 * @throws InterruptedException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void produce(DataEvent event, DataSource dataSource ,JdDataEventType commandType,BasePublisher publisher) throws InterruptedException{
		produce(event, dataSource , commandType.toString(), publisher);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void produce(DataEvent event, DataSource dataSource ,JdDataEventType commandType) throws InterruptedException{
		produce(event, dataSource , commandType.toString(), new JdDataPublisher());
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void produce(DataEvent event ,JdDataEventType commandType) throws InterruptedException{
		produce(event, dataSource , commandType.toString(), new JdDataPublisher());
	}
}
