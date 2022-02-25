package com.mmxlabs.models.lng.adp.rateability.spacing;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;

import com.google.ortools.sat.CpModel;
import com.google.ortools.sat.CpSolver;
import com.google.ortools.sat.CpSolverStatus;
import com.google.ortools.sat.IntVar;
import com.google.ortools.sat.IntervalVar;
import com.google.ortools.sat.LinearExpr;
import com.google.ortools.sat.Literal;
import com.google.ortools.sat.ScalProd;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.Triple;
import com.mmxlabs.common.time.Hours;
import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.DesSpacingAllocation;
import com.mmxlabs.models.lng.adp.DesSpacingRow;
import com.mmxlabs.models.lng.adp.FobSpacingAllocation;
import com.mmxlabs.models.lng.adp.SpacingProfile;
import com.mmxlabs.models.lng.adp.rateability.export.Feasible;
import com.mmxlabs.models.lng.adp.rateability.export.Infeasible;
import com.mmxlabs.models.lng.adp.rateability.export.InvalidModel;
import com.mmxlabs.models.lng.adp.rateability.export.Optimal;
import com.mmxlabs.models.lng.adp.rateability.export.SpacingRateabilitySolverResult;
import com.mmxlabs.models.lng.adp.rateability.export.Unknown;
import com.mmxlabs.models.lng.adp.rateability.export.Unrecognised;
import com.mmxlabs.models.lng.adp.rateability.spacing.containers.ContainerBuilder;
import com.mmxlabs.models.lng.adp.rateability.spacing.containers.ShippedCargoModellingContainer;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.SchedulingTimeWindow;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.util.CargoTravelTimeUtils;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.util.ModelDistanceProvider;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.LNGScenarioSharedModelTypes;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioElementNameHelper;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

/**
 * Original CP based solver spitting out .csv files.
 * 
 * @author Simon Goodall, modified by Patrick Mills to minimise schedule span and ensure JERA cargoes 30 days apart.
 */
public class CPBasedSolver {

	@NonNull
	private static final LocalDate FIRST_DATE = LocalDate.of(2021, 12, 1);

