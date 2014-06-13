package com.cloudkick.cep.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.cloudkick.util.SimulateState;
import telescope.thrift.AlarmState;

import java.util.Arrays;

public class SimulateStateTest {

    @Test
    public void testGetLatestObservations() {
        int[][] observations = {{1, 1, 2, 2, 6},
                                {1, 1, 1, 1, 6},
                                {1, 1, 1, 2, 6},
                                {1, 1, 1, 2, 2}};

        int[] latestObservations = SimulateState.getLatestObservations(1, observations, 3, 2);
        assertTrue(Arrays.equals(latestObservations, new int[]{1, 1, 1, 2}));

        latestObservations = SimulateState.getLatestObservations(1, observations, 2, 2);
        assertTrue(Arrays.equals(latestObservations, new int[]{1, 1, 2, 1}));

        latestObservations = SimulateState.getLatestObservations(1, observations, 2, 0);
        assertTrue(Arrays.equals(latestObservations, new int[]{1, 1, 1, 0}));

        latestObservations = SimulateState.getLatestObservations(1, observations, 0, 0);
        assertTrue(Arrays.equals(latestObservations, new int[]{1, 0, 0, 0}));

        latestObservations = SimulateState.getLatestObservations(2, observations, 3, 2);
        assertTrue(Arrays.equals(latestObservations, new int[] {1, 1, 1, 1}));

        latestObservations = SimulateState.getLatestObservations(2, observations, 3, 3);
        assertTrue(Arrays.equals(latestObservations, new int[]{1, 1, 1, 2}));

        latestObservations = SimulateState.getLatestObservations(2, observations, 3, 0);
        assertTrue(Arrays.equals(latestObservations, new int[]{0, 0, 0, 0}));

        latestObservations = SimulateState.getLatestObservations(2, observations, 2, 0);
        assertTrue(Arrays.equals(latestObservations, new int[]{0, 0, 0, 0}));

        latestObservations = SimulateState.getLatestObservations(2, observations, 2, 2);
        assertTrue(Arrays.equals(latestObservations, new int[] {1, 1, 1, 1}));
    }

    @Test
    public void testGetLatestObservation() {
        int[][] observations = {{1, 1, 2, 2, 6},
                                {1, 1, 1, 1, 6},
                                {1, 1, 1, 2, 6},
                                {1, 1, 1, 2, 2}};

        int latestObservation = SimulateState.getLatestObservationForOne(1, observations, 2, 2);
        assertEquals(latestObservation, 1);

        latestObservation = SimulateState.getLatestObservationForOne(1, observations, 2, 3);
        assertEquals(latestObservation, 2);

        latestObservation = SimulateState.getLatestObservationForOne(2, observations, 2, 2);
        assertEquals(latestObservation, 1);

        latestObservation = SimulateState.getLatestObservationForOne(2, observations, 2, 3);
        assertEquals(latestObservation, 0);
    }

    @Test
    public void testALL() {
        AlarmState state;
        int observationsMap [][] = {{1, 1, 2, 2, 2},
                                    {1, 1, 1, 1, 2},
                                    {1, 1, 1, 2, 1},
                                    {1, 1, 1, 2, 2}};

        state = SimulateState.getState(1, "ALL", 3, 1, observationsMap);
        assertEquals(AlarmState.OK, state);

        state = SimulateState.getState(1, "ALL", 2, 1, observationsMap);
        assertEquals(AlarmState.OK, state);

        state = SimulateState.getState(1, "ALL", 3, 2, observationsMap);
        assertEquals(null, state);

        state = SimulateState.getState(1, "ALL", 1, 4, observationsMap);
        assertEquals(AlarmState.WARNING, state);

        state = SimulateState.getState(2, "ALL", 3, 1, observationsMap);
        assertEquals(AlarmState.OK, state);

        state = SimulateState.getState(2, "ALL", 0, 2, observationsMap);
        assertEquals(AlarmState.OK, state);

        state = SimulateState.getState(2, "ALL", 0, 3, observationsMap);
        assertEquals(null, state);
    }


    @Test
    public void testQuorum() {
        AlarmState state;
        int observationsMap [][] = {{1, 1, 2, 2, 6, 6},
                                    {1, 1, 1, 1, 6, 6},
                                    {1, 1, 1, 2, 6, 6},
                                    {1, 1, 1, 2, 2, 6}};

        state = SimulateState.getState(1, "Quorum", 3, 4, observationsMap);
        assertEquals(AlarmState.CRITICAL, state);

        state = SimulateState.getState(1, "Quorum", 3, 2, observationsMap);
        assertEquals(AlarmState.OK, state);

        state = SimulateState.getState(2, "Quorum", 3, 2, observationsMap);
        assertEquals(AlarmState.OK, state);

        state = SimulateState.getState(2, "Quorum", 3, 3, observationsMap);
        assertEquals(AlarmState.OK, state);

        state = SimulateState.getState(2, "Quorum", 3, 5, observationsMap);
        assertEquals(AlarmState.CRITICAL, state);

        state = SimulateState.getState(1, "Quorum", 1, 4, observationsMap);
        assertEquals(AlarmState.WARNING, state);

        int observationsMap2 [][]= {{1, 1, 2, 1, 6, 6},
                                    {1, 1, 1, 1, 6, 6},
                                    {1, 1, 1, 2, 6, 6},
                                    {1, 1, 1, 2, 2, 6}};

        state = SimulateState.getState(1, "Quorum", 3, 3, observationsMap2);
        assertEquals(null, state);

        /*int[][] observations6 = new int[][]{{6, 1, 6, 6, 1, 1},
                                            {1, 6, 1, 6, 1, 1},
                                            {6, 1, 6, 6, 1, 1},
                                            {1, 6, 1, 6, 1, 1},
                                            {6, 1, 6, 6, 1, 1}};
        state = SimulateState.getState(2, "Quorum", 2, 3, observations6);
        assertEquals(null, state);*/
    }

    @Test
    public void testOne() {
        AlarmState state;
        int observationsMap [][] = {{1, 1, 2, 2, 6, 6},
                                    {1, 1, 1, 1, 6, 6},
                                    {1, 1, 1, 2, 6, 6},
                                    {1, 1, 1, 2, 2, 6}};

        state = SimulateState.getState(1, "One", 0, 0, observationsMap);
        assertEquals(AlarmState.OK, state);

        state = SimulateState.getState(1, "One", 0, 2, observationsMap);
        assertEquals(AlarmState.WARNING, state);

        state = SimulateState.getState(1, "One", 1, 4, observationsMap);
        assertEquals(AlarmState.CRITICAL, state);

        state = SimulateState.getState(2, "One", 1, 3, observationsMap);
        assertEquals(AlarmState.OK, state);

        state = SimulateState.getState(2, "One", 0, 2, observationsMap);
        assertEquals(null, state);

        state = SimulateState.getState(2, "One", 0, 3, observationsMap);
        assertEquals(AlarmState.WARNING, state);

        state = SimulateState.getState(3, "One", 1, 3, observationsMap);
        assertEquals(AlarmState.OK, state);
    }


}