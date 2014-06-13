package com.cloudkick.util;

import telescope.thrift.AlarmState;

import javax.xml.transform.OutputKeys;
import java.util.HashMap;
import java.util.Map.Entry;

public class SimulateState {

    HashMap<String, Integer> counterValues = new HashMap<String, Integer>();

    public SimulateState() {
    }
    // 1 OK , 2 Warn, 6 Critical

    /*
      * int observationsMap [][]= {{1, 1, 2, 2, 2}, {1, 1, 1, 1, 1}, {1, 1, 1, 2,
      * 2}, {1, 1, 1, 2, 2}};
      */
    public static AlarmState getState(int consecutiveCount, String consistencyLevel, int mz, int timePeriod,
                           int[][] observationsMap) {

        if (consistencyLevel.equalsIgnoreCase("One")) {
            return  getStateForOne(getLatestObservationForOne(consecutiveCount, observationsMap, mz, timePeriod));
        } else if (consistencyLevel.equalsIgnoreCase("All")) {
            return getStateForAll(getLatestObservations(consecutiveCount, observationsMap, mz, timePeriod));

        } else if (consistencyLevel.equalsIgnoreCase("Quorum")) {
            return getStateForQuorum(getLatestObservations(consecutiveCount, observationsMap, mz, timePeriod));
        }

        return null;

    }


    private static AlarmState getStateForQuorum(int[] latestObservations) {
        int monitoringZones = latestObservations.length;
        int quorumCount = monitoringZones / 2 + 1;
        int ok = 0;
        int crit = 0;
        int warn = 0;

        for (int i = 0; i < latestObservations.length; i++) {
            switch (latestObservations[i]) {
                case 1:
                    ok++;
                    break;
                case 2:
                    warn++;
                    break;
                case 6:
                    crit++;
                    break;
            }

        }

        if (crit >= quorumCount) {
            return AlarmState.CRITICAL;
        } else if (warn + crit >= quorumCount) {
            return AlarmState.WARNING;
        } else if (ok >= quorumCount) {
            return AlarmState.OK;
        }

        return null;
    }



    private static AlarmState getStateForAll(int[] latestObservations) {

        int monitoringZones = latestObservations.length;
        int ok = 0;
        int crit = 0;
        int warn = 0;

        for (int i = 0; i < latestObservations.length; i++) {
            switch (latestObservations[i]) {
                case 1:
                    ok++;
                    break;
                case 2:
                    warn++;
                    break;
                case 6:
                    crit++;
                    break;
            }

        }

        if (crit == monitoringZones) {
            return AlarmState.CRITICAL;
        } else if (warn == monitoringZones) {
            return AlarmState.WARNING;
        } else if (ok == monitoringZones) {
            return AlarmState.OK;
        }

        return null;
    }


    private static AlarmState getStateForOne(int latestObservation) {
        switch (latestObservation) {
            case 1:
                return AlarmState.OK;
            case 2:
                return AlarmState.WARNING;
            case 6:
                return AlarmState.CRITICAL;
            default:
                return null;
        }
    }


    public static int getLatestObservationForOne(int consecutiveCount, int[][] observations, int mz, int timePeriod) {
        if (consecutiveCount == 1) {
            return observations[mz][timePeriod];
        } else {
            int state = 0;
            int countSoFar = 0;
            for (int i = 0; i < consecutiveCount; i++) {
                if (timePeriod - i < 0) {
                    break;
                }
                if (observations[mz][timePeriod - i] == state) {
                    countSoFar++;
                } else {
                    state = observations[mz][timePeriod - i];
                    countSoFar = 1;
                }
            }

            if (countSoFar == consecutiveCount) {
                return state;
            } else {
                return 0;
            }
        }
    }

    public static int[] getLatestObservations(int consecutiveCount, int[][] observations, int mz, int timePeriod) {
        int[] latestObservations = new int[observations.length];
        int _mz = mz;
        int _time = timePeriod;

        if (consecutiveCount == 1) {
            for (int i = 0; i < observations.length; i++) {
                if (_mz == 0 && _time == 0) {
                    latestObservations[i] = observations[_mz][_time];
                    break;
                } else if (_mz == 0 && _time != 0) {
                    latestObservations[i] = observations[_mz][_time];
                    _mz = observations.length - 1;
                    _time -= 1;
                } else {
                    latestObservations[i] = observations[_mz][_time];
                    _mz -= 1;
                }
            }
            return latestObservations;
        } else {
            int[][] latestConsecutiveObservations = new int[observations.length][2];
            for (int i = 0; i < observations.length; i++) {
                for (int j = _time; j >= 0; j--) {
                    if (observations[_mz][j] == latestConsecutiveObservations[i][0]) {
                        latestConsecutiveObservations[i][1]++;
                    } else {
                        latestConsecutiveObservations[i][0] = observations[_mz][j];
                        latestConsecutiveObservations[i][1] = 1;
                    }

                    if (latestConsecutiveObservations[i][1] == consecutiveCount) {
                        break;
                    }
                }

                if (_mz == 0 && _time == 0) {
                    break;
                } else if (_mz == 0 && _time != 0) {
                    _mz = observations.length - 1;
                    _time -= 1;
                } else {
                    _mz -= 1;
                }
            }

            for (int i = 0; i < latestConsecutiveObservations.length; i++) {
                if (latestConsecutiveObservations[i][1] == consecutiveCount) {
                    latestObservations[i] = latestConsecutiveObservations[i][0];
                } else {
                    latestObservations[i] = 0;
                }
            }
            return latestObservations;
        }
    }

}
