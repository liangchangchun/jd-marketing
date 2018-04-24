package com.ttt.test;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class OrderNoTest {

	private static AtomicInteger count = new AtomicInteger(0);

	public static void main(String[] args) throws InterruptedException {
		int threadNumber = 90000;
        final CountDownLatch countDownLatch = new CountDownLatch(threadNumber);
		for (int i=0; i<3000 ; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					for (int j=0; j < 30 ; j++) {
						final int person = j;
						String orderNo = OrderNoHandler.getInstance().getOrder(person);
						OrderNoHandler.getInstance().addNo(orderNo);
						System.out.println(Thread.currentThread().getName()+" ---- 订单编号:"+orderNo + ",order数量:" + OrderNoHandler.getInstance().getOrderNoSize());
						countDownLatch.countDown();
						count.incrementAndGet();
					}
					
				}
			}).start();
		}
		 countDownLatch.await();
		 System.out.println("xxxxxxxxxxx最终 order数量:" + OrderNoHandler.getInstance().getOrderNoSize() + ",count:" +count);
	}

}
