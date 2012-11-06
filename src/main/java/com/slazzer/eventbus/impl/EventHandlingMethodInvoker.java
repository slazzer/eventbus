package com.slazzer.eventbus.impl;

import java.lang.reflect.InvocationTargetException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Performs a method invocation using provided parameters. Failures are wrapped in an unchecked
 * EventHandlingException.
 * 
 */
public class EventHandlingMethodInvoker {

   private static final Logger LOGGER = LoggerFactory.getLogger(EventHandlingMethodInvoker.class);

   public void invoke(CommandHandlerInvocationParameters invocationParameters) {
      LOGGER.debug("Invoking method " + invocationParameters.method.getName() + " of object "
               + invocationParameters.object + " with message " + invocationParameters.argument);

      try {
         invocationParameters.method.invoke(invocationParameters.object,
                  invocationParameters.argument);
      } catch (IllegalArgumentException e) {
         throw new EventHandlingException(e);
      } catch (IllegalAccessException e) {
         throw new EventHandlingException(e);
      } catch (InvocationTargetException e) {
         throw new EventHandlingException(e.getCause());
      }
   }

}
