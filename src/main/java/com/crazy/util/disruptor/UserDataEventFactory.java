package com.crazy.util.disruptor;

import com.crazy.util.event.UserDataEvent;
import com.lmax.disruptor.EventFactory;

public class UserDataEventFactory implements EventFactory<UserDataEvent>{

	@Override
	public UserDataEvent newInstance() {
		return new UserDataEvent();
	}
}
