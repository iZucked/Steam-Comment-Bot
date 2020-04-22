/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.optimiser.pairing;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.ConstraintInfo;

/**
 * Optimise cargo allocations with Google OR.
 * <p>
 */
public class GoogleORToolsPairingMatrixOptimiser<P,C> implements IPairingMatrixOptimiser<P,C> {

	private static final long NON_OPTIONAL_PENALTY = 36_000_000_000L;
	private static final double SYMMETRY_CONSTANT = 0.1;

	public boolean[][] optimise(SerializablePairingData pairingData) {
		long[][] pnl = pairingData.getPnl();
		boolean[][] validCargoes = pairingData.getValidCargoes();
		boolean[] optionalLoads = pairingData.getOptionalLoads();
		boolean[] optionalDischarges = pairingData.getOptionalDischarges();

		// Init
		int no_loads = pnl.length;
		int no_discharges = pnl[0].length;

		List<Map<String, List<Integer>>> maxDischargeSlotsInformation = pairingData.getMaxDischargeSlots();
		List<Map<String, List<Integer>>> minDischargeSlotsInformation = pairingData.getMinDischargeSlots();

		List<Map<String, List<Integer>>> maxLoadSlotsInformation = pairingData.getMaxLoadSlots();
		List<Map<String, List<Integer>>> minLoadSlotsInformation = pairingData.getMinLoadSlots();

		System.out.println("# loads: " + no_loads);
		System.out.println("# discharges: " + no_discharges);
		// Set up non optional penalties - default 0
		long[][] nonOptionalPenalty = new long[no_loads][no_discharges];

		// Modify loads with a non-optional penalty
		for (int load_id = 0; load_id < no_loads; load_id++) {
			if (!optionalLoads[load_id]) {
				for (int discharge_id = 0; discharge_id < no_discharges; discharge_id++) {
					nonOptionalPenalty[load_id][discharge_id] += NON_OPTIONAL_PENALTY;
				}
			}
		}

		// Modify discharges with a non-optional penalty
		for (int dischargeId = 0; dischargeId < no_discharges; dischargeId++) {
			if (!optionalDischarges[dischargeId]) {
				for (int loadId = 0; loadId < no_loads; loadId++) {
					nonOptionalPenalty[loadId][dischargeId] += NON_OPTIONAL_PENALTY;
				}
			}
		}

		// Set up symmettry reward - default 0
		double[][] symmetryPenalty = new double[no_loads][no_discharges];

		// Add symmettry reward
		for (int load_id = 0; load_id < no_loads; load_id++) {
			for (int discharge_id = 0; discharge_id < no_discharges; discharge_id++) {
				symmetryPenalty[load_id][discharge_id] = (((double) load_id) + .1) * (((double) discharge_id) + .1) * SYMMETRY_CONSTANT;
			}
		}

		/** google initialization */
		MPSolver solver = createSolver("CBC_MIXED_INTEGER_PROGRAMMING");

		/** variables */
		MPVariable[][] vars = new MPVariable[no_loads][no_discharges];
		for (int i = 0; i < no_loads; i++) {
			vars[i] = solver.makeBoolVarArray(no_discharges);
		}

		/** constraints */
		MPConstraint loadConstraints[] = new MPConstraint[no_loads];
		MPConstraint dischargeConstraints[] = new MPConstraint[no_discharges];

		for (int i = 0; i < no_loads; i++) {
			loadConstraints[i] = solver.makeConstraint(0, 1);
			for (int j = 0; j < no_discharges; j++) {
				loadConstraints[i].setCoefficient(vars[i][j], 1);

				if (!validCargoes[i][j]) {
					vars[i][j].setUb(0);
				}
			}
		}

		for (int i = 0; i < no_discharges; i++) {
			dischargeConstraints[i] = solver.makeConstraint(0, 1);
			for (int j = 0; j < no_loads; j++) {
				dischargeConstraints[i].setCoefficient(vars[j][i], 1);
			}
		}

		// max slots constraints

		for (Map<String, List<Integer>> info : maxLoadSlotsInformation) {
			List<Integer> slots = info.get("loads");
			List<Integer> count = info.get("count");
			MPConstraint msConstraint = solver.makeConstraint(0, count.get(0));
			for (int i : slots) {
				for (int j = 0; j < no_discharges; j++) {
					msConstraint.setCoefficient(vars[i][j], 1);
				}
			}
		}

		// min slots constraints

		for (Map<String, List<Integer>> info : minLoadSlotsInformation) {
			List<Integer> slots = info.get("loads");
			List<Integer> count = info.get("count");
			MPConstraint msConstraint = solver.makeConstraint(count.get(0), MPSolver.infinity());
			for (int i : slots) {
				for (int j = 0; j < no_discharges; j++) {
					msConstraint.setCoefficient(vars[i][j], 1);
				}
			}
		}

		// max slots constraints

		for (Map<String, List<Integer>> info : maxDischargeSlotsInformation) {
			List<Integer> slots = info.get("discharges");
			List<Integer> count = info.get("count");
			MPConstraint msConstraint = solver.makeConstraint(0, count.get(0));
			for (int i : slots) {
				for (int j = 0; j < no_loads; j++) {
					msConstraint.setCoefficient(vars[j][i], 1);
				}
			}
		}

		// min slots constraints

		for (Map<String, List<Integer>> info : minDischargeSlotsInformation) {
			List<Integer> slots = info.get("discharges");
			List<Integer> count = info.get("count");
			MPConstraint msConstraint = solver.makeConstraint(count.get(0), MPSolver.infinity());
			for (int i : slots) {
				for (int j = 0; j < no_loads; j++) {
					msConstraint.setCoefficient(vars[j][i], 1);
				}
			}
		}

		/** objective */
		MPObjective obj = solver.objective();
		obj.setMaximization();

		for (int i = 0; i < no_loads; i++) {
			for (int j = 0; j < no_discharges; j++) {
				obj.setCoefficient(vars[i][j], pnl[i][j] + nonOptionalPenalty[i][j] + symmetryPenalty[i][j]);
			}
		}

		final MPSolver.ResultStatus resultStatus = solver.solve();
		System.out.println("solver v:" + obj.value());

		if (resultStatus != MPSolver.ResultStatus.OPTIMAL) {
			System.err.println("The problem does not have an optimal solution");
			// System.exit(1);
			return null;
		}

		/** print */
		System.out.println("Problem solved in: ");
		System.out.println("\t" + solver.iterations() + " iterations");
		System.out.println("\t" + solver.wallTime() + " milliseconds");

		int cargoes = 0;
		double ds[][] = new double[no_loads][no_discharges];
		for (int i = 0; i < no_loads; i++) {
			for (int j = 0; j < no_discharges; j++) {
				ds[i][j] = vars[i][j].solutionValue();
				if (ds[i][j] == 1) {
					cargoes++;
					System.out.println(String.format("%s -> %s", i, j));
				}
			}
		}

		System.out.println("#cargoes:" + cargoes);

		boolean[][] allocations = null;
		List<String> bestSolution = findSequence(ds);
		// find fitness of solution
		long bestFitness = findFitness(ds, pnl, optionalLoads, optionalDischarges);
		// serialise output
		allocations = getAllocations(ds);
		return allocations;
	}

