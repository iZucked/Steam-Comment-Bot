/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.verifier;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.util.CargoModelFinder;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelFinder;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

/**
 * Optimisation results checker. Currently validates cargoes exist in at least one solution. Need to expand API to build cargoes and allow for more complex solutions to be built up (multiple cargoes,
 * open positions, etc).
 * 
 * @author Simon Goodall
 *
 */
public class OptimiserResultVerifier {

	private final ScenarioModelFinder scenarioModelFinder;

	private final List<Function<SolutionData, Boolean>> checks = new LinkedList<>();
	private final OptimiserDataMapper mapper;

	public static OptimiserResultVerifier begin(final LNGScenarioRunner scenarioRunner) {
		return new OptimiserResultVerifier(scenarioRunner);
	}

	private OptimiserResultVerifier(final LNGScenarioRunner scenarioRunner) {
		this.scenarioModelFinder = new ScenarioModelFinder(scenarioRunner.getScenarioToOptimiserBridge().getOptimiserScenario());
		this.mapper = new OptimiserDataMapper(scenarioRunner.getScenarioToOptimiserBridge());
	}

	public VesselVerifier withCargo(final String loadName, final String dischargeName) {

		final CargoModelFinder finder = scenarioModelFinder.getCargoModelFinder();
		final LoadSlot loadSlot = finder.findLoadSlot(loadName);
		final DischargeSlot dischargeSlot = finder.findDischargeSlot(dischargeName);

		final Function<SolutionData, Pair<SolutionData, IResource>> p = (s) -> {

			final ISequenceElement l = s.getOptimiserDataMapper().getElementFor(loadSlot);
			final ISequenceElement d = s.getOptimiserDataMapper().getElementFor(dischargeSlot);

			final Pair<IResource, Integer> a = s.getLookupManager().lookup(l);
			final Pair<IResource, Integer> b = s.getLookupManager().lookup(d);

			if (a != null && b != null) {
				if (a.getFirst() != null && a.getFirst() == b.getFirst()) {
					if (a.getSecond() + 1 == b.getSecond()) {
						return new Pair<>(s, a.getFirst());
					}
				}

			}
			return null;
		};
		return new VesselVerifier(this, p);
	}

	/**
	 * Ensure load slot has been allocated in some way
	 * 
	 * @param loadName
	 * @return
	 */
	public VesselVerifier withUsedLoad(final String loadName) {

		final CargoModelFinder finder = scenarioModelFinder.getCargoModelFinder();
		final LoadSlot loadSlot = finder.findLoadSlot(loadName);

		final Function<SolutionData, Pair<SolutionData, IResource>> p = (s) -> {

			final ISequenceElement l = s.getOptimiserDataMapper().getElementFor(loadSlot);

			final Pair<IResource, Integer> a = s.getLookupManager().lookup(l);

			if (a != null) {
				if (a.getFirst() != null) {
					return new Pair<>(s, a.getFirst());
				}
			}
			return null;
		};
		return new VesselVerifier(this, p);
	}

	/**
	 * Ensure load slot has been allocated in some way
	 * 
	 * @param dischargeName
	 * @return
	 */
	public VesselVerifier withUsedDischarge(final String dischargeName) {

		final CargoModelFinder finder = scenarioModelFinder.getCargoModelFinder();
		final DischargeSlot dischargeSlot = finder.findDischargeSlot(dischargeName);

		final Function<SolutionData, Pair<SolutionData, IResource>> p = (s) -> {

			final ISequenceElement l = s.getOptimiserDataMapper().getElementFor(dischargeSlot);

			final Pair<IResource, Integer> a = s.getLookupManager().lookup(l);

			if (a != null) {
				if (a.getFirst() != null) {
					return new Pair<>(s, a.getFirst());
				}
			}
			return null;
		};
		return new VesselVerifier(this, p);
	}

	public OptimiserResultVerifier withUnusedSlot(final String name) {

		final CargoModelFinder finder = scenarioModelFinder.getCargoModelFinder();
		final Slot slot = finder.findSlot(name);

		final Function<SolutionData, Boolean> p = (s) -> {

			final ISequenceElement l = s.getOptimiserDataMapper().getElementFor(slot);
			final Pair<IResource, Integer> a = s.getLookupManager().lookup(l);

			if (a != null) {
				if (a.getFirst() == null) {
					return true;
				}

			}
			return false;
		};
		this.checks.add(p);
		return this;
	}

	public static class VesselVerifier {
		private final Function<SolutionData, Pair<SolutionData, IResource>> result;
		private final OptimiserResultVerifier verifier;

		VesselVerifier(final OptimiserResultVerifier verifier, final Function<SolutionData, Pair<SolutionData, IResource>> result) {
			this.verifier = verifier;
			this.result = result;

		}

