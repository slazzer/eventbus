package com.slazzer.eventbus;

import java.util.Map;
import java.util.Set;


/**
 * Interface for a strategy to find event registrations made by a given object (the subscriber).
 * Those registrations consist in sets of event handlers methods, each one of those sets subscribing
 * to a certain event type.
 */
public interface IEventHandlerLookup {

   public Map<Class<?>, Set<EventHandlerInfo>> findSynchronousHandlers(Object subscriber);

   Map<Class<?>, Set<EventHandlerInfo>> findAsynchronousHandlers(Object subscriber);

}