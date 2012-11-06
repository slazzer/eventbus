package com.slazzer.eventbus.impl;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.slazzer.eventbus.EventHandlerInfo;
import com.slazzer.eventbus.IEventHandlerInvoker;
import com.slazzer.eventbus.ISyncDispatcher;

public class SyncInMemoryDispatcher implements ISyncDispatcher {

   private static final Logger LOGGER = LoggerFactory.getLogger(SyncInMemoryDispatcher.class);

   private final IEventHandlerInvoker eventDispatcher;

   private final ConcurrentMap<Class<?>, Set<EventHandlerInfo>> registeredEventHandlers = new ConcurrentHashMap<Class<?>, Set<EventHandlerInfo>>();

   public SyncInMemoryDispatcher(IEventHandlerInvoker eventDispatcher) {
      this.eventDispatcher = eventDispatcher;
   }

   @Override
   public void publish(Object event) {
      Set<EventHandlerInfo> eventHandlerInfos = getHandlers(event);
      for (EventHandlerInfo eventHandlerInfo : eventHandlerInfos) {
         CommandHandlerInvocationParameters invocationParameters = new CommandHandlerInvocationParameters(
                  eventHandlerInfo.getSubscriber(), eventHandlerInfo.getHandlerMethod(), event);
         LOGGER.debug("Dispatching event " + event + " to " + eventHandlerInfo.getSubscriber()
                  + "." + eventHandlerInfo.getHandlerMethod().getName());
         eventDispatcher.invoke(invocationParameters);
      }

   }

   @Override
   public void register(Class<?> eventClass, EventHandlerInfo info) {
      Set<EventHandlerInfo> registered = registeredEventHandlers.get(eventClass);
      if (registered == null) {
         registered = new HashSet<EventHandlerInfo>();
         registeredEventHandlers.put(eventClass, registered);
      }
      LOGGER.debug("Adding handlers for " + eventClass.getCanonicalName() + " : " + registered);
      registered.add(info);

   }

   private Set<EventHandlerInfo> getHandlers(Object message) {
      Set<EventHandlerInfo> eventHandlers = registeredEventHandlers.get(message.getClass());
      return eventHandlers != null ? eventHandlers : new HashSet<EventHandlerInfo>();
   }

}
