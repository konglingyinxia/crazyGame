package com.crazy.config.web;

import com.crazy.util.disruptor.UserDataEventFactory;
import com.crazy.util.disruptor.UserEventHandler;
import com.crazy.util.event.UserDataEvent;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Component
public class DisruptorConfigure {
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Bean(name="disruptor")   
    public Disruptor<UserDataEvent> disruptor() {   
		Executor executor = Executors.newCachedThreadPool();
    	 UserDataEventFactory factory = new UserDataEventFactory();
    	 Disruptor<UserDataEvent> disruptor = new Disruptor<UserDataEvent>(factory, 1024, executor, ProducerType.SINGLE , new SleepingWaitStrategy());
    	 disruptor.setDefaultExceptionHandler(new BeiMiExceptionHandler());
    	 disruptor.handleEventsWith(new UserEventHandler());
    	 disruptor.start();
         return disruptor;   
    }  
}
