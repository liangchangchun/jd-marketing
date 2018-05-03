package com.mk.convention.utils.jd;

import com.lmax.disruptor.RingBuffer;

public class JdDataEventProducer {
	private final RingBuffer<JdDataEvent> ringBuffer;

    public JdDataEventProducer(RingBuffer<JdDataEvent> ringBuffer)
    {
        this.ringBuffer = ringBuffer;
    }

    public void onData(DataEvent dataEvent)
    {
        long id = ringBuffer.next();  // Grab the next sequence
        try{
        	JdDataEvent event = ringBuffer.get(id);
        	event.setEvent(dataEvent);
        }finally{
            ringBuffer.publish(id);
        }
    }
}
