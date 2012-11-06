package com.slazzer.eventbus.impl;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.slazzer.eventbus.EventHandlerInfo;
import com.slazzer.eventbus.IAsyncDispatcher;
import com.slazzer.eventbus.IEventBus;
import com.slazzer.eventbus.IEventHandlerInvoker;
import com.slazzer.eventbus.IEventHandlerLookup;
import com.slazzer.eventbus.ISyncDispatcher;

/**
 * Simple implementation of an in-memory, JVM-local event bus. Event handlers are detected on
 * subscribing objects using a {@link IEventHandlerLookup}. Published events will be dispatched to
 * each subscriber using a {@link IEventHandlerInvoker}.
 * 
 * Please note that there is no guarantee regarding the order in which handlers of a given event
 * type are being dispatched an event of that type.
 * 
 */
public class EventBus implements IEventBus {

   private static final Logger LOGGER = LoggerFactory.getLogger(EventBus.class);

   private final IAsyncDispatcher asyncDispatcher;
   private final IEventHandlerLookup eventHandlerLookup;

   private final ISyncDispatcher syncDispatcher;

   public EventBus(IEventHandlerLookup subscriptionsLookup, IAsyncDispatcher asyncDispatcher,
            ISyncDispatcher syncDispatcher) {
      LOGGER.info("Initializing event bus");
      this.eventHandlerLookup = subscriptionsLookup;
      this.asyncDispatcher = asyncDispatcher;
      this.syncDispatcher = syncDispatcher;
   }

   @Override
   public void publish(Object event) {
      LOGGER.debug("Publishing event " + event);
      syncDispatcher.publish(event);
      asyncDispatcher.publish(event);
   }

   @Override
   public void subscribe(Object subscriber) {
      Map<Class<?>, Set<EventHandlerInfo>> synchronousEventHandlers = eventHandlerLookup.findSynchronousHandlers(subscriber);
      for (Entry<Class<?>, Set<EventHandlerInfo>> eventSubscriptions : synchronousEventHandlers.entrySet()) {
         registerSynchronousHandlers(eventSubscriptions.getKey(), eventSubscriptions.getValue());
      }

      Map<Class<?>, Set<EventHandlerInfo>> asynchrounousEventHandlers = eventHandlerLookup.findAsynchronousHandlers(subscriber);
      for (Entry<Class<?>, Set<EventHandlerInfo>> eventSubscriptions : asynchrounousEventHandlers.entrySet()) {
         registerAsynchronousHandlers(eventSubscriptions.getKey(), eventSubscriptions.getValue());
      }
   }

   private void registerAsynchronousHandlers(Class<?> eventClass,
            Set<EventHandlerInfo> eventHandlers) {
      for (EventHandlerInfo info : eventHandlers) {
         asyncDispatcher.register(eventClass, info);
      }

      LOGGER.debug("Adding async handlers for " + eventClass.getCanonicalName() + " : "
               + eventHandlers);
   }

   private void registerSynchronousHandlers(Class<?> eventClass, Set<EventHandlerInfo> eventHandlers) {
      for (EventHandlerInfo info : eventHandlers) {
         syncDispatcher.register(eventClass, info);
      }

   }
}