package com.mk.convention.utils.disruptor;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

//单元测试类
public class TestDisruptor2 {
  private static final Logger log = LoggerFactory
          .getLogger(TestDisruptor2.class);
  @Test
  public void myTest() throws Exception {
	  Executor executor = Executors.newCachedThreadPool();
      Disruptor<MyEvent> disruptor = new Disruptor<>(new MyEventFactory(),
              1024, executor,
              ProducerType.SINGLE, new YieldingWaitStrategy());

      disruptor.handleExceptionsWith(new MyHandlerException());
     // disruptor.handleEventsWith(new ProductDetailHandler());
      disruptor.handleEventsWithWorkerPool(new ProductDetailHandler());
     /* disruptor.handleEventsWithWorkerPool(new Handler1())
              .thenHandleEventsWithWorkerPool(new Handler11())
              .thenHandleEventsWithWorkerPool(new Handler2());
      	*/
      disruptor.start();
      CountDownLatch countDownLatch = new CountDownLatch(1);
      for (int i=0;i<10;i++) {
    	//  int no = i + 1;
    	  long seq = disruptor.getRingBuffer().next();
    	  NewProduce ep1 = new NewProduce().setDisruptor(disruptor,seq,countDownLatch);
    	  executor.execute(ep1);
      }
      
      countDownLatch.await();
     // countDownLatch2.await();
      
      disruptor.shutdown();

      log.debug("运行完毕");
  }

}