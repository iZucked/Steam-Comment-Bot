/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.logging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.caches.MemoryUsageInfo;
import com.mmxlabs.optimiser.common.logging.ILoggingDataStore;
import com.mmxlabs.optimiser.common.logging.impl.EvaluationNumberKey;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IEvaluatedStateConstraintChecker;
import com.mmxlabs.optimiser.core.fitness.IFitnessEvaluator;
import com.mmxlabs.optimiser.core.impl.Sequences;
import com.mmxlabs.optimiser.core.moves.IMove;
import com.mmxlabs.optimiser.lso.INullMove;

public class LSOLogger implements ILoggingDataStore {

	public static class LoggingParameters {
		public static final Object ACCEPTED_FITNESS_KEY = "ACCEPTED_FITNESS";
		private static final Object REJECTED_FITNESS_KEY = "REJECTED_FITNESS";
		private static final Object HEAP_USAGE_KEY = "HEAP_USAGE";

		public int loggingInterval;
		public String[] metricsToLog;

		public boolean doLogAcceptedFitnesses() {
			return Arrays.asList(metricsToLog).contains(ACCEPTED_FITNESS_KEY);
		}

		public boolean doLogRejectedFitnesses() {
			return Arrays.asList(metricsToLog).contains(REJECTED_FITNESS_KEY);
		}

		public boolean doLogHeapUsage() {
			return Arrays.asList(metricsToLog).contains(HEAP_USAGE_KEY);
		}

	}

	private Map<String, AtomicInteger> moveMap = new HashMap<>();
	private Map<String, AtomicInteger> successfulMoveMap = new HashMap<>();
	private Map<String, Map<String, AtomicInteger>> nullMovesMap = new HashMap<>();
	private Map<String, AtomicInteger> failedToValidateMoveMap = new HashMap<>();
	private Map<String, Map<String, AtomicInteger>> failedConstraintsMovesMap = new HashMap<>();
	private Map<String, Map<String, AtomicInteger>> failedEvaluatedConstraintsMovesMap = new HashMap<>();
	private Map<EvaluationNumberKey, long[]> progressLogMap = new HashMap<>();
	private Map<Long, MemoryUsageInfo> heapUsageMap = new HashMap<>();

	private Map<String, Integer> progressKeys = new HashMap<>();
	private Map<ISequences, SequencesCounts> seenSequencesCount = new HashMap<>();
	private List<Pair<Integer, Long>> acceptedSolutionFitnesses = new LinkedList<>();
	private List<Pair<Integer, Long>> rejectedSolutionFitnesses = new LinkedList<>();


	private int numberOfMovesTried;
	private int numberOfMovesAccepted;
	private int numberOfRejectedMoves;
	private int numberOfFailedEvaluations;
	private int numberOfFailedToValidate;

	private IFitnessEvaluator lsafe;
	private FitnessAnnotationLogger fitnessAnnotationLogger;
	private GeneralAnnotationLogger generalAnnotationLogger;

	private LoggingParameters loggingParameters;

	public LSOLogger(LoggingParameters parameters, IFitnessEvaluator lsafe, IOptimisationContext context) {
		progressKeys = getProgressKeysMap();
		this.loggingParameters = parameters;
		this.fitnessAnnotationLogger = new FitnessAnnotationLogger(lsafe);
		this.generalAnnotationLogger = new GeneralAnnotationLogger(lsafe, context);
	}

	public Map<String, Integer> getProgressKeysMap() {
		Map<String, Integer> lProgressKeys = new HashMap<>();
		lProgressKeys.put("number-accepted-moves", 0);
		lProgressKeys.put("number-rejected-moves", 1);
		lProgressKeys.put("number-failed-evaluations", 2);
		lProgressKeys.put("number-failed-validate", 3);
		lProgressKeys.put("best-fitness", 4);
		lProgressKeys.put("current-fitness", 5);
		lProgressKeys.put("time", 6);
		return lProgressKeys;
	}

	public List<String> getProgressKeys() {
		return new ArrayList<>(getProgressKeysMap().keySet());
	}

	public boolean keyInProgressLog(String key) {
		return progressKeys.containsKey(key);
	}

