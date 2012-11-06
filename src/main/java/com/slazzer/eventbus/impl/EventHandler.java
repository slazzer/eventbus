package com.slazzer.eventbus.impl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventHandler {
   /**
    * The value indicates if the handler process the event as an asynhronous task
    * 
    * @return the nature of the handler if must be treated as an asynchronous event
    */
   boolean isAsync() default false;
}
