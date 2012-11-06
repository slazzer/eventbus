package com.slazzer.eventbus.impl;

import java.lang.reflect.InvocationTargetException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.slazzer.eventbus.EventHandlerInfo;

import akka.actor.UntypedActor;

public class GenericActorHandler extends UntypedActor {

   private static final Logger LOGGER = LoggerFactory.getLogger(SimpleEventHandlerInvoker.class);

   private final EventHandlerInfo info;

   public GenericActorHandler(EventHandlerInfo info) {
      this.info = info;
   }

   @Override
   public void onReceive(Object message) throws Exception {
      LOGGER.debug("Async Actor Received message " + message + " to be handled by "
               + info.getSubscriber() + "." + info.getHandlerMethod().getName());

      try {
         info.getHandlerMethod().invoke(info.getSubscriber(), message);
      } catch (IllegalArgumentException e) {
         throw new EventHandlingException(e);
      } catch (IllegalAccessException e) {
         throw new EventHandlingException(e);
      } catch (InvocationTargetException e) {
         throw new EventHandlingException(e.getCause());
      }

   }

}
