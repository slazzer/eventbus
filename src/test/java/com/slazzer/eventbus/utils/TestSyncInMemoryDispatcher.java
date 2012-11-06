package com.slazzer.eventbus.utils;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

import com.slazzer.eventbus.EventHandlerInfo;
import com.slazzer.eventbus.IAsyncDispatcher;
import com.slazzer.eventbus.ISyncDispatcher;
import com.slazzer.eventbus.impl.PostCommitExecutor;

public class TestSyncInMemoryDispatcher implements IAsyncDispatcher, PostCommitExecutor {

   private ISyncDispatcher dispatcher;

   ThreadLocal<List<Object>> eventToPublish = new ThreadLocal<List<Object>>();

   @Autowired
   SessionFactory sessionFactory;

   public TestSyncInMemoryDispatcher() {
   }

   public TestSyncInMemoryDispatcher(ISyncDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   @Override
   public void endTransaction() {
      eventToPublish.get().clear();
      eventToPublish.remove();
   }

   @Override
   public void performTransaction() {
      try {
         Thread.sleep(100L);
      } catch (InterruptedException e) {
         e.printStackTrace();
      }
      List<Object> eventADispatcher = eventToPublish.get();
      for (Object message : eventADispatcher) {
         internalPublish(message);
      }
      // Nécessaire en mode sync pour forcer le comportement d'execution des events async
      SessionFactoryUtils.getSession(sessionFactory, false).flush();
   }

   @Override
   public void publish(Object event) {
      List<Object> list = eventToPublish.get();
      list.add(event);
   }

   @Override
   public void register(Class<?> eventClass, EventHandlerInfo info) {
      dispatcher.register(eventClass, info);
   }

   @Override
   public void startNewTransaction() {
      eventToPublish.set(new ArrayList<Object>());
   }

   private void internalPublish(Object event) {
      dispatcher.publish(event);
   }

}
