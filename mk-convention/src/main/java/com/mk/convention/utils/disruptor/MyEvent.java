package com.mk.convention.utils.disruptor;

import java.util.concurrent.CountDownLatch;

//首先定义一个事件
public class MyEvent {
	private String name;
    private CountDownLatch countDownLatch;

    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCountDownLatch(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }
    
    
    public void setMyEvent(MyEvent myEvent){
        name = myEvent.name;
        countDownLatch = myEvent.countDownLatch;
    }

}
