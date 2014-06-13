package ceptestertest;

import static org.junit.Assert.*;

import org.junit.Test;

import ceptester.SimulateState;
import ceptester.StateChange;

public class SimuateStateTest {

	@Test
	public void testALL() {
		SimulateState simulatestate = new SimulateState(); 
        String state;
        int observationsMap [][] = {{1, 1, 2, 2, 2},
        		                   {1, 1, 1, 1, 1},
        		                   {1, 1, 1, 2, 2},
        		                   {1, 1, 1, 2, 2}};

		state = simulatestate.getState(1, "ALL", 1, observationsMap);	
		System.out.println(state);
		assertEquals("OK", state);
	}
	

	@Test
	public void testQuorum() {
		SimulateState simulatestate = new SimulateState(); 
        String state;
        int observationsMap [][] = {{1, 1, 2, 2, 3},
        		                   {1, 1, 1, 1, 3},
        		                   {1, 1, 1, 2, 3},
        		                   {1, 1, 1, 2, 2}};

		state = simulatestate.getState(1, "Quorum", 4, observationsMap);	
		System.out.println(state);
		assertEquals("CRITICAL", state);
	}
	
	@Test
	public void testALLWithConsecutiveCount3() {
		SimulateState simulatestate = new SimulateState(); 
        String state;
        int observationsMap [][] = {{1, 1, 1, 2, 2},
        		                   {1, 1, 1, 1, 1},
        		                   {1, 1, 1, 2, 2},
        		                   {1, 1, 1, 2, 2}};

		state = simulatestate.getState(3, "ALL", 2, observationsMap);	
		System.out.println(state);
		assertEquals("OK", state);
	}
	
	@Test
	public void testQuorumWithConsecutiveCount3() {
		SimulateState simulatestate = new SimulateState(); 
        String state;
        int observationsMap [][] = {{1, 2, 2, 2, 2},
        		                   {1, 1, 1, 1, 1},
        		                   {1, 2, 2, 2, 2},
        		                   {1, 2, 2, 2, 2}};

		state = simulatestate.getState(3, "Quorum", 3, observationsMap);	
		System.out.println(state);
		assertEquals("WARNING", state);
	}
	
	@Test
	public void testStateChangeForALLForConsecutiveCount1() {
		SimulateState simulatestate = new SimulateState(); 
        StateChange state;
        int observationsMap [][] = {{1, 2, 2, 2, 2},
        		                   {1, 1, 1, 1, 1},
        		                   {1, 2, 2, 2, 2},
        		                   {1, 2, 2, 2, 2}};

		state = simulatestate.getStateChange(1, "Quorum", 0, 2, observationsMap);	
		System.out.println("State Changed from:" + state.getInitialState() + " to " + state.getFinalState());
		assertEquals("OK", state.getInitialState());
		assertEquals("WARNING", state.getFinalState());
	}
	
	@Test
	public void testStateChangeForALLForConsecutiveCount3() {
		SimulateState simulatestate = new SimulateState(); 
        StateChange state;
        int observationsMap [][] = {{1, 1, 1, 1, 1, 1 },
        		                   {1, 1, 1, 2, 2, 2},
        		                   {1, 1, 1, 2, 2, 2},
        		                   {1, 1, 2, 2, 2, 2}};

		state = simulatestate.getStateChange(3, "Quorum", 2, 5, observationsMap);	
		System.out.println("State Changed from:" + state.getInitialState() + " to " + state.getFinalState());
		assertEquals("OK", state.getInitialState());
		assertEquals("WARNING", state.getFinalState());
	}
	
	
}