	public SpacingRateabilitySolverResult runCpSolver(@NonNull final SpacingProfile spacingProfile, final LNGScenarioModel sm, final EditingDomain ed, final IScenarioDataProvider sdp) {

		final List<Contract> inputContracts = new ArrayList<>();
		final List<Vessel> inputVessels = new ArrayList<>();
		for (final FobSpacingAllocation fobSpacingAllocation : spacingProfile.getFobSpacingAllocations()) {
			final SalesContract salesContract = fobSpacingAllocation.getContract();
			final Contract contract = new Contract(salesContract.getName(), 0, true, fobSpacingAllocation.getMinSpacing());
			if (fobSpacingAllocation.isSetMaxSpacing()) {
				contract.maxInterval = fobSpacingAllocation.getMaxSpacing();
			}
			final Cargo[] cargoes = new Cargo[fobSpacingAllocation.getCargoCount()];
			for (int i = 0; i < fobSpacingAllocation.getCargoCount(); ++i) {
				cargoes[i] = new Cargo(contract);
			}
			final String vesselName = String.format("Fob-vessel-%s", salesContract.getName());
			final Vessel vessel = new Vessel(vesselName, "0", cargoes);
			inputContracts.add(contract);
			inputVessels.add(vessel);
		}

		for (final DesSpacingAllocation desSpacingAllocation : spacingProfile.getDesSpacingAllocations()) {
			final SalesContract salesContract = desSpacingAllocation.getContract();
			final Contract contract = new Contract(salesContract.getName(), 20, false, 20);
			if (desSpacingAllocation.getPort() != null) {
				contract.port = desSpacingAllocation.getPort().getName();
			}
			inputContracts.add(contract);
			final Cargo[] cargoes = new Cargo[desSpacingAllocation.getDesSpacingRows().size()];
			int i = 0;
			for (final DesSpacingRow desSpacingRow : desSpacingAllocation.getDesSpacingRows()) {
				final Cargo cargo = new Cargo(contract);
				if (desSpacingRow.isSetMinDischargeDate()) {
					final LocalDateTime minDischargeDate = desSpacingRow.getMinDischargeDate();
					cargo.minDischargeDate = minDischargeDate.toLocalDate();
				}
				if (desSpacingRow.isSetMaxDischargeDate()) {
					final LocalDateTime maxDischargeDate = desSpacingRow.getMaxDischargeDate();
					cargo.maxDischargeDate = maxDischargeDate.toLocalDate();
				}
				cargoes[i] = cargo;
				++i;
			}
			final Vessel vessel = new Vessel(desSpacingAllocation.getVessel().getVessel().getName(), "0", cargoes);
			inputVessels.add(vessel);
		}

		// This needs to made into user input
		final String defaultPortName = spacingProfile.getDefaultPort().getName();

		final CpModel model = new CpModel();
		final ADPModel adpModel = ScenarioModelUtil.getADPModel(sdp);

		// Relative date that the solver works against - not necessarily earliest valid UTC time, e.g. a 1 Jan JST datetime may correspond to a 31 Dec UTC datetime
		final ZoneId utcZoneId = ZoneId.of("UTC");
		@NonNull
		final ZonedDateTime dateTimeZero = adpModel.getYearStart().atDay(1).atStartOfDay().atZone(utcZoneId);
		final ZonedDateTime dateTimeEnd = adpModel.getYearEnd().plusMonths(6).atEndOfMonth().atTime(23, 0).atZone(utcZoneId);

		final int dateTimeEndInt = getHourValue(dateTimeEnd, dateTimeZero);

		@NonNull
		final ModelDistanceProvider modelDistanceProvider = sdp.getExtraDataProvider(LNGScenarioSharedModelTypes.DISTANCES, ModelDistanceProvider.class);

		final Map<String, Contract> processedContracts = new HashMap<>();
		for (final Contract contract : inputContracts) {
			processedContracts.put(contract.getName(), contract);
		}
		final Vessel[] processedVessels = new Vessel[inputVessels.size()];
		int ii = 0;
		for (final Vessel vessel : inputVessels) {
			processedVessels[ii] = vessel;
			++ii;
		}

		Pair<Map<String, Contract>, Vessel[]> inputProcessResult = Pair.of(processedContracts, processedVessels);

		final Map<String, Contract> loadedContracts = inputProcessResult.getFirst();
		final Vessel[] vessels = inputProcessResult.getSecond();
		final Map<Contract, @NonNull List<LoadSlot>> usedLoadSlots = getUsedLoadSlots(sm, loadedContracts);
		final Set<LoadSlot> usedLoadSlotsSet = usedLoadSlots.values().stream().flatMap(List::stream).collect(Collectors.toSet());

		final Map<Contract, @NonNull Pair<@NonNull ZonedDateTime, ZonedDateTime>> latestDatePairs = getLatestPreAdpContractDatePairs(sm, loadedContracts, modelDistanceProvider, utcZoneId);
		final Map<Contract, SalesContract> contractMap = getExpectedContracts(loadedContracts, sm);
		final Map<SalesContract, Contract> reverseContractMap = new HashMap<>();
		contractMap.entrySet().stream().forEach(e -> reverseContractMap.put(e.getValue(), e.getKey()));
		final Set<SalesContract> expectedSalesContracts = new HashSet<>(contractMap.values());

		final YearMonth adpStart = sm.getAdpModel().getYearStart();
		final List<LoadSlot> adpLoadSlots = sm.getCargoModel().getLoadSlots().stream().filter(slot -> !adpStart.isAfter(YearMonth.from(slot.getWindowStart()))).collect(Collectors.toList());
		final Set<LoadSlot> adpLoadSlotSet = new HashSet<>(adpLoadSlots);

		final Map<LoadSlot, Contract> loadSlotToContractMap = new HashMap<>();
		final Map<Contract, @NonNull List<LoadSlot>> usedLoadSlotsToIgnore = new HashMap<>();
		usedLoadSlots.entrySet().forEach(entry -> entry.getValue().forEach(slot -> loadSlotToContractMap.put(slot, entry.getKey())));
		for (final Entry<Contract, @NonNull List<LoadSlot>> entry : usedLoadSlots.entrySet()) {
			loadedContracts.values().stream().filter(c -> c != entry.getKey()).forEach(c -> usedLoadSlotsToIgnore.computeIfAbsent(c, con -> new LinkedList<>()).addAll(entry.getValue()));
		}

		final Map<com.mmxlabs.models.lng.fleet.Vessel, Vessel> eVesselToVesselMap = new HashMap<>();
		final Map<Vessel, com.mmxlabs.models.lng.fleet.Vessel> vesselToEVesselMap = new HashMap<>();
		for (final Vessel v : vessels) {
			if (Arrays.stream(v.cargoes).anyMatch(c -> !c.contract.fob)) {
				final com.mmxlabs.models.lng.fleet.Vessel eVessel = sm.getReferenceModel().getFleetModel().getVessels().stream().filter(vv -> vv.getName().equalsIgnoreCase(v.name)).findAny().get();
				eVesselToVesselMap.put(eVessel, v);
				vesselToEVesselMap.put(v, eVessel);
			}
		}

		final Map<Vessel, List<com.mmxlabs.models.lng.cargo.Cargo>> cargoVessels = new HashMap<>();
		for (final com.mmxlabs.models.lng.cargo.Cargo cargo : sm.getCargoModel().getCargoes()) {
			final LoadSlot loadSlot = (LoadSlot) cargo.getSlots().get(0);
			final DischargeSlot dischargeSlot = (DischargeSlot) cargo.getSlots().get(1);
			if (dischargeSlot.isFOBSale() || loadSlot.isDESPurchase()) {
				continue;
			}
			if (!(dischargeSlot instanceof SpotSlot) && expectedSalesContracts.contains(dischargeSlot.getContract())) {
				continue;
			}
			final VesselAssignmentType cargoVA = cargo.getVesselAssignmentType();
			if (cargoVA instanceof VesselAvailability va) {
				final com.mmxlabs.models.lng.fleet.Vessel eVessel = va.getVessel();
				final Vessel vessel = eVesselToVesselMap.get(eVessel);
				if (vessel != null) {
					cargoVessels.computeIfAbsent(vessel, v -> new ArrayList<>()).add(cargo);
				}
			}
		}

		cargoVessels.values().forEach(l -> l.sort((c1, c2) -> c1.getSlots().get(0).getWindowStart().compareTo(c2.getSlots().get(0).getWindowStart())));

		// load slot contract restrictions
		for (final LoadSlot loadSlot : adpLoadSlots) {
			final List<com.mmxlabs.models.lng.commercial.Contract> contractRestrictions = loadSlot.getSlotOrDelegateContractRestrictions();
			if (!contractRestrictions.isEmpty()) {
				if (loadSlot.getSlotOrDelegateContractRestrictionsArePermissive()) {
					final Set<com.mmxlabs.models.lng.commercial.Contract> restrictionsSet = new HashSet<>(contractRestrictions);
					for (final SalesContract contract : expectedSalesContracts) {
						if (!restrictionsSet.contains(contract)) {
							final Contract oContract = reverseContractMap.get(contract);
							usedLoadSlotsToIgnore.computeIfAbsent(oContract, con -> new LinkedList<>()).add(loadSlot);
						}
					}
				} else {
					final Set<com.mmxlabs.models.lng.commercial.Contract> restrictionsSet = new HashSet<>(contractRestrictions);
					for (final SalesContract contract : expectedSalesContracts) {
						if (restrictionsSet.contains(contract)) {
							final Contract oContract = reverseContractMap.get(contract);
							usedLoadSlotsToIgnore.computeIfAbsent(oContract, con -> new LinkedList<>()).add(loadSlot);
						}
					}
				}
			}
		}

		// Get load slots used by markets
		final Set<LoadSlot> unusableLoadSlots = Stream.concat(sm.getCargoModel().getCargoes().stream().filter(cargo -> adpLoadSlotSet.contains(cargo.getSlots().get(0))).filter(cargo -> {
			final DischargeSlot dischargeSlot = (DischargeSlot) cargo.getSlots().get(1);
			return dischargeSlot instanceof SpotDischargeSlot || !expectedSalesContracts.contains(dischargeSlot.getContract());
		}).map(c -> (LoadSlot) c.getSlots().get(0)), adpLoadSlots.stream().filter(LoadSlot::isLocked)).collect(Collectors.toSet());

		if (!unusableLoadSlots.isEmpty()) {
			for (final Contract contract : loadedContracts.values()) {
				usedLoadSlotsToIgnore.computeIfAbsent(contract, con -> new LinkedList<>()).addAll(unusableLoadSlots);
			}
		}

		final Set<String> expectedVessels = Arrays.stream(vessels).filter(v -> Arrays.stream(v.cargoes).anyMatch(c -> !c.contract.fob)).map(v -> v.name).collect(Collectors.toSet());
		final Set<com.mmxlabs.models.lng.fleet.Vessel> eVessels = expectedVessels.stream()
				.map(name -> sm.getReferenceModel().getFleetModel().getVessels().stream().filter(v -> v.getName().equalsIgnoreCase(name)).findAny().get()).collect(Collectors.toSet());

		final Map<com.mmxlabs.models.lng.fleet.Vessel, List<DryDockEvent>> dryDockEvents = new HashMap<>();
		for (final VesselEvent event : sm.getCargoModel().getVesselEvents()) {
			if (event instanceof final DryDockEvent dryDockEvent) {
				if (dryDockEvent.getVesselAssignmentType() instanceof final VesselAvailability va) {
					final com.mmxlabs.models.lng.fleet.Vessel vessel = va.getVessel();
					if (eVessels.contains(vessel)) {
						dryDockEvents.computeIfAbsent(vessel, v -> new LinkedList<>()).add(dryDockEvent);
					}
				}
			} else {
				throw new IllegalStateException("Non dry dock events are not supported.");
			}
		}

		int totalTasks = 0;
		for (Vessel v : vessels) {
			totalTasks += v.cargoes.length;
		}

		// Get Dry dock events

		IntVar[] loadVarsStart = new IntVar[totalTasks];
		IntVar[] cargoEnd = new IntVar[totalTasks];

		HashMap<String, List<List<IntVar>>> contractToVesselLoadVars = new HashMap<>();

		HashMap<String, List<IntVar>> contractToLoadVars = new HashMap<>();
		HashMap<String, HashMap<String, List<IntVar>>> contractToVesselToLoadVars = new HashMap<>();

		HashMap<String, Contract> contracts = new HashMap<>();

		// New code
		final Set<ZonedDateTime> possibleLoadZdts = sm.getCargoModel().getLoadSlots().stream().filter(s -> !s.getWindowStart().isBefore(adpStart.atDay(1))).flatMap(s -> {
			final ZonedDateTime startZdtUtc = s.getSchedulingTimeWindow().getStart().withZoneSameInstant(utcZoneId);
			final ZonedDateTime endZdtUtc = s.getSchedulingTimeWindow().getEnd().withZoneSameInstant(utcZoneId);
			if (startZdtUtc.equals(endZdtUtc)) {
				return Collections.singleton(startZdtUtc).stream();
			} else {
				return Stream.iterate(startZdtUtc, zdt -> !zdt.isAfter(endZdtUtc), zdt -> zdt.plusHours(1L));
			}
		}).collect(Collectors.toCollection(TreeSet::new));
		final List<ZonedDateTime> utcSortedLoadDates = new ArrayList<>(possibleLoadZdts);
		final ZonedDateTime earliestLoadTime = utcSortedLoadDates.get(0);
		final ZonedDateTime latestLoadTime = utcSortedLoadDates.get(utcSortedLoadDates.size() - 1);

		final int earliestLoadTimeInt = getHourValue(earliestLoadTime, dateTimeZero);
		final int latestLoadTimeInt = getHourValue(latestLoadTime, dateTimeZero);

		final List<LoadSlot> sortedLoadSlots = sm.getCargoModel().getLoadSlots().stream().filter(s -> !s.getWindowStart().isBefore(adpStart.atDay(1)))
				.sorted((s1, s2) -> s1.getWindowStart().compareTo(s2.getWindowStart())).collect(Collectors.toList());

		int minLoadDuration = Integer.MAX_VALUE;
		int maxLoadDuration = -1;
		for (final LoadSlot loadSlot : sortedLoadSlots) {
			final int loadDuration = loadSlot.getSchedulingTimeWindow().getDuration();
			if (loadDuration < minLoadDuration) {
				minLoadDuration = loadDuration;
			}
			if (loadDuration > maxLoadDuration) {
				maxLoadDuration = loadDuration;
			}
		}

		final int numLoadSlots = sortedLoadSlots.size();
		final int numCargoes = Arrays.stream(vessels).mapToInt(v -> v.cargoes.length).sum();
		final IntVar[][] cargoToLoadSlotSelectionVars = new IntVar[numCargoes][numLoadSlots];
		final ZonedDateTime[][] loadSlotEndPoints = new ZonedDateTime[numLoadSlots][2];
		final Set<Port> loadSlotPorts = sortedLoadSlots.stream().map(Slot::getPort).collect(Collectors.toSet());

		final Iterator<LoadSlot> loadSlotIter = sortedLoadSlots.iterator();
		for (int i = 0; i < numLoadSlots; ++i) {
			final LoadSlot currentLoadSlot = loadSlotIter.next();
			final SchedulingTimeWindow schedulingTimeWindow = currentLoadSlot.getSchedulingTimeWindow();
			final ZonedDateTime startDateInclusive = schedulingTimeWindow.getStart().withZoneSameInstant(utcZoneId);
			final ZonedDateTime endDateExclusive = schedulingTimeWindow.getEnd().plusHours(1L).withZoneSameInstant(utcZoneId);
			loadSlotEndPoints[i][0] = startDateInclusive;
			loadSlotEndPoints[i][1] = endDateExclusive;
		}

		final int[][] loadTimePoints = new int[numLoadSlots][2];
		for (int i = 0; i < numLoadSlots; ++i) {
			loadTimePoints[i][0] = getHourValue(loadSlotEndPoints[i][0], dateTimeZero);
			loadTimePoints[i][1] = getHourValue(loadSlotEndPoints[i][1], dateTimeZero);
		}

		final Map<LoadSlot, Integer> loadIndices = new HashMap<>();
		{
			int loadIndex = 0;
			for (final LoadSlot loadSlot : sortedLoadSlots) {
				loadIndices.put(loadSlot, loadIndex);
				++loadIndex;
			}
		}

		final Cargo[] orderedCargoes = new Cargo[numCargoes];
		final Map<Contract, List<Integer>> contractConnectedCargoIndices = new HashMap<>();
		int index = 0;
		final Map<Cargo, Integer> cargoIndices = new HashMap<>();
		for (int idx = 0; idx < vessels.length; ++idx) {
			final Vessel v = vessels[idx];
			for (int i = 0; i < v.cargoes.length; ++i) {
				final Cargo c = v.cargoes[i];
				cargoIndices.put(c, index);
				for (int loadIndex = 0; loadIndex < numLoadSlots; ++loadIndex) {
					cargoToLoadSlotSelectionVars[index][loadIndex] = model.newBoolVar(String.format("cargo %d load %d", index, loadIndex));
				}
				orderedCargoes[index] = c;
				contractConnectedCargoIndices.computeIfAbsent(c.contract, con -> new ArrayList<>()).add(index);
				++index;
			}
		}

		final Map<Vessel, VesselAssignmentType> vesselToVatMap = new HashMap<>();
		for (final Cargo cargo : orderedCargoes) {
			if (cargo.contract.fob || vesselToVatMap.containsKey(cargo.vessel)) {
				continue;
			}
			final com.mmxlabs.models.lng.fleet.Vessel eVessel = vesselToEVesselMap.get(cargo.vessel);
			final Optional<VesselAvailability> optVa = ScenarioModelUtil.getCargoModel(sm).getVesselAvailabilities().stream().filter(va -> va.getVessel() == eVessel).findAny();
			if (optVa.isPresent()) {
				vesselToVatMap.put(cargo.vessel, optVa.get());
			} else {
				final Optional<CharterInMarket> optCim = ScenarioModelUtil.getSpotMarketsModel(sm).getCharterInMarkets().stream()
						.filter(cim -> cim.getName().equalsIgnoreCase(cargo.vessel.name.toLowerCase())).findAny();
				if (optCim.isPresent()) {
					vesselToVatMap.put(cargo.vessel, optCim.get());
				} else {
					throw new IllegalStateException(String.format("Could not find vessel availability or charter in market: %s", cargo.vessel));
				}
			}
		}

		final Map<Cargo, int[]> minMaxLadenTravelTimes = new HashMap<>();
		for (final Cargo cargo : orderedCargoes) {
			if (cargo.contract.fob) {
				continue;
			}
			int minTravelTime = Integer.MAX_VALUE;
			int maxTravelTime = Integer.MIN_VALUE;
			for (final Port fromPort : loadSlotPorts) {
				final String toPortName = cargo.contract.port == null ? defaultPortName : cargo.contract.port;
				final Optional<Port> optToPort = ScenarioModelUtil.findReferenceModel(sm).getPortModel().getPorts().stream().filter(p -> p.getName().equalsIgnoreCase(toPortName)).findAny();
				if (optToPort.isEmpty()) {
					throw new IllegalStateException(String.format("Unknown port: %s", toPortName));
				}
				final Port toPort = optToPort.get();
				final VesselAssignmentType vat = vesselToVatMap.get(cargo.vessel);
				final int travelTime = CargoTravelTimeUtils.getFobMinTimeInHours(fromPort, toPort, vat, ScenarioModelUtil.getPortModel(sm), 0, modelDistanceProvider, 0);
				if (travelTime < minTravelTime) {
					minTravelTime = travelTime;
				}
				if (travelTime > maxTravelTime) {
					maxTravelTime = travelTime;
				}
			}
			minMaxLadenTravelTimes.put(cargo, new int[] { minTravelTime, maxTravelTime });
		}

		final Map<Triple<Vessel, Port, LoadSlot>, Integer> ballastTravelTimes = new HashMap<>();
		final Map<Cargo, int[]> minMaxBallastTravelTimes = new HashMap<>();
		for (final Cargo cargo : orderedCargoes) {
			if (cargo.contract.fob) {
				continue;
			}
			int minTravelTime = Integer.MAX_VALUE;
			int maxTravelTime = Integer.MIN_VALUE;
			for (final LoadSlot toSlot : sortedLoadSlots) {
				// Minor enhancement skip first load slot
				final String fromPortName = cargo.contract.port == null ? defaultPortName : cargo.contract.port;
				final Optional<Port> optFromPort = ScenarioModelUtil.findReferenceModel(sm).getPortModel().getPorts().stream().filter(p -> p.getName().equalsIgnoreCase(fromPortName)).findAny();
				if (optFromPort.isEmpty()) {
					throw new IllegalStateException(String.format("Unknown port: %s", fromPortName));
				}
				final Port fromPort = optFromPort.get();
				final Triple<Vessel, Port, LoadSlot> ballastTravelKey = Triple.of(cargo.vessel, fromPort, toSlot);
				final VesselAssignmentType vat = vesselToVatMap.get(cargo.vessel);

				// Create temporary discharge slot with port set to match CargoTravelTimeUtils#getFobMinTimeInHours type signature
				final DischargeSlot tempDischargeSlot = CargoFactory.eINSTANCE.createDischargeSlot();
				tempDischargeSlot.setPort(fromPort);

				final int travelTime = CargoTravelTimeUtils.getFobMinTimeInHours(tempDischargeSlot, toSlot, FIRST_DATE, vat, ScenarioModelUtil.getPortModel(sm), 0, modelDistanceProvider);
				ballastTravelTimes.put(ballastTravelKey, travelTime);
				if (travelTime < minTravelTime) {
					minTravelTime = travelTime;
				}
				if (travelTime > maxTravelTime) {
					maxTravelTime = travelTime;
				}
			}
			minMaxBallastTravelTimes.put(cargo, new int[] { 0, maxTravelTime });
		}

		// Each load slot can be associated with at most one cargo
		for (int loadIndex = 0; loadIndex < numLoadSlots; ++loadIndex) {
			final IntVar[] cargoes = new IntVar[numCargoes];
			for (int cargoIndex = 0; cargoIndex < numCargoes; ++cargoIndex) {
				cargoes[cargoIndex] = cargoToLoadSlotSelectionVars[cargoIndex][loadIndex];
			}
			final LinearExpr cargoSum = LinearExpr.sum(cargoes);
			model.addLessOrEqual(cargoSum, 1L);
		}

		// Each cargo must be associated with a load slot
		for (int cargoIndex = 0; cargoIndex < numCargoes; ++cargoIndex) {
			final IntVar[] loads = Arrays.copyOf(cargoToLoadSlotSelectionVars[cargoIndex], numLoadSlots);
			final LinearExpr loadSum = LinearExpr.sum(loads);
			model.addEquality(loadSum, 1L);
		}

		// Load slots must be not be associated with contracts that are excluded from them, e.g. (1) part of fixed cargo
		for (int cargoIndex = 0; cargoIndex < numCargoes; ++cargoIndex) {
			final Cargo cargo = orderedCargoes[cargoIndex];
			final Contract contract = cargo.contract;
			final List<LoadSlot> loadSlotsToIgnore = usedLoadSlotsToIgnore.get(contract);
			if (loadSlotsToIgnore != null) {
				for (final LoadSlot slot : loadSlotsToIgnore) {
					final int loadIndex = loadIndices.get(slot);
					model.addEquality(cargoToLoadSlotSelectionVars[cargoIndex][loadIndex], 0);
				}
			}
		}

		// Load slots used by a contract must be associated with a cargo on that contract
		for (final Entry<Contract, List<LoadSlot>> entry : usedLoadSlots.entrySet()) {
			final List<Integer> localCargoIndices = contractConnectedCargoIndices.get(entry.getKey());
			if (localCargoIndices != null) {
				for (final LoadSlot slot : entry.getValue()) {
					final IntVar[] loads = new IntVar[localCargoIndices.size()];
					final int loadIndex = loadIndices.get(slot);
					int i = 0;
					for (final int cargoIndex : localCargoIndices) {
						loads[i] = cargoToLoadSlotSelectionVars[cargoIndex][loadIndex];
						++i;
					}
					final LinearExpr loadSum = LinearExpr.sum(loads);
					model.addEquality(loadSum, 1);
				}
			}
		}

		final Map<String, List<Triple<IntVar, IntervalVar, IntVar>>> dryDockIntervalVars = new HashMap<>();
		final Map<DryDockEvent, Triple<IntVar, IntervalVar, IntVar>> dryDockIntervalVarsMap = new HashMap<>();

		final Map<Vessel, List<IntervalVar>> dryDockIntervalVars2 = new HashMap<>();
		for (final Entry<com.mmxlabs.models.lng.fleet.Vessel, List<DryDockEvent>> entry : dryDockEvents.entrySet()) {
			for (final DryDockEvent event : entry.getValue()) {
				final int durationInHours = event.getDurationInDays() * 24;
				final ZonedDateTime startByZdt = event.getStartByAsDateTime().withZoneSameInstant(utcZoneId);
				final ZonedDateTime startAfterZdt = event.getStartAfterAsDateTime().withZoneSameInstant(utcZoneId);
				final int startByInt = getHourValue(startByZdt, dateTimeZero);
				final int startAfterInt = getHourValue(startAfterZdt, dateTimeZero);
				final IntVar dryDockStart = model.newIntVar(startAfterInt, startByInt, String.format("DD-%s-start", event.getName()));
				final IntVar dryDockEnd = model.newIntVar(startAfterInt + durationInHours, startByInt + durationInHours, String.format("DD-%s-end", event.getName()));
				final IntervalVar interval = model.newIntervalVar(dryDockStart, durationInHours, dryDockEnd, String.format("DD-%s-interval", event.getName()));
				final com.mmxlabs.models.lng.fleet.Vessel vessel = ((VesselAvailability) event.getVesselAssignmentType()).getVessel();
				final Triple<IntVar, IntervalVar, IntVar> triple = Triple.of(dryDockStart, interval, dryDockEnd);
				dryDockIntervalVars.computeIfAbsent(vessel.getName(), v -> new LinkedList<>()).add(triple);
				dryDockIntervalVarsMap.put(event, triple);
				dryDockIntervalVars2.computeIfAbsent(eVesselToVesselMap.get(vessel), v -> new LinkedList<>()).add(interval);
			}
		}

		final Set<Port> fobLoadPorts = sortedLoadSlots.stream().filter(s -> !s.isDESPurchase()).map(Slot::getPort).collect(Collectors.toSet());
		final List<LoadSlot> uniquePortFobLoadSlots = fobLoadPorts.stream().map(p -> sortedLoadSlots.stream().filter(s -> !s.isDESPurchase()).filter(s -> s.getPort() == p).findAny().get())
				.collect(Collectors.toList());
		final Map<String, Map<DryDockEvent, Map<Port, Integer>>> fromDryDockBallastTravelTimes = new HashMap<>();
		for (final Entry<com.mmxlabs.models.lng.fleet.Vessel, List<DryDockEvent>> entry : dryDockEvents.entrySet()) {
			final Map<DryDockEvent, Map<Port, Integer>> dryDockMap = fromDryDockBallastTravelTimes.computeIfAbsent(entry.getKey().getName().toLowerCase(), v -> new HashMap<>());
			for (final DryDockEvent dryDockEvent : entry.getValue()) {
				final Map<Port, Integer> portTimesMap = new HashMap<>();
				dryDockMap.put(dryDockEvent, portTimesMap);
				final Port dryDockPort = dryDockEvent.getPort();
				final LoadSlot ghostDryDockLoadSlot = CargoFactory.eINSTANCE.createLoadSlot();
				ghostDryDockLoadSlot.setPort(dryDockPort);
				for (final LoadSlot currentLoadSlot : uniquePortFobLoadSlots) {
					final int minTravelTime = CargoTravelTimeUtils.getFobMinTimeInHours(ghostDryDockLoadSlot, currentLoadSlot, FIRST_DATE, dryDockEvent.getVesselAssignmentType(),
							sm.getReferenceModel().getPortModel(), entry.getKey().getVesselOrDelegateMaxSpeed(), modelDistanceProvider);
					portTimesMap.put(currentLoadSlot.getPort(), minTravelTime);
				}
			}
		}

		final Map<String, Map<DryDockEvent, Map<String, Integer>>> toDryDockBallastTravelTimes = new HashMap<>();
		for (final Entry<com.mmxlabs.models.lng.fleet.Vessel, List<DryDockEvent>> entry : dryDockEvents.entrySet()) {
			final Vessel v = Arrays.stream(vessels).filter(vess -> vess.name.equalsIgnoreCase(entry.getKey().getName())).findAny().get();
			final List<Port> dischargePorts = Stream.concat(Stream.of(defaultPortName), Arrays.stream(v.cargoes).filter(cargo -> cargo.contract.port != null).map(cargo -> cargo.contract.port))
					.map(port -> sm.getReferenceModel().getPortModel().getPorts().stream().filter(p -> p.getName().equalsIgnoreCase(port)).findAny().get()).distinct().collect(Collectors.toList());
			final Map<DryDockEvent, Map<String, Integer>> dryDockMap = toDryDockBallastTravelTimes.computeIfAbsent(entry.getKey().getName().toLowerCase(), vess -> new HashMap<>());
			for (final DryDockEvent dryDockEvent : entry.getValue()) {
				final Map<String, Integer> portTimesMap = new HashMap<>();
				dryDockMap.put(dryDockEvent, portTimesMap);
				for (final Port currentPort : dischargePorts) {
					final LoadSlot ghostLoadSlot = CargoFactory.eINSTANCE.createLoadSlot();
					final DischargeSlot ghostDryDockSlot = CargoFactory.eINSTANCE.createDischargeSlot();
					ghostLoadSlot.setPort(currentPort);
					ghostDryDockSlot.setPort(dryDockEvent.getPort());
					final int minTravelTime = CargoTravelTimeUtils.getFobMinTimeInHours(ghostLoadSlot, ghostDryDockSlot, FIRST_DATE, dryDockEvent.getVesselAssignmentType(),
							sm.getReferenceModel().getPortModel(), entry.getKey().getVesselOrDelegateMaxSpeed(), modelDistanceProvider);
					portTimesMap.put(currentPort.getName().toLowerCase(), minTravelTime);
				}
			}
		}
		for (final Cargo cargo : orderedCargoes) {
			if (cargo.contract.fob) {
				continue;
			}
			final Map<DryDockEvent, Map<String, Integer>> dryDockMap = toDryDockBallastTravelTimes.get(cargo.vessel.name.toLowerCase());
			if (dryDockMap == null) {
				continue;
			}
			final int[] minMaxTravelTime = minMaxBallastTravelTimes.get(cargo);
			final String portName = cargo.contract.port == null ? defaultPortName.toLowerCase() : cargo.contract.port.toLowerCase();
			for (final Map<String, Integer> portTimesMap : dryDockMap.values()) {
				final int travelTime = portTimesMap.get(portName);
				if (travelTime < minMaxTravelTime[0]) {
					minMaxTravelTime[0] = travelTime;
				}
				if (travelTime > minMaxTravelTime[1]) {
					minMaxTravelTime[1] = travelTime;
				}
			}

		}

		final Map<DryDockEvent, Map<Cargo, IntVar>> dryDockCargoSelectionVars = new HashMap<>();
		final Map<Cargo, ShippedCargoModellingContainer> shippedCargoVars = new HashMap<>();
		int taskId = 0;
		IntVar[][] timesVar = new IntVar[vessels.length][];
		for (int idx = 0; idx < vessels.length; ++idx) {
			Vessel v = vessels[idx];

			// Cargo start day
			IntVar[] vesselVarsStart = new IntVar[v.cargoes.length];
			// Cargo end day
			IntVar[] vesselVarsEnd = new IntVar[v.cargoes.length];
			// Interval variable bounded by corresponding vesselVarsStart and vesselVarsEnd variables
			IntervalVar[] vesselVarsInterval = new IntervalVar[v.cargoes.length];

			final List<IntervalVar> nonOverlappingCargoIntervals = new ArrayList<>();
			dryDockIntervalVars2.getOrDefault(v, Collections.emptyList()).forEach(nonOverlappingCargoIntervals::add);

			final ShippedCargoModellingContainer[] shippedCargoModellingVariables = new ShippedCargoModellingContainer[v.cargoes.length];
			final Map<com.mmxlabs.models.lng.cargo.Cargo, ShippedCargoModellingContainer> shippedFixedCargoDetails = new HashMap<>();

			// The list that is added to timesVar
			List<IntVar> vesselLoadVars = new ArrayList<>();

			// for each cargo attached to vessel v
			for (int i = 0; i < v.cargoes.length; ++i) {
				Cargo c = v.cargoes[i];

				contracts.put(c.contract.name, c.contract);
				HashMap<String, List<IntVar>> vesselToLoadVars = contractToVesselToLoadVars.computeIfAbsent(c.contract.name, k -> new HashMap<>());
				List<IntVar> contractVesselLoadVars = vesselToLoadVars.computeIfAbsent(v.name, k -> new ArrayList<>());
				List<List<IntVar>> contractLoadVarsPerVessel = contractToVesselLoadVars.computeIfAbsent(c.contract.name, k -> new ArrayList<>());
				List<IntVar> contractLoadVars = contractToLoadVars.computeIfAbsent(c.contract.name, k -> new ArrayList<>());

				if (!contractLoadVarsPerVessel.contains(vesselLoadVars)) {
					contractLoadVarsPerVessel.add(vesselLoadVars);
				}

				vesselVarsStart[i] = createLoadVar(model, v, c.contract, earliestLoadTimeInt, latestLoadTimeInt, i);
				vesselVarsEnd[i] = model.newIntVar(earliestLoadTimeInt, latestLoadTimeInt + c.contract.interval * 24, v.name + "-" + c.contract.name + "-end-" + i);
				// Cargo spacing per vessel.
				vesselVarsInterval[i] = model.newIntervalVar(vesselVarsStart[i], c.contract.interval * 24, vesselVarsEnd[i], v.name + "-interval-" + i);

				// Each cargo load start
				for (int loadIndex = 0; loadIndex < numLoadSlots; ++loadIndex) {
					model.addLinearConstraint(vesselVarsStart[i], loadTimePoints[loadIndex][0], loadTimePoints[loadIndex][1] - 1)
							.onlyEnforceIf(cargoToLoadSlotSelectionVars[cargoIndices.get(c)][loadIndex]);
				}

				if (c.contract.fob) {
					// Shipped cargoes use a different load var
					loadVarsStart[taskId] = vesselVarsStart[i];
					final List<LoadSlot> localUsedLoadSlots = usedLoadSlots.get(c.contract);
					if (localUsedLoadSlots != null) {
						for (final LoadSlot eLoadslot : localUsedLoadSlots) {
							final DischargeSlot eDischargeSlot = (DischargeSlot) eLoadslot.getCargo().getSlots().get(1);
							final ZonedDateTime windowStart = eDischargeSlot.getSchedulingTimeWindow().getStart().withZoneSameInstant(utcZoneId);
							final ZonedDateTime windowEnd = eDischargeSlot.getSchedulingTimeWindow().getEnd().withZoneSameInstant(utcZoneId);

							final int windowStartInt = getHourValue(windowStart, dateTimeZero);
							final int windowEndInt = getHourValue(windowEnd, dateTimeZero);
							final int cargoIndex = cargoIndices.get(c);
							final int loadIndex = loadIndices.get(eLoadslot);
							// Restrict load times if paired with used load slot
							model.addLinearConstraint(vesselVarsStart[i], windowStartInt, windowEndInt).onlyEnforceIf(cargoToLoadSlotSelectionVars[cargoIndex][loadIndex]);
						}
					}
				} else {
					final String dischargePortName = c.contract.port == null ? defaultPortName : c.contract.port;
					final Port dischargeEPort = ScenarioModelUtil.findReferenceModel(sm).getPortModel().getPorts().stream().filter(p -> p.getName().equalsIgnoreCase(dischargePortName)).findAny()
							.get();
					final int dischargeDuration = dischargeEPort.getDischargeDuration();
					@NonNull
					final ShippedCargoModellingContainer cargoModelVariables = buildShippedCargoModellingVariables(model, earliestLoadTimeInt, latestLoadTimeInt, v, c, i, minLoadDuration,
							maxLoadDuration, minMaxLadenTravelTimes.get(c), minMaxBallastTravelTimes.get(c), dischargeDuration, dateTimeEndInt);
					shippedCargoVars.put(c, cargoModelVariables);

					if (c.minDischargeDate != null) {
						final ZonedDateTime minDischargeZdtPortTz = c.minDischargeDate.atStartOfDay(dischargeEPort.getZoneId());
						final ZonedDateTime minDischargeZdtUtc = minDischargeZdtPortTz.withZoneSameInstant(utcZoneId);
						final int minDischargeDateInt = getHourValue(minDischargeZdtUtc, dateTimeZero);
						model.addGreaterOrEqual(cargoModelVariables.getDischargePortVariables().getStart(), minDischargeDateInt);
					}
					if (c.maxDischargeDate != null) {
						final ZonedDateTime maxDischargeZdtPortTz = c.maxDischargeDate.atStartOfDay(dischargeEPort.getZoneId());
						final ZonedDateTime maxDischargeZdtUtc = maxDischargeZdtPortTz.withZoneSameInstant(utcZoneId);
						final int maxDischargeDateInt = getHourValue(maxDischargeZdtUtc, dateTimeZero);
						model.addLessOrEqual(cargoModelVariables.getDischargePortVariables().getEnd(), maxDischargeDateInt);
					}

					loadVarsStart[taskId] = cargoModelVariables.getLoadPortVariables().getStart();

					// If paired with load slot, load should be within window
					for (int loadIndex = 0; loadIndex < numLoadSlots; ++loadIndex) {
						model.addLinearConstraint(cargoModelVariables.getLoadPortVariables().getStart(), loadTimePoints[loadIndex][0], loadTimePoints[loadIndex][1] - 1)
								.onlyEnforceIf(cargoToLoadSlotSelectionVars[cargoIndices.get(c)][loadIndex]);
					}

					shippedCargoModellingVariables[i] = cargoModelVariables;
					nonOverlappingCargoIntervals.add(cargoModelVariables.getCargoInterval());

					// Current cargo laden should end before discharge starts
					model.addLessOrEqual(cargoModelVariables.getLadenTravelVariables().getEnd(), cargoModelVariables.getDischargePortVariables().getStart());
					// Allow at most 3 days laden idle
					final LinearExpr ladenMaxIdleLinearExpr = LinearExpr
							.scalProd(new IntVar[] { cargoModelVariables.getDischargePortVariables().getStart(), cargoModelVariables.getLadenTravelVariables().getEnd() }, new int[] { 1, -1 });
					model.addLessOrEqual(ladenMaxIdleLinearExpr, 72);

					// If assigned to fixed cargo
					final List<LoadSlot> localUsedLoadSlots = usedLoadSlots.get(c.contract);
					if (localUsedLoadSlots != null) {
						for (final LoadSlot loadSlot : localUsedLoadSlots) {
							final int cargoIndex = cargoIndices.get(c);
							final int loadIndex = loadIndices.get(loadSlot);
							final DischargeSlot dischargeSlot = (DischargeSlot) loadSlot.getCargo().getSlots().get(1);
							final IntVar cargoLoadSelectionVar = cargoToLoadSlotSelectionVars[cargoIndex][loadIndex];
							// Work out load limits
							final int ladenTravelTime = CargoTravelTimeUtils.getFobMinTimeInHours(loadSlot, dischargeSlot, FIRST_DATE, vesselToVatMap.get(v), ScenarioModelUtil.getPortModel(sm), 0,
									modelDistanceProvider);
							// TODO: No need to add constraint if ladenTravelTime is less than or equal to lower laden travel duration bound
							model.addGreaterOrEqual(cargoModelVariables.getLadenTravelVariables().getDurationVar(), ladenTravelTime).onlyEnforceIf(cargoLoadSelectionVar);

							final ZonedDateTime earliestLoadStartUtc = loadSlot.getSchedulingTimeWindow().getStart().withZoneSameInstant(utcZoneId);
							final ZonedDateTime dischargeEndZdtUtc = dischargeSlot.getSchedulingTimeWindow().getEnd().withZoneSameInstant(utcZoneId);
							final ZonedDateTime latestLoadUtc = dischargeEndZdtUtc.minusHours(ladenTravelTime);
							final ZonedDateTime latestLoadStwStart = loadSlot.getSchedulingTimeWindow().getEnd().withZoneSameInstant(utcZoneId);
							final ZonedDateTime earliestDischargeStart = earliestLoadStartUtc.plusHours(loadSlot.getSchedulingTimeWindow().getDuration() + ladenTravelTime);
							final ZonedDateTime earliestDischargeStwStart = dischargeSlot.getSchedulingTimeWindow().getStart().withZoneSameInstant(utcZoneId);
							final ZonedDateTime actualLatestLoadStart;
							if (latestLoadUtc.isAfter(latestLoadStwStart)) {
								actualLatestLoadStart = latestLoadStwStart;
							} else {
								actualLatestLoadStart = latestLoadUtc;
							}
							final ZonedDateTime actualEarliestDischargeStart;
							if (earliestDischargeStart.isBefore(earliestDischargeStwStart)) {
								actualEarliestDischargeStart = earliestDischargeStwStart;
							} else {
								actualEarliestDischargeStart = earliestDischargeStart;
							}
							final int latestLoadInt = getHourValue(actualLatestLoadStart, dateTimeZero);
							final int earliestDischargeInt = getHourValue(actualEarliestDischargeStart, dateTimeZero);
							model.addLessOrEqual(cargoModelVariables.getLoadPortVariables().getEnd(), latestLoadInt).onlyEnforceIf(cargoLoadSelectionVar);
							model.addGreaterOrEqual(cargoModelVariables.getDischargePortVariables().getStart(), earliestDischargeInt).onlyEnforceIf(cargoLoadSelectionVar);
						}
					}
					// Sequencing around other cargoes
					final List<com.mmxlabs.models.lng.cargo.Cargo> fixedCargoes = cargoVessels.get(v);
					if (fixedCargoes != null) {
						for (final com.mmxlabs.models.lng.cargo.Cargo fixedCargo : fixedCargoes) {
							final LoadSlot fixedLoadSlot = (LoadSlot) fixedCargo.getSlots().get(0);
							final DischargeSlot fixedDischargeSlot = (DischargeSlot) fixedCargo.getSlots().get(1);
							final VesselAssignmentType vat = fixedCargo.getVesselAssignmentType();
							ShippedCargoModellingContainer fixedCargoModellingVariables = shippedFixedCargoDetails.get(fixedCargo);
							if (fixedCargoModellingVariables == null) {
								final int fastestFixedCargoLadenTime = CargoTravelTimeUtils.getFobMinTimeInHours(fixedLoadSlot, fixedDischargeSlot, FIRST_DATE, vat,
										sm.getReferenceModel().getPortModel(), ((VesselAvailability) vat).getVessel().getVesselOrDelegateMaxSpeed(), modelDistanceProvider);
								final int[] minMaxLadenTravel = new int[] { fastestFixedCargoLadenTime, fastestFixedCargoLadenTime };
								fixedCargoModellingVariables = buildFixedCargoShippedCargoModellingVariables(model, earliestLoadTimeInt, latestLoadTimeInt, v, fixedLoadSlot.getName(),
										fixedDischargeSlot.getName(), fixedLoadSlot.getSchedulingTimeWindow().getDuration(), fixedLoadSlot.getSchedulingTimeWindow().getDuration(), minMaxLadenTravel,
										minMaxBallastTravelTimes.get(c), fixedDischargeSlot.getSchedulingTimeWindow().getDuration(), dateTimeEndInt);
								model.addLessOrEqual(fixedCargoModellingVariables.getLadenTravelVariables().getEnd(), fixedCargoModellingVariables.getDischargePortVariables().getStart());

								final ZonedDateTime loadStart = fixedLoadSlot.getSchedulingTimeWindow().getStart().withZoneSameInstant(utcZoneId);
								final ZonedDateTime loadEnd = fixedLoadSlot.getSchedulingTimeWindow().getEnd().withZoneSameInstant(utcZoneId);
								model.addGreaterOrEqual(fixedCargoModellingVariables.getLoadPortVariables().getStart(), getHourValue(loadStart, dateTimeZero));
								model.addLessOrEqual(fixedCargoModellingVariables.getLoadPortVariables().getStart(), getHourValue(loadEnd, dateTimeZero));

								final ZonedDateTime dischargeStart = fixedDischargeSlot.getSchedulingTimeWindow().getStart().withZoneSameInstant(utcZoneId);
								final ZonedDateTime dischargeEnd = fixedDischargeSlot.getSchedulingTimeWindow().getEnd().withZoneSameInstant(utcZoneId);
								model.addGreaterOrEqual(fixedCargoModellingVariables.getDischargePortVariables().getStart(), getHourValue(dischargeStart, dateTimeZero));
								model.addLessOrEqual(fixedCargoModellingVariables.getDischargePortVariables().getStart(), getHourValue(dischargeEnd, dateTimeZero));

								// nonOverlappingCargoIntervals.add(fixedCargoModellingVariables.getCargoInterval());
							}

							// Create cargo sequencing binary
							final String selectionVarName = String.format("FC-%s-%s-isAfter-%s-%d", fixedLoadSlot.getName(), fixedDischargeSlot.getName(), v.name, i);
							final IntVar fixedCargoIsAfterSelection = model.newBoolVar(selectionVarName);
							// Fixed cargo is after this cargo
							final ZonedDateTime fixedLoadLatestStart = fixedLoadSlot.getSchedulingTimeWindow().getEnd();
							final DischargeSlot ghostDischargeSlot = CargoFactory.eINSTANCE.createDischargeSlot();
							// final Port dischargePort = sm.getReferenceModel().getPortModel().getPorts().stream().filter(p -> p.getName().equalsIgnoreCase(dischargePortName)).findAny().get();
							ghostDischargeSlot.setPort(dischargeEPort);
							final int fastestBallastTravelTime = CargoTravelTimeUtils.getFobMinTimeInHours(ghostDischargeSlot, fixedLoadSlot, FIRST_DATE, vat, sm.getReferenceModel().getPortModel(),
									((VesselAvailability) vat).getVessel().getVesselOrDelegateMaxSpeed(), modelDistanceProvider);
							final LinearExpr linearExpr = LinearExpr.scalProd(
									new IntVar[] { cargoModelVariables.getDischargePortVariables().getEnd(), fixedCargoModellingVariables.getLoadPortVariables().getStart() }, new int[] { 1, -1 });
							model.addLessOrEqual(linearExpr, -fastestBallastTravelTime).onlyEnforceIf(fixedCargoIsAfterSelection);

							for (final LoadSlot potentialLoadSlot : sortedLoadSlots) {
								final int fastestBallastToReachTravelTime = CargoTravelTimeUtils.getFobMinTimeInHours(fixedDischargeSlot, potentialLoadSlot, FIRST_DATE, vat,
										sm.getReferenceModel().getPortModel(), ((VesselAvailability) vat).getVessel().getVesselOrDelegateMaxSpeed(), modelDistanceProvider);

								final IntVar cargoLoadSlotSelection = cargoToLoadSlotSelectionVars[cargoIndices.get(c)][loadIndices.get(potentialLoadSlot)];
								final LinearExpr linearExpr2 = LinearExpr.scalProd(
										new IntVar[] { fixedCargoModellingVariables.getDischargePortVariables().getEnd(), cargoModelVariables.getLoadPortVariables().getStart() }, new int[] { 1, -1 });
								model.addLessOrEqual(linearExpr2, -fastestBallastToReachTravelTime).onlyEnforceIf(new Literal[] { cargoLoadSlotSelection, fixedCargoIsAfterSelection.not() });
							}
						}
					}
					final Map<DryDockEvent, Map<Port, Integer>> map = fromDryDockBallastTravelTimes.get(v.name.toLowerCase());
					if (map != null) {

						final Map<DryDockEvent, IntVar> dryDockSelectionVars = new HashMap<>();
						for (final Entry<DryDockEvent, Map<Port, Integer>> entry : map.entrySet()) {
							final DryDockEvent dryDockEvent = entry.getKey();

							final String selectionVarName = String.format("DD-%s-isBefore-%s-%d", dryDockEvent.getName(), v.name, i);
							final IntVar drydockBeforeCargoSelection = model.newBoolVar(selectionVarName);
							dryDockSelectionVars.put(dryDockEvent, drydockBeforeCargoSelection);
							dryDockCargoSelectionVars.computeIfAbsent(dryDockEvent, k -> new HashMap<>()).put(c, drydockBeforeCargoSelection);

							final IntVar dryDockEnd = dryDockIntervalVarsMap.get(dryDockEvent).getThird();
							for (int loadIndex = 0; loadIndex < numLoadSlots; ++loadIndex) {
								final Port loadPort = sortedLoadSlots.get(loadIndex).getPort();
								final IntVar cargoLoadSlotSelection = cargoToLoadSlotSelectionVars[cargoIndices.get(c)][loadIndex];
								final int travelTime = entry.getValue().get(loadPort);
								final LinearExpr linearExpr = LinearExpr.scalProd(new IntVar[] { dryDockEnd, cargoModelVariables.getLoadPortVariables().getStart() }, new int[] { 1, -1 });
								model.addLessOrEqual(linearExpr, -travelTime).onlyEnforceIf(new Literal[] { drydockBeforeCargoSelection, cargoLoadSlotSelection });

							}
						}
						final Map<DryDockEvent, Map<String, Integer>> map2 = toDryDockBallastTravelTimes.get(v.name.toLowerCase());
						for (final Entry<DryDockEvent, Map<String, Integer>> entry : map2.entrySet()) {
							final DryDockEvent dryDockEvent = entry.getKey();
							final IntVar beforeAfterSelection = dryDockSelectionVars.get(dryDockEvent);
							if (beforeAfterSelection == null) {
								throw new IllegalArgumentException("Sequencing selector not initialised.");
							}
							final IntVar dryDockStart = dryDockIntervalVarsMap.get(dryDockEvent).getFirst();
							String dischargePort = c.contract.port;
							if (dischargePort == null) {
								dischargePort = defaultPortName;
							}
							final int travelTime = entry.getValue().get(dischargePort.toLowerCase());
							final LinearExpr linearExpr = LinearExpr.scalProd(new IntVar[] { cargoModelVariables.getDischargePortVariables().getEnd(), dryDockStart }, new int[] { 1, -1 });
							model.addLessOrEqual(linearExpr, -travelTime).onlyEnforceIf(beforeAfterSelection.not());
						}
					}

					if (i > 0) {
						final ShippedCargoModellingContainer previousCargoModelVariables = shippedCargoModellingVariables[i - 1];
						// previous ballast should end before current cargo load starts
						model.addLessOrEqual(previousCargoModelVariables.getBallastTravelVariables().getEnd(), cargoModelVariables.getLoadPortVariables().getStart());
						// Constrain previous ballast time
						final Cargo previousCargo = v.cargoes[i - 1];
						final String previousDischargePortName = previousCargo.contract.port != null ? previousCargo.contract.port : defaultPortName;
						final Optional<Port> optFromPort = ScenarioModelUtil.findReferenceModel(sm).getPortModel().getPorts().stream()
								.filter(p -> p.getName().equalsIgnoreCase(previousDischargePortName)).findAny();
						if (optFromPort.isEmpty()) {
							throw new IllegalStateException(String.format("Unknown port: %s", previousDischargePortName));
						}
						final Port fromPort = optFromPort.get();
						for (final LoadSlot eLoadSlot : sortedLoadSlots) {
							final int ballastTravelTime = ballastTravelTimes.get(Triple.of(v, fromPort, eLoadSlot));
							final IntVar cargoLoadSelection = cargoToLoadSlotSelectionVars[cargoIndices.get(c)][loadIndices.get(eLoadSlot)];
							final LinearExpr linearExpr = LinearExpr.scalProd(
									new IntVar[] { previousCargoModelVariables.getDischargePortVariables().getEnd(), shippedCargoModellingVariables[i].getLoadPortVariables().getStart() },
									new int[] { 1, -1 });
							model.addLessOrEqual(linearExpr, -ballastTravelTime).onlyEnforceIf(cargoLoadSelection);
						}
					} else {
						final Pair<@NonNull ZonedDateTime, ZonedDateTime> zdtPair = latestDatePairs.get(c.contract);
						if (zdtPair != null) {
							final ZonedDateTime earliestDate = zdtPair.getFirst();
							final int latestLoadDateValue = getHourValue(earliestDate, dateTimeZero);
							model.addGreaterOrEqual(cargoModelVariables.getLoadPortVariables().getStart(), latestLoadDateValue);
						}
					}
				}

				// Ensure that any previous cargo starts (and ends) before this one.
				if (i > 0) {
					model.addLessOrEqual(vesselVarsStart[i - 1], vesselVarsStart[i]);
					model.addLessOrEqual(vesselVarsEnd[i - 1], vesselVarsEnd[i]);
					model.addLessOrEqual(vesselVarsEnd[i - 1], vesselVarsStart[i]);

					if (c.contract.maxInterval != Integer.MAX_VALUE) {
						// left + offset >= right.
						model.addGreaterOrEqualWithOffset(vesselVarsStart[i - 1], vesselVarsStart[i], c.contract.maxInterval * 24);
					}
				} else {
					final Pair<@NonNull ZonedDateTime, ZonedDateTime> zdtPair = latestDatePairs.get(c.contract);
					if (zdtPair != null) {
						final ZonedDateTime earliestDate = zdtPair.getFirst();
						final int latestLoadDateValue = getHourValue(earliestDate, dateTimeZero);
						if (c.contract.fob) {
							model.addGreaterOrEqual(vesselVarsStart[i], latestLoadDateValue + c.contract.interval * 24);
							if (c.contract.maxInterval != Integer.MAX_VALUE) {
								model.addLessOrEqual(vesselVarsStart[i], latestLoadDateValue + c.contract.maxInterval * 24);
							}
						} else {
							model.addGreaterOrEqual(vesselVarsStart[i], latestLoadDateValue);
						}
					}
				}

				contractVesselLoadVars.add(vesselVarsStart[i]);
				vesselLoadVars.add(vesselVarsStart[i]);
				contractLoadVars.add(vesselVarsStart[i]);

				cargoEnd[taskId] = vesselVarsEnd[i];

				taskId++;
			}

			model.addNoOverlap(nonOverlappingCargoIntervals.toArray(new IntervalVar[0]));

			// No overlapping cargoes on the vessel
			model.addNoOverlap(vesselVarsInterval);

			timesVar[idx] = vesselVarsStart;
		}

		// All load variables must take different start dates.
		model.addAllDifferent(loadVarsStart);

		IntVar lastLoad = model.newIntVar(0, dateTimeEndInt, "lastLoad");
		// IntVar firstLoad = model.newIntVar(0, latestLoadTimeInt, "firstLoad");

		// model.addMaxEquality(lastLoad, loadVarsStart);
		// model.addMinEquality(firstLoad, loadVarsStart);

		// Objective is to minimise (lastLoadDay - firstLoadDay).
		// ScalProd objVar = new ScalProd(new IntVar[] { lastLoad, firstLoad }, new long[] { 1, -1 });
		ScalProd objVar = new ScalProd(new IntVar[] { lastLoad }, new long[] { 1 });
		model.minimize(objVar);

		CpSolver solver = new CpSolver();

		final String validate = model.validate();

		double maxTime = 10.0;
		CpSolverStatus solve;

		// Setting hard solve time of maxTime
		solver.getParameters().setMaxTimeInSeconds(maxTime);
		solve = solver.solve(model);

		if (solve == CpSolverStatus.FEASIBLE || solve == CpSolverStatus.OPTIMAL) {
			// System.out.println("Number of days between first load and last load = " + (solver.value(lastLoad) - solver.value(firstLoad)));
		} else {
			if (solve == CpSolverStatus.MODEL_INVALID) {
				// No solution so no point in trying to write out to .csv file.
				// System.out.println("Model was invalid");
				return new InvalidModel();
			} else if (solve == CpSolverStatus.INFEASIBLE) {
				// System.out.println("Model proven infeasible");
				return new Infeasible();
			} else if (solve == CpSolverStatus.UNKNOWN) {
				// System.out.println("Did not find a solution but did not prove infeasibility");
				return new Unknown();
			} else if (solve == CpSolverStatus.UNRECOGNIZED) {
				// System.out.println("Got unrecognised. Do not really know how to interpret this.");
				return new Unrecognised();
			}
			return null;
		}

		final long[][] cargoLoadSlotAssignments = new long[numCargoes][numLoadSlots];
		for (int cargoIndex = 0; cargoIndex < numCargoes; ++cargoIndex) {
			for (int loadIndex = 0; loadIndex < numLoadSlots; ++loadIndex) {
				cargoLoadSlotAssignments[cargoIndex][loadIndex] = solver.value(cargoToLoadSlotSelectionVars[cargoIndex][loadIndex]);
			}
		}
		final Map<Integer, List<Integer>> cargoAssignments = new HashMap<>();
		for (int cargoIndex = 0; cargoIndex < numCargoes; ++cargoIndex) {
			final List<Integer> list = new ArrayList<>();
			for (int loadIndex = 0; loadIndex < numLoadSlots; ++loadIndex) {
				if (cargoLoadSlotAssignments[cargoIndex][loadIndex] == 1L) {
					list.add(loadIndex);
				}
			}
			cargoAssignments.put(cargoIndex, list);
		}

		final Map<Cargo, LoadSlot> cargoToLoadSlotMap = new HashMap<>();
		for (int cargoIndex = 0; cargoIndex < numCargoes; ++cargoIndex) {
			int loadIndex = 0;
			for (loadIndex = 0; loadIndex < numLoadSlots; ++loadIndex) {
				if (cargoLoadSlotAssignments[cargoIndex][loadIndex] == 1L) {
					break;
				}
			}
			cargoToLoadSlotMap.put(orderedCargoes[cargoIndex], sortedLoadSlots.get(loadIndex));
		}

		// dumpSolutionToYaml(vessels, timesVar, latestLoadDates, solver);
		final Command modelPopulationCommand = createModelPopulationCommands(vessels, timesVar, solver, sm, ed, cargoToLoadSlotMap, usedLoadSlotsSet, dateTimeZero, defaultPortName, sdp);

		if (solve == CpSolverStatus.OPTIMAL) {
			return new Optimal(modelPopulationCommand);
		} else {
			return new Feasible(modelPopulationCommand);
		}
	}

