package com.slazzer.eventbus;

import org.junit.Assert;
import org.junit.Test;

import com.slazzer.eventbus.impl.AkkaDispatcherImpl;
import com.slazzer.eventbus.impl.AnnotationEventHandlerLookup;
import com.slazzer.eventbus.impl.EventBus;
import com.slazzer.eventbus.impl.SimpleEventHandlerInvoker;
import com.slazzer.eventbus.impl.SyncInMemoryDispatcher;

public class EventBusTest {
   @Test
   public void testBusWithCommit() {
      AnnotationEventHandlerLookup subscriptionsLookup = new AnnotationEventHandlerLookup();
      AkkaDispatcherImpl asyncInMemoryHandler = new AkkaDispatcherImpl();
      EventBus bus = new EventBus(subscriptionsLookup, asyncInMemoryHandler,
               new SyncInMemoryDispatcher(new SimpleEventHandlerInvoker()));
      TestHandler subscriber = new TestHandler();
      bus.subscribe(subscriber);
      asyncInMemoryHandler.startNewTransaction();
      bus.publish(new Message("Hello"));
      asyncInMemoryHandler.performTransaction();
      asyncInMemoryHandler.endTransaction();
      try {
         Thread.sleep(500);
      } catch (InterruptedException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      // Assert
      Assert.assertFalse(subscriber.idThreadASync == subscriber.idThreadSync);
      Assert.assertFalse(subscriber.idThreadASync == -1);
      Assert.assertFalse(subscriber.idThreadSync == -1);
   }

   @Test
   public void testBusWithoutCommit() {
      AnnotationEventHandlerLookup subscriptionsLookup = new AnnotationEventHandlerLookup();
      AkkaDispatcherImpl asyncInMemoryHandler = new AkkaDispatcherImpl();
      EventBus bus = new EventBus(subscriptionsLookup, asyncInMemoryHandler,
               new SyncInMemoryDispatcher(new SimpleEventHandlerInvoker()));
      TestHandler subscriber = new TestHandler();
      bus.subscribe(subscriber);
      asyncInMemoryHandler.startNewTransaction();
      bus.publish(new Message("Hello"));
      asyncInMemoryHandler.performTransaction();
      asyncInMemoryHandler.endTransaction();
      // Assert
      Assert.assertFalse(subscriber.idThreadASync == subscriber.idThreadSync);
      Assert.assertFalse(subscriber.idThreadSync == -1);
      Assert.assertTrue(subscriber.idThreadASync == -1);
   }

}