	/**
	 * In the case of an Infeasible solution error, use this method to determine which constraints are causing the issue.
	 * @param optimiserRecorder
	 * @param pairingData
	 * @param maxDischargeSlotsInformation
	 * @param minDischargeSlotsInformation
	 * @param maxLoadSlotsInformation
	 * @param minLoadSlotsInformation
	 * @return the minimum set of violated constraints that the mip can find.
	 */
	public List<ConstraintInfo<P, C, ?>> minimiseViolations(PairingOptimisationData<?, ?> optimiserRecorder, SerializablePairingData pairingData,
			List<ConstraintInfo<P, C, IDischargeOption>> maxDischargeSlotsInformation, List<ConstraintInfo<P, C, IDischargeOption>> minDischargeSlotsInformation,
			List<ConstraintInfo<P, C, ILoadOption>> maxLoadSlotsInformation, List<ConstraintInfo<P, C, ILoadOption>> minLoadSlotsInformation) {
		long[][] pnl = pairingData.getPnl();
		boolean[][] validCargoes = pairingData.getValidCargoes();
		boolean[] optionalLoads = pairingData.getOptionalLoads();
		boolean[] optionalDischarges = pairingData.getOptionalDischarges();

		// Init
		int no_loads = pnl.length;
		int no_discharges = pnl[0].length;

//		List<Map<String, List<Integer>>> maxDischargeSlotsInformation = pairingData.getMaxDischargeSlots();
//		List<Map<String, List<Integer>>> minDischargeSlotsInformation = pairingData.getMinDischargeSlots();
//
//		List<Map<String, List<Integer>>> maxLoadSlotsInformation = pairingData.getMaxLoadSlots();
//		List<Map<String, List<Integer>>> minLoadSlotsInformation = pairingData.getMinLoadSlots();

		System.out.println("# loads: " + no_loads);
		System.out.println("# discharges: " + no_discharges);
		// Set up non optional penalties - default 0
		long[][] nonOptionalPenalty = new long[no_loads][no_discharges];

		// Modify loads with a non-optional penalty
		for (int load_id = 0; load_id < no_loads; load_id++) {
			if (!optionalLoads[load_id]) {
				for (int discharge_id = 0; discharge_id < no_discharges; discharge_id++) {
					nonOptionalPenalty[load_id][discharge_id] += NON_OPTIONAL_PENALTY;
				}
			}
		}

		// Modify discharges with a non-optional penalty
		for (int dischargeId = 0; dischargeId < no_discharges; dischargeId++) {
			if (!optionalDischarges[dischargeId]) {
				for (int loadId = 0; loadId < no_loads; loadId++) {
					nonOptionalPenalty[loadId][dischargeId] += NON_OPTIONAL_PENALTY;
				}
			}
		}

		// Set up symmettry reward - default 0
		double[][] symmetryPenalty = new double[no_loads][no_discharges];

		// Add symmettry reward
		for (int load_id = 0; load_id < no_loads; load_id++) {
			for (int discharge_id = 0; discharge_id < no_discharges; discharge_id++) {
				symmetryPenalty[load_id][discharge_id] = (((double) load_id) + .1) * (((double) discharge_id) + .1) * SYMMETRY_CONSTANT;
			}
		}

		/** google initialization */
		MPSolver solver = createSolver("CBC_MIXED_INTEGER_PROGRAMMING");

		/** variables */
		MPVariable[][] vars = new MPVariable[no_loads][no_discharges];
		for (int i = 0; i < no_loads; i++) {
			vars[i] = solver.makeBoolVarArray(no_discharges);
		}
			
		/** constraints */
		MPConstraint loadConstraints[] = new MPConstraint[no_loads];
		MPConstraint dischargeConstraints[] = new MPConstraint[no_discharges];

		for (int i = 0; i < no_loads; i++) {
			loadConstraints[i] = solver.makeConstraint(0, 1);
			for (int j = 0; j < no_discharges; j++) {
				loadConstraints[i].setCoefficient(vars[i][j], 1);

				if (!validCargoes[i][j]) {
					vars[i][j].setUb(0);
				}
			}
		}

		for (int i = 0; i < no_discharges; i++) {
			dischargeConstraints[i] = solver.makeConstraint(0, 1);
			for (int j = 0; j < no_loads; j++) {
				dischargeConstraints[i].setCoefficient(vars[j][i], 1);
			}
		}

		// max slots constraints
		MPVariable[] maxLoadSlotsVars = solver.makeBoolVarArray(maxLoadSlotsInformation.size());
		
		for (int v = 0; v < maxLoadSlotsInformation.size(); v++) {
			ConstraintInfo<P, C, ILoadOption> info = maxLoadSlotsInformation.get(v);
			Set<ILoadOption> slots = info.getSlots();
			
			//Count(loads) <= maxCount.
			int maxCount = info.getBound();
			//MPConstraint msConstraint = solver.makeConstraint(0, count.get(0));
			MPConstraint msConstraint = solver.makeConstraint(0, Short.MAX_VALUE);		
			for (ILoadOption slot : slots) {
				int i = optimiserRecorder.getIndex(slot);
				for (int j = 0; j < no_discharges; j++) {
					msConstraint.setCoefficient(vars[i][j], 1);
				}
			}
			
			//Add boolean variable to get output of the constraint: 1 if satisfied, 0 otherwise.
			msConstraint.setCoefficient(maxLoadSlotsVars[v], Short.MAX_VALUE - maxCount);
		}

		// min slots constraints
		MPVariable[] minLoadSlotsVars = solver.makeBoolVarArray(minLoadSlotsInformation.size());

		for (int v = 0; v < minLoadSlotsInformation.size(); v++) {
			ConstraintInfo<P, C, ILoadOption> info = minLoadSlotsInformation.get(v);
			Set<ILoadOption> slots = info.getSlots();
			
			//Count(loads) >= minCount.
			int minCount = info.getBound();
			
			//MPConstraint msConstraint = solver.makeConstraint(count.get(0), MPSolver.infinity());
			MPConstraint msConstraint = solver.makeConstraint(0, MPSolver.infinity());
			for (ILoadOption slot : slots) {
				int i = optimiserRecorder.getIndex(slot);
				for (int j = 0; j < no_discharges; j++) {
					msConstraint.setCoefficient(vars[i][j], 1);
				}
			}
			
			//Add boolean variable to get output of the constraint: 1 if satisfied, 0 otherwise.
			msConstraint.setCoefficient(minLoadSlotsVars[v], -minCount);
		}

		// max slots constraints
		MPVariable[] maxDischargeSlotsVars = solver.makeBoolVarArray(maxDischargeSlotsInformation.size());

		for (int v = 0; v < maxDischargeSlotsInformation.size(); v++) {
			ConstraintInfo<P, C, IDischargeOption> info = maxDischargeSlotsInformation.get(v);
			Set<IDischargeOption> slots = info.getSlots();
			
			//Count(loads) <= maxCount.
			int maxCount = info.getBound();
			
			MPConstraint msConstraint = solver.makeConstraint(0, Short.MAX_VALUE);
			//			MPConstraint msConstraint = solver.makeConstraint(0, count.get(0));
			for (IDischargeOption slot : slots) {
				int i = optimiserRecorder.getIndex(slot);
				for (int j = 0; j < no_loads; j++) {
					msConstraint.setCoefficient(vars[j][i], 1);
				}
			}

			//Add boolean variable to get output of the constraint: 1 if satisfied, 0 otherwise.
			msConstraint.setCoefficient(maxDischargeSlotsVars[v], Short.MAX_VALUE - maxCount);
		}

		// min slots constraints
		MPVariable[] minDischargeSlotsVars = solver.makeBoolVarArray(minDischargeSlotsInformation.size());

		for (int v = 0; v < minDischargeSlotsInformation.size(); v++) {
			ConstraintInfo<P, C, IDischargeOption> info = minDischargeSlotsInformation.get(v);
			Set<IDischargeOption> slots = info.getSlots();
			
			//Count(loads) <= maxCount.
			int minCount = info.getBound();
			MPConstraint msConstraint = solver.makeConstraint(0, MPSolver.infinity());
//			MPConstraint msConstraint = solver.makeConstraint(count.get(0), MPSolver.infinity());
			for (IDischargeOption slot : slots) {
				int i = optimiserRecorder.getIndex(slot);
				for (int j = 0; j < no_loads; j++) {
					msConstraint.setCoefficient(vars[j][i], 1);
				}				
			}
			
			//Add boolean variable to get output of the constraint: 1 if satisfied, 0 otherwise.
			msConstraint.setCoefficient(minDischargeSlotsVars[v], -minCount);
		}

		/** objective */
		MPObjective obj = solver.objective();
		obj.setMaximization();

		//We don't care about pnl now, only which constraints are violated.
		for (int i = 0; i < no_loads; i++) {
			for (int j = 0; j < no_discharges; j++) {
				obj.setCoefficient(vars[i][j], 0);
			}
		}

		//Add boolean variable to get output of the constraint: 1 if satisfied, 0 otherwise.
		for (int v = 0; v < maxLoadSlotsVars.length; v++) {
			obj.setCoefficient(maxLoadSlotsVars[v], 1);
		}
		for (int v = 0; v < minLoadSlotsVars.length; v++) {
			obj.setCoefficient(minLoadSlotsVars[v], 1);
		}
		for (int v = 0; v < maxDischargeSlotsVars.length; v++) {
			obj.setCoefficient(maxDischargeSlotsVars[v], 1);
		}
		for (int v = 0; v < minDischargeSlotsVars.length; v++) {
			obj.setCoefficient(minDischargeSlotsVars[v], 1);
		}
		
		final MPSolver.ResultStatus resultStatus = solver.solve();
		System.out.println("solver objective value (number of constraints satisfied): " + obj.value());

		int totalConstraints = maxLoadSlotsVars.length + minLoadSlotsVars.length + maxDischargeSlotsVars.length + minDischargeSlotsVars.length;
		
		System.out.println("total numnber of constraints: "+totalConstraints);
		System.out.println("Number of min load slot constraints"+minLoadSlotsVars.length);
		System.out.println("Number of max load slot constraints"+maxLoadSlotsVars.length);
		System.out.println("Number of min discharge slot constraints"+minDischargeSlotsVars.length);
		System.out.println("Number of max discharge slot constraints"+maxDischargeSlotsVars.length);
		System.out.println("#min constraints violated = "+ (totalConstraints - obj.value()));
		
		if (resultStatus != MPSolver.ResultStatus.OPTIMAL) {
			System.err.println("The problem does not have an optimal solution");
			// System.exit(1);
			return null;
		}

		/** print */
		System.out.println("Problem solved in: ");
		System.out.println("\t" + solver.iterations() + " iterations");
		System.out.println("\t" + solver.wallTime() + " milliseconds");

		List<ConstraintInfo<P, C, ?>> violatedConstraints = new ArrayList<>();
		
		for (int i = 0; i < maxLoadSlotsVars.length; i++) {
			if (maxLoadSlotsVars[i].solutionValue() < 1.0) {
				ConstraintInfo<P, C, ILoadOption> violated = maxLoadSlotsInformation.get(i);
				violatedConstraints.add(violated);
				int loadCount = getLoadSlotCount(optimiserRecorder,violated,vars);
				violated.setViolatedAmount(ConstraintInfo.ViolationType.Max, loadCount);
			}
		}
		for (int i = 0; i < minLoadSlotsVars.length; i++) {
			if (minLoadSlotsVars[i].solutionValue() < 1.0) {
				ConstraintInfo<P, C, ILoadOption> violated = minLoadSlotsInformation.get(i);
				violatedConstraints.add(violated);
				int loadCount = getLoadSlotCount(optimiserRecorder,violated,vars);
				violated.setViolatedAmount(ConstraintInfo.ViolationType.Min, loadCount);
			}
		}
		for (int i = 0; i < maxDischargeSlotsVars.length; i++) {
			if (maxDischargeSlotsVars[i].solutionValue() < 1.0) {
				ConstraintInfo<P, C, IDischargeOption> violated = maxDischargeSlotsInformation.get(i);
				violatedConstraints.add(violated);
				int dischargeCount = getDischargeSlotCount(optimiserRecorder,violated,vars);
				violated.setViolatedAmount(ConstraintInfo.ViolationType.Max, dischargeCount);
			}
		}
		for (int i = 0; i < minDischargeSlotsVars.length; i++) {
			if (minDischargeSlotsVars[i].solutionValue() < 1.0) {
				ConstraintInfo<P, C, IDischargeOption> violated = minDischargeSlotsInformation.get(i);
				violatedConstraints.add(violated);
				int dischargeCount = getDischargeSlotCount(optimiserRecorder,violated,vars);
				violated.setViolatedAmount(ConstraintInfo.ViolationType.Min, dischargeCount);
			}
		}
		
		return violatedConstraints;
	}