	@NonNull
	private ShippedCargoModellingContainer buildFixedCargoShippedCargoModellingVariables(@NonNull final CpModel model, final int earliestLoadTimeInt, final int latestLoadTimeInt,
			@NonNull final Vessel v, @NonNull String loadId, @NonNull String dischargeId, final int minLoadDuration, final int maxLoadDuration, final int[] minMaxLadenTravel,
			final int[] minMaxBallastTravel, final int dischargeDurationInt, final int lastDateTime) {
		final ContainerBuilder builder = ShippedCargoModellingContainer.startBuilding();
		final IntVar loadStart = model.newIntVar(earliestLoadTimeInt, lastDateTime, v.name + "-load-start-" + loadId + "-" + dischargeId);
		final IntVar loadEnd = model.newIntVar(earliestLoadTimeInt, lastDateTime, v.name + "-load-end-" + loadId + "-" + dischargeId);
		final IntVar loadDuration;
		if (minLoadDuration != maxLoadDuration) {
			loadDuration = model.newIntVar(minLoadDuration, maxLoadDuration, v.name + "-load-duration-" + loadId + "-" + dischargeId);
		} else {
			loadDuration = model.newConstant(minLoadDuration);
		}
		final IntervalVar loadInterval = model.newIntervalVar(loadStart, loadDuration, loadEnd, v.name + "-load-interval-" + loadId + "-" + dischargeId);

		builder.withLoadStart(loadStart) //
				.withLoadEnd(loadEnd) //
				.withLoadDuration(loadDuration)//
				.withLoadInterval(loadInterval);

		// Laden variables
		final IntVar ladenEnd = model.newIntVar(earliestLoadTimeInt + minMaxLadenTravel[1], lastDateTime, v.name + "-laden-end-" + loadId + "-" + dischargeId);
		final IntVar ladenMinDuration;
		if (minMaxLadenTravel[0] != minMaxLadenTravel[1]) {
			ladenMinDuration = model.newIntVar(minMaxLadenTravel[0], minMaxLadenTravel[1], v.name + "-laden-duration-" + loadId + "-" + dischargeId);
		} else {
			ladenMinDuration = model.newConstant(minMaxLadenTravel[0]);
		}
		final IntervalVar ladenInterval = model.newIntervalVar(loadEnd, ladenMinDuration, ladenEnd, v.name + "-laden-interval-" + loadId + "-" + dischargeId);

		builder.withLadenEnd(ladenEnd) //
				.withLadenDuration(ladenMinDuration) //
				.withLadenInterval(ladenInterval);

		// Discharge variables
		final IntVar dischargeStart = model.newIntVar(earliestLoadTimeInt, lastDateTime, v.name + "-discharge-start-" + loadId + "-" + dischargeId);
		final IntVar dischargeEnd = model.newIntVar(earliestLoadTimeInt + dischargeDurationInt, lastDateTime, v.name + "-discharge-end-" + loadId + "-" + dischargeId);
		final IntVar dischargeDuration = model.newConstant(dischargeDurationInt);
		final IntervalVar dischargeInterval = model.newIntervalVar(dischargeStart, dischargeDuration, dischargeEnd, v.name + "-discharge-interval-" + loadId + "-" + dischargeId);

		builder.withDischargeStart(dischargeStart) //
				.withDischargeEnd(dischargeEnd) //
				.withDischargeDuration(dischargeDuration) //
				.withDischargeInterval(dischargeInterval);

		// Ballast variables
		final IntVar ballastEnd = model.newIntVar(earliestLoadTimeInt + minMaxLadenTravel[1], lastDateTime, v.name + "-ballast-duration-" + loadId + "-" + dischargeId);
		final IntVar ballastMinDuration;
		final IntervalVar ballastInterval;
		if (minMaxBallastTravel[0] != minMaxBallastTravel[1]) {
			ballastMinDuration = model.newIntVar(minMaxBallastTravel[0], minMaxBallastTravel[1], v.name + "-ballast-duration-" + loadId + "-" + dischargeId);
		} else {
			ballastMinDuration = model.newConstant(minMaxBallastTravel[0]);
		}
		ballastInterval = model.newIntervalVar(dischargeEnd, ballastMinDuration, ballastEnd, v.name + "-ballast-interval-" + loadId + "-" + dischargeId);

		builder.withBallastEnd(ballastEnd) //
				.withBallastDuration(ballastMinDuration) //
				.withBallastInterval(ballastInterval);

		// TODO: Get tighter bounds on trip duration
		final IntVar cargoTripDuration = model.newIntVar(0, lastDateTime, v.name + "-cargo-trip-duration" + loadId + "-" + dischargeId);
		final IntervalVar cargoInterval = model.newIntervalVar(loadStart, cargoTripDuration, ballastEnd, v.name + "-cargo-trip-interval" + loadId + "-" + dischargeId);

		builder.withCargoInterval(cargoInterval);

		return builder.build();
	}

