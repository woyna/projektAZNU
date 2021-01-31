package org.bp.media.state;


import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.bp.media.model.OrderMediaRequest;
import org.bp.media.model.OrderInfo;
import org.springframework.stereotype.Service;


public class StateService {
	private HashMap<String, StateMachine> processingStates=new HashMap<>();

	public StateService(StateMachineBuilder stateMachineBuilder) {
		this.stateMachineBuilder = stateMachineBuilder;
	}


	private StateMachineBuilder stateMachineBuilder = null;

	public ProcessingState sendEvent(String orderId, ProcessingEvent event) {
		StateMachine stateMachine;
		synchronized(this){
			stateMachine = processingStates.get(orderId);
			if (stateMachine==null) {
				stateMachine=stateMachineBuilder.build();
				processingStates.put(orderId, stateMachine);
			}
		}
		return stateMachine.sendEvent(event);
		
	}

	public void removeState(String orderId) {
		processingStates.remove(orderId);
	}

}