package com.slazzer.eventbus.utils;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.slazzer.eventbus.EventHandlerClass;
import com.slazzer.eventbus.IEventBus;

@Service
public class EventHandlerBeanProcessor implements ApplicationContextAware {

   private ApplicationContext applicationContext;
   @Autowired
   IEventBus eventBus;

   @PostConstruct
   public void register() {
      Map<String, EventHandlerClass> eventHandlerBeans = applicationContext.getBeansOfType(EventHandlerClass.class);
      for (Map.Entry<String, EventHandlerClass> element : eventHandlerBeans.entrySet()) {
         eventBus.subscribe(element.getValue());
      }

   }

   @Override
   public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
      this.applicationContext = applicationContext;
   }

}