	@NonNull
	private ShippedCargoModellingContainer buildShippedCargoModellingVariables(@NonNull final CpModel model, final int earliestLoadTimeInt, final int latestLoadTimeInt, @NonNull final Vessel v,
			@NonNull final Cargo c, final int i, final int minLoadDuration, final int maxLoadDuration, final int[] minMaxLadenTravel, final int[] minMaxBallastTravel, final int dischargeDurationInt,
			final int lastDateTime) {

		final ContainerBuilder builder = ShippedCargoModellingContainer.startBuilding();
		final IntVar loadStart = model.newIntVar(earliestLoadTimeInt, lastDateTime, v.name + "-load-start-" + c.contract.name + "-" + i);
		final IntVar loadEnd = model.newIntVar(earliestLoadTimeInt, lastDateTime, v.name + "-load-end-" + c.contract.name + "-" + i);
		final IntVar loadDuration;
		if (minLoadDuration != maxLoadDuration) {
			loadDuration = model.newIntVar(minLoadDuration, maxLoadDuration, v.name + "-load-duration-" + c.contract.name + "-" + i);
		} else {
			loadDuration = model.newConstant(minLoadDuration);
		}
		final IntervalVar loadInterval = model.newIntervalVar(loadStart, loadDuration, loadEnd, v.name + "-load-interval-" + i);

		builder.withLoadStart(loadStart) //
				.withLoadEnd(loadEnd) //
				.withLoadDuration(loadDuration)//
				.withLoadInterval(loadInterval);

		// Laden variables
		final IntVar ladenEnd = model.newIntVar(earliestLoadTimeInt + minMaxLadenTravel[1], lastDateTime, v.name + "-laden-end-" + c.contract.name + "-" + i);
		final IntVar ladenMinDuration;
		if (minMaxLadenTravel[0] != minMaxLadenTravel[1]) {
			ladenMinDuration = model.newIntVar(minMaxLadenTravel[0], minMaxLadenTravel[1], v.name + "-laden-duration-" + c.contract.name + "-" + i);
		} else {
			ladenMinDuration = model.newConstant(minMaxLadenTravel[0]);
		}
		final IntervalVar ladenInterval = model.newIntervalVar(loadEnd, ladenMinDuration, ladenEnd, v.name + "-laden-interval-" + i);

		builder.withLadenEnd(ladenEnd) //
				.withLadenDuration(ladenMinDuration) //
				.withLadenInterval(ladenInterval);

		// Discharge variables
		final IntVar dischargeStart = model.newIntVar(earliestLoadTimeInt, lastDateTime, v.name + "-discharge-start-" + c.contract.name + "-" + i);
		final IntVar dischargeEnd = model.newIntVar(earliestLoadTimeInt + dischargeDurationInt, lastDateTime, v.name + "-discharge-end-" + c.contract.name + "-" + i);
		final IntVar dischargeDuration = model.newConstant(dischargeDurationInt);
		final IntervalVar dischargeInterval = model.newIntervalVar(dischargeStart, dischargeDuration, dischargeEnd, v.name + "-discharge-interval-" + i);

		builder.withDischargeStart(dischargeStart) //
				.withDischargeEnd(dischargeEnd) //
				.withDischargeDuration(dischargeDuration) //
				.withDischargeInterval(dischargeInterval);

		// Ballast variables
		final IntVar ballastEnd = model.newIntVar(earliestLoadTimeInt + minMaxLadenTravel[1], lastDateTime, v.name + "-ballast-duration-" + c.contract.name + "-" + i);
		final IntVar ballastMinDuration;
		final IntervalVar ballastInterval;
		if (minMaxBallastTravel[0] != minMaxBallastTravel[1]) {
			ballastMinDuration = model.newIntVar(minMaxBallastTravel[0], minMaxBallastTravel[1], v.name + "-ballast-duration-" + c.contract.name + "-" + i);
		} else {
			ballastMinDuration = model.newConstant(minMaxBallastTravel[0]);
		}
		ballastInterval = model.newIntervalVar(dischargeEnd, ballastMinDuration, ballastEnd, v.name + "-ballast-interval-" + i);

		builder.withBallastEnd(ballastEnd) //
				.withBallastDuration(ballastMinDuration) //
				.withBallastInterval(ballastInterval);

		// TODO: Get tighter bounds on trip duration
		final IntVar cargoTripDuration = model.newIntVar(0, lastDateTime, v.name + "-cargo-trip-duration" + i);
		final IntervalVar cargoInterval = model.newIntervalVar(loadStart, cargoTripDuration, ballastEnd, v.name + "-cargo-trip-interval" + i);

		builder.withCargoInterval(cargoInterval);

		return builder.build();
	}

