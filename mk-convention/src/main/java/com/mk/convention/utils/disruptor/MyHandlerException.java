package com.mk.convention.utils.disruptor;

import com.lmax.disruptor.ExceptionHandler;

//异常处理事件类
public class MyHandlerException implements ExceptionHandler {

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.lmax.disruptor.ExceptionHandler#handleEventException(java.lang.Throwable
   * , long, java.lang.Object)
   */
  @Override
  public void handleEventException(Throwable ex, long sequence, Object event) {
      // TODO Auto-generated method stub
      System.out.println("MyHandlerException handleEventException...");
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.lmax.disruptor.ExceptionHandler#handleOnStartException(java.lang.
   * Throwable)
   */
  @Override
  public void handleOnStartException(Throwable ex) {
      System.out.println("MyHandlerException handleOnStartException...");
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.lmax.disruptor.ExceptionHandler#handleOnShutdownException(java.lang
   * .Throwable)
   */
  @Override
  public void handleOnShutdownException(Throwable ex) {
      System.out.println("MyHandlerException handleOnShutdownException...");
  }

}