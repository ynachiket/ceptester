package ceptestertest;

import static org.junit.Assert.*;

import org.junit.Test;

import ceptester.SimulateState;

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
		assertEquals("Critical", state);
	}
}
