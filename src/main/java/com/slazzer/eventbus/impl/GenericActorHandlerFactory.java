package com.slazzer.eventbus.impl;

import akka.actor.Actor;
import akka.actor.UntypedActorFactory;

import com.slazzer.eventbus.EventHandlerInfo;

public class GenericActorHandlerFactory implements UntypedActorFactory {

   private static final long serialVersionUID = -1651067807121422672L;
   private final EventHandlerInfo info;

   public GenericActorHandlerFactory(EventHandlerInfo info) {
      this.info = info;
   }

   @Override
   public Actor create() {
      GenericActorHandler handler = new GenericActorHandler(info);
      return handler;
   }
}
