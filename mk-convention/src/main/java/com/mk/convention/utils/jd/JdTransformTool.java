package com.mk.convention.utils.jd;


import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.mk.convention.config.DataSource;
import com.mk.convention.utils.SpringUtil;

public class JdTransformTool {
	public static DataSource dataSource;
	public static RingBuffer<JdDataEvent> ringBuffer = null;

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
		published(jdDataEvent.getEvent(), dataSource, jdDataEvent.getEsRes(),jdDataEvent.getCommand());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void published(DataEvent event, DataSource dataSource, ElasticsearchCrudRepository esRes, String command){
		Disruptor<JdDataEvent> disruptor = (Disruptor<JdDataEvent>) SpringUtil.getContext().getBean("disruptor") ;
		long seq = disruptor.getRingBuffer().next();
		JdDataEvent jdDataEvent = disruptor.getRingBuffer().get(seq) ;
		jdDataEvent.setEvent(event);
		jdDataEvent.setDataSource(dataSource);
		jdDataEvent.setEsRes(esRes);
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
	public static void produce(DataEvent event, DataSource dataSource ,ElasticsearchCrudRepository esRes ,String command) throws InterruptedException {
	
		BasePublisher publisher = new JdDataPublisher();
		ExecutorService executor = (ExecutorService) SpringUtil.getContext().getBean("executor");
		Disruptor<JdDataEvent> disruptor = (Disruptor<JdDataEvent>) SpringUtil.getContext().getBean("disruptor");
		ringBuffer = disruptor.getRingBuffer();
		//JdDataPublisher ep = new JdDataPublisher();//生产者
		publisher.setDisruptor(disruptor);
		JdDataEvent jdDataEvent = new JdDataEvent();
		jdDataEvent.setEvent(event);
		jdDataEvent.setDataSource(dataSource);
		jdDataEvent.setEsRes(esRes);
		jdDataEvent.setCommand(command);
		publisher.setJdDataEvent(jdDataEvent);
		CountDownLatch countDownLatch = publisher.getCountDownLatch();
		executor.execute(publisher);
		countDownLatch.await();
	
	}
	
	/**
	 * 默认数据形式
	 * @param event
	 * @throws InterruptedException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void produce(DataEvent event) throws InterruptedException{
		produce(event, dataSource, null , JdDataEventType.JDBC_SAVE.toString());
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void produce(DataEvent event,JdDataEventType commandType) throws InterruptedException{
		produce(event, dataSource, null , commandType.toString());
	}
	/**
	 * 数据库存储
	 * @param event
	 * @param dataSource
	 * @throws InterruptedException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void produce(DataEvent event, DataSource dataSource) throws InterruptedException{
		produce(event, dataSource ,null ,JdDataEventType.JDBC_SAVE.toString());
	}
	/**
	 * 索引存储
	 * @param event
	 * @param esRes
	 * @throws InterruptedException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void produce(DataEvent event ,ElasticsearchCrudRepository esRes) throws InterruptedException{
		produce(event, dataSource, esRes, JdDataEventType.ES_SAVE.toString());
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
		produce(event, dataSource, null , commandType.toString());
	}
	

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void produce(DataEvent event, DataSource dataSource ,ElasticsearchCrudRepository esRes,JdDataEventType commandType) throws InterruptedException{
		produce(event, dataSource, esRes, commandType.toString());
	}
}