		public OptimiserResultVerifier any() {
			final Function<Pair<SolutionData, IResource>, Boolean> v = p -> {
				if (p == null || p.getSecond() == null) {
					return false;
				}
				return true;
			};

			final Function<SolutionData, Boolean> aa = v.compose(result);
			verifier.checks.add(aa);
			return verifier;
		}

		public OptimiserResultVerifier anyNominalVessel() {
			final Function<Pair<SolutionData, IResource>, Boolean> v = p -> {
				if (p == null || p.getSecond() == null) {
					return false;
				}
				final IResource r = p.getSecond();
				final IVesselProvider vesselProvider = p.getFirst().getInjector().getInstance(IVesselProvider.class);
				final IVesselAvailability va = vesselProvider.getVesselAvailability(r);
				return va.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP;
			};

			final Function<SolutionData, Boolean> aa = v.compose(result);
			verifier.checks.add(aa);
			return verifier;
		}

		public OptimiserResultVerifier anySpotCharterVessel() {
			final Function<Pair<SolutionData, IResource>, Boolean> v = p -> {
				if (p == null || p.getSecond() == null) {
					return false;
				}
				final IResource r = p.getSecond();
				final IVesselProvider vesselProvider = p.getFirst().getInjector().getInstance(IVesselProvider.class);
				final IVesselAvailability va = vesselProvider.getVesselAvailability(r);
				return va.getVesselInstanceType() == VesselInstanceType.SPOT_CHARTER;
			};

			final Function<SolutionData, Boolean> aa = v.compose(result);
			verifier.checks.add(aa);
			return verifier;
		}

		public OptimiserResultVerifier anyFleetVessel() {
			final Function<Pair<SolutionData, IResource>, Boolean> v = p -> {
				if (p == null || p.getSecond() == null) {
					return false;
				}
				final IResource r = p.getSecond();
				final IVesselProvider vesselProvider = p.getFirst().getInjector().getInstance(IVesselProvider.class);
				final IVesselAvailability va = vesselProvider.getVesselAvailability(r);
				return va.getVesselInstanceType() == VesselInstanceType.FLEET || va.getVesselInstanceType() == VesselInstanceType.TIME_CHARTER;
			};

			final Function<SolutionData, Boolean> combinedChecker = v.compose(result);
			verifier.checks.add(combinedChecker);
			return verifier;
		}
	}

	public void verifyInsertionResults(final IMultiStateResult results, final Consumer<String> errorHandler) {

		final List<NonNullPair<ISequences, Map<String, Object>>> solutions = results.getSolutions();
		if (solutions.size() < 2) {
			errorHandler.accept("No solutions found");
		}

		final List<SolutionData> data = new ArrayList<SolutionData>(solutions.size() - 1);
		for (int i = 1; i < solutions.size(); ++i) {
			data.add(new SolutionData(mapper, solutions.get(i).getFirst()));
		}

		for (final Function<SolutionData, Boolean> checker : checks) {
			boolean foundMatch = false;
			for (final SolutionData d : data) {
				if (checker.apply(d)) {
					foundMatch = true;
					break;
				}
			}
			if (!foundMatch) {
				errorHandler.accept("Solution requirement not found");
			}
		}
	}

	public void verifySingleOptimisationResult(final IMultiStateResult result, final Consumer<String> errorHandler) {

		final List<NonNullPair<ISequences, Map<String, Object>>> solutions = result.getSolutions();
		if (solutions.size() < 2) {
			errorHandler.accept("No solution found");
		}

		final List<SolutionData> data = new ArrayList<>(1);
		for (int i = 1; i < solutions.size(); ++i) {
			data.add(new SolutionData(mapper, solutions.get(i).getFirst()));
		}

		for (final Function<SolutionData, Boolean> checker : checks) {
			boolean foundMatch = false;
			for (final SolutionData d : data) {
				if (checker.apply(d)) {
					foundMatch = true;
					break;
				}
			}
			if (!foundMatch) {
				errorHandler.accept("Solution requirement not found");
			}
		}
	}
	
	public void verifyCargoCountInOptimisationResultWithoutNominals(int solution, int cargoCount, final IMultiStateResult result, final Consumer<String> errorHandler) {

		final List<NonNullPair<ISequences, Map<String, Object>>> solutions = result.getSolutions();
		if (solutions.size() < 2 + solution) {
			errorHandler.accept("No solution found");
		}

		final List<SolutionData> data = new ArrayList<>(1);
		for (int i = 1; i < solutions.size(); ++i) {
			data.add(new SolutionData(mapper, solutions.get(i).getFirst()));
		}

		SolutionData solutionData = data.get(solution);
		ISequences sequences = solutionData.getLookupManager().getRawSequences();
		int totalLoadsOnSequences = OptimiserResultVerifierUtils.getTotalLoadsOnSequences(sequences, mapper, true);
		if (totalLoadsOnSequences != cargoCount) {
			errorHandler.accept(String.format("Cargo count: expected %s but was %s", cargoCount, totalLoadsOnSequences));
		}
	}

}
