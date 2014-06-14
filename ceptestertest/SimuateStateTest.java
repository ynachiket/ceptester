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

		state = simulatestate.getState(1, "QUORUM", 4, observationsMap);	
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

		state = simulatestate.getState(3, "QUORUM", 3, observationsMap);	
		assertEquals("WARNING", state);
	}

	@Test
	public void testStateChangeForALLForConsecutiveCount1() {
		SimulateState simulatestate = new SimulateState(); 
        StateChange state;
        int observationsMap [][] = {{1, 2, 2, 2, 2},
        		                   {1, 3, 2, 1, 1},
        		                   {1, 1, 2, 2, 2},
        		                   {1, 2, 2, 2, 2}};

		state = simulatestate.getStateChange(1, "ALL", 0, 2, observationsMap);	
		System.out.println("State Changed from:" + state.getInitialState() + " to " + state.getFinalState());
		assertEquals("OK", state.getInitialState());
		assertEquals("WARNING", state.getFinalState());
	}

	@Test
	public void testStateChangeForALLForConsecutiveCount3() {
		SimulateState simulatestate = new SimulateState(); 
        StateChange state;
        int observationsMap [][] = {{1, 1, 1, 2, 2, 2 },
        		                   {1, 1, 1, 2, 2, 2},
        		                   {1, 1, 1, 2, 2, 2},
        		                   {1, 1, 1, 2, 2, 2}};

		state = simulatestate.getStateChange(3, "ALL", 2, 5, observationsMap);	
		System.out.println("State Changed from:" + state.getInitialState() + " to " + state.getFinalState());
		assertEquals("OK", state.getInitialState());
		assertEquals("WARNING", state.getFinalState());
	}
	
	
	@Test
	public void testStateChangeSequenceForALLForConsecutiveCount3() {
		SimulateState simulatestate = new SimulateState(); 
        StateChange state;
        int observationsMap [][] = {{1, 1, 1, 2, 2, 2},
        		                   {1, 1, 1, 2, 2, 2},
        		                   {1, 1, 1, 2, 2, 2},
        		                   {1, 1, 1, 2, 2, 2}};

		state = simulatestate.getStateChangeSequence(3, "ALL", 2, 5, observationsMap);	
		System.out.println("State Changed from:" + state.getInitialState() + " to " + state.getFinalState());
		assertEquals("OK", state.getInitialState());
		assertEquals("WARNING", state.getFinalState());
	}
	
	@Test
	public void testStateChangeSequenceForALLForConsecutiveCount1() {
		SimulateState simulatestate = new SimulateState(); 
        StateChange state;
        int observationsMap [][] = {{1, 1, 3, 2, 1, 2},
        		                   {1, 1, 2, 2, 1, 2},
        		                   {1, 1, 3, 2, 1, 2},
        		                   {1, 1, 3, 2, 1, 2}};

		state = simulatestate.getStateChangeSequence(1, "ALL", 1, 5, observationsMap);	
		System.out.println("State Changed from:" + state.getInitialState() + " to " + state.getFinalState());
		String [] sequence = state.getstateChangeSequence();
		for (int i = 0; i < sequence.length; i++) {
			System.out.println(sequence[i]);
		}
		assertEquals("OK", sequence[0]);
		assertEquals("NO-OP", sequence[1]);
		assertEquals("WARNING", sequence[2]);
		assertEquals("OK", sequence[3]);
		assertEquals("WARNING", sequence[4]);
	}
		
}
