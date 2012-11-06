package com.slazzer.eventbus.impl;

import java.lang.reflect.InvocationTargetException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.slazzer.eventbus.IEventHandlerInvoker;

/**
 * Event dispatcher that always dispatches events in a synchronous fashion, by directly invoking the
 * handler method on the subscriber.
 */
public class SimpleEventHandlerInvoker implements IEventHandlerInvoker {

   private static final Logger LOGGER = LoggerFactory.getLogger(SimpleEventHandlerInvoker.class);

   @Override
   public void invoke(CommandHandlerInvocationParameters invocationParameters) {
      LOGGER.debug("Received message " + invocationParameters.argument + " to be handled by "
               + invocationParameters.object + "." + invocationParameters.method.getName());

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