	public void logCurrentJavaHeapUsage(long time) {
		Runtime runtime = Runtime.getRuntime();

		MemoryUsageInfo info = new MemoryUsageInfo(runtime.freeMemory(), runtime.totalMemory());

		heapUsageMap.put(time, info);
	}

	public void logNullMove(IMove move) {
		if (move instanceof INullMove) {
			INullMove nmove = (INullMove) move;
			String generator = nmove.getGenerator();
			String failure = nmove.getFailure();

			if (!nullMovesMap.containsKey(generator)) {
				Map<String, AtomicInteger> nullMap = new HashMap<>();
				nullMap.put(failure, new AtomicInteger(1));
				nullMovesMap.put(generator, nullMap);
			} else {
				AtomicInteger count = nullMovesMap.get(generator).get(failure);
				if (count == null) {
					nullMovesMap.get(generator).put(failure, new AtomicInteger(1));
				} else {
					count.incrementAndGet();
				}
			}

		} else {
			if (!nullMovesMap.containsKey("null")) {
				Map<String, AtomicInteger> map = new HashMap<>();
				map.put("null", new AtomicInteger(1));
				nullMovesMap.put("null", map);
			} else {
				nullMovesMap.get("null").get("null").incrementAndGet();
			}

		}
	}

	public void logAppliedMove(IMove move) {
		String moveName = move.getClass().getName();
		logAppliedMove(moveName);
	}

	public void logAppliedMove(String moveName) {
		logMoveEvent(moveName, moveMap);
	}

	public void logSuccessfulMove(IMove move, int numberOfMovesTried, long fitness) {
		String moveName = move.getClass().getName();
		logSuccessfulMove(moveName);
		if (loggingParameters.doLogAcceptedFitnesses()) {
			addFitnessIterationPoint(acceptedSolutionFitnesses, numberOfMovesTried, fitness);
		}
	}

	public void logRejectedMove(IMove move, int numberOfMovesTried, long fitness) {
		if (loggingParameters.doLogRejectedFitnesses()) {
			addFitnessIterationPoint(rejectedSolutionFitnesses, numberOfMovesTried, fitness);
		}
	}