	private int getLoadSlotCount(PairingOptimisationData<?, ?> optimiserRecorder, ConstraintInfo<P, C, ILoadOption> info, MPVariable[][] vars) {
		int cnt = 0;
		Set<ILoadOption> slots = info.getSlots();
		for (ILoadOption slot : slots) {
			int i = optimiserRecorder.getIndex(slot);
			for (int j = 0; j < vars[i].length; j++) {
				if (vars[i][j].solutionValue() > 0.0) {
					cnt++;
				}
			}
		}
		return cnt;
	}

	private int getDischargeSlotCount(PairingOptimisationData<?, ?> optimiserRecorder, ConstraintInfo<P, C, IDischargeOption> info, MPVariable[][] vars) {
		int cnt = 0;
		Set<IDischargeOption> slots = info.getSlots();
		for (int i = 0; i < vars.length; i++) {
			for (IDischargeOption slot : slots) {
				int j = optimiserRecorder.getIndex(slot);
				if (vars[i][j].solutionValue() > 0.0) {
					cnt++;
				}
			}
		}
		return cnt;
	}
	
	private static List<String> findSequence(double[][] ds) {
		List<String> strings = new LinkedList<String>();
		for (int i = 0; i < ds.length; i++) {
			StringBuilder sb = new StringBuilder();
			for (int j = 0; j < ds[i].length; j++) {
				sb.append(" ");
				sb.append(ds[i][j]);
			}
			strings.add(sb.toString());
		}
		return strings;
	}

