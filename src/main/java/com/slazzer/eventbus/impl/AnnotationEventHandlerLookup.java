package com.slazzer.eventbus.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.MethodCallback;

import com.slazzer.eventbus.EventHandlerInfo;
import com.slazzer.eventbus.IEventHandlerLookup;

/**
 * Strategy to find event subscriptions made by a given object (the subscriber). The event handling
 * methods are considered to be the one declaring the OrderProcessEventHandler annotation, and
 * taking the event class as a parameter.
 */
public class AnnotationEventHandlerLookup implements IEventHandlerLookup {

   @Override
   public Map<Class<?>, Set<EventHandlerInfo>> findAsynchronousHandlers(Object subscriber) {
      return findHandlers(subscriber, true);
   }

   public Map<Class<?>, Set<EventHandlerInfo>> findHandlers(Object subscriber,
            boolean synchronousFilter) {
      Map<Class<?>, Set<EventHandlerInfo>> handlers = new HashMap<Class<?>, Set<EventHandlerInfo>>();
      for (Method m : getAllDeclaredMethods(subscriber.getClass())) {
         for (Annotation annotation : m.getAnnotations()) {
            if (annotation.annotationType().isAssignableFrom(EventHandler.class)) {
               if (((EventHandler) annotation).isAsync() == synchronousFilter) {
                  Class<?> eventClass = m.getParameterTypes()[0];
                  EventHandlerInfo eventHandlerInfo = new EventHandlerInfo(subscriber, m,
                           ((EventHandler) annotation).isAsync());
                  addHandler(handlers, eventClass, eventHandlerInfo);
               }
            }
         }
      }
      return handlers;
   }

   @Override
   public Map<Class<?>, Set<EventHandlerInfo>> findSynchronousHandlers(Object subscriber) {
      return findHandlers(subscriber, false);
   }

   private void addHandler(Map<Class<?>, Set<EventHandlerInfo>> subscriptions, Class<?> eventClass,
            EventHandlerInfo eventHandlerInfo) {
      Set<EventHandlerInfo> eventHandlers = subscriptions.get(eventClass);
      if (eventHandlers == null) {
         eventHandlers = new HashSet<EventHandlerInfo>();
         subscriptions.put(eventClass, eventHandlers);
      }
      eventHandlers.add(eventHandlerInfo);
   }

   private Method[] getAllDeclaredMethods(Class<?> leafClass) throws IllegalArgumentException {
      final List<Method> list = new ArrayList<Method>(32);
      ReflectionUtils.doWithMethods(leafClass, new MethodCallback() {
         @Override
         public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
            list.add(method);
         }
      });
      return list.toArray(new Method[list.size()]);
   }

}
