/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.verifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.util.CargoModelFinder;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelFinder;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelKPIUtils;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.components.ISpotCharterInMarket;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.ICharterMarketProvider;
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

	private final @NonNull List<Function<SolutionData, Boolean>> anySolutionChecks = new LinkedList<>();
	private final @NonNull Map<Integer, List<Function<SolutionData, Boolean>>> specificSolutionChecks = new HashMap<>();
	private final @NonNull List<Function<List<SolutionData>, Boolean>> multipleSolutionChecks = new LinkedList<>();

	private boolean needsSchedule = false;
	private final @NonNull OptimiserDataMapper mapper;

	public static List<SolutionData> createSolutionData(final boolean needsSchedule, final IMultiStateResult results, final @NonNull OptimiserDataMapper mapper) {

		final List<NonNullPair<ISequences, Map<String, Object>>> solutions = results.getSolutions();
		final List<SolutionData> solutionDataList = new LinkedList<>();
		for (int i = 1; i < solutions.size(); ++i) {
			final NonNullPair<ISequences, Map<String, Object>> solutionDataPair = solutions.get(i);
			final SolutionData data = new SolutionData(mapper, solutionDataPair.getFirst());
			if (needsSchedule) {
				data.setSchedule(mapper.getScenarioToOptimiserBridge().createSchedule(solutionDataPair.getFirst(), solutionDataPair.getSecond()));
			}
			solutionDataList.add(data);
		}
		return solutionDataList;
	}

	public static OptimiserResultVerifier begin(final @NonNull LNGScenarioRunner scenarioRunner) {
		return new OptimiserResultVerifier(new OptimiserDataMapper(scenarioRunner.getScenarioToOptimiserBridge()));
	}

	public static OptimiserResultVerifier begin(final @NonNull OptimiserDataMapper mapper) {
		return new OptimiserResultVerifier(mapper);
	}

	private OptimiserResultVerifier(final OptimiserDataMapper mapper) {
		this.scenarioModelFinder = new ScenarioModelFinder(mapper.getScenarioToOptimiserBridge().getOptimiserScenario());
		this.mapper = mapper;
	}

	public static final class OptimiserResultChecker {
		private final OptimiserResultVerifier verifier;
		private final List<Function<SolutionData, Boolean>> checkers = new LinkedList<>();
		private int solutionIndex = -1;

		public OptimiserResultChecker(OptimiserResultVerifier verifier) {
			this.verifier = verifier;
		}

		public OptimiserResultChecker(OptimiserResultVerifier verifier, int solutionIndex) {
			this.verifier = verifier;
			this.solutionIndex = solutionIndex;
		}

		public OptimiserResultVerifier build() {
			checkers.forEach(c -> addCheckerToFinalResult(c));
			return this.verifier;
		}

		private void addCheckerToFinalResult(Function<SolutionData, Boolean> checker) {
			if (solutionIndex == -1) {
				verifier.addAnySolutionChecker(checker);
			} else {
				verifier.addSpecificSolutionChecker(solutionIndex, checker);
			}
		}

		public void addChecker(Function<SolutionData, Boolean> checker) {
			checkers.add(checker);
		}

		public VesselVerifier withCargo(final String loadName, final String dischargeName) {

			final CargoModelFinder finder = verifier.scenarioModelFinder.getCargoModelFinder();
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

			final CargoModelFinder finder = verifier.scenarioModelFinder.getCargoModelFinder();
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
		 * Ensure discharge slot has been allocated in some way
		 * 
		 * @param dischargeName
		 * @return
		 */
		public VesselVerifier withUsedDischarge(final String dischargeName) {

			final CargoModelFinder finder = verifier.scenarioModelFinder.getCargoModelFinder();
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

		/**
		 * Ensure vessel event has been allocated in some way
		 * 
		 * @param dischargeName
		 * @return
		 */
		public VesselVerifier withUsedVesselEvent(final String vesselEventName) {

			final CargoModelFinder finder = verifier.scenarioModelFinder.getCargoModelFinder();
			final VesselEvent vesselEvent = finder.findVesselEvent(vesselEventName);

			final Function<SolutionData, Pair<SolutionData, IResource>> p = (s) -> {

				final ISequenceElement l = s.getOptimiserDataMapper().getElementFor(vesselEvent);

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

		public OptimiserResultChecker withUnusedSlot(final String name) {

			final CargoModelFinder finder = verifier.scenarioModelFinder.getCargoModelFinder();
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
			this.addChecker(p);
			return this;
		}

		public OptimiserResultChecker withCargoCount(final int count, boolean ignoreRoundTrips) {

			final Function<SolutionData, Boolean> p = (s) -> {
				final ISequences sequences = s.getLookupManager().getRawSequences();
				final int totalLoadsOnSequences = OptimiserResultVerifierUtils.getTotalLoadsOnSequences(sequences, verifier.mapper, ignoreRoundTrips);
				if (totalLoadsOnSequences != count) {
					return false;
				}

				return true;
			};
			this.addChecker(p);
			return this;
		}

		public OptimiserResultChecker pnlDelta(final long initialValue, final long expectedChange, final long delta) {
			verifier.needsSchedule = true;

			final Function<SolutionData, Boolean> aa = (data) -> {
				final Schedule schedule = data.getSchedule();
				if (schedule == null) {
					return false;
				}
				final long value = ScheduleModelKPIUtils.getScheduleProfitAndLoss(schedule);
				return Math.abs(expectedChange - (value - initialValue)) <= delta;
			};
			this.addChecker(aa);
			return this;
		}

		public OptimiserResultChecker latenessDelta(final long initialValue, final long expectedChange) {
			verifier.needsSchedule = true;

			final Function<SolutionData, Boolean> aa = (data) -> {
				final Schedule schedule = data.getSchedule();
				if (schedule == null) {
					return false;
				}
				final long value = ScheduleModelKPIUtils.getScheduleLateness(schedule)[ScheduleModelKPIUtils.LATENESS_WITHOUT_FLEX_IDX];
				return expectedChange == (value - initialValue);
			};
			this.addChecker(aa);
			return this;
		}

		public OptimiserResultChecker violationDelta(final long initialValue, final long expectedChange) {
			verifier.needsSchedule = true;

			final Function<SolutionData, Boolean> aa = (data) -> {
				final Schedule schedule = data.getSchedule();
				if (schedule == null) {
					return false;
				}
				final long value = ScheduleModelKPIUtils.getScheduleViolationCount(schedule);
				return expectedChange == (value - initialValue);
			};
			this.addChecker(aa);
			return this;
		}
	}

	public OptimiserResultChecker withAnySolutionResultChecker() {
		return new OptimiserResultChecker(this);
	}

	public OptimiserResultChecker withSolutionResultChecker(int solution) {
		return new OptimiserResultChecker(this, solution);
	}

	public OptimiserResultVerifier withMultipleSolutionCount(final int count) {

		final Function<List<SolutionData>, Boolean> p = (s) -> {
			return s.size() == count;
		};
		this.multipleSolutionChecks.add(p);
		return this;
	}

	public static class VesselVerifier {
		private final Function<SolutionData, Pair<SolutionData, IResource>> result;
		private final OptimiserResultChecker resultChecker;

		VesselVerifier(final OptimiserResultChecker verifier, final Function<SolutionData, Pair<SolutionData, IResource>> result) {
			this.resultChecker = verifier;
			this.result = result;
		}

		public OptimiserResultChecker any() {
			final Function<Pair<SolutionData, IResource>, Boolean> v = p -> {
				if (p == null || p.getSecond() == null) {
					return false;
				}
				return true;
			};

			final Function<SolutionData, Boolean> aa = v.compose(result);
			resultChecker.addChecker(aa);
			return this.resultChecker;
		}

		public OptimiserResultChecker anyNominalVessel() {
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
			resultChecker.addChecker(aa);
			return this.resultChecker;
		}

		public OptimiserResultChecker anySpotCharterVessel() {
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
			resultChecker.addChecker(aa);
			return this.resultChecker;
		}

		public OptimiserResultChecker anyFleetVessel() {
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
			resultChecker.addChecker(combinedChecker);
			return this.resultChecker;
		}

		public OptimiserResultChecker onFleetVessel(final @NonNull String name) {
			final Function<Pair<SolutionData, IResource>, Boolean> v = p -> {
				if (p == null || p.getSecond() == null) {
					return false;
				}
				final IResource r = p.getSecond();
				final IVesselProvider vesselProvider = p.getFirst().getInjector().getInstance(IVesselProvider.class);
				final IVesselAvailability va = vesselProvider.getVesselAvailability(r);
				return (va.getVesselInstanceType() == VesselInstanceType.FLEET || va.getVesselInstanceType() == VesselInstanceType.TIME_CHARTER) //
						&& (name.equalsIgnoreCase(va.getVessel().getName()));
			};

			final Function<SolutionData, Boolean> combinedChecker = v.compose(result);
			resultChecker.addChecker(combinedChecker);
			return this.resultChecker;
		}

		public OptimiserResultChecker onSpotCharter(final String marketName) {
			final Function<Pair<SolutionData, IResource>, Boolean> v = p -> {
				if (p == null || p.getSecond() == null) {
					return false;
				}
				final IResource r = p.getSecond();
				final IVesselProvider vesselProvider = p.getFirst().getInjector().getInstance(IVesselProvider.class);
				final ICharterMarketProvider charterMarketProvider = p.getFirst().getInjector().getInstance(ICharterMarketProvider.class);
				final IVesselAvailability va = vesselProvider.getVesselAvailability(r);
				final ISpotCharterInMarket spotCharterInMarket = va.getSpotCharterInMarket();
				return (va.getVesselInstanceType() == VesselInstanceType.SPOT_CHARTER) //
						&& (marketName.equalsIgnoreCase(spotCharterInMarket.getName()));
			};

			final Function<SolutionData, Boolean> checker = v.compose(result);
			resultChecker.addChecker(checker);
			return this.resultChecker;
		}
	}

	public @Nullable ISequences verifySolutionExistsInResults(final IMultiStateResult results, final Consumer<String> errorHandler) {
		final List<NonNullPair<ISequences, Map<String, Object>>> solutions = results.getSolutions();
		if (solutions.size() < 2) {
			errorHandler.accept("No solutions found");
			return null;
		}

		final List<SolutionData> solutionDataList = createSolutionData(needsSchedule, results, mapper);
		return verifySolutionExistsInResults(solutionDataList, errorHandler);
	}

	public @Nullable ISequences verifySolutionExistsInResults(final List<SolutionData> solutionDataList, final Consumer<String> errorHandler) {

		for (final SolutionData data : solutionDataList) {
			for (final Function<SolutionData, Boolean> checker : anySolutionChecks) {
				if (!checker.apply(data)) {
					continue;
				}
			}
			return data.getLookupManager().getRawSequences();
		}
		errorHandler.accept("Solution requirement not found");
		return null;
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

		checkAnySolutionsCheckers(errorHandler, data);

		checkMultipleSolutionCheckers(errorHandler, data);
	}

	public void verifyOptimisationResults(final IMultiStateResult result, final Consumer<String> errorHandler) {

		final List<NonNullPair<ISequences, Map<String, Object>>> solutions = result.getSolutions();
		if (solutions.size() < 2) {
			errorHandler.accept("No solution found");
		}

		final List<SolutionData> data = new ArrayList<>(1);
		for (int i = 1; i < solutions.size(); ++i) {
			data.add(new SolutionData(mapper, solutions.get(i).getFirst()));
		}

		checkAnySolutionsCheckers(errorHandler, data);

		checkMultipleSolutionCheckers(errorHandler, data);

		checkSpecificSolutionCheckers(errorHandler, data);
	}

	private void checkSpecificSolutionCheckers(final Consumer<String> errorHandler, final List<SolutionData> data) {
		boolean failed = false;
		List<Integer> keySet = new LinkedList<>(specificSolutionChecks.keySet()).stream() //
				.sorted() //
				.collect(Collectors.toList());
		for (Integer solutionIndex : keySet) {
			if (solutionIndex >= data.size()) {
				failed = true;
				break;
			}
			for (final Function<SolutionData, Boolean> checker : specificSolutionChecks.get(solutionIndex)) {
				if (!checker.apply(data.get(solutionIndex))) {
					failed = true;
					break;
				}
			}
		}
		if (failed) {
			errorHandler.accept("Solution requirement not found");
		}
	}

	private void checkMultipleSolutionCheckers(final Consumer<String> errorHandler, final List<SolutionData> data) {
		boolean failed = false;
		for (final Function<List<SolutionData>, Boolean> checker : multipleSolutionChecks) {
			if (!checker.apply(data)) {
				failed = true;
				break;
			}
		}
		if (failed) {
			errorHandler.accept("Solution requirement not found");
		}
	}

	private void checkAnySolutionsCheckers(final Consumer<String> errorHandler, final List<SolutionData> data) {
		for (final Function<SolutionData, Boolean> checker : anySolutionChecks) {
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

	public void verifyCargoCountInOptimisationResultWithoutNominals(final int solution, final int cargoCount, final IMultiStateResult result, final Consumer<String> errorHandler) {

		final List<NonNullPair<ISequences, Map<String, Object>>> solutions = result.getSolutions();
		if (solutions.size() < 2 + solution) {
			errorHandler.accept("No solution found");
		}

		final List<SolutionData> data = new ArrayList<>(1);
		for (int i = 1; i < solutions.size(); ++i) {
			data.add(new SolutionData(mapper, solutions.get(i).getFirst()));
		}

		final SolutionData solutionData = data.get(solution);
		final ISequences sequences = solutionData.getLookupManager().getRawSequences();
		final int totalLoadsOnSequences = OptimiserResultVerifierUtils.getTotalLoadsOnSequences(sequences, mapper, true);
		if (totalLoadsOnSequences != cargoCount) {
			errorHandler.accept(String.format("Cargo count: expected %s but was %s", cargoCount, totalLoadsOnSequences));
		}
	}

	private void addAnySolutionChecker(Function<SolutionData, Boolean> check) {
		anySolutionChecks.add(check);
	}

	private void addSpecificSolutionChecker(int solutionIndex, Function<SolutionData, Boolean> check) {
		specificSolutionChecks.compute(solutionIndex, (k, v) -> {
			List<Function<SolutionData, Boolean>> solutionCheckers = v;
			if (v == null) {
				solutionCheckers = new LinkedList<>();
			}
			solutionCheckers.add(check);
			return solutionCheckers;
		});
	}

}
