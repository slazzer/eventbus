package com.slazzer.eventbus.impl;

/**
 * Exception thrown in the case of an error occurring during the invocation of an event handling
 * method.
 */
public class EventHandlingException extends RuntimeException {

   private static final long serialVersionUID = -953799980763460616L;

   public EventHandlingException(String message) {
      super(message);
   }

   public EventHandlingException(Throwable e) {
      super(e);
   }

}