	private static long findFitness(double[][] ds, long[][] profit, boolean[] loads, boolean[] discharges) {
		long total = 0L;
		List<Double> totals = new LinkedList<Double>();
		for (int i = 0; i < ds.length; i++) {
			for (int j = 0; j < ds[i].length; j++) {
				total += (ds[i][j] * profit[i][j] + ds[i][j] * (loads[i] == false ? 1 * NON_OPTIONAL_PENALTY : 0) + ds[i][j] * (discharges[j] == false ? 1 * NON_OPTIONAL_PENALTY : 0));
				if (ds[i][j] > 0) {
					totals.add((ds[i][j] * profit[i][j] + ds[i][j] * (loads[i] == false ? 1 * NON_OPTIONAL_PENALTY : 0) + ds[i][j] * (discharges[j] == false ? 1 * NON_OPTIONAL_PENALTY : 0)));
				}
			}
		}
		System.out.println("fitness:" + total);
		for (int i = 0; i < ds.length; i++) {
			double max = 0;
			for (int j = 0; j < ds[i].length; j++) {
				if (ds[i][j] > max) {
					max = ds[i][j];
				}
			}
			if (max == 0) {
				System.out.println("load " + i + " missing");
			}
		}
		for (int j = 0; j < ds[0].length; j++) {
			double max = 0;
			for (int i = 0; i < ds.length; i++) {
				if (ds[i][j] > max) {
					max = ds[i][j];
				}
			}
			if (max == 0) {
				System.out.println("discharges " + j + " missing");
			}
		}

		Comparator<Double> compareDouble = (Double d1, Double d2) -> Double.compare(d1, d2) * -1;
		Collections.sort(totals, compareDouble);
		for (double d : totals) {
			System.out.println(d);
		}
		return total;
	}