	private void addFitnessIterationPoint(List<Pair<Integer, Long>> log, int iteration, long fitness) {
		log.add(new Pair<Integer, Long>(iteration, fitness));
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
			checkerStore = new HashMap<>();
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

	// REPLICATED FROM FailConstraints
	public void logFailedEvaluatedStateConstraints(IEvaluatedStateConstraintChecker checker, IMove move) {
		logFailedEvaluatedConstraints(checker.getName(), move.getClass().getName());
	}

	public void logFailedEvaluatedConstraints(String checkerName, String moveName) {
		Map<String, AtomicInteger> checkerStore;
		if (!failedEvaluatedConstraintsMovesMap.containsKey(checkerName)) {
			checkerStore = new HashMap<>();
			failedEvaluatedConstraintsMovesMap.put(checkerName, checkerStore);
		} else {
			checkerStore = failedEvaluatedConstraintsMovesMap.get(checkerName);
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
		fitnessAnnotationLogger.report(numberOfMovesTried);
		generalAnnotationLogger.report(numberOfMovesTried);
		logCurrentJavaHeapUsage(time);
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
		return loggingParameters.loggingInterval;
	}

	public Map<String, Long> getEndProgressResults() {
		List<String> attributes = new ArrayList<>(progressKeys.keySet());
		Collections.sort(attributes);
		Map<String, Long> finalResults = new HashMap<>();
		if (progressLogMap.size() != 0) {
			for (String key : attributes) {
				if (!key.equals("time")) {
					finalResults.put(key, progressLogMap.get(getProgressEvaluations().get(getProgressEvaluations().size() - 1))[progressKeys.get(key)]);
				} else {
					finalResults.put(key, getFinalTime());
				}
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
		return new ArrayList<>(moveMap.keySet());
	}

	public int getMoveCount(String key) {
		return moveMap.get(key).get();
	}

	public List<String> getNullMovesList() {
		return new ArrayList<>(nullMovesMap.keySet());
	}

	public int getNullMoveCount(String key) {
		Map<String, AtomicInteger> counts = nullMovesMap.get(key);
		int total = 0;
		for (Entry<String, AtomicInteger> count : counts.entrySet()) {
			total += count.getValue().get();
		}
		return total;
	}

	public List<String> getNullMoveSubKeys(String key) {
		return new ArrayList<>(nullMovesMap.get(key).keySet());
	}

	public int getSpecificNullMoveCount(String key, String subkey) {
		return nullMovesMap.get(key).get(subkey).get();
	}

	public List<String> getFailedToValidateMovesList() {
		return new ArrayList<>(failedToValidateMoveMap.keySet());
	}

	public int getFailedToValidateMoveCount(String key) {
		return failedToValidateMoveMap.get(key).get();
	}

	public List<String> getSuccessfulMovesList() {
		return new ArrayList<>(successfulMoveMap.keySet());
	}

	public int getSuccessfulMoveCount(String key) {
		return successfulMoveMap.get(key).get();
	}

	public List<String> getFailedConstraints() {
		return new ArrayList<>(failedConstraintsMovesMap.keySet());
	}

	// REPLICATED FROM getFailedConstraints
	public List<String> getFailedEvaluatedConstraints() {
		return new ArrayList<>(failedEvaluatedConstraintsMovesMap.keySet());
	}

	public int getFailedConstraintsMovesTotalCount(String constraint) {
		int count = 0;
		for (String move : getFailedConstraintsMoves(constraint)) {
			count += failedConstraintsMovesMap.get(constraint).get(move).get();
		}
		return count;
	}

	// REPLICATED FROM getFailedConstraintsMovesTotalCount
	public int getFailedEvaluatedConstraintsMovesTotalCount(String constraint) {
		int count = 0;
		for (String move : getFailedEvaluatedConstraintsMoves(constraint)) {
			count += failedEvaluatedConstraintsMovesMap.get(constraint).get(move).get();
		}
		return count;
	}

	public List<String> getFailedConstraintsMoves(String constraint) {
		return new ArrayList<>(failedConstraintsMovesMap.get(constraint).keySet());
	}

	// REPLICATED FROM getFailedConstraintMoves
	public List<String> getFailedEvaluatedConstraintsMoves(String constraint) {
		return new ArrayList<>(failedEvaluatedConstraintsMovesMap.get(constraint).keySet());
	}

	public int getFailedConstraintsMovesIndividualCount(String constraint, String move) {
		return failedConstraintsMovesMap.get(constraint).get(move).get();
	}

	// REPLICATED FROM getFailedConstraintsMovesIndividualCount
	public int getFailedEvaluatedConstraintsMovesIndividualCount(String constraint, String move) {
		return failedEvaluatedConstraintsMovesMap.get(constraint).get(move).get();
	}

	public void logSequence(@NonNull ISequences sequence) {
		SequencesCounts count = seenSequencesCount.get(sequence);
		if (count == null) {
			count = new SequencesCounts(0);
			count.total.set(1);
			seenSequencesCount.put(new Sequences(sequence), count);
		} else {
			count.total.incrementAndGet();
		}
	}

	public List<Integer> getSequenceFrequencyCounts() {
		return getFrequencyFromSequencesCounts(seenSequencesCount, SequenceCountType.TOTAL);
	}

	private List<Integer> getFrequencyFromSequencesCounts(Map<ISequences, SequencesCounts> counts, SequenceCountType type) {
		List<Integer> frequencies = new ArrayList<>();
		for (SequencesCounts s : counts.values()) {
			frequencies.add(s.getValue(type));
		}
		return frequencies;
	}

	public void logSequenceFailedConstraint(IConstraintChecker checker, @NonNull ISequences pinnedPotentialRawSequences) {
		SequencesCounts counts = seenSequencesCount.get(pinnedPotentialRawSequences);
		counts.constraints.incrementAndGet();
	}

	// REPLICATED FROM FAILEDCONSTRAINT
	public void logSequenceFailedEvaluatedStateConstraint(IEvaluatedStateConstraintChecker checker, @NonNull ISequences pinnedPotentialRawSequences) {
		SequencesCounts counts = seenSequencesCount.get(pinnedPotentialRawSequences);
		counts.constraints.incrementAndGet();
	}

	public List<Integer> getInterestingSequenceFrequencies(Map<ISequences, SequencesCounts> interestingSequences, SequenceCountType type) {
		List<Integer> frequencies = new ArrayList<>();
		for (SequencesCounts counts : interestingSequences.values()) {
			int count = counts.getValue(type);
			if (count > 0) {
				frequencies.add(count);
			}
		}
		return frequencies;
	}

	public List<Integer> getSequenceCountFailedConstraint() {
		return getInterestingSequenceFrequencies(seenSequencesCount, SequenceCountType.CONSTRAINTS);
	}

	public void logSequenceAccepted(ISequences pinnedPotentialRawSequences, long currentFitness) {
		SequencesCounts counts = seenSequencesCount.get(pinnedPotentialRawSequences);
		counts.accepted.incrementAndGet();
	}

	public List<Integer> getSequenceCountAccepted() {
		return getInterestingSequenceFrequencies(seenSequencesCount, SequenceCountType.ACCEPTED);
	}

	public void logSequenceRejected(ISequences pinnedPotentialRawSequences, long currentFitness) {
		SequencesCounts counts = seenSequencesCount.get(pinnedPotentialRawSequences);
		counts.rejected.incrementAndGet();
	}

	public List<Integer> getSequenceCountRejected() {
		return getInterestingSequenceFrequencies(seenSequencesCount, SequenceCountType.REJECTED);
	}

	public List<Pair<Integer, Long>> getAcceptedFitnesses() {
		return acceptedSolutionFitnesses;
	}

	public List<Pair<Integer, Long>> getRejectedFitnesses() {
		return rejectedSolutionFitnesses;
	}

	public int getNumberOfMovesTried() {
		return numberOfMovesTried;
	}

	public void setNumberOfMovesTried(int numberOfMovesTried) {
		this.numberOfMovesTried = numberOfMovesTried;
	}

	public int getNumberOfMovesAccepted() {
		return numberOfMovesAccepted;
	}

	public void setNumberOfMovesAccepted(int numberOfMovesAccepted) {
		this.numberOfMovesAccepted = numberOfMovesAccepted;
	}

	public int getNumberOfRejectedMoves() {
		return numberOfRejectedMoves;
	}

	public void setNumberOfRejectedMoves(int numberOfRejectedMoves) {
		this.numberOfRejectedMoves = numberOfRejectedMoves;
	}

	public int getNumberOfFailedEvaluations() {
		return numberOfFailedEvaluations;
	}

	public void setNumberOfFailedEvaluations(int numberOfFailedEvaluations) {
		this.numberOfFailedEvaluations = numberOfFailedEvaluations;
	}

	public int getNumberOfFailedToValidate() {
		return numberOfFailedToValidate;
	}

	public void setNumberOfFailedToValidate(int numberOfFailedToValidate) {
		this.numberOfFailedToValidate = numberOfFailedToValidate;
	}

	public FitnessAnnotationLogger getFitnessAnnotationLogger() {
		return fitnessAnnotationLogger;
	}

	public GeneralAnnotationLogger getGeneralAnnotationLogger() {
		return generalAnnotationLogger;
	}

	public Map<Long, MemoryUsageInfo> getHeapUsageMap() {
		return heapUsageMap;
	}

	private class SequencesCounts {
		public AtomicInteger total;
		public AtomicInteger accepted;
		public AtomicInteger rejected;
		public AtomicInteger constraints;

		public SequencesCounts() {
			this(0);
		}

		public SequencesCounts(int initVal) {
			total = new AtomicInteger(initVal);
			accepted = new AtomicInteger(initVal);
			rejected = new AtomicInteger(initVal);
			constraints = new AtomicInteger(initVal);
		}

		public Integer getValue(SequenceCountType type) {
			switch (type) {
			case TOTAL:
				return total.get();
			case ACCEPTED:
				return accepted.get();
			case REJECTED:
				return rejected.get();
			case CONSTRAINTS:
				return constraints.get();
			default:
				return null;
			}

		}
	}

	private enum SequenceCountType {
		TOTAL, ACCEPTED, REJECTED, CONSTRAINTS
	}

	public LoggingParameters getLoggingParameters() {
		return loggingParameters;
	}

}
