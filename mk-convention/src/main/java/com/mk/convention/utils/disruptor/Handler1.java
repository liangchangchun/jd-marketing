package com.mk.convention.utils.disruptor;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;


//第一个消费者
public class Handler1 implements EventHandler<MyEvent>, WorkHandler<MyEvent> {
  private static final Logger log = LoggerFactory.getLogger(Handler1.class);

  @Override
  public void onEvent(MyEvent event) throws Exception {
      log.debug(event.getName() + "====Handler1 。。。。");
//      throw new RuntimeException("测试异常");
  }

  @Override
  public void onEvent(MyEvent event, long sequence, boolean endOfBatch)
          throws Exception {
      System.out.println("not go");
      onEvent(event);
  }

}