package com.slazzer.eventbus.impl;

import java.lang.reflect.Method;

public class CommandHandlerInvocationParameters {

   public Object argument;
   public Method method;
   public Object object;

   public CommandHandlerInvocationParameters(Object object, Method method, Object argument) {
      this.object = object;
      this.method = method;
      this.argument = argument;
   }

}
