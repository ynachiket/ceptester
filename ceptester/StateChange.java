package ceptester;

public class StateChange {
	
	String initialState;
	String finalState;
	String [] stateChangeSequence;
	
	public String getInitialState() {
		return initialState;
	} 
	
	public String getFinalState() {
		return finalState;
	}
	
	public String [] getstateChangeSequence() {
		return stateChangeSequence;
	}
	
	public void setInitialState(String state) {
		initialState = state;
	}
	
	public  void setFinalState(String state) {
		finalState = state;
	}
	
	public void setstateChangeSequence(String [] ChangeSequence) {
		stateChangeSequence = ChangeSequence;
	}

}
