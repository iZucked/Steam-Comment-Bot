package com.mmxlabs.optimiser.lso.logging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import com.mmxlabs.optimiser.common.logging.ILoggingDataStore;
import com.mmxlabs.optimiser.common.logging.impl.EvaluationNumberKey;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.lso.IMove;
import com.mmxlabs.optimiser.lso.INullMove;

public class LSOLogger implements ILoggingDataStore {
	private Map<String, AtomicInteger> moveMap = new HashMap<>();
	private Map<String, AtomicInteger> successfulMoveMap = new HashMap<>();
	private Map<String, AtomicInteger> nullMovesMap = new HashMap<>();
	private Map<String, AtomicInteger> failedToValidateMoveMap = new HashMap<>();
	private Map<String, Map<String, AtomicInteger>> failedConstraintsMovesMap = new HashMap<>();
	private Map<EvaluationNumberKey, long[]> progressLogMap = new HashMap<>();
	private Map<String, Integer> progressKeys = new HashMap<>();
	private int reportingInterval;

	public LSOLogger(int reportingInterval) {
		nullMovesMap.put("null", new AtomicInteger(0));
		progressKeys = getProgressKeysMap();
		this.reportingInterval = reportingInterval;
	}

	public Map<String, Integer> getProgressKeysMap() {
		Map<String, Integer> progressKeys = new HashMap<>();
		progressKeys.put("number-accepted-moves", 0);
		progressKeys.put("number-rejected-moves", 1);
		progressKeys.put("number-failed-evaluations", 2);
		progressKeys.put("number-failed-validate", 3);
		progressKeys.put("best-fitness", 4);
		progressKeys.put("current-fitness", 5);
		progressKeys.put("time", 6);
		return progressKeys;
	}

	public List<String> getProgressKeys() {
		return new ArrayList<String>(getProgressKeysMap().keySet());
	}

	public boolean keyInProgressLog(String key) {
		return progressKeys.containsKey(key);
	}

	public void logNullMove(IMove move) {
		if (move instanceof INullMove) {
			AtomicInteger count = nullMovesMap.get(move.getClass().getName());
			if (count == null) {
				nullMovesMap.put(move.getClass().getName(), new AtomicInteger(1));
			} else {
				count.incrementAndGet();
			}
		} else {
			nullMovesMap.get("null").incrementAndGet();
		}
	}

	public void logAppliedMove(IMove move) {
		String moveName = move.getClass().getName();
		logAppliedMove(moveName);
	}

	public void logAppliedMove(String moveName) {
		logMoveEvent(moveName, moveMap);
	}

	public void logSuccessfulMove(IMove move) {
		String moveName = move.getClass().getName();
		logSuccessfulMove(moveName);
	}
	
	public void logSuccessfulMove(String moveName) {
		logMoveEvent(moveName, successfulMoveMap);
	}
	
	public void logFailedToValidateMove(IMove move) {
		String moveName = move.getClass().getName();
		logFailedToValidateMove(moveName);
	}

	public void logFailedToValidateMove(String moveName) {
		logMoveEvent(moveName, failedToValidateMoveMap);
	}

	private void logMoveEvent(String moveName, Map<String, AtomicInteger> eventMap) {
		AtomicInteger count = eventMap.get(moveName);
		if (count == null) {
			eventMap.put(moveName, new AtomicInteger(1));
		} else {
			count.incrementAndGet();
		}
	}

	public void logFailedConstraints(IConstraintChecker checker, IMove move) {
		logFailedConstraints(checker.getName(), move.getClass().getName());
	}

	public void logFailedConstraints(String checkerName, String moveName) {
		Map<String, AtomicInteger> checkerStore;
		if (!failedConstraintsMovesMap.containsKey(checkerName)) {
			checkerStore = new HashMap<String, AtomicInteger>();
			failedConstraintsMovesMap.put(checkerName, checkerStore);
		} else {
			checkerStore = failedConstraintsMovesMap.get(checkerName);
		}
		AtomicInteger count = checkerStore.get(moveName);
		if (count == null) {
			checkerStore.put(moveName, new AtomicInteger(1));
		} else {
			count.incrementAndGet();
		}
	}

