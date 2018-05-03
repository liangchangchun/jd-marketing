package com.mk.convention.utils.disruptor;

import java.util.concurrent.CountDownLatch;

import com.lmax.disruptor.dsl.Disruptor;

//数据构造
public class MyEventProduce implements Runnable {
  private final int SIZE = 3;
  private CountDownLatch countDownLatch;
  private Disruptor<MyEvent> disruptor;

  public MyEventProduce() {
      countDownLatch = new CountDownLatch(SIZE);
  }

  public MyEventProduce setDisruptor(Disruptor<MyEvent> disruptor) {
      this.disruptor = disruptor;
      return this;
  }

  public CountDownLatch getCountDownLatch() {
      return countDownLatch;
  }

  @Override
  public void run() {
      for (int i = 1; i <= SIZE; i++) {
          MyEvent event = new MyEvent();
          event.setName("name--" + i);
          event.setCountDownLatch(countDownLatch);
          disruptor.publishEvent(new MyEventTranslator(event));
      }
  }

}