package com.slazzer.eventbus;

import java.lang.reflect.Method;

/**
 * Describes an event handler, which consists of a subscribing object and a handling method.
 */
public class EventHandlerInfo {

   private final boolean async;
   private final Method handlerMethod;
   private final Object subscriber;

   public EventHandlerInfo(Object subscriber, Method handlerMethod, boolean isAsync) {
      this.subscriber = subscriber;
      this.handlerMethod = handlerMethod;
      this.async = isAsync;
   }

   public Method getHandlerMethod() {
      return handlerMethod;
   }

   public Object getSubscriber() {
      return subscriber;
   }

   public boolean isAsync() {
      return async;
   }

}