	private Map<Contract, SalesContract> getExpectedContracts(Map<String, Contract> loadedContracts, LNGScenarioModel sm) {
		final Map<Contract, SalesContract> contractMap = new HashMap<>();
		for (final Contract contract : loadedContracts.values()) {
			final Optional<SalesContract> optSalesContract = sm.getReferenceModel().getCommercialModel().getSalesContracts().stream().filter(sc -> sc.getName().equalsIgnoreCase(contract.getName()))
					.findAny();
			if (optSalesContract.isPresent()) {
				contractMap.put(contract, optSalesContract.get());
			} else {
				throw new IllegalStateException(String.format("Could not find sales contract %s", contract.getName()));
			}
		}
		return contractMap;
	}

	private Map<Contract, @NonNull LocalDate> getLatestPreAdpContractLoadDates(final LNGScenarioModel sm, final Map<String, Contract> expectedContracts) {
		final LocalDate adpStart = sm.getAdpModel().getYearStart().atDay(1);
		final Map<String, SalesContract> eContractsMap = sm.getReferenceModel().getCommercialModel().getSalesContracts().stream().collect(Collectors.toMap(NamedObject::getName, Function.identity()));
		final Map<Contract, @NonNull LocalDate> latestLoadDates = new HashMap<>();
		for (final Entry<String, Contract> expectedContractEntry : expectedContracts.entrySet()) {
			final String expectedContractName = expectedContractEntry.getKey();
			final SalesContract expectedEmfContract = eContractsMap.get(expectedContractName);
			assert expectedEmfContract != null;
			final Optional<LocalDate> optLatestLoadDate = sm.getCargoModel().getCargoes().stream().filter(cargo -> cargo.getSlots().get(1).getContract() == expectedEmfContract)
					.map(c -> c.getSlots().get(0).getWindowStart()).filter(date -> date.isBefore(adpStart)).max((a, b) -> a.compareTo(b));
			if (optLatestLoadDate.isPresent()) {
				final LocalDate latestLoadDate = optLatestLoadDate.get();
				latestLoadDates.put(expectedContractEntry.getValue(), latestLoadDate);
			}
		}
		return latestLoadDates;
	}

