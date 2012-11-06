package com.slazzer.eventbus;

import com.slazzer.eventbus.impl.EventHandler;

public class TestHandler {

   public long idThreadASync = -1;
   public long idThreadSync = -1;

   @EventHandler(isAsync = true)
   public void aSyncHandler(Message message) {
      idThreadASync = Thread.currentThread().getId();
   }

   @EventHandler
   public void syncHandler(Message message) {
      idThreadSync = Thread.currentThread().getId();
   }
}
