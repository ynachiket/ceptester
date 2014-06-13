package ceptester;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

public class SimulateState {

	HashMap<String, Integer> counterValues = new HashMap<String, Integer>();
	
    public SimulateState() {
    	
		counterValues.put("OK", 0);
		counterValues.put("WARNING", 0);
		counterValues.put("CRITICAL", 0);
    }

	public String getState(int consecutiveCount, String consistencyLevel, int index,
			int[][] observationsMap) {

		String state = null;

		if (consecutiveCount > 1) {
			return getStateForPluralConsecutiveCount(consecutiveCount, consistencyLevel,
					index, observationsMap);
		}
       
		counterValues = getCounterValuesAtIndex(index, observationsMap);
		
		if (consistencyLevel.equalsIgnoreCase("All")) {
			
			state = getStateForAll(consecutiveCount, consistencyLevel, index, counterValues,
					observationsMap);

		} else if (consistencyLevel.equalsIgnoreCase("Quorum")) {
			state = getStateForQuorum(consecutiveCount, consistencyLevel, index, counterValues,
					observationsMap);

		}

		return state;

	}
	
	public StateChange getStateChange(int consecutiveCount, String consistencyLevel, int initialIndex,
			int finalIndex, int[][] observationsMap) {
		
		StateChange stateChangeInstance = new StateChange();
		
		String initialState = getState(consecutiveCount, consistencyLevel, initialIndex, observationsMap);
		String finalState = getState(consecutiveCount, consistencyLevel, finalIndex, observationsMap);
		
		stateChangeInstance.setInitialState(initialState);
		stateChangeInstance.setFinalState(finalState);
		
		return stateChangeInstance;	
		
	}
	
	private String getStateForPluralConsecutiveCount(int consecutiveCount,
			String consistencyLevel, int index, int[][] observationsMap) {
		
		int[] consecutiveObservations = new int[consecutiveCount];
		String state = null;
		
		for (int j = 0; j < observationsMap.length; j++) {
			for (int m = 0; m < consecutiveCount; m++) {
				consecutiveObservations[m] = observationsMap[j][index - m];				
			}
			if (isArrayConsistsSameValues(consecutiveObservations)) {
				populateCounterValues(index, j, observationsMap);	
			}
			if (consistencyLevel.equalsIgnoreCase("All")) {
				state = getStateForAll(consecutiveCount, consistencyLevel, index, counterValues,
						observationsMap);

			} else if (consistencyLevel.equalsIgnoreCase("Quorum")) {
				state = getStateForQuorum(consecutiveCount, consistencyLevel, index, counterValues,
						observationsMap);
			}
		}

		return state;
	}
	

	private String getStateForQuorum(int consecutiveCount, String method,
			int index, HashMap<String, Integer> counterValues, int[][] observationsMap) {

		int monitoringZones = observationsMap.length;
		int quorumCount = monitoringZones / 2 + 1;
		String state = null;

		for (Entry<String, Integer> entry : counterValues.entrySet()) {
			if (entry.getValue() >= quorumCount) {
				state = entry.getKey();
			}
		}

		return state;
	}


	private String getStateForAll(int consecutiveCount, String method,
			int index, HashMap<String, Integer> counterValues, int[][] observationsMap) {
		
		int monitoringZones = observationsMap.length;
		String state = null;

		for (Entry<String, Integer> entry : counterValues.entrySet()) {
			if (entry.getValue() == monitoringZones) {
				state = entry.getKey();
			}
		}

		return state;
	}
	
	private HashMap<String, Integer> getCounterValuesAtIndex(int index,
			int[][] observationsMap) {
		
		for (int j = 0; j < observationsMap.length; j++) {			
			populateCounterValues(index, j, observationsMap);
		}
		return counterValues;
	}
	

	private void populateCounterValues(int index, int j, int[][] observationsMap) {
		if (observationsMap[j][index] == 1) {
			counterValues.put("OK", counterValues.get("OK") + 1);
		} else if (observationsMap[j][index] == 2) {
			counterValues.put("WARNING", counterValues.get("WARNING") + 1);
		} else if (observationsMap[j][index] == 3) {
			counterValues.put("CRITICAL", counterValues.get("CRITICAL") + 1);
		}
	
	}

	private boolean isArrayConsistsSameValues(int[] consecutiveObservation) {
		boolean flag = true;
		int first = consecutiveObservation[0];
		for(int i = 1; i < consecutiveObservation.length && flag; i++)
		{
		  if (consecutiveObservation[i] != first) flag = false;
		}
		return flag;
	}

}