	private Map<Contract, @NonNull List<LoadSlot>> getUsedLoadSlots(final LNGScenarioModel sm, final Map<String, Contract> expectedContracts) {
		final LocalDate adpStart = sm.getAdpModel().getYearStart().atDay(1);
		final Map<String, SalesContract> eContractsMap = sm.getReferenceModel().getCommercialModel().getSalesContracts().stream().collect(Collectors.toMap(NamedObject::getName, Function.identity()));
		final Map<Contract, @NonNull List<LoadSlot>> usedLoadSlots = new HashMap<>();
		for (final Entry<String, Contract> expectedContractEntry : expectedContracts.entrySet()) {
			final String expectedContractName = expectedContractEntry.getKey();
			final SalesContract expectedEmfContract = eContractsMap.get(expectedContractName);
			assert expectedEmfContract != null;
			final List<LoadSlot> usedSlots = sm.getCargoModel().getCargoes().stream().filter(cargo -> cargo.getSlots().get(1).getContract() == expectedEmfContract)
					.map(c -> (LoadSlot) c.getSlots().get(0)).filter(slot -> !slot.getWindowStart().isBefore(adpStart)).sorted((l1, l2) -> l1.getWindowStart().compareTo(l2.getWindowStart()))
					.collect(Collectors.toList());
			if (!usedSlots.isEmpty()) {
				usedLoadSlots.put(expectedContractEntry.getValue(), usedSlots);
			}
		}
		return usedLoadSlots;
	}