	private static void serializeAllocations(double[][] ds, String path) {
		boolean[][] allocations = getAllocations(ds);
		serialize(allocations, path + "gurobi_allocations.lt");
	}

	private static boolean[][] getAllocations(double[][] ds) {
		boolean[][] allocations = new boolean[ds.length][ds[0].length];
		for (int i = 0; i < ds.length; i++) {
			for (int j = 0; j < ds[i].length; j++) {
				if (ds[i][j] == 1.0)
					allocations[i][j] = true;
			}
		}
		return allocations;
	}

	private static void serialize(Serializable serializable, String path) {
		try {
			File f = new File(path);
			f.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try (FileOutputStream fout = new FileOutputStream(path, false)) {
			try (ObjectOutputStream oos = new ObjectOutputStream(fout)) {
				oos.writeObject(serializable);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static MPSolver createSolver(String solverType) {
		try {
			return new MPSolver("LNGSolver", MPSolver.OptimizationProblemType.valueOf("CBC_MIXED_INTEGER_PROGRAMMING"));
		} catch (IllegalArgumentException e) {
			System.err.println("Bad google type: " + e);
			return null;
		}
	}

	@Override
	public boolean[][] findOptimalPairings(long[][] values, boolean[] optionalLoads, boolean[] optionalDischarges, boolean[][] valid, List<Map<String, List<Integer>>> maxDischargeSlotsConstraints,
			List<Map<String, List<Integer>>> minDischargeSlotsConstraints, List<Map<String, List<Integer>>> maxLoadSlotsConstraints, List<Map<String, List<Integer>>> minLoadSlotsConstraints) {

		SerializablePairingData data = createSerializablePairingData(values, optionalLoads, optionalDischarges, valid, maxDischargeSlotsConstraints, minDischargeSlotsConstraints,
				maxLoadSlotsConstraints, minLoadSlotsConstraints);

		return optimise(data);
	}

	private SerializablePairingData createSerializablePairingData(long[][] values, boolean[] optionalLoads, boolean[] optionalDischarges, boolean[][] valid,
			List<Map<String, List<Integer>>> maxDischargeSlotsConstraints, List<Map<String, List<Integer>>> minDischargeSlotsConstraints, List<Map<String, List<Integer>>> maxLoadSlotsConstraints,
			List<Map<String, List<Integer>>> minLoadSlotsConstraints) {
		SerializablePairingData data = new SerializablePairingData();
		data.pnl = values;
		data.optionalLoads = optionalLoads;
		data.optionalDischarges = optionalDischarges;
		data.validCargoes = valid;

		data.minLoadSlots = minLoadSlotsConstraints;
		data.maxLoadSlots = maxLoadSlotsConstraints;
		data.minDischargeSlots = minDischargeSlotsConstraints;
		data.maxDischargeSlots = maxDischargeSlotsConstraints;
		return data;
	}

	@Override
	public List<ConstraintInfo<P, C, ?>>  findMinViolatedConstraints(PairingOptimisationData<?, ?> optimiserRecorder, long[][] values, boolean[] optionalLoads, boolean[] optionalDischarges, boolean[][] valid,
			List<ConstraintInfo<P, C, IDischargeOption>> maxDischargeSlotsConstraints, List<ConstraintInfo<P, C, IDischargeOption>> minDischargeSlotsConstraints,
			List<ConstraintInfo<P, C, ILoadOption>> maxLoadSlotsConstraints, List<ConstraintInfo<P, C, ILoadOption>> minLoadSlotsConstraints) {
		SerializablePairingData data = createSerializablePairingData(values, optionalLoads, optionalDischarges, valid, null, null, null, null);
		return minimiseViolations(optimiserRecorder, data, maxDischargeSlotsConstraints, minDischargeSlotsConstraints, maxLoadSlotsConstraints, minLoadSlotsConstraints);
	}
}
