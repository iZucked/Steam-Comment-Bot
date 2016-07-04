/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.logging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.common.logging.ILoggingDataStore;
import com.mmxlabs.optimiser.common.logging.impl.EvaluationNumberKey;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.fitness.IFitnessEvaluator;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
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
	private Map<ISequences, SequencesCounts> seenSequencesCount = new HashMap<>();
	private Map<ISequences, String> constraintsFailedSequences = new HashMap<>();
	private Map<ISequences, Long> acceptedMovesSequencesFitness = new HashMap<>();
	private Map<ISequences, Long> rejectedMovesSequencesFitness = new HashMap<>();
	private Map<ISequences, SequencesCounts> acceptedSequencesCount = new HashMap<>();
	private Map<ISequences, SequencesCounts> rejectedSequencesCount = new HashMap<>();
	private List<Pair<Integer, Long>> acceptedSolutionFitnesses = new LinkedList<Pair<Integer, Long>>();
	private List<Pair<Integer, Long>> rejectedSolutionFitnesses = new LinkedList<Pair<Integer, Long>>();

	private List<IResource> resourceList = null;

	private int reportingInterval;
	private int numberOfMovesTried;
	private int numberOfMovesAccepted;
	private int numberOfRejectedMoves;
	private int numberOfFailedEvaluations;
	private int numberOfFailedToValidate;

	private IFitnessEvaluator lsafe;
	private FitnessAnnotationLogger fitnessAnnotationLogger;
	private GeneralAnnotationLogger generalAnnotationLogger;

	public LSOLogger(int reportingInterval, IFitnessEvaluator lsafe, IOptimisationContext context) {
		nullMovesMap.put("null", new AtomicInteger(0));
		progressKeys = getProgressKeysMap();
		this.reportingInterval = reportingInterval;
		this.fitnessAnnotationLogger = new FitnessAnnotationLogger(lsafe);
		this.generalAnnotationLogger = new GeneralAnnotationLogger(lsafe, context);
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

	public void logSuccessfulMove(IMove move, int numberOfMovesTried, long fitness) {
		String moveName = move.getClass().getName();
		logSuccessfulMove(moveName);
		addFitnessIterationPoint(acceptedSolutionFitnesses, numberOfMovesTried, fitness);
	}

	public void logRejectedMove(IMove move, int numberOfMovesTried, long fitness) {
		addFitnessIterationPoint(rejectedSolutionFitnesses, numberOfMovesTried, fitness);
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
		fitnessAnnotationLogger.report(numberOfMovesTried);
		generalAnnotationLogger.report(numberOfMovesTried);
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
		if (progressLogMap.size() != 0) {
			for (String key : attributes) {
				if (key != "time") {
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

	public void logSequence(IModifiableSequences sequence) {
		SequencesCounts count = seenSequencesCount.get(sequence);
		if (count == null) {
			count = new SequencesCounts(0);
			count.total.set(1);
			seenSequencesCount.put(new ModifiableSequences(sequence), count);
		} else {
			count.total.incrementAndGet();
		}
	}

	public List<Integer> getSequenceFrequencyCounts() {
		return getFrequencyFromSequencesCounts(seenSequencesCount, SequenceCountType.TOTAL);
	}

	private List<Integer> getFrequencyFromSequencesCounts(Map<ISequences, SequencesCounts> counts, SequenceCountType type) {
		List<Integer> frequencies = new ArrayList<Integer>();
		for (SequencesCounts s : counts.values()) {
			frequencies.add(s.getValue(type));
		}
		return frequencies;
	}

	public void logSequenceFailedConstraint(IConstraintChecker checker, ModifiableSequences pinnedPotentialRawSequences) {
		SequencesCounts counts = seenSequencesCount.get(pinnedPotentialRawSequences);
		counts.constraints.incrementAndGet();
	}

	public List<Integer> getInterestingSequenceFrequencies(Map<ISequences, SequencesCounts> interestingSequences, SequenceCountType type) {
		List<Integer> frequencies = new ArrayList<Integer>();
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

	public void logSequenceAccepted(ModifiableSequences pinnedPotentialRawSequences, long currentFitness) {
		SequencesCounts counts = seenSequencesCount.get(pinnedPotentialRawSequences);
		counts.accepted.incrementAndGet();
	}

	public List<Integer> getSequenceCountAccepted() {
		return getInterestingSequenceFrequencies(seenSequencesCount, SequenceCountType.ACCEPTED);
	}

	public void logSequenceRejected(ModifiableSequences pinnedPotentialRawSequences, long currentFitness) {
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

}
