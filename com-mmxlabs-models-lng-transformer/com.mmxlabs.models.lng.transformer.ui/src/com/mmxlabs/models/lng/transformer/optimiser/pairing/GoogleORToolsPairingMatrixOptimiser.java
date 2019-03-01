/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.optimiser.pairing;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;

/**
 * Optimise cargo allocations with Google OR.
 * <p>
 */
public class GoogleORToolsPairingMatrixOptimiser implements IPairingMatrixOptimiser {

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

		SerializablePairingData data = new SerializablePairingData();
		data.pnl = values;
		data.optionalLoads = optionalLoads;
		data.optionalDischarges = optionalDischarges;
		data.validCargoes = valid;

		data.minLoadSlots = minLoadSlotsConstraints;
		data.maxLoadSlots = maxLoadSlotsConstraints;
		data.minDischargeSlots = minDischargeSlotsConstraints;
		data.maxDischargeSlots = maxDischargeSlotsConstraints;

		return optimise(data);
	}
}
