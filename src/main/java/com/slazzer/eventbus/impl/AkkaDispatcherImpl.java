package com.slazzer.eventbus.impl;

import java.util.ArrayList;
import java.util.List;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import com.slazzer.eventbus.EventHandlerInfo;
import com.slazzer.eventbus.IAsyncDispatcher;

public class AkkaDispatcherImpl implements PostCommitExecutor, IAsyncDispatcher {

   private final ActorSystem system = ActorSystem.create("DomainEvents");
   ThreadLocal<List<Object>> eventToPublish = new ThreadLocal<List<Object>>();

   public AkkaDispatcherImpl() {
   }

   @Override
   public void endTransaction() {
      eventToPublish.get().clear();
      eventToPublish.remove();
   }

   @Override
   public void performTransaction() {
      List<Object> eventADispatcher = eventToPublish.get();
      for (Object message : eventADispatcher) {
         internalPublish(message);
      }
   }

   @Override
   public void publish(Object event) {
      List<Object> list = eventToPublish.get();
      list.add(event);
   }

   @Override
   public void register(Class<?> eventClass, EventHandlerInfo info) {
      ActorRef actor = system.actorOf(new Props(new GenericActorHandlerFactory(info)));
      system.eventStream().subscribe(actor, eventClass);
   }

   @Override
   public void startNewTransaction() {
      eventToPublish.set(new ArrayList<Object>());
   }

   private void internalPublish(Object message) {
      system.eventStream().publish(message);
   }

}
