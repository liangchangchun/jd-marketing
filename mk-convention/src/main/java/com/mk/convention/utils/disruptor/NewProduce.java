package com.mk.convention.utils.disruptor;

import java.util.concurrent.CountDownLatch;

import com.lmax.disruptor.dsl.Disruptor;
import com.mk.convention.utils.jd.JdDataEvent;
//数据构造
public class NewProduce  implements Runnable {
	  private final int SIZE = 3;
	  private CountDownLatch countDownLatch;
	  private Disruptor<MyEvent> disruptor;
	  private long seq =0;

	  public NewProduce() {
	     // countDownLatch = new CountDownLatch(SIZE);
	  }

	  public NewProduce setDisruptor(Disruptor<MyEvent> disruptor,long seq,CountDownLatch countDownLatch) {
	      this.disruptor = disruptor;
	      this.seq  = seq;
	      this.countDownLatch = countDownLatch;
	      return this;
	  }

	  public CountDownLatch getCountDownLatch() {
	      return countDownLatch;
	  }

	  @Override
	  public void run() {
	      //for (int i = 1; i <= SIZE; i++) {
		  	MyEvent event = disruptor.getRingBuffer().get(seq) ;
	          event.setName("name--" + seq);
	          event.setCountDownLatch(countDownLatch);
	          disruptor.publishEvent(new MyEventTranslator(event));
	     // }
	  }

	}
