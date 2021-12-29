package com.mmxlabs.models.lng.adp.rateability.spacing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.ortools.sat.CpModel;
import com.google.ortools.sat.CpSolver;
import com.google.ortools.sat.CpSolverStatus;
import com.google.ortools.sat.IntVar;
import com.google.ortools.sat.IntervalVar;
import com.google.ortools.sat.LinearExpr;
import com.google.ortools.sat.Literal;
import com.google.ortools.sat.ScalProd;
import com.google.ortools.util.Domain;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.Triple;
import com.mmxlabs.common.time.Days;
import com.mmxlabs.models.lng.adp.rateability.export.Feasible;
import com.mmxlabs.models.lng.adp.rateability.export.Infeasible;
import com.mmxlabs.models.lng.adp.rateability.export.InvalidModel;
import com.mmxlabs.models.lng.adp.rateability.export.Optimal;
import com.mmxlabs.models.lng.adp.rateability.export.SpacingRateabilitySolverResult;
import com.mmxlabs.models.lng.adp.rateability.export.Unknown;
import com.mmxlabs.models.lng.adp.rateability.export.Unrecognised;
import com.mmxlabs.models.lng.adp.rateability.input.RateabilityInput;
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

	enum Scenario {
		Scratch, Current
	}

	static boolean balanceLegs = false;

	static class RateabilityConstraint {
		int everyNMonths;
		int minusDays;

		public RateabilityConstraint(int everyNMonths, int minusDays) {
			this.everyNMonths = everyNMonths;
			this.minusDays = minusDays;
		}
	}

	static final Scenario scenario = Scenario.Current;

	// static final int NUMBER_OF_VESSELS = 12; // Number of vessels

	static final int SEARCH_RANGE = 80; // Latest vessel start time (exclusive).

	static final int NUMBER_OF_ITERATIONS = 20_000_000;

	static final int INTERVAL_COMPUTE_FUZZ = 3; // Allow -3 days to intervals
	static final int INTERVAL_FILTER_FUZZ = 5; // +/- fuzz when filtering results
	static final int LOADING_INTERVAL = 5; // Keep n days between loadings

	@NonNull
	private static final LocalDate FIRST_DATE = LocalDate.of(2021, 12, 1);
	static final int firstLoadDate = 31; // 31 = 1/1/2021, 0 = 1/12/2020
	static final int lastLoadDate = firstLoadDate + 365; // Last load date = 31/12/2021.

	public RateabilityInput loadInputData(final Shell shell) throws IOException {
		final ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.registerModule(new JavaTimeModule());
		mapper.registerModule(new Jdk8Module());
		final IPath wsPath = ResourcesPlugin.getWorkspace().getRoot().getLocation();
		final File directory = new File(wsPath.toOSString() + IPath.SEPARATOR + "adp-rateability");
		boolean dirExists = directory.exists();
		if (!dirExists) {
			MessageDialog.openInformation(shell, "", "No directory \"adp-rateability\" found.");
			return null;
		}
		if (!directory.isDirectory()) {
			MessageDialog.openInformation(shell, "", "\"adp-rateability\" should be a directory.");
			return null;
		}

		final Display display = PlatformUI.getWorkbench().getDisplay();
		final InputDialog dialog = new InputDialog(display.getActiveShell(), "Enter rateability source json", "Enter name of json file", "", null);
		if (dialog.open() == Window.OK) {
			final String jsonFilename = dialog.getValue();
			final String fullJsonFilename;
			if (jsonFilename.endsWith(".json")) {
				fullJsonFilename = jsonFilename;
			} else {
				fullJsonFilename = String.format("%s.json", jsonFilename);
			}
			final String filePath = directory.getAbsolutePath() + File.separator + fullJsonFilename;
			File file = new File(filePath);
			if (!file.exists()) {
				MessageDialog.openInformation(shell, "Input file not found", String.format("Could not find: \"%s\"", filePath));
				return null;
			}

			final RateabilityInput input;
			try (InputStream inputStream = new FileInputStream(file)) {
				input = mapper.readValue(inputStream, RateabilityInput.class);
			}
			return input;
		}

		return null;
	}

	private static List<Pair<LocalDate, LocalDate>> getAvailableDates(final List<List<List<Integer>>> inputDates) {
		final List<Pair<LocalDate, LocalDate>> availableDates = new ArrayList<>();
		for (List<List<Integer>> datePair : inputDates) {
			List<Integer> fromInput = datePair.get(0);
			List<Integer> toInput = datePair.get(1);
			LocalDate from = LocalDate.of(fromInput.get(0).intValue(), fromInput.get(1).intValue(), fromInput.get(2).intValue());
			LocalDate to = LocalDate.of(toInput.get(0).intValue(), toInput.get(1).intValue(), toInput.get(2).intValue());
			availableDates.add(Pair.of(from, to));
		}
		return availableDates;
	}

	private static @NonNull Contract buildContractFromInput(final com.mmxlabs.models.lng.adp.rateability.input.Contract inputContract) {
		final Contract contract;
		if (inputContract.maxInterval.isPresent()) {
			contract = new Contract(inputContract.name, inputContract.contractSpacing, Integer.MAX_VALUE, inputContract.isFOB, inputContract.interval, inputContract.maxInterval.getAsInt());
		} else {
			if (inputContract.port.isPresent()) {
				contract = new Contract(inputContract.name, inputContract.contractSpacing, inputContract.isFOB, inputContract.interval, inputContract.port.get());
			} else {
				contract = new Contract(inputContract.name, inputContract.contractSpacing, inputContract.isFOB, inputContract.interval);
			}
		}
		if (inputContract.turnaroundTime.isPresent()) {
			contract.turnaroundTime = inputContract.turnaroundTime.getAsInt();
		}
		return contract;
	}

	private static @NonNull Vessel buildVessel(final com.mmxlabs.models.lng.adp.rateability.input.Vessel inputVessel, final Map<String, Contract> contractMap) {
		final String spotIndex = inputVessel.spotIndex.isPresent() ? inputVessel.spotIndex.get() : "";
		if (inputVessel.contract.isPresent()) {
			final Contract c = contractMap.get(inputVessel.contract.get());
			if (inputVessel.availableDates.isPresent()) {
				final List<Pair<LocalDate, LocalDate>> availableDates = getAvailableDates(inputVessel.availableDates.get());
				return Vessel.createVessel(inputVessel.name, spotIndex, c, inputVessel.number.getAsInt(), availableDates);
			} else {
				return Vessel.createVessel(inputVessel.name, spotIndex, c, inputVessel.number.getAsInt());
			}
		} else {
			final Cargo[] cargoes = new Cargo[inputVessel.cargoes.get().size()];
			int j = 0;
			for (com.mmxlabs.models.lng.adp.rateability.input.Cargo cargoInput : inputVessel.cargoes.get()) {
				final Contract c = contractMap.get(cargoInput.contract);
				if (cargoInput.minLoadDate.isPresent()) {
					final LocalDate minLoadDate = LocalDate.of(cargoInput.minLoadDate.get().get(0), cargoInput.minLoadDate.get().get(1), cargoInput.minLoadDate.get().get(2));
					final LocalDate maxLoadDate = LocalDate.of(cargoInput.maxLoadDate.get().get(0), cargoInput.maxLoadDate.get().get(1), cargoInput.maxLoadDate.get().get(2));
					cargoes[j] = new Cargo(c, minLoadDate, maxLoadDate, null, null);
				} else if (cargoInput.minDischargeDate.isPresent()) {
					final LocalDate minDischargeDate = LocalDate.of(cargoInput.minDischargeDate.get().get(0), cargoInput.minDischargeDate.get().get(1), cargoInput.minDischargeDate.get().get(2));
					final LocalDate maxDischargeDate = LocalDate.of(cargoInput.maxDischargeDate.get().get(0), cargoInput.maxDischargeDate.get().get(1), cargoInput.maxDischargeDate.get().get(2));
					cargoes[j] = new Cargo(c, minDischargeDate, maxDischargeDate);
				} else {
					cargoes[j] = new Cargo(c);
				}
				++j;
			}
			if (inputVessel.availableDates.isPresent()) {
				final List<Pair<LocalDate, LocalDate>> availableDates = getAvailableDates(inputVessel.availableDates.get());
				return new Vessel(inputVessel.name, spotIndex, cargoes, availableDates);
			} else {
				return new Vessel(inputVessel.name, spotIndex, cargoes);
			}
		}
	}

	private static @NonNull RateabilityConstraint buildRateabilityConstraint(final com.mmxlabs.models.lng.adp.rateability.input.RateabilityConstraint inputRateabilityConstraint) {
		return new RateabilityConstraint(inputRateabilityConstraint.everyNMonths, 0);
	}

	public Pair<Map<String, Contract>, Vessel[]> processInput(final RateabilityInput input) {
		final Map<String, Contract> contractMap = input.contracts.stream().map(CPBasedSolver::buildContractFromInput).collect(Collectors.toMap(Contract::getName, Function.identity()));
		final Vessel[] vessels = input.vessels.stream().map(v -> buildVessel(v, contractMap)).toArray(Vessel[]::new);
		return Pair.of(contractMap, vessels);
	}

	public Map<String, @NonNull RateabilityConstraint> getRateabilityConstraintsFromInput(final RateabilityInput input) {
		return input.contracts.stream().filter(c -> c.rateabilityConstraint.isPresent()).collect(Collectors.toMap(c -> c.name, c -> buildRateabilityConstraint(c.rateabilityConstraint.get())));
	}

	public SpacingRateabilitySolverResult runCpSolver(final LNGScenarioModel sm, final EditingDomain ed, final IScenarioDataProvider sdp, Shell shell) throws IOException {

		final RateabilityInput input;
		try {
			input = loadInputData(shell);
		} catch (IOException e) {
			// System.out.println("Error in reading input json.");
			return null;
		}
		if (input == null) {
			// System.out.println("Cancelled.");
			return null;
		}

		final CpModel model = new CpModel();

		int contractIntervalFlex = 0;

		@NonNull
		final ModelDistanceProvider modelDistanceProvider = sdp.getExtraDataProvider(LNGScenarioSharedModelTypes.DISTANCES, ModelDistanceProvider.class);

		Pair<Map<String, Contract>, Vessel[]> inputProcessResult = processInput(input);

		final Map<String, Contract> loadedContracts = inputProcessResult.getFirst();
		final Vessel[] vessels = inputProcessResult.getSecond();
		final Map<Contract, @NonNull LocalDate> latestLoadDates = getLatestPreAdpContractLoadDates(sm, loadedContracts);
		final Map<Contract, @NonNull List<LoadSlot>> usedLoadSlots = getUsedLoadSlots(sm, loadedContracts);
		final Set<LoadSlot> usedLoadSlotsSet = usedLoadSlots.values().stream().flatMap(List::stream).collect(Collectors.toSet());

		final Map<Contract, @NonNull Pair<@NonNull LocalDate, LocalDate>> latestDatePairs = getLatestPreAdpContractDatePairs(sm, loadedContracts, modelDistanceProvider);
		final Map<Contract, SalesContract> contractMap = getExpectedContracts(loadedContracts, sm);
		final Map<SalesContract, Contract> reverseContractMap = new HashMap<>();
		contractMap.entrySet().stream().forEach(e -> reverseContractMap.put(e.getValue(), e.getKey()));
		final Set<SalesContract> expectedSalesContracts = new HashSet<>(contractMap.values());

		final Map<Contract, Integer> latestLoadDatesConvertedToInt = latestLoadDates.entrySet().stream().collect(Collectors.toMap(Entry::getKey, e -> getDateValue(e.getValue())));

		final Map<String, @NonNull RateabilityConstraint> contractRateabilityConstraints = getRateabilityConstraintsFromInput(input);

		final YearMonth adpStart = sm.getAdpModel().getYearStart();
		final List<LoadSlot> adpLoadSlots = sm.getCargoModel().getLoadSlots().stream().filter(slot -> !adpStart.isAfter(YearMonth.from(slot.getWindowStart()))).collect(Collectors.toList());
		final Set<LoadSlot> adpLoadSlotSet = new HashSet<>(adpLoadSlots);
		final Map<LocalDate, LoadSlot> dateLoadSlotMap = adpLoadSlots.stream().collect(Collectors.toMap(LoadSlot::getWindowStart, Function.identity()));
		final List<LocalDate> loadDates = adpLoadSlots.stream().map(LoadSlot::getWindowStart).collect(Collectors.toList());

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
			if (cargoVA instanceof VesselAvailability) {
				final VesselAvailability va = (VesselAvailability) cargoVA;
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
		final Set<LoadSlot> unusableLoadSlots = Stream.concat(sm.getCargoModel().getCargoes().stream().filter(cargo -> adpLoadSlotSet.contains((LoadSlot) cargo.getSlots().get(0))).filter(cargo -> {
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
			if (event instanceof DryDockEvent) {
				final DryDockEvent dryDockEvent = (DryDockEvent) event;
				if (dryDockEvent.getVesselAssignmentType() instanceof VesselAvailability) {
					final VesselAvailability va = (VesselAvailability) dryDockEvent.getVesselAssignmentType();
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
		IntVar[] loadVarsEnd = new IntVar[totalTasks];
		IntervalVar[] loadVarsIntervals = new IntervalVar[totalTasks];
		IntVar[] cargoEnd = new IntVar[totalTasks];

		HashMap<String, List<IntervalVar>> contractToSpacingIntervals = new HashMap<>();

		HashMap<String, List<List<IntVar>>> contractToVesselLoadVars = new HashMap<>();

		HashMap<String, List<IntVar>> contractToLoadVars = new HashMap<>();
		HashMap<String, HashMap<String, List<IntVar>>> contractToVesselToLoadVars = new HashMap<>();

		HashMap<String, Contract> contracts = new HashMap<>();

		// Get possible load dates
		final Set<LocalDate> possibleLoadDates = sm.getCargoModel().getLoadSlots().stream().filter(s -> !s.getWindowStart().isBefore(adpStart.atDay(1))).flatMap(s -> {
			if (s.isSetWindowSize() && s.isSetWindowSizeUnits()) {
				final LocalDate firstInvalidDate;
				if (s.getWindowSize() > 0) {
					if (s.getWindowSizeUnits() == TimePeriod.DAYS) {
						firstInvalidDate = s.getSchedulingTimeWindow().getStart().plusDays(s.getWindowSize()).minusHours(1).toLocalDate().plusDays(1);
					} else if (s.getWindowSizeUnits() == TimePeriod.HOURS) {
						firstInvalidDate = s.getSchedulingTimeWindow().getStart().plusHours(s.getWindowSize()).toLocalDate().plusDays(1);
					} else if (s.getWindowSizeUnits() == TimePeriod.MONTHS) {
						firstInvalidDate = s.getSchedulingTimeWindow().getStart().plusMonths(s.getWindowSize()).minusHours(1).toLocalDate().plusDays(1);
					} else {
						throw new IllegalStateException("Unknown time period");
					}
				} else {
					firstInvalidDate = s.getWindowStart().plusDays(1);
				}
				return s.getWindowStart().datesUntil(firstInvalidDate);
			} else {
				return Collections.singleton(s.getWindowStart()).stream();
			}
		}).collect(Collectors.toCollection(TreeSet::new));
		final List<LocalDate> sortedLoadDates = new ArrayList<>(possibleLoadDates);
		final List<LoadSlot> sortedLoadSlots = sm.getCargoModel().getLoadSlots().stream().filter(s -> !s.getWindowStart().isBefore(adpStart.atDay(1)))
				.sorted((s1, s2) -> s1.getWindowStart().compareTo(s2.getWindowStart())).collect(Collectors.toList());

		final int numLoadSlots = sortedLoadSlots.size();
		final int numCargoes = Arrays.stream(vessels).mapToInt(v -> v.cargoes.length).sum();
		final IntVar[][] cargoToLoadSlotSelectionVars = new IntVar[numCargoes][numLoadSlots];
		final LocalDate[][] loadSlotEndPoints = new LocalDate[numLoadSlots][2];

		final Iterator<LoadSlot> loadSlotIter = sortedLoadSlots.iterator();
		for (int i = 0; i < numLoadSlots; ++i) {
			final LoadSlot currentLoadSlot = loadSlotIter.next();
			final LocalDate startDateInclusive = currentLoadSlot.getWindowStart();
			final LocalDate endDateExclusive;
			if (currentLoadSlot.isSetWindowSize() && currentLoadSlot.isSetWindowSizeUnits()) {
				if (currentLoadSlot.getWindowSize() > 0) {
					if (currentLoadSlot.getWindowSizeUnits() == TimePeriod.DAYS) {
						endDateExclusive = currentLoadSlot.getSchedulingTimeWindow().getStart().plusDays(currentLoadSlot.getWindowSize()).minusHours(1).toLocalDate().plusDays(1);
					} else if (currentLoadSlot.getWindowSizeUnits() == TimePeriod.HOURS) {
						endDateExclusive = currentLoadSlot.getSchedulingTimeWindow().getStart().plusHours(currentLoadSlot.getWindowSize()).toLocalDate().plusDays(1);
					} else if (currentLoadSlot.getWindowSizeUnits() == TimePeriod.MONTHS) {
						endDateExclusive = currentLoadSlot.getSchedulingTimeWindow().getStart().plusMonths(currentLoadSlot.getWindowSize()).minusHours(1).toLocalDate().plusDays(1);
					} else {
						throw new IllegalStateException("Unknown time period");
					}
				} else {
					endDateExclusive = currentLoadSlot.getWindowStart().plusDays(1);
				}
			} else {
				endDateExclusive = startDateInclusive.plusDays(1);
			}
			loadSlotEndPoints[i][0] = startDateInclusive;
			loadSlotEndPoints[i][1] = endDateExclusive;
		}
		final int[][] loadTimePoints = new int[numLoadSlots][2];
		for (int i = 0; i < numLoadSlots; ++i) {
			loadTimePoints[i][0] = getDateValue(loadSlotEndPoints[i][0]);
			loadTimePoints[i][1] = getDateValue(loadSlotEndPoints[i][1]);
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
			final IntVar[] loads = new IntVar[numLoadSlots];
			for (int loadIndex = 0; loadIndex < numLoadSlots; ++loadIndex) {
				loads[loadIndex] = cargoToLoadSlotSelectionVars[cargoIndex][loadIndex];
			}
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
		int dryDockCount = 0;
		for (final Entry<com.mmxlabs.models.lng.fleet.Vessel, List<DryDockEvent>> entry : dryDockEvents.entrySet()) {
			for (final DryDockEvent event : entry.getValue()) {
				final int duration = event.getDurationInDays();
				final LocalDate startBy = event.getStartBy().toLocalDate();
				final LocalDate startAfter = event.getStartAfter().toLocalDate();
				final int startByInt = getDateValue(startBy);
				final int startAfterInt = getDateValue(startAfter);
				final IntVar dryDockStart = model.newIntVar(startAfterInt, startByInt, String.format("DD-%s-start", event.getName()));
				final IntVar dryDockEnd = model.newIntVar(startAfterInt + duration, startByInt + duration, String.format("DD-%s-end", event.getName()));
				final IntervalVar interval = model.newIntervalVar(dryDockStart, duration, dryDockEnd, String.format("DD-%s-interval", event.getName()));
				++dryDockCount;
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
					final int minTravelDaysRoundedUp = (int) Math.ceil(minTravelTime / 24.0);
					portTimesMap.put(currentLoadSlot.getPort(), minTravelDaysRoundedUp);
				}
			}
		}
		final String defaultPortName = "Himeji";
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
					final int minTravelDaysRoundedUp = (int) Math.ceil(minTravelTime / 24.0);
					portTimesMap.put(currentPort.getName().toLowerCase(), minTravelDaysRoundedUp);
				}
			}
		}

		int loadInterval = 4;
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

				// FOB-FOB has to be in current year, FOB-DES can load in previous year.
				int desDeliveryTime = (c.contract.fob ? 0 : (c.contract.interval / 2 + 1));
				int startTime = firstLoadDate - desDeliveryTime;
				int endTime = lastLoadDate;

				if (c.minDischargeDate != null) {
					int minLoadTime = (int) getDateValue(c.minDischargeDate) - desDeliveryTime;
					startTime = Math.max(minLoadTime, startTime);
				}
				if (c.maxDischargeDate != null) {
					int maxLoadTime = (int) getDateValue(c.maxDischargeDate) - desDeliveryTime;
					endTime = Math.min(maxLoadTime, endTime);
				}
				if (c.minLoadDate != null) {
					int minLoadTime = (int) getDateValue(c.minLoadDate);
					startTime = Math.max(minLoadTime, startTime);
				}
				if (c.maxLoadDate != null) {
					int maxLoadTime = (int) getDateValue(c.maxLoadDate);
					endTime = Math.min(maxLoadTime, endTime);
				}

				vesselVarsStart[i] = createLoadVar(model, v, c.contract, startTime, endTime, i, desDeliveryTime, sortedLoadDates);
				vesselVarsEnd[i] = model.newIntVar(startTime, endTime + c.contract.interval, v.name + "-" + c.contract.name + "-end-" + i);
				// Cargo spacing per vessel.
				vesselVarsInterval[i] = model.newIntervalVar(vesselVarsStart[i], c.contract.interval, vesselVarsEnd[i], v.name + "-interval-" + i);

				if (c.contract.turnaroundTime != Integer.MAX_VALUE) {
					final IntVar ballastEnd = model.newIntVar(startTime, endTime + c.contract.turnaroundTime, v.name + "-ballast-end" + i);
					final IntervalVar roundTripIntervalVar = model.newIntervalVar(vesselVarsStart[i], c.contract.turnaroundTime, ballastEnd, v.name + "-round-trip-interval-" + i);
					dryDockIntervalVars2.computeIfAbsent(v, vv -> new LinkedList<>()).add(roundTripIntervalVar);
				}

				for (int loadIndex = 0; loadIndex < numLoadSlots; ++loadIndex) {
					model.addLinearConstraint(vesselVarsStart[i], loadTimePoints[loadIndex][0], loadTimePoints[loadIndex][1] - 1)
							.onlyEnforceIf(cargoToLoadSlotSelectionVars[cargoIndices.get(c)][loadIndex]);
				}

				if (c.contract.fob) {
					final List<LoadSlot> localUsedLoadSlots = usedLoadSlots.get(c.contract);
					if (localUsedLoadSlots != null) {
						for (final LoadSlot slot : localUsedLoadSlots) {
							final LocalDate dischargeDate = slot.getCargo().getSlots().get(1).getWindowStart();
							final int timepoint = getDateValue(dischargeDate);
							final int cargoIndex = cargoIndices.get(c);
							final int loadIndex = loadIndices.get(slot);
							model.addEquality(vesselVarsStart[i], timepoint).onlyEnforceIf(cargoToLoadSlotSelectionVars[cargoIndex][loadIndex]);
						}
					}
				} else {
					final List<LoadSlot> localUsedLoadSlots = usedLoadSlots.get(c.contract);
					if (localUsedLoadSlots != null) {
						for (final LoadSlot loadSlot : localUsedLoadSlots) {
							final int cargoIndex = cargoIndices.get(c);
							final int loadIndex = loadIndices.get(loadSlot);
							final DischargeSlot dischargeSlot = (DischargeSlot) loadSlot.getCargo().getSlots().get(1);
							// Work out load limits
							final int minimumTravelTime = getMinimumTravelTime(dischargeSlot, loadSlot, vesselToEVesselMap.get(v), sdp, sm);
							final ZonedDateTime dischargeEndZdt = dischargeSlot.getSchedulingTimeWindow().getEnd();
							final ZonedDateTime dischargeTzLatestLoadZdt = dischargeEndZdt.minusHours(minimumTravelTime);
							final ZonedDateTime loadTzLatestLoadZdt = dischargeTzLatestLoadZdt.withZoneSameInstant(loadSlot.getPort().getZoneId());
							final LocalDate loadTzLatestLoad = loadTzLatestLoadZdt.toLocalDate();
							final int loadTzLatestLoadInt = getDateValue(loadTzLatestLoad);
							final LinearExpr term = LinearExpr.term(vesselVarsStart[i], 1);
							model.addLessOrEqual(vesselVarsStart[i], loadTzLatestLoadInt).onlyEnforceIf(cargoToLoadSlotSelectionVars[cargoIndex][loadIndex]);
						}
					}
					// Sequencing around other cargoes
					final List<com.mmxlabs.models.lng.cargo.Cargo> fixedCargoes = cargoVessels.get(v);
					if (fixedCargoes != null) {
						for (final com.mmxlabs.models.lng.cargo.Cargo fixedCargo : fixedCargoes) {
							// Create cargo sequencing binary
							final LoadSlot fixedLoadSlot = (LoadSlot) fixedCargo.getSlots().get(0);
							final DischargeSlot fixedDischargeSlot = (DischargeSlot) fixedCargo.getSlots().get(1);
							final VesselAssignmentType vat = fixedCargo.getVesselAssignmentType();
							final String selectionVarName = String.format("FC-%s-%s-isAfter-%s-%d", fixedLoadSlot.getName(), fixedDischargeSlot.getName(), v.name, i);
							final IntVar fixedCargoIsAfterSelection = model.newBoolVar(selectionVarName);
							// Fixed cargo is after this cargo
							final ZonedDateTime fixedLoadLatestStart = fixedLoadSlot.getSchedulingTimeWindow().getEnd();
							final DischargeSlot ghostDischargeSlot = CargoFactory.eINSTANCE.createDischargeSlot();
							final String dischargePortName = c.contract.port != null ? c.contract.port : defaultPortName;
							final Port dischargePort = sm.getReferenceModel().getPortModel().getPorts().stream().filter(p -> p.getName().equalsIgnoreCase(dischargePortName)).findAny().get();
							ghostDischargeSlot.setPort(dischargePort);
							final int fastestBallastTravelTime = CargoTravelTimeUtils.getFobMinTimeInHours(ghostDischargeSlot, fixedDischargeSlot, FIRST_DATE, vat,
									sm.getReferenceModel().getPortModel(), ((VesselAvailability) vat).getVessel().getVesselOrDelegateMaxSpeed(), modelDistanceProvider);
							final int fastestFixedCargoLadenTime = CargoTravelTimeUtils.getFobMinTimeInHours(fixedLoadSlot, fixedDischargeSlot, FIRST_DATE, vat, sm.getReferenceModel().getPortModel(),
									((VesselAvailability) vat).getVessel().getVesselOrDelegateMaxSpeed(), modelDistanceProvider);
							final ZonedDateTime earliestDischargeStart = fixedLoadSlot.getSchedulingTimeWindow().getStart().plusHours(fixedLoadSlot.getDuration()).plusHours(fastestFixedCargoLadenTime)
									.withZoneSameInstant(fixedDischargeSlot.getPort().getZoneId());
							final ZonedDateTime actualEarliestDischargeStart;
							if (earliestDischargeStart.isBefore(fixedDischargeSlot.getSchedulingTimeWindow().getStart())) {
								actualEarliestDischargeStart = fixedDischargeSlot.getSchedulingTimeWindow().getStart();
							} else {
								actualEarliestDischargeStart = earliestDischargeStart;
							}
							for (final LoadSlot potentialLoadSlot : sortedLoadSlots) {
								final int loadDuration = potentialLoadSlot.getDuration();
								final int fastestLadenTime = CargoTravelTimeUtils.getFobMinTimeInHours(potentialLoadSlot, fixedDischargeSlot, FIRST_DATE, vat, sm.getReferenceModel().getPortModel(),
										((VesselAvailability) vat).getVessel().getVesselOrDelegateMaxSpeed(), modelDistanceProvider);
								final int dischargeDuration = dischargePort.getDischargeDuration();
								final int totalTravelTimeInHours = loadDuration + fastestLadenTime + dischargeDuration + fastestBallastTravelTime;
								final ZonedDateTime latestStartTime = fixedLoadLatestStart.minusHours(totalTravelTimeInHours);
								final ZonedDateTime zonedLatestStartTime = latestStartTime.withZoneSameInstant(potentialLoadSlot.getPort().getZoneId());
								if (!potentialLoadSlot.getSchedulingTimeWindow().getStart().isBefore(zonedLatestStartTime)) {
									final int cargoIndex = cargoIndices.get(c);
									final int loadIndex = loadIndices.get(potentialLoadSlot);
									final IntVar cargoLoadSelectionVar = cargoToLoadSlotSelectionVars[cargoIndex][loadIndex];
									model.addEquality(cargoLoadSelectionVar, 0).onlyEnforceIf(fixedCargoIsAfterSelection);
								}
								final int fastestBallastToReachTravelTime = CargoTravelTimeUtils.getFobMinTimeInHours(fixedDischargeSlot, potentialLoadSlot, FIRST_DATE, vat,
										sm.getReferenceModel().getPortModel(), ((VesselAvailability) vat).getVessel().getVesselOrDelegateMaxSpeed(), modelDistanceProvider);
								final ZonedDateTime earliestLoadTime = actualEarliestDischargeStart.plusHours(fixedDischargeSlot.getPort().getDischargeDuration() + fastestBallastToReachTravelTime)
										.withZoneSameInstant(potentialLoadSlot.getPort().getZoneId());
								if (earliestLoadTime.isAfter(potentialLoadSlot.getSchedulingTimeWindow().getEnd())) {
									final int cargoIndex = cargoIndices.get(c);
									final int loadIndex = loadIndices.get(potentialLoadSlot);
									final IntVar cargoLoadSelectionVar = cargoToLoadSlotSelectionVars[cargoIndex][loadIndex];
									model.addEquality(cargoLoadSelectionVar, 0).onlyEnforceIf(fixedCargoIsAfterSelection.not());
								}
							}
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
						model.addGreaterOrEqualWithOffset(vesselVarsStart[i - 1], vesselVarsStart[i], c.contract.maxInterval);
					}
				} else {
					Pair<@NonNull LocalDate, LocalDate> datePair = latestDatePairs.get(c.contract);
					if (datePair != null) {
						final LocalDate earliestDate = datePair.getFirst();
						final int latestLoadDateValue = getDateValue(earliestDate);
						if (c.contract.fob) {
							model.addGreaterOrEqual(vesselVarsStart[i], latestLoadDateValue + c.contract.interval);
							if (c.contract.maxInterval != Integer.MAX_VALUE) {
								model.addLessOrEqual(vesselVarsStart[i], latestLoadDateValue + c.contract.maxInterval);
							}
						} else {
							model.addGreaterOrEqual(vesselVarsStart[i], latestLoadDateValue);
						}
					}
				}

				// Ensure for example, that JERA cargoes are spaced 30 days apart even though same ship.
				if (c.contract.contractSpacing > 0) {
					// Instead make sure that the load dates (since we just generated discharge dates relative to these anyway) are the correct distance apart per contract.
					IntVar endSpacing = model.newIntVar(startTime, lastLoadDate + c.contract.contractSpacing, "end-" + v.name + "-" + c.contract + "-spacing-" + taskId);
					List<IntervalVar> spacingIntervals = contractToSpacingIntervals.computeIfAbsent(c.contract.name, k -> new ArrayList<IntervalVar>());
					spacingIntervals.add(model.newIntervalVar(vesselVarsStart[i], c.contract.contractSpacing - contractIntervalFlex, endSpacing,
							"contract-spacing-interval" + v.name + "-" + c.contract + "-" + spacingIntervals.size()));
				}
				contractVesselLoadVars.add(vesselVarsStart[i]);
				vesselLoadVars.add(vesselVarsStart[i]);
				contractLoadVars.add(vesselVarsStart[i]);

				final IntVar vesselStart = vesselVarsStart[i];

				final Map<DryDockEvent, Map<Port, Integer>> map = fromDryDockBallastTravelTimes.get(v.name.toLowerCase());
				if (map != null) {
					final Map<DryDockEvent, IntVar> dryDockSelectionVars = new HashMap<>();
					for (final Entry<DryDockEvent, Map<Port, Integer>> entry : map.entrySet()) {
						final DryDockEvent dryDockEvent = entry.getKey();
						final String selectionVarName = String.format("DD-%s-isBefore-%s-%d", dryDockEvent.getName(), v.name, i);
						final IntVar beforeAfterSelection = model.newBoolVar(selectionVarName);
						dryDockSelectionVars.put(dryDockEvent, beforeAfterSelection);

						final IntVar dryDockEnd = dryDockIntervalVarsMap.get(dryDockEvent).getThird();
						for (int loadIndex = 0; loadIndex < numLoadSlots; ++loadIndex) {
							final Port loadPort = sortedLoadSlots.get(loadIndex).getPort();
							final IntVar cargoLoadSlotSelection = cargoToLoadSlotSelectionVars[cargoIndices.get(c)][loadIndex];
							final int travelTime = entry.getValue().get(loadPort);
							// Only add constraint if it's needed
							if (travelTime > 0) {
								final LinearExpr linearExpr = LinearExpr.scalProd(new IntVar[] { dryDockEnd, vesselStart }, new int[] { 1, -1 });
								model.addLessOrEqual(linearExpr, -travelTime).onlyEnforceIf(new Literal[] { beforeAfterSelection, cargoLoadSlotSelection });
							}
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
						if (travelTime > 0) {
							final LinearExpr linearExpr = LinearExpr.scalProd(new IntVar[] { vesselVarsEnd[i], dryDockStart }, new int[] { 1, -1 });
							model.addLessOrEqual(linearExpr, -travelTime).onlyEnforceIf(beforeAfterSelection.not());
						}
					}
				}

				// Load spacing
				loadVarsStart[taskId] = vesselVarsStart[i]; // Start is same as the cargo.
				if (scenario == Scenario.Scratch) {
					loadVarsEnd[taskId] = model.newIntVar(startTime, lastLoadDate, v.name + "-task-end-" + taskId);
					loadVarsIntervals[taskId] = model.newIntervalVar(loadVarsStart[taskId], loadInterval, loadVarsEnd[taskId], v.name + "-task-interval-" + taskId);

				}
				// The end of each cargo, so we can minimise the schedule span.
				cargoEnd[taskId] = vesselVarsEnd[i];

				taskId++;
			}

			// No overlapping cargoes on the vessel
			model.addNoOverlap(vesselVarsInterval);

			timesVar[idx] = vesselVarsStart;
		}

		if (scenario == Scenario.Scratch) {
			// No overlapping loads
			model.addNoOverlap(loadVarsIntervals);
		} else {
			// All load variables must take different start dates.
			model.addAllDifferent(loadVarsStart);
		}

		dryDockIntervalVars2.values().stream().filter(l -> l.size() > 1).forEach(l -> model.addNoOverlap(l.toArray(new IntervalVar[l.size()])));

		// Search for load variable's values first.
		// model.addDecisionStrategy(loadVarsStart,
		// VariableSelectionStrategy.CHOOSE_FIRST,
		// DomainReductionStrategy.SELECT_MIN_VALUE);

		// Add contract spacing intervals
		for (String contractName : contractToSpacingIntervals.keySet()) {
			List<IntervalVar> spacingIntervals = contractToSpacingIntervals.get(contractName);

			if (spacingIntervals != null) {
				model.addNoOverlap(spacingIntervals.toArray(new IntervalVar[spacingIntervals.size()]));
			}

			Contract contract = contracts.get(contractName);

			if (contract.maxContractSpacing != Integer.MAX_VALUE) {

				// System.out.println("Adding max constraint spacing constraint for " + contract.name + ":");

				List<IntVar> loadVars = contractToLoadVars.get(contractName);
				HashMap<String, List<IntVar>> contractVesselLoadVars = contractToVesselToLoadVars.get(contractName);
				try {
					long[][] allowedTuples = generateAllowedMaxContractSpacingValues(contractVesselLoadVars, loadVars, contract.contractSpacing, contract.maxContractSpacing);
					IntVar[] loadVarsArray = loadVars.toArray(new IntVar[loadVars.size()]);
					model.addAllowedAssignments(loadVarsArray, allowedTuples);
				} catch (Exception ex) {
					ex.printStackTrace(System.err);
				}
			}
		}

		addRateabilityConstraints(model, contractRateabilityConstraints, contractToVesselLoadVars, firstLoadDate);

		IntVar lastLoad = model.newIntVar(0, lastLoadDate, "lastLoad");
		IntVar firstLoad = model.newIntVar(0, lastLoadDate, "firstLoad");
		model.addMaxEquality(lastLoad, loadVarsStart);
		model.addMinEquality(firstLoad, loadVarsStart);

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

		// do {
		// solver.getParameters().setMaxTimeInSeconds(maxTime);
		// solve = solver.solve(model);
		// System.out.println(solver.wallTime());
		// maxTime -= solver.wallTime();
		// // Make sure extra CpSolverStatus are added
		// } while ((solve != CpSolverStatus.FEASIBLE && solve != CpSolverStatus.OPTIMAL));

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

		final boolean hasSingleElementLists = cargoAssignments.values().stream().allMatch(l -> l.size() == 1);
		final Set<Integer> loadElems = cargoAssignments.values().stream().map(l -> l.get(0)).collect(Collectors.toSet());
		final boolean loadElemsDistinct = loadElems.size() == numCargoes;
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

		// exportToLingoCSVFiles(vessels, loadInterval, timesVar, solver);
		// dumpSolutionToYaml(vessels, timesVar, latestLoadDates, solver);
		final Command modelPopulationCommand = createModelPopulationCommands(vessels, timesVar, solver, sm, ed, dateLoadSlotMap, cargoToLoadSlotMap, usedLoadSlotsSet, sdp);

		if (solve == CpSolverStatus.OPTIMAL) {
			return new Optimal(modelPopulationCommand);
		} else {
			return new Feasible(modelPopulationCommand);
		}
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

	private static LocalDate getEndWindowDateInclusive(final Slot<?> slot) {
		if (slot.isSetWindowSize() && slot.isSetWindowSizeUnits() && slot.getWindowSize() > 0) {
			if (slot.getWindowSizeUnits() == TimePeriod.DAYS) {
				return slot.getSchedulingTimeWindow().getStart().plusDays(slot.getWindowSize()).minusHours(1).toLocalDate();
			} else if (slot.getWindowSizeUnits() == TimePeriod.HOURS) {
				return slot.getSchedulingTimeWindow().getStart().plusHours(slot.getWindowSize()).toLocalDate();
			} else if (slot.getWindowSizeUnits() == TimePeriod.MONTHS) {
				return slot.getSchedulingTimeWindow().getStart().plusMonths(slot.getWindowSize()).minusHours(1).toLocalDate();
			} else {
				throw new IllegalStateException("Unknown time period");
			}
		}
		return slot.getWindowStart();
	}

	private Map<Contract, @NonNull Pair<@NonNull LocalDate, LocalDate>> getLatestPreAdpContractDatePairs(final LNGScenarioModel sm, final Map<String, Contract> expectedContracts,
			final ModelDistanceProvider modelDistanceProvider) {
		final LocalDate adpStart = sm.getAdpModel().getYearStart().atDay(1);
		final Map<String, SalesContract> eContractsMap = sm.getReferenceModel().getCommercialModel().getSalesContracts().stream().collect(Collectors.toMap(NamedObject::getName, Function.identity()));
		final Map<Contract, @NonNull Pair<@NonNull LocalDate, LocalDate>> datePairs = new HashMap<>();
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
					final LocalDate earliestLoadDate = loadSlot.getWindowStart();
					final LocalDate latestLoadDate = getEndWindowDateInclusive(loadSlot);
					final LocalDate earliestDischargeDate = dischargeSlot.getWindowStart();
					final LocalDate latestDischargeDate = dischargeSlot.getWindowStart();

					final LocalDate earliestDate = earliestLoadDate.compareTo(earliestDischargeDate) == 1 ? earliestLoadDate : earliestDischargeDate;
					final LocalDate latestDate = latestLoadDate.compareTo(latestDischargeDate) == -1 ? latestLoadDate : latestDischargeDate;
					datePairs.put(expectedContractEntry.getValue(), Pair.of(earliestDate, latestDate));
				} else {
					final LoadSlot loadSlot = (LoadSlot) eCargo.getSlots().get(0);
					final DischargeSlot dischargeSlot = (DischargeSlot) eCargo.getSlots().get(1);
					final int ladenTravelTime = CargoTravelTimeUtils.getFobMinTimeInHours(loadSlot, dischargeSlot, loadSlot.getWindowStart(), eCargo.getVesselAssignmentType(),
							ScenarioModelUtil.getPortModel(sm), 0.0, modelDistanceProvider);
					final int ballastTravelTime = CargoTravelTimeUtils.getFobMinTimeInHours(dischargeSlot, loadSlot, dischargeSlot.getWindowStart(), eCargo.getVesselAssignmentType(),
							ScenarioModelUtil.getPortModel(sm), 0.0, modelDistanceProvider);
					ZonedDateTime travelTimeTracker = loadSlot.getSchedulingTimeWindow().getStart();
					travelTimeTracker = travelTimeTracker.plusHours(loadSlot.getDuration());
					travelTimeTracker = travelTimeTracker.plusHours(ladenTravelTime);

					final ZonedDateTime dischargeArrivalEarliestTime = travelTimeTracker.withZoneSameInstant(dischargeSlot.getPort().getZoneId());
					if (dischargeArrivalEarliestTime.isBefore(dischargeSlot.getSchedulingTimeWindow().getStart())) {
						travelTimeTracker = dischargeSlot.getSchedulingTimeWindow().getStart();
					} else {
						travelTimeTracker = dischargeArrivalEarliestTime;
					}
					travelTimeTracker = travelTimeTracker.plusHours(dischargeSlot.getDuration());
					travelTimeTracker = travelTimeTracker.plusHours(ballastTravelTime);
					final ZonedDateTime earliestRoundTripReturnZdt = travelTimeTracker.withZoneSameInstant(loadSlot.getPort().getZoneId());
					final LocalDateTime earliestRoundTripReturnLdt = earliestRoundTripReturnZdt.toLocalDateTime();
					final LocalDate earliestRoundTripReturnDate = earliestRoundTripReturnLdt.getHour() > 0 ? earliestRoundTripReturnLdt.plusDays(1).toLocalDate()
							: earliestRoundTripReturnLdt.toLocalDate();
					datePairs.put(expectedContractEntry.getValue(), Pair.of(earliestRoundTripReturnDate, null));
				}
			}
		}
		return datePairs;
	}

	private static long[][] generateAllowedMaxContractSpacingValues(HashMap<String, List<IntVar>> vesselToLoadVars, List<IntVar> loadVars, int minContractSpacing, int maxContractSpacing) {
		List<List<Long>> tuples = generateAllowedMaxContractSpacingValues(loadVars, new ArrayList<>(loadVars.size()), minContractSpacing, maxContractSpacing);

		List<long[]> distTuples = new ArrayList<>(tuples.size());

		for (int t = 0; t < tuples.size(); t++) {
			List<Long> tuple = tuples.get(t);
			// System.out.print(tuple+" -> ");
			// List<long[]> distTuples2 = generatePermutations(tuple);
			// List<long[]> distTuples2 = distributeLoads(loadVars, tuple, vesselToLoadVars);
			List<long[]> distTuples2 = generateVesselPerms(tuple, loadVars, vesselToLoadVars);
			/*
			 * System.out.print(distTuples2.size()+" "); for (long[] distTuple : distTuples2) { System.out.print("["); for (int j = 0; j < distTuple.length; j++) { if (j != 0) System.out.print(",");
			 * System.out.print(" "+distTuple[j]); } System.out.print("] "); } System.out.println();
			 */
			distTuples.addAll(distTuples2);
		}

		// if (distTuples.size() == 0) {
		// System.out.println("Unsatisfiable contract spacing requirement.");
		// } else {
		// System.out.println(distTuples.size() + " allowed " + loadVars.size() + "-tuples added to create constraint.");
		// }
		long[][] tuplesArray = new long[distTuples.size()][loadVars.size()];
		for (int i = 0; i < distTuples.size(); i++) {
			long[] tuple = distTuples.get(i);
			for (int j = 0; j < tuple.length; j++) {
				tuplesArray[i][j] = tuple[j];
			}
		}
		return tuplesArray;
	}

	private static List<long[]> generateVesselPerms(List<Long> tuple, List<IntVar> loadVars, HashMap<String, List<IntVar>> vesselToLoadVars) {
		List<Long> perm = new ArrayList<>();
		HashMap<String, Pair<String, Integer>> loadVarsToVesselIndex = new HashMap<>();
		HashMap<String, Integer> loadVarsToTupleIndex = new HashMap<>();
		List<String> vessels = new ArrayList<>();
		for (String vessel : vesselToLoadVars.keySet()) {
			List<IntVar> vesselLoadVars = vesselToLoadVars.get(vessel);
			for (int i = 0; i < vesselLoadVars.size(); i++) {
				loadVarsToVesselIndex.put(vesselLoadVars.get(i).getName(), Pair.of(vessel, i));
			}
			vessels.add(vessel);
		}
		for (int i = 0; i < loadVars.size(); i++) {
			loadVarsToTupleIndex.put(loadVars.get(i).getName(), i);
		}
		return generateVesselPerms(tuple, perm, loadVarsToVesselIndex, loadVarsToTupleIndex, vessels, loadVars, vesselToLoadVars);
	}

	private static List<long[]> generateVesselPerms(List<Long> tuple, List<Long> perm, HashMap<String, Pair<String, Integer>> loadVarsToVesselIndex, HashMap<String, Integer> loadVarsToTupleIndex,
			List<String> vessels, List<IntVar> loadVars, HashMap<String, List<IntVar>> vesselToLoadVars) {
		if (perm.size() == tuple.size()) {
			// Full permutation done.
			long[] permArr = new long[perm.size()];
			for (int i = 0; i < perm.size(); i++) {
				permArr[i] = perm.get(i);
			}
			return Collections.singletonList(permArr);
		} else {
			List<long[]> tuplePerms = new ArrayList<>();

			for (int i = 0; i < tuple.size(); i++) {
				long val = tuple.get(i);
				if (!perm.contains(val)) {
					List<Long> nextPerm = new ArrayList<>(perm);
					nextPerm.add(val);

					if (isValidVesselSchedule(nextPerm, loadVars, loadVarsToVesselIndex)) {
						tuplePerms.addAll(generateVesselPerms(tuple, nextPerm, loadVarsToVesselIndex, loadVarsToTupleIndex, vessels, loadVars, vesselToLoadVars));
					}
				}
			}

			return tuplePerms;
		}
	}

	private static boolean isValidVesselSchedule(List<Long> perm, List<IntVar> loadVars, HashMap<String, Pair<String, Integer>> loadVarsToVesselIndex) {

		for (int i = 0; i < perm.size() - 1; i++) {
			IntVar v1 = loadVars.get(i);
			IntVar v2 = loadVars.get(i + 1);
			String v1Vessel = loadVarsToVesselIndex.get(v1.getName()).getFirst();
			String v2Vessel = loadVarsToVesselIndex.get(v2.getName()).getFirst();
			if (v1Vessel == v2Vessel) {
				long value1 = perm.get(i);
				long value2 = perm.get(i + 1);
				if (value1 > value2) {
					return false; // Out of sequence.
				}
			}
		}

		return true;
	}

	private static List<long[]> generatePermutations(List<Long> tuple) {
		List<Long> perm = new ArrayList<>();
		return generatePermutations(tuple, perm);
	}

	private static List<long[]> generatePermutations(List<Long> tuple, List<Long> perm) {
		if (perm.size() == tuple.size()) {
			// Full permutation done.
			long[] permArr = new long[perm.size()];
			for (int i = 0; i < perm.size(); i++) {
				permArr[i] = perm.get(i);
			}
			return Collections.singletonList(permArr);
		} else {
			List<long[]> tuplePerms = new ArrayList<>();

			for (int i = 0; i < tuple.size(); i++) {
				long val = tuple.get(i);
				if (!perm.contains(val)) {
					List<Long> nextPerm = new ArrayList<>(perm);
					nextPerm.add(val);
					tuplePerms.addAll(generatePermutations(tuple, nextPerm));
				}
			}

			return tuplePerms;
		}
	}

	private static List<long[]> distributeLoads(List<IntVar> loadVars, List<Long> tuple, HashMap<String, List<IntVar>> vesselToLoadVars) {
		HashMap<String, Pair<String, Integer>> loadVarsToVesselIndex = new HashMap<>();
		HashMap<String, Integer> loadVarsToTupleIndex = new HashMap<>();

		List<String> vessels = new ArrayList<>();
		for (String vessel : vesselToLoadVars.keySet()) {
			List<IntVar> vesselLoadVars = vesselToLoadVars.get(vessel);
			for (int i = 0; i < vesselLoadVars.size(); i++) {
				loadVarsToVesselIndex.put(vesselLoadVars.get(i).getName(), Pair.of(vessel, i));
			}
			vessels.add(vessel);
		}
		for (int i = 0; i < loadVars.size(); i++) {
			loadVarsToTupleIndex.put(loadVars.get(i).getName(), i);
		}

		List<long[]> distTuples = new ArrayList<>();

		for (int initVesselIdx = 0; initVesselIdx < vessels.size(); initVesselIdx++) {
			int vesselIdx = initVesselIdx;
			int cargoIdxs[] = new int[vessels.size()];
			long[] distTuple = new long[loadVars.size()];

			for (int i = 0; i < tuple.size(); i++) {
				String vessel = vessels.get(vesselIdx);
				List<IntVar> loadsOnVessel = vesselToLoadVars.get(vessel);
				if (cargoIdxs[vesselIdx] < loadsOnVessel.size()) {
					IntVar loadVar = vesselToLoadVars.get(vessel).get(cargoIdxs[vesselIdx]);
					Integer index = loadVarsToTupleIndex.get(loadVar.getName());
					if (index != null) {
						distTuple[index] = tuple.get(i);
						cargoIdxs[vesselIdx]++;
						vesselIdx = (vesselIdx + 1) % vessels.size();
					}
				} else {
					vesselIdx = (vesselIdx + 1) % vessels.size();
					i--; // Try again on next vessel.
				}
			}
			distTuples.add(distTuple);
		}

		return distTuples;
	}

	private static List<List<Long>> generateAllowedMaxContractSpacingValues(List<IntVar> loadVars, List<Long> tuple, int minContractSpacing, int maxContractSpacing) {
		if (tuple.size() == loadVars.size()) {
			return Collections.singletonList(tuple);
		} else {
			List<List<Long>> tuples = new ArrayList<>();
			IntVar loadVar = loadVars.get(tuple.size());
			List<Long> values = getValues(loadVar);
			assert values.size() == loadVar.getDomain().size();
			for (int v = 0; v < values.size(); v++) {
				long value = values.get(v);
				if (!tuple.contains(value)) {
					List<Long> nextTuple = new ArrayList<Long>(tuple);
					nextTuple.add(value);
					if (checkSpacingConstraints(nextTuple, minContractSpacing, maxContractSpacing)) {
						tuples.addAll(generateAllowedMaxContractSpacingValues(loadVars, nextTuple, minContractSpacing, maxContractSpacing));
					}
				}
			}
			return tuples;
		}
	}

	private static boolean checkSpacingConstraints(List<Long> tuple, long minSpacing, long maxSpacing) {
		for (int i = 0; i < tuple.size() - 1; i++) {
			long diff = tuple.get(i + 1) - tuple.get(i);
			if (diff < 0 || diff < minSpacing || diff > maxSpacing) {
				return false;
			}
		}
		return true;
	}

	private static List<Long> getValues(IntVar v) {
		Domain d = v.getDomain();
		long[] intervals = d.flattenedIntervals();
		List<Long> vals = new ArrayList<>();
		for (int i = 0; i < intervals.length; i += 2) {
			long mn = intervals[i];
			long mx = intervals[i + 1];
			for (long val = mn; val <= mx; val++) {
				vals.add(val);
			}
		}
		return vals;
	}

	private static IntVar createLoadVar(CpModel model, Vessel v, Contract c, int startTime, int endTime, int cargoNo, int desDeliveryTime, final List<LocalDate> loadDates) {
		String varName = v.name + "-start-" + c.name + "-" + cargoNo;
		if (scenario == Scenario.Scratch) {
			return model.newIntVar(startTime, endTime, varName);
		} else if (scenario == Scenario.Current) {
			List<Pair<Long, Long>> allowedDateValueRanges = getAllowedVesselLoadDates(v, desDeliveryTime);
			long[] dateValues = createDatesDomainValues(model, startTime, endTime, allowedDateValueRanges, loadDates);
			// System.out.println("Creating variable: " + varName + " with values: " + toString(dateValues));
			return model.newIntVarFromDomain(Domain.fromValues(dateValues), varName);
		} else {
			return null;
		}
	}

	private static List<Pair<Long, Long>> getAllowedVesselLoadDates(Vessel v, int desDeliveryTime) {
		List<Pair<Long, Long>> allowedDateValueRanges = new ArrayList<>();
		if (v.allowedDateRanges != null) {
			for (Pair<LocalDate, LocalDate> range : v.allowedDateRanges) {
				long from = getDateValue(range.getFirst());
				long to = getDateValue(range.getSecond()) - desDeliveryTime;
				if (from < to) {
					Pair<Long, Long> valuesRange = Pair.of(from, to);
					allowedDateValueRanges.add(valuesRange);
				}
			}
		}
		return allowedDateValueRanges;
	}

	private static long[] createDatesDomainValues(CpModel model, int startTime, int endTime, List<Pair<Long, Long>> allowedIntervals, List<LocalDate> loadDates) {
		long[] dateValues = getDateValues(loadDates);
		dateValues = filterWithinRange(startTime, endTime, dateValues);
		if (allowedIntervals != null && !allowedIntervals.isEmpty()) {
			dateValues = filterWithinRanges(allowedIntervals, dateValues);
		}
		return dateValues;
	}

	private static String toString(long[] array) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < array.length; i++) {
			if (i != 0) {
				sb.append(",");
			}
			sb.append(array[i]);
		}
		sb.append("]");
		return sb.toString();
	}

	private static long[] filterWithinRanges(List<Pair<Long, Long>> allowedIntervals, long[] values) {
		List<Long> filtered = new ArrayList<>(values.length);
		for (long v : values) {
			if (withinIntervals(v, allowedIntervals)) {
				filtered.add(v);
			}
		}
		values = new long[filtered.size()];
		for (int i = 0; i < filtered.size(); i++) {
			values[i] = filtered.get(i);
		}
		return values;
	}

	private static boolean withinIntervals(long value, List<Pair<Long, Long>> allowedIntervals) {
		// Unbounded case.
		if (allowedIntervals == null || allowedIntervals.isEmpty()) {
			return true;
		}
		// Restricted case.
		for (Pair<Long, Long> range : allowedIntervals) {
			if (value >= range.getFirst() && value <= range.getSecond()) {
				return true;
			}
		}
		// Not within any allowed interval range.
		return false;
	}

	private static long[] filterWithinRange(int startTime, int endTime, long[] values) {
		List<Long> filtered = new ArrayList<>(values.length);
		for (long v : values) {
			if (v >= startTime && v <= endTime) {
				filtered.add(v);
			}
		}
		values = new long[filtered.size()];
		for (int i = 0; i < filtered.size(); i++) {
			values[i] = filtered.get(i);
		}
		return values;
	}

	private static long[] getDateValues(List<LocalDate> dates) {
		long[] dateValues = new long[dates.size()];
		for (int i = 0; i < dates.size(); i++) {
			dateValues[i] = getDateValue(dates.get(i));
		}
		return dateValues;
	}

	private static int getDateValue(@NonNull LocalDate date) {
		return Days.between(FIRST_DATE, date);
	}

	private static List<LocalDate> parseDates(String[] dateStrs) {
		List<LocalDate> dates = new ArrayList<>();
		for (String dateStr : dateStrs) {
			LocalDate date = parseDate(dateStr);
			dates.add(date);
		}
		return dates;
	}

	private static LocalDate parseDate(String dateStr) {
		if (dateStr.charAt(1) == '-') {
			dateStr = "0" + dateStr;
		}
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
		return LocalDate.parse(dateStr, dtf);
	}

	private void dumpSolutionToYaml(final Vessel[] vessels, final IntVar[][] timesVar, final Map<Contract, @NonNull LocalDate> latestLoadDates, final CpSolver solver) {
		final Map<Contract, TreeSet<LocalDate>> contractToLocalDates = new HashMap<>();

		for (int vv = 0; vv < vessels.length; ++vv) {
			final int vesselIdx = vv;
			// TreeSet<Cargo> vesselTimes = new TreeSet<>();
			for (int ii = 0; ii < timesVar[vv].length; ++ii) {
				long t = solver.value(timesVar[vv][ii]);
				Cargo c = vessels[vv].cargoes[ii];
				c.dayOfSchedule = t;
				final LocalDate loadDate = getDate(c.dayOfSchedule);
				contractToLocalDates.computeIfAbsent(c.contract, con -> new TreeSet<>()).add(loadDate);
				// vesselTimes.add(c);
			}
		}

		for (final Entry<Contract, @NonNull LocalDate> entry : latestLoadDates.entrySet()) {
			contractToLocalDates.computeIfAbsent(entry.getKey(), con -> new TreeSet<>()).add(entry.getValue());
		}
		final List<Pair<Contract, List<LocalDate>>> sortedPairs = contractToLocalDates.entrySet().stream().map(e -> Pair.of(e.getKey(), (List<LocalDate>) new ArrayList<>(e.getValue())))
				.sorted((p1, p2) -> p1.getFirst().name.compareTo(p2.getFirst().name)).collect(Collectors.toList());
		final File outputYml = new File("C:\\Users\\miten\\mmxlabs\\R\\ADP\\spacing_yaml_dumps\\dump.yml");
		try {
			final FileWriter writer = new FileWriter("C:\\Users\\miten\\mmxlabs\\R\\ADP\\spacing_yaml_dumps\\dump.yml");
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			final String tabToSpace = "    ";
			for (final Pair<Contract, List<LocalDate>> pair : sortedPairs) {
				writer.write(pair.getFirst().name);
				writer.write(":\n");
				writer.write(tabToSpace + "minSpace: ");
				writer.write(Integer.toString(pair.getFirst().interval));
				writer.write("\n");
				writer.write(tabToSpace + "maxSpace: ");
				writer.write(Integer.toString(pair.getFirst().maxInterval));
				writer.write("\n");
				writer.write(tabToSpace + "dates: ");
				if (pair.getSecond().isEmpty()) {
					writer.write("[]\n");
				} else {
					writer.write("\n");
					for (final LocalDate date : pair.getSecond()) {
						writer.write(tabToSpace + tabToSpace + "- \"");
						writer.write(date.format(formatter));
						writer.write("\"\n");
					}
				}

			}
			writer.close();
		} catch (IOException e) {
			System.out.println("A yaml dumping error occurred.");
		}
	}

	private static @NonNull Command createModelPopulationCommands(Vessel[] vessels, IntVar[][] timesVar, CpSolver solver, final LNGScenarioModel sm, final EditingDomain ed,
			final Map<LocalDate, LoadSlot> dateToLoadSlotMap, Map<Cargo, LoadSlot> cargoToLoadSlotMap, Set<LoadSlot> usedLoadSlotsSet, final IScenarioDataProvider sdp) {
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

			Cargo prevCargo = null;
			ArrayList<Cargo> sortedCargoes = new ArrayList<>(vesselTimes);
			for (int i = 0; i < sortedCargoes.size(); i++) {
				Cargo t = sortedCargoes.get(i);
				String cargoID = String.format("load-%02d", cargoId);
				if (first) {
					first = false;
				} else {

				}
				LocalDate loadDate = getDate(t.dayOfSchedule);
				// final LoadSlot loadSlot = dateToLoadSlotMap.get(loadDate);
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
						final String portName = t.contract.port == null ? "Himeji" : t.contract.port;
						dischargeSlot.setPort(sm.getReferenceModel().getPortModel().getPorts().stream().filter(p -> p.getName().equalsIgnoreCase(portName)).findAny().get());
						final com.mmxlabs.models.lng.fleet.Vessel vess = va instanceof VesselAvailability ? ((VesselAvailability) va).getVessel() : ((CharterInMarket) va).getVessel();
						final LocalDate dischargeDate = calculateDischargeDate(loadSlot, dischargeSlot, vess, sdp, sm);
						int ladenLegLength = t.contract.interval / 2;
						if (balanceLegs && i < sortedCargoes.size() - 1) {
							Cargo nextLoad = sortedCargoes.get(i + 1);
							long nextLoadDay = nextLoad.dayOfSchedule;
							long balancedDischargeDay = t.dayOfSchedule + ((nextLoadDay - t.dayOfSchedule) / 2);
							if (withinIntervals(balancedDischargeDay, getAllowedVesselLoadDates(vessels[vv], ladenLegLength))
									&& (t.maxDischargeDate == null || balancedDischargeDay <= getDateValue(t.maxDischargeDate))
									&& (t.minDischargeDate == null || balancedDischargeDay >= getDateValue(t.minDischargeDate))) {
								ladenLegLength = (int) (nextLoadDay - t.dayOfSchedule) / 2;
							}
						}
						// Create cargo
						dischargeSlot.setWindowStart(dischargeDate);
						dischargeSlot.setWindowStartTime(0);
						dischargeSlot.unsetWindowStartTime();
						dischargeSlot.setWindowSize(1);
						dischargeSlot.setWindowSizeUnits(TimePeriod.MONTHS);

					} else {
						dischargeSlot.setWindowStart(loadDate);
						dischargeSlot.setPort(loadSlot.getPort());
						dischargeSlot.setNominatedVessel(vesselNameMap.get(vessels[vv].name.toLowerCase()));
					}
					// loadSlotsToAdd.add(loadSlot);
					dischargeSlotsToAdd.add(dischargeSlot);
					cargoesToAdd.add(cargo);
				}
				++cargoId;
			}

		}
		// cmd.append(AddCommand.create(ed, sm.getCargoModel(), CargoPackage.Literals.CARGO_MODEL__LOAD_SLOTS, loadSlotsToAdd));
		cmd.append(AddCommand.create(ed, sm.getCargoModel(), CargoPackage.Literals.CARGO_MODEL__DISCHARGE_SLOTS, dischargeSlotsToAdd));
		cmd.append(AddCommand.create(ed, sm.getCargoModel(), CargoPackage.Literals.CARGO_MODEL__CARGOES, cargoesToAdd));
		return cmd;
	}

	private static void exportToLingoCSVFiles(Vessel[] vessels, int loadInterval, IntVar[][] timesVar, CpSolver solver) throws FileNotFoundException, IOException {
		List<String> headers = new LinkedList<>();
		headers.add("buy.name");
		headers.add("sell.name");
		headers.add("buy.date");
		headers.add("sell.date");
		headers.add("sell.fob");
		headers.add("buy.contract");
		headers.add("sell.contract");
		headers.add("sell.nominatedvessel");

		headers.add("buy.entity");
		headers.add("sell.entity");

		// Other defaults
		headers.add("buy.port");
		headers.add("sell.port");

		BufferedWriter cargoCSV = new BufferedWriter(new PrintWriter("C:\\temp\\adp\\Cargoes.csv"));
		cargoCSV.append(String.join(",", headers));
		cargoCSV.append("\n");

		BufferedWriter assignmentsCSV = new BufferedWriter(new PrintWriter("C:\\temp\\adp\\Assignments.csv"));
		assignmentsCSV.append("vesselassignment,spotindex,charterindex,assignedobjects\n");

		{
			// Determine last discharge date
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
			// System.out.printf("Min %d load interval\n", loadInterval);

			int cargoId = 1;
			for (int vv = 0; vv < vessels.length; ++vv) {
				assignmentsCSV.append(vessels[vv].name);
				assignmentsCSV.append(",");
				assignmentsCSV.append(vessels[vv].spotIndex);
				assignmentsCSV.append(",1,\"");

				// objVar
				// System.out.printf("%s %s (%s - %d avg interval)", vessels[vv].name, getContractsString(vessels[vv]), vessels[vv].cargoes[0].contract.fob ? "FOB" : "DES",
				// Cargo.getAverageInterval(vessels[vv].cargoes));
				TreeSet<Cargo> vesselTimes = new TreeSet<>();

				for (int ii = 0; ii < timesVar[vv].length; ++ii) {
					long t = solver.value(timesVar[vv][ii]);// timesVar[vv][ii].getCoefficient(0);//getvalue();
					Cargo c = vessels[vv].cargoes[ii];
					c.dayOfSchedule = t;
					vesselTimes.add(c);
					allTimes.add(t);
				}
				boolean first = true;

				Cargo prevCargo = null;

				ArrayList<Cargo> sortedCargoes = new ArrayList<>(vesselTimes);

				// for (Cargo t : vesselTimes) {
				for (int i = 0; i < sortedCargoes.size(); i++) {

					Cargo t = sortedCargoes.get(i);
					String cargoID = String.format("load-%02d", cargoId);
					if (first) {
						first = false;
					} else {
						assignmentsCSV.append(",");
					}

					if (!t.contract.fob) {
						assignmentsCSV.append(cargoID);
					}

					LocalDate loadDate = getDate(t.dayOfSchedule);
					// System.out.printf(", (L) %s", loadDate);
					if (!t.contract.fob) {
						int ladenLegLength = t.contract.fob ? 0 : t.contract.interval / 2;
						if (balanceLegs && i < sortedCargoes.size() - 1) {
							Cargo nextLoad = sortedCargoes.get(i + 1);
							long nextLoadDay = nextLoad.dayOfSchedule;
							long balancedDischargeDay = t.dayOfSchedule + ((nextLoadDay - t.dayOfSchedule) / 2);

							// If within allowed intervals for vessel.
							if (withinIntervals(balancedDischargeDay, getAllowedVesselLoadDates(vessels[vv], ladenLegLength))
									&& (t.maxDischargeDate == null || balancedDischargeDay <= getDateValue(t.maxDischargeDate))
									&& (t.minDischargeDate == null || balancedDischargeDay >= getDateValue(t.minDischargeDate))) {
								ladenLegLength = (int) (nextLoadDay - t.dayOfSchedule) / 2;
							}
						}
						// System.out.printf(", (D) %s", loadDate.plusDays(ladenLegLength));
						printRow(cargoId, vessels[vv].name, t.contract.fob, t.contract.name, t.contract.port, loadDate, loadDate.plusDays(ladenLegLength), cargoCSV, headers);
					} else {
						printRow(cargoId, vessels[vv].name, t.contract.fob, t.contract.name, t.contract.port, loadDate, loadDate, cargoCSV, headers);
					}
					// Assert round trip interval
					if (prevCargo != null) {
						assert t.dayOfSchedule - prevCargo.dayOfSchedule >= prevCargo.contract.interval;
						if (t.dayOfSchedule - prevCargo.dayOfSchedule >= prevCargo.contract.maxInterval) {
							System.err.println("\nLong discharge interval: " + (t.dayOfSchedule - prevCargo.dayOfSchedule) + " days");
						}
					}
					prevCargo = t;

					++cargoId;
				}
				assignmentsCSV.append("\",\n");
				// System.out.println();
			}

			long lastT = Long.MIN_VALUE;
			for (long t : allTimes) {
				// Assert min load interval
				if (scenario == Scenario.Scratch && lastT != Long.MIN_VALUE) {
					assert t - lastT >= loadInterval;
					if (t - lastT >= loadInterval + 10) {
						// System.err.println("Long load interval: "+(t-lastT)+" days");
					}
				}
				lastT = t;
			}
		}

		assignmentsCSV.close();
		cargoCSV.close();
	}

	private static String getContractsString(Vessel vessel) {
		HashSet<String> contracts = new HashSet<>();
		for (Cargo c : vessel.cargoes) {
			contracts.add(c.contract.name);
		}
		return contracts.toString();
	}

	private static void addRateabilityConstraints(final CpModel model, final Map<String, RateabilityConstraint> contractRateabilityConstraints,
			final HashMap<String, List<List<IntVar>>> contractToLoadVars, final int firstLoadDate) {
		for (final Entry<String, RateabilityConstraint> entry : contractRateabilityConstraints.entrySet()) {
			final String contractName = entry.getKey();
			final RateabilityConstraint rateabilityConstraint = entry.getValue();
			List<List<IntVar>> loadVarsForContract = contractToLoadVars.get(contractName);
			int totalCargoes = sumSizes(loadVarsForContract);

			if (totalCargoes > (12 / rateabilityConstraint.everyNMonths) && totalCargoes > 0) {
				int periodsPerYear = (12 / rateabilityConstraint.everyNMonths);
				int daysInYear = LocalDate.of(2021, 1, 1).lengthOfYear();
				double cargoAtLeastEveryNDays = ((double) daysInYear) / ((double) totalCargoes);
				int maxDaysBetweenCargoes = daysInYear / periodsPerYear;
				int firstT = firstLoadDate - rateabilityConstraint.minusDays;
				int c = 1;
				int startT = firstT;
				IntVar lastLoadVar = null;
				for (int i = 0; i < maxSize(loadVarsForContract); i++) {
					for (int v = 0; v < loadVarsForContract.size(); v++) {
						List<IntVar> loadVars = loadVarsForContract.get(v);

						if (i < loadVars.size()) {
							IntVar loadVar = loadVars.get(i);
							model.addGreaterOrEqual(loadVar, startT);
							int endT = firstT + (int) Math.floor(c * cargoAtLeastEveryNDays) - 1;
							model.addLessOrEqual(loadVar, endT);

							// System.out.println("Set " + loadVar.getName() + " [" + getDate(startT) + "," + getDate(endT) + "]");
							startT = endT + 1;
							c++;
							// Ensure at least one cargo delivered every month, by ensuring no more than 1 month apart.
							if (lastLoadVar != null) {
								// left + offset >= right
								// loadVar - lastLoadVar <= maxDaysBetweenCargoes.
								// => lastLoadVar + maxDaysBetweenCargoes >= loadVar
								model.addGreaterOrEqualWithOffset(loadVar, lastLoadVar, maxDaysBetweenCargoes);
							}
							lastLoadVar = loadVar;
						}
					}
				}
			} else {
				int startT = firstLoadDate - rateabilityConstraint.minusDays;
				int m = rateabilityConstraint.everyNMonths;
				for (int i = 0; i < maxSize(loadVarsForContract); i++) {
					for (int v = 0; v < loadVarsForContract.size(); v++) {
						List<IntVar> loadVars = loadVarsForContract.get(v);

						if (i < loadVars.size() && m <= 12) {
							int endT = firstLoadDate + computeNMonthsInDays(2021, m) - (1 + rateabilityConstraint.minusDays);
							IntVar loadVar = loadVars.get(i);
							model.addGreaterOrEqual(loadVar, startT);
							model.addLessOrEqual(loadVar, endT);
							// System.out.println("Set " + loadVar.getName() + " [" + getDate(startT) + "," + getDate(endT) + "]");
							m += rateabilityConstraint.everyNMonths;
							startT = endT + 1;
						}
					}
				}
			}
		}
	}

	private static int getMonth(int time) {
		LocalDate date = getDate(time);
		return date.getMonthValue();
	}

	private static int sumSizes(List<List<IntVar>> twoDimList) {
		return twoDimList.stream().mapToInt(List::size).sum();
	}

	private static int maxSize(List<List<IntVar>> twoDimList) {
		return twoDimList.stream().mapToInt(List::size).max().orElse(0);
	}

	static LocalDate getDate(long t) {
		return FIRST_DATE.plusDays(t);
	}

	static int computeNMonthsInDays(int year, int month) {
		LocalDate date = LocalDate.of(year, month, 1);
		date = date.plusMonths(1);
		date = date.minusDays(1);
		return date.getDayOfYear();
	}

	static void printRow(int cargoId, String vesselName, boolean fob, String contract, String port, LocalDate loadDate, LocalDate dischargeDate, BufferedWriter writer, List<String> headers)
			throws IOException {
		Map<String, String> row = printRow(cargoId, vesselName, fob, contract, port, loadDate, dischargeDate);
		for (String key : headers) {
			writer.append(row.getOrDefault(key, ""));
			writer.append(",");
		}
		writer.append("\n");

	}

	static Map<String, String> printRow(int cargoId, String vesselName, boolean fob, String contract, String port, LocalDate loadDate, LocalDate dischargeDate) {

		Map<String, String> map = new HashMap<>();
		// From data
		map.put("buy.name", String.format("load-%02d", cargoId));
		map.put("sell.name", String.format("discharge-%02d", cargoId));
		map.put("buy.date", String.format("%04d-%02d-%02d", loadDate.getYear(), loadDate.getMonthValue(), loadDate.getDayOfMonth()));
		map.put("sell.date", String.format("%04d-%02d-%02d", dischargeDate.getYear(), dischargeDate.getMonthValue(), dischargeDate.getDayOfMonth()));
		map.put("sell.fob", Boolean.toString(fob));

		// Other defaults
		map.put("buy.port", "Cameron");
		if (fob) {
			map.put("sell.port", "Cameron");
			map.put("sell.nominatedvessel", vesselName);
		} else {
			if (port != null) {
				map.put("sell.port", port);
			} else {
				map.put("sell.port", "Himeji");
			}
		}

		map.put("buy.contract", "MCGG Cameron Volumes");
		map.put("sell.contract", contract);

		map.put("buy.entity", "DGI");
		map.put("sell.entity", "DGI");

		return map;
	}
	//
	//
	// smae for assingmnet csv

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
