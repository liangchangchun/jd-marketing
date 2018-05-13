package com.mk.convention.utils.disruptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

public class ProductDetailHandler implements EventHandler<MyEvent>, WorkHandler<MyEvent>{
	  private static final Logger log = LoggerFactory.getLogger(ProductDetailHandler.class);

	  @Override
	  public void onEvent(MyEvent event) throws Exception {
	      log.debug(event.getName() + "====ProductDetailHandler........");
	      event.getCountDownLatch().countDown();
	  }

	  @Override
	  public void onEvent(MyEvent event, long sequence, boolean endOfBatch)
	          throws Exception {
	      onEvent(event);
	  }
}