	public void intialiseProgressLog(long bestFitness, long currentFitness) {
		logProgress(0, 0, 0, 0, 0, bestFitness, currentFitness, new Date().getTime());
	}

	public void logProgress(int numberOfMovesTried, int numberOfMovesAccepted, int numberOfMovesRejected, int numberOfFailedEvaluations, int numberOfFailedToValidate, long bestFitness,
			long currentFitness, long time) {
		long[] progress = new long[7];
		progress[0] = numberOfMovesAccepted;
		progress[1] = numberOfMovesRejected;
		progress[2] = numberOfFailedEvaluations;
		progress[3] = numberOfFailedToValidate;
		progress[4] = bestFitness;
		progress[5] = currentFitness;
		progress[6] = time;
		progressLogMap.put(new EvaluationNumberKey(numberOfMovesTried), progress);
	}

	public List<EvaluationNumberKey> getProgressEvaluations() {
		List<EvaluationNumberKey> list = new ArrayList<>(progressLogMap.keySet());
		Collections.sort(list);
		return list;
	}

	public long getProgressValue(EvaluationNumberKey evaluationNumber, String key) {
		long value = -1;
		if (keyInProgressLog(key)) {
			long[] values = progressLogMap.get(evaluationNumber);
			if (values != null) {
				return values[progressKeys.get(key)];
			}
		}
		return value;
	}

	public int getReportingInterval() {
		return reportingInterval;
	}

	public Map<String, Long> getEndProgressResults() {
		List<String> attributes = new ArrayList<String>(progressKeys.keySet());
		Collections.sort(attributes);
		Map<String, Long> finalResults = new HashMap<String, Long>();
		for (String key : attributes) {
			if (key != "time") {
				finalResults.put(key, progressLogMap.get(getProgressEvaluations().get(getProgressEvaluations().size() - 1))[progressKeys.get(key)]);
			} else {
				finalResults.put(key, getFinalTime());
			}
		}
		return finalResults;
	}

	private Long getFinalTime() {
		long end = progressLogMap.get(getProgressEvaluations().get(getProgressEvaluations().size() - 1))[progressKeys.get("time")];
		long start = progressLogMap.get(getProgressEvaluations().get(0))[progressKeys.get("time")];
		return end - start;
	}

	public List<String> getMovesList() {
		return new ArrayList<String>(moveMap.keySet());
	}

	public int getMoveCount(String key) {
		return moveMap.get(key).get();
	}

	public List<String> getNullMovesList() {
		return new ArrayList<String>(nullMovesMap.keySet());
	}

	public int getNullMoveCount(String key) {
		return nullMovesMap.get(key).get();
	}

	public List<String> getFailedToValidateMovesList() {
		return new ArrayList<String>(failedToValidateMoveMap.keySet());
	}

	public int getFailedToValidateMoveCount(String key) {
		return failedToValidateMoveMap.get(key).get();
	}

	public List<String> getSuccessfulMovesList() {
		return new ArrayList<String>(successfulMoveMap.keySet());
	}
	
	public int getSuccessfulMoveCount(String key) {
		return successfulMoveMap.get(key).get();
	}
	
	public List<String> getFailedConstraints() {
		return new ArrayList<String>(failedConstraintsMovesMap.keySet());
	}

	public int getFailedConstraintsMovesTotalCount(String constraint) {
		int count = 0;
		for (String move : getFailedConstraintsMoves(constraint)) {
			count += failedConstraintsMovesMap.get(constraint).get(move).get();
		}
		return count;
	}

	public List<String> getFailedConstraintsMoves(String constraint) {
		return new ArrayList<String>(failedConstraintsMovesMap.get(constraint).keySet());
	}

	public int getFailedConstraintsMovesIndividualCount(String constraint, String move) {
		return failedConstraintsMovesMap.get(constraint).get(move).get();
	}

}
