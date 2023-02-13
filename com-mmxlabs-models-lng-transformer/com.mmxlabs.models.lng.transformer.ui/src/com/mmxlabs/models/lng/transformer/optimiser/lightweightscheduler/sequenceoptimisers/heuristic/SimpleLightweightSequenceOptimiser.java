/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
///**
// * Copyright (C) Minimax Labs Ltd., 2010 - 2018
// * All rights reserved.
// */
//package com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.sequenceoptimisers.heuristic;
//
//import java.util.ArrayList;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Random;
//import java.util.Set;
//import java.util.stream.Collectors;
//import java.util.stream.IntStream;
//
//import org.eclipse.core.runtime.IProgressMonitor;
//
//import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ILightWeightConstraintChecker;
//import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ILightWeightFitnessFunction;
//import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ILightWeightOptimisationData;
//import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ILightWeightSequenceOptimiser;
//import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.impl.LightWeightCargoDetails;
//import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.impl.LightWeightSchedulerMoves;
//import com.mmxlabs.optimiser.common.components.ITimeWindow;
//import com.mmxlabs.optimiser.lso.impl.thresholders.GeometricThresholder;
//import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
//import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
//
//public class SimpleLightweightSequenceOptimiser implements ILightWeightSequenceOptimiser {
//
//	private static final long COST_WEIGHT = 1;
//	private static final long PNL_WEIGHT = -1;
//	private static final int LATENESS_WEIGHT = 1_000_000;
//
//	@Override
//	public
//	List<List<Integer>> optimise(ILightWeightOptimisationData lightWeightOptimisationData,
//			List<ILightWeightConstraintChecker> constraintCheckers,
//			List<ILightWeightFitnessFunction> fitnessFunctions, IProgressMonitor monitor) {
//		
//		// Data Transform
//	     List<List<IPortSlot>> cargoes = lightWeightOptimisationData.getCargoes();
//	     List<IVesselCharter> vessels = lightWeightOptimisationData.getVessels();
//	     long[] cargoPNL = lightWeightOptimisationData.getCargoPNL();
//	     Long[][][] cargoToCargoCostsOnAvailability = lightWeightOptimisationData.getCargoToCargoCostsOnAvailability();
//	     List<Set<Integer>> cargoVesselRestrictions = lightWeightOptimisationData.getCargoVesselRestrictions();
//	     int[][][] cargoToCargoMinTravelTimes = lightWeightOptimisationData.getCargoToCargoMinTravelTimes();
//	     int[][] cargoMinTravelTimes = lightWeightOptimisationData.getCargoMinTravelTimes();
//	     long[] volumes = lightWeightOptimisationData.getCargoesVolumesInM3();
//	     LightWeightCargoDetails[] cargoDetails = lightWeightOptimisationData.getCargoDetails();
//		
//		
//		List<List<Integer>> sequences = createSequences(vessels);
//		sequences.get(0).add(0);
//		sequences.get(0).add(1);
//		LightWeightSchedulerMoves moves = new LightWeightSchedulerMoves();
//		GeometricThresholder geometricThresholder = new GeometricThresholder(new Random(0), 10_000, 45_000, 0.96);
//		Long currentFitness = evaluate(sequences, cargoes, vessels, cargoPNL, cargoToCargoCostsOnAvailability, cargoVesselRestrictions, cargoToCargoMinTravelTimes, cargoMinTravelTimes);
//		List<Integer> cargoesList = IntStream.range(0, cargoes.size()).boxed().collect(Collectors.toCollection(ArrayList::new));
//		Random r = new Random(0);
//		for (int i = 0; i < 5_000_000; i++) {
//			List<List<Integer>> newSequences = moves.move(sequences, cargoesList);
//			if (r.nextBoolean()) {
//				newSequences = moves.move(newSequences, cargoesList);
//			}
//			if (r.nextBoolean()) {
//				newSequences = moves.move(newSequences, cargoesList);
//			}
//			if (r.nextBoolean()) {
//				newSequences = moves.move(newSequences, cargoesList);
//			}
//
//			Long newFitness = evaluate(newSequences, cargoes, vessels, cargoPNL, cargoToCargoCostsOnAvailability, cargoVesselRestrictions, cargoToCargoMinTravelTimes, cargoMinTravelTimes);
//			if (newFitness == null) {
//				continue;
//			}
//			if (newFitness < currentFitness) {
//				currentFitness = newFitness;
//				sequences = newSequences;
//				System.out.println(String.format("%s new best:%s",i,newFitness));
//			}
//		}
//		return sequences;
//	}
//	
//	private Long evaluate(List<List<Integer>> sequences, List<List<IPortSlot>> cargoes, List<IVesselCharter> vessels, long[] cargoPNL, Long[][][] cargoToCargoCostsOnAvailability, List<Set<Integer>> cargoVesselRestrictions, int[][][] cargoToCargoMinTravelTimes, int[][] cargoMinTravelTimes) {
//		long totalCost = 0;
//		long totalPNL = 0;
//		long totalLateness = 0;
//		Long[] capacities = vessels.stream().map(v->v.getVessel().getCargoCapacity()).collect(Collectors.toList()).toArray(new Long[vessels.size()]);
//		for (int availability = 0; availability < sequences.size(); availability++) {
//			List<Integer> sequence = sequences.get(availability);
//			// constraints
//			boolean checkRestrictions = checkRestrictions(sequences.get(availability), availability, cargoVesselRestrictions);
//			if (!checkRestrictions) {
//				return null;
//			}
//			// calculate costs
//			long cost = calculateCostOnSequence(sequence, availability, cargoToCargoCostsOnAvailability);
//			totalCost += cost;
//			// calculate lateness
//			long lateness = calculateLatenessOnSequence(sequence, availability, cargoes, cargoToCargoMinTravelTimes, cargoMinTravelTimes);
//			totalLateness += lateness;
//			// calculate pnl
//			long profit = calculateProfitOnSequence(sequence, capacities[availability], cargoPNL);
//			totalPNL += profit;
//		}
//		
//		return COST_WEIGHT * (totalCost/1_000_000)
//				+ PNL_WEIGHT * (totalPNL/1_000)
//				+ LATENESS_WEIGHT*totalLateness;
//	}
//	
//	private boolean  checkRestrictions(List<Integer> sequence, int vessel, List<Set<Integer>> cargoVesselRestrictions) {
//		for (int c : sequence) {
//			if (cargoVesselRestrictions.get(c).contains(vessel)) {
//				return false;
//			}
//		}
//		return true;
//	}
//
//	private List<List<Integer>> createSequences(List<IVesselCharter> vessels) {
//		return vessels
//				.stream()
//				.map(v -> (List<Integer>) new LinkedList<Integer>())
//				.collect(Collectors.toList());
//	}
//
//	private int calculateLatenessOnSequence(List<Integer> sequence, int availability, List<List<IPortSlot>> cargoes, int[][][] cargoToCargoMinTravelTimes, int[][] cargoMinTravelTimes) {
//		int current = 0;
//		int lateness = 0;
//		for (int i = 0; i < sequence.size(); i++) {
//			int currIdx = sequence.get(i);
//			List<IPortSlot> currentCargo = cargoes.get(currIdx);
//			int earliestCargoStart = Math.max(current, getFirstTime(currentCargo).getInclusiveStart());
//			lateness += (Math.min(0, getFirstTime(currentCargo).getExclusiveEnd() - 1 - earliestCargoStart) * -1);
//			int earliestCargoEnd = Math.max(earliestCargoStart + cargoMinTravelTimes[currIdx][availability],
//					getLastTime(currentCargo).getInclusiveStart());
//			lateness += (Math.min(0, getLastTime(currentCargo).getExclusiveEnd() - 1 - earliestCargoEnd) * -1);
//			if (i < sequence.size() - 1) {
//				current = earliestCargoEnd + cargoToCargoMinTravelTimes[currIdx][sequence.get(i+1)][availability];
//			}
//		}
//		return lateness;
//	}
//	
//	private long calculateProfitOnSequence(List<Integer> sequence, Long capacity, long[] cargoPNL) {
//		return sequence
//				.stream()
//				.mapToLong(s -> cargoPNL[s]*capacity)
//				.sum();
//	}
//	
//	private long calculateCostOnSequence(List<Integer> sequence, int availability, Long[][][] cargoToCargoCostsOnAvailability) {
//		long total = 0;
//		for (int currIdx = 0; currIdx < sequence.size() - 1; currIdx++) {
//			total += cargoToCargoCostsOnAvailability[currIdx][currIdx + 1][availability];
//		}
//		return total;
//	}
//
//	private ITimeWindow getFirstTime(List<IPortSlot> cargo) {
//		return cargo.get(0).getTimeWindow();
//	}
//
//	private ITimeWindow getLastTime(List<IPortSlot> cargo) {
//		return cargo.get(cargo.size() - 1).getTimeWindow();
//	}
//}
