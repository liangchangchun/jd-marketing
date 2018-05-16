package com.mk.convention.utils.disruptor;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

//单元测试类
public class TestDisruptor {
  private static final Logger log = LoggerFactory
          .getLogger(TestDisruptor.class);

  public void myTest() throws Exception {
	  Executor executor = Executors.newCachedThreadPool();
      Disruptor<MyEvent> disruptor = new Disruptor<>(new MyEventFactory(),
              1024, executor,
              ProducerType.SINGLE, new YieldingWaitStrategy());

      disruptor.handleExceptionsWith(new MyHandlerException());
      disruptor.handleEventsWithWorkerPool(new Handler1())
              .thenHandleEventsWithWorkerPool(new Handler11())
              .thenHandleEventsWithWorkerPool(new Handler2());

      disruptor.start();

      MyEventProduce ep = new MyEventProduce().setDisruptor(disruptor);
      CountDownLatch countDownLatch = ep.getCountDownLatch();
      executor.execute(ep);
      countDownLatch.await();
      disruptor.shutdown();

      log.debug("运行完毕");
  }

}