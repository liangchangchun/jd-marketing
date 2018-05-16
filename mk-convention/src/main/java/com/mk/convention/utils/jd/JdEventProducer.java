package com.mk.convention.utils.jd;

import java.nio.ByteBuffer;

import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

import com.lmax.disruptor.RingBuffer;
import com.mk.convention.config.DataSource;

public class JdEventProducer {
	 private final RingBuffer<JdDataEvent> ringBuffer;

	    public JdEventProducer(RingBuffer<JdDataEvent> ringBuffer)
	    {
	        this.ringBuffer = ringBuffer;
	    }

	    public void onData(JdDataEvent dataEvent, DataSource dataSource ,ElasticsearchCrudRepository esRes ,String command) {
	        long sequence = ringBuffer.next();  // Grab the next sequence  请求下一个事件序号；
	        try {
	        	JdDataEvent event = ringBuffer.get(sequence); // Get the entry in the Disruptor for the sequence  获取该序号对应的事件对象；
	            event.setId(sequence);  // Fill with data
	            event.setEvent(dataEvent.getEvent());
	            event.setCommand(command);
	            event.setDataSource(dataSource);
	            event.setEsRes(esRes);
	        } finally {
	            /*
	             注意，最后的 ringBuffer.publish 方法必须包含在 finally 中以确保必须得到调用；如果某个请求的 sequence 未被提交，将会堵塞后续的发布操作或者其它的 producer。

	             此外，Disruptor 要求 RingBuffer.publish 必须得到调用的潜台词就是，如果发生异常也一样要调用 publish ，
	             那么，很显然这个时候需要调用者在事件处理的实现上来判断事件携带的数据是否是正确的或者完整的，这是实现者应该要注意的事情。
	             */
	            ringBuffer.publish(sequence);//发布事件；
	        }
	    }
}
