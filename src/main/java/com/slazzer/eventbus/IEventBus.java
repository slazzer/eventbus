package com.slazzer.eventbus;


/**
 * Interface for a pub/sub mechanism, allowing indirect communications between components located
 * inside a single JVM.
 */
public interface IEventBus {

   /**
    * Publishes an event to all subscribed handlers.
    * 
    * @param event the event to dispatch
    */
   void publish(Object event);

   /**
    * Subscribes the object to events dispatched on this bus.
    * 
    * @param subscriber the subscribing object
    */
   void subscribe(Object subscriber);

}