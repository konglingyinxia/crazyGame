package com.crazy.core.statemachine;

import com.crazy.core.statemachine.config.StateConfigurer;
import com.crazy.core.statemachine.config.StateMachineTransitionConfigurer;
import com.crazy.core.statemachine.impl.BeiMiStateContext;
import com.crazy.core.statemachine.impl.BeiMiTransitionConfigurer;

public class BeiMiStateMachine<T,S> {
	/**
	 * 
	 */
	private StateConfigurer<String,String> config = new BeiMiStateContext<String,String>();
	private StateMachineTransitionConfigurer<T,S> transitions = new BeiMiTransitionConfigurer<T,S>() ;
	
	public StateConfigurer<String,String> getConfig() {
		return config;
	}
	public void setConfig(StateConfigurer<String,String> config) {
		this.config = config;
	}
	public StateMachineTransitionConfigurer<T,S> getTransitions() {
		return transitions;
	}
	public void setTransitions(
			StateMachineTransitionConfigurer<T,S> transitions) {
		this.transitions = transitions;
	}
}
