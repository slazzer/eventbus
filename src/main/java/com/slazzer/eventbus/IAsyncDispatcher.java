package com.slazzer.eventbus;


public interface IAsyncDispatcher {

   public void publish(Object event);

   public void register(Class<?> eventClass, EventHandlerInfo info);

}