	private Map<Contract, @NonNull Pair<@NonNull ZonedDateTime, ZonedDateTime>> getLatestPreAdpContractDatePairs(final LNGScenarioModel sm, final Map<String, Contract> expectedContracts,
			final ModelDistanceProvider modelDistanceProvider, @NonNull final ZoneId utcZone) {
		final LocalDate adpStart = sm.getAdpModel().getYearStart().atDay(1);
		final Map<String, SalesContract> eContractsMap = sm.getReferenceModel().getCommercialModel().getSalesContracts().stream().collect(Collectors.toMap(NamedObject::getName, Function.identity()));
		final Map<Contract, @NonNull Pair<@NonNull ZonedDateTime, ZonedDateTime>> datePairs = new HashMap<>();
		for (final Entry<String, Contract> expectedContractEntry : expectedContracts.entrySet()) {
			final String expectedContractName = expectedContractEntry.getKey();
			final SalesContract expectedEmfContract = eContractsMap.get(expectedContractName);
			final Optional<com.mmxlabs.models.lng.cargo.Cargo> optLatestCargo = sm.getCargoModel().getCargoes().stream().filter(cargo -> cargo.getSlots().get(1).getContract() == expectedEmfContract)
					.filter(cargo -> cargo.getSlots().get(0).getWindowStart().isBefore(adpStart))
					.max((c1, c2) -> c1.getSlots().get(0).getWindowStart().compareTo(c2.getSlots().get(0).getWindowStart()));
			if (optLatestCargo.isPresent()) {
				final com.mmxlabs.models.lng.cargo.Cargo eCargo = optLatestCargo.get();
				if (expectedContractEntry.getValue().fob) {
					final LoadSlot loadSlot = (LoadSlot) eCargo.getSlots().get(0);
					final DischargeSlot dischargeSlot = (DischargeSlot) eCargo.getSlots().get(1);
					final ZonedDateTime earliestLoadZdt = loadSlot.getSchedulingTimeWindow().getStart().withZoneSameInstant(utcZone);
					final ZonedDateTime latestLoadZdt = loadSlot.getSchedulingTimeWindow().getEnd().withZoneSameInstant(utcZone);
					final ZonedDateTime earliestDischargeZdt = dischargeSlot.getSchedulingTimeWindow().getStart().withZoneSameInstant(utcZone);
					final ZonedDateTime latestDischargeZdt = dischargeSlot.getSchedulingTimeWindow().getEnd().withZoneSameInstant(utcZone);

					final ZonedDateTime earliestZdt = earliestLoadZdt.compareTo(latestLoadZdt) >= 0 ? earliestLoadZdt : earliestDischargeZdt;
					final ZonedDateTime latestZdt = latestLoadZdt.compareTo(latestDischargeZdt) <= 0 ? latestLoadZdt : latestDischargeZdt;

					datePairs.put(expectedContractEntry.getValue(), Pair.of(earliestZdt, latestZdt));
				} else {
					// This calculates on a round trip basis - might not be correct
					final LoadSlot loadSlot = (LoadSlot) eCargo.getSlots().get(0);
					final DischargeSlot dischargeSlot = (DischargeSlot) eCargo.getSlots().get(1);
					final int ladenTravelTime = CargoTravelTimeUtils.getFobMinTimeInHours(loadSlot, dischargeSlot, loadSlot.getWindowStart(), eCargo.getVesselAssignmentType(),
							ScenarioModelUtil.getPortModel(sm), 0.0, modelDistanceProvider);
					final int ballastTravelTime = CargoTravelTimeUtils.getFobMinTimeInHours(dischargeSlot, loadSlot, dischargeSlot.getWindowStart(), eCargo.getVesselAssignmentType(),
							ScenarioModelUtil.getPortModel(sm), 0.0, modelDistanceProvider);
					ZonedDateTime travelTimeTracker = loadSlot.getSchedulingTimeWindow().getStart();
					travelTimeTracker = travelTimeTracker.plusHours(loadSlot.getSchedulingTimeWindow().getDuration());
					travelTimeTracker = travelTimeTracker.plusHours(ladenTravelTime);

					final ZonedDateTime dischargeArrivalEarliestTime = travelTimeTracker.withZoneSameInstant(dischargeSlot.getPort().getZoneId());
					if (dischargeArrivalEarliestTime.isBefore(dischargeSlot.getSchedulingTimeWindow().getStart())) {
						travelTimeTracker = dischargeSlot.getSchedulingTimeWindow().getStart();
					} else {
						travelTimeTracker = dischargeArrivalEarliestTime;
					}
					travelTimeTracker = travelTimeTracker.plusHours(dischargeSlot.getSchedulingTimeWindow().getDuration());
					travelTimeTracker = travelTimeTracker.plusHours(ballastTravelTime);
					final ZonedDateTime earliestRoundTripReturnZdt = travelTimeTracker.withZoneSameInstant(utcZone);
					datePairs.put(expectedContractEntry.getValue(), Pair.of(earliestRoundTripReturnZdt, null));
				}
			}
		}
		return datePairs;
	}

	private static IntVar createLoadVar(@NonNull final CpModel model, @NonNull final Vessel v, @NonNull final Contract c, int startTime, int endTime, int cargoNo) {
		String varName = v.name + "-start-" + c.name + "-" + cargoNo;
		return model.newIntVar(startTime, endTime, varName);
	}

	private static int getHourValue(@NonNull final ZonedDateTime date, @NonNull final ZonedDateTime dateZero) {
		return Hours.between(dateZero, date);
	}

	@NonNull
	private static ZonedDateTime getDate(final long hour, @NonNull final ZonedDateTime dateZero) {
		return dateZero.plusHours(hour);
	}

