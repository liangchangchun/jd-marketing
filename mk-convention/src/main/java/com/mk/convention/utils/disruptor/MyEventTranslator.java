package com.mk.convention.utils.disruptor;

import com.lmax.disruptor.EventTranslator;

public class MyEventTranslator implements EventTranslator<MyEvent> {
    private MyEvent myEvent;

    public MyEventTranslator(MyEvent myEvent) {
        this.myEvent = myEvent;
    }

    @Override
    public void translateTo(MyEvent event, long sequence) {
        event.setMyEvent(myEvent);
    }

}