package com.crazy.core.statemachine.action;

import com.crazy.core.statemachine.impl.BeiMiExtentionTransitionConfigurer;
import com.crazy.core.statemachine.message.Message;

public interface Action<T,S> {
	void execute(Message<T> message, BeiMiExtentionTransitionConfigurer<T, S> configurer);
}