	// private void dumpSolutionToYaml(final Vessel[] vessels, final IntVar[][] timesVar, final Map<Contract, @NonNull LocalDate> latestLoadDates, final CpSolver solver) {
	// final Map<Contract, TreeSet<LocalDate>> contractToLocalDates = new HashMap<>();
	//
	// for (int vv = 0; vv < vessels.length; ++vv) {
	// final int vesselIdx = vv;
	// // TreeSet<Cargo> vesselTimes = new TreeSet<>();
	// for (int ii = 0; ii < timesVar[vv].length; ++ii) {
	// long t = solver.value(timesVar[vv][ii]);
	// Cargo c = vessels[vv].cargoes[ii];
	// c.dayOfSchedule = t;
	// final LocalDate loadDate = getDate(c.dayOfSchedule);
	// contractToLocalDates.computeIfAbsent(c.contract, con -> new TreeSet<>()).add(loadDate);
	// // vesselTimes.add(c);
	// }
	// }
	//
	// for (final Entry<Contract, @NonNull LocalDate> entry : latestLoadDates.entrySet()) {
	// contractToLocalDates.computeIfAbsent(entry.getKey(), con -> new TreeSet<>()).add(entry.getValue());
	// }
	// final List<Pair<Contract, List<LocalDate>>> sortedPairs = contractToLocalDates.entrySet().stream().map(e -> Pair.of(e.getKey(), (List<LocalDate>) new ArrayList<>(e.getValue())))
	// .sorted((p1, p2) -> p1.getFirst().name.compareTo(p2.getFirst().name)).collect(Collectors.toList());
	// final File outputYml = new File("C:\\Users\\miten\\mmxlabs\\R\\ADP\\spacing_yaml_dumps\\dump.yml");
	// try {
	// final FileWriter writer = new FileWriter("C:\\Users\\miten\\mmxlabs\\R\\ADP\\spacing_yaml_dumps\\dump.yml");
	// DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	// final String tabToSpace = " ";
	// for (final Pair<Contract, List<LocalDate>> pair : sortedPairs) {
	// writer.write(pair.getFirst().name);
	// writer.write(":\n");
	// writer.write(tabToSpace + "minSpace: ");
	// writer.write(Integer.toString(pair.getFirst().interval));
	// writer.write("\n");
	// writer.write(tabToSpace + "maxSpace: ");
	// writer.write(Integer.toString(pair.getFirst().maxInterval));
	// writer.write("\n");
	// writer.write(tabToSpace + "dates: ");
	// if (pair.getSecond().isEmpty()) {
	// writer.write("[]\n");
	// } else {
	// writer.write("\n");
	// for (final LocalDate date : pair.getSecond()) {
	// writer.write(tabToSpace + tabToSpace + "- \"");
	// writer.write(date.format(formatter));
	// writer.write("\"\n");
	// }
	// }
	//
	// }
	// writer.close();
	// } catch (IOException e) {
	// System.out.println("A yaml dumping error occurred.");
	// }
	// }

	private static @NonNull Command createModelPopulationCommands(Vessel[] vessels, IntVar[][] timesVar, CpSolver solver, final LNGScenarioModel sm, final EditingDomain ed,
			Map<Cargo, LoadSlot> cargoToLoadSlotMap, Set<LoadSlot> usedLoadSlotsSet, final ZonedDateTime timeZero, final String defaultDischargePort, final IScenarioDataProvider sdp) {
		TreeSet<Long> allTimes = new TreeSet<>();
		HashMap<String, TreeSet<Long>> contractToTimes = new HashMap<>();

		for (int vv = 0; vv < vessels.length; ++vv) {
			for (int ii = 0; ii < timesVar[vv].length; ++ii) {
				long t = solver.value(timesVar[vv][ii]);
				allTimes.add(t);
				TreeSet<Long> contractTimes = contractToTimes.computeIfAbsent(vessels[vv].cargoes[ii].contract.name, k -> new TreeSet<>());
				contractTimes.add(t);
				if (!vessels[vv].cargoes[ii].contract.fob) {
					allTimes.add(t + vessels[vv].cargoes[ii].contract.interval / 2);
				}
			}
		}
		allTimes.clear();

		// Don't have to fetch all just simpler for PoC - can make this lazy
		final Map<String, SalesContract> salesContractNameMap = new HashMap<>();
		sm.getReferenceModel().getCommercialModel().getSalesContracts().forEach(sc -> salesContractNameMap.put(sc.getName().toLowerCase(), sc));

		final HashSet<String> vesselsToFetch = new HashSet<>();
		for (int vv = 0; vv < vessels.length; ++vv) {
			vesselsToFetch.add(vessels[vv].name.toLowerCase());
		}
		final Map<String, com.mmxlabs.models.lng.fleet.Vessel> vesselNameMap = new HashMap<>();
		sm.getReferenceModel().getFleetModel().getVessels().stream().filter(v -> vesselsToFetch.contains(v.getName().toLowerCase())).forEach(v -> {
			vesselNameMap.put(v.getName().toLowerCase(), v);
		});
		final CompoundCommand cmd = new CompoundCommand("Generate spaced cargoes");
		int cargoId = 1;
		final List<DischargeSlot> dischargeSlotsToAdd = new ArrayList<>();
		final List<com.mmxlabs.models.lng.cargo.Cargo> cargoesToAdd = new ArrayList<>();
		for (int vv = 0; vv < vessels.length; ++vv) {
			final int vesselIdx = vv;
			TreeSet<Cargo> vesselTimes = new TreeSet<>();
			for (int ii = 0; ii < timesVar[vv].length; ++ii) {
				long t = solver.value(timesVar[vv][ii]);
				Cargo c = vessels[vv].cargoes[ii];
				c.dayOfSchedule = t;
				vesselTimes.add(c);
				allTimes.add(t);
			}
			boolean first = true;

			ArrayList<Cargo> sortedCargoes = new ArrayList<>(vesselTimes);
			for (int i = 0; i < sortedCargoes.size(); i++) {
				Cargo t = sortedCargoes.get(i);
				if (first) {
					first = false;
				}
				final ZonedDateTime loadZdtUtc = getDate(t.dayOfSchedule, timeZero);
				final LoadSlot loadSlot = cargoToLoadSlotMap.get(t);
				if (!usedLoadSlotsSet.contains(loadSlot)) {
					final DischargeSlot dischargeSlot = CargoFactory.eINSTANCE.createDischargeSlot();
					dischargeSlot.setName(String.format("discharge-%02d", cargoId));

					dischargeSlot.setFOBSale(t.contract.fob);
					dischargeSlot.setContract(salesContractNameMap.get(t.contract.name.toLowerCase()));
					final com.mmxlabs.models.lng.cargo.Cargo cargo = CargoFactory.eINSTANCE.createCargo();
					cargo.getSlots().add(loadSlot);
					cargo.getSlots().add(dischargeSlot);
					if (!t.contract.fob) {
						// VesselAssignmentType
						final com.mmxlabs.models.lng.fleet.Vessel expectedVessel = vesselNameMap.get(vessels[vv].name.toLowerCase());
						Optional<@NonNull VesselAvailability> optVa = sm.getCargoModel().getVesselAvailabilities().stream().filter(VesselAvailability.class::isInstance)
								.map(VesselAvailability.class::cast).filter(va -> va.getVessel() == expectedVessel).findAny();
						final VesselAssignmentType va;
						if (optVa.isPresent()) {
							va = optVa.get();
						} else {
							va = sm.getReferenceModel().getSpotMarketsModel().getCharterInMarkets().stream().filter(cim -> cim.getName().equalsIgnoreCase(vessels[vesselIdx].name.toLowerCase()))
									.findAny().get();
						}
						cargo.setVesselAssignmentType(va);
						final String portName = t.contract.port == null ? defaultDischargePort : t.contract.port;
						dischargeSlot.setPort(sm.getReferenceModel().getPortModel().getPorts().stream().filter(p -> p.getName().equalsIgnoreCase(portName)).findAny().get());
						final com.mmxlabs.models.lng.fleet.Vessel vess = va instanceof VesselAvailability ? ((VesselAvailability) va).getVessel() : ((CharterInMarket) va).getVessel();
						final LocalDate dischargeDate = calculateDischargeDate(loadSlot, dischargeSlot, vess, sdp, sm);
						// Create cargo
						dischargeSlot.setWindowStart(dischargeDate);
						dischargeSlot.setWindowStartTime(0);
						dischargeSlot.unsetWindowStartTime();
						dischargeSlot.setWindowSize(1);
						dischargeSlot.setWindowSizeUnits(TimePeriod.MONTHS);

					} else {
						dischargeSlot.setWindowStart(loadZdtUtc.withZoneSameInstant(loadSlot.getPort().getZoneId()).toLocalDate());
						dischargeSlot.setPort(loadSlot.getPort());
						dischargeSlot.setNominatedVessel(vesselNameMap.get(vessels[vv].name.toLowerCase()));
					}
					dischargeSlotsToAdd.add(dischargeSlot);
					cargoesToAdd.add(cargo);
				}
				++cargoId;
			}

		}
		cmd.append(AddCommand.create(ed, sm.getCargoModel(), CargoPackage.Literals.CARGO_MODEL__DISCHARGE_SLOTS, dischargeSlotsToAdd));
		cmd.append(AddCommand.create(ed, sm.getCargoModel(), CargoPackage.Literals.CARGO_MODEL__CARGOES, cargoesToAdd));
		return cmd;
	}

	public static int getMinimumTravelTime(@NonNull final Slot<?> startSlot, @NonNull final Slot<?> endSlot, final com.mmxlabs.models.lng.fleet.Vessel vessel, @NonNull final IScenarioDataProvider sdp,
			final LNGScenarioModel sm) {
		final List<VesselAvailability> vaList = sm.getCargoModel().getVesselAvailabilities().stream().filter(va -> va.getVessel() == vessel).collect(Collectors.toList());
		if (vaList.isEmpty()) {
			throw new IllegalStateException(String.format("No fleet data provided for %s", ScenarioElementNameHelper.getName(vessel, "<Unknown>")));
		}
		@NonNull
		final ModelDistanceProvider modelDistanceProvider = sdp.getExtraDataProvider(LNGScenarioSharedModelTypes.DISTANCES, ModelDistanceProvider.class);
		final VesselAvailability va = vaList.get(0);
		final int minTravelTime = CargoTravelTimeUtils.getFobMinTimeInHours(startSlot, endSlot, FIRST_DATE, va, sm.getReferenceModel().getPortModel(), vessel.getVesselOrDelegateMaxSpeed(),
				modelDistanceProvider);
		if (minTravelTime == Integer.MAX_VALUE) {
			final String message = String.format("Cannot determine travel time between %s and %s.%n Travel time cannot be %d hours.", startSlot.getPort().getName(), endSlot.getPort().getName(),
					minTravelTime);
			throw new IllegalStateException(message);
		}
		return minTravelTime;
	}

	public static LocalDate calculateDischargeDate(@NonNull final LoadSlot loadSlot, @NonNull final DischargeSlot dischargeSlot, final com.mmxlabs.models.lng.fleet.Vessel vessel,
			@NonNull final IScenarioDataProvider sdp, final LNGScenarioModel sm) {

		final int minTravelTime = getMinimumTravelTime(loadSlot, dischargeSlot, vessel, sdp, sm);
		final SchedulingTimeWindow loadSTW = loadSlot.getSchedulingTimeWindow();
		return loadSTW.getStart().plusHours(minTravelTime + loadSTW.getDuration()).withZoneSameInstant(dischargeSlot.getPort().getZoneId()).withDayOfMonth(1).withHour(0).toLocalDate();
	}
}
