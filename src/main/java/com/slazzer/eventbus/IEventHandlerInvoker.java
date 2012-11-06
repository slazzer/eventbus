package com.slazzer.eventbus;

import com.slazzer.eventbus.impl.CommandHandlerInvocationParameters;

/**
 * Object responsible for dispatching an event to a certain object, which consists in a method
 * invocation on that object.
 */
public interface IEventHandlerInvoker {

   public void invoke(CommandHandlerInvocationParameters params);

}
