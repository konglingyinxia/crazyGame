package com.crazy.config.web;

import com.crazy.core.statemachine.BeiMiStateMachine;
import com.crazy.core.statemachine.impl.BeiMiMachineHandler;
import com.crazy.config.web.model.Game;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class BeiMiStateMachineHandlerConfig {
	
	@Resource(name="dizhu")    
	private BeiMiStateMachine<String,String> dizhuConfigure ;
	
	@Resource(name="majiang")    
	private BeiMiStateMachine<String,String> maJiangConfigure ;
	
    @Bean("dizhuGame")
    public Game dizhu() {
        return new Game(new BeiMiMachineHandler(this.dizhuConfigure));
    }
    
    @Bean("majiangGame")
    public Game majiang() {
        return new Game(new BeiMiMachineHandler(this.maJiangConfigure));
    }
}
