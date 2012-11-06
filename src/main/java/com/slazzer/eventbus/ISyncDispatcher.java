package com.slazzer.eventbus;


public interface ISyncDispatcher {

   public void publish(Object event);

   public void register(Class<?> eventClass, EventHandlerInfo info);

}
