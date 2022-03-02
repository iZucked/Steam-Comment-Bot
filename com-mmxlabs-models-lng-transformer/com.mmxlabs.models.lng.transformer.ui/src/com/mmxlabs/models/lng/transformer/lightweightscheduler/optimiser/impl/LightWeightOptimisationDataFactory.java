/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.impl;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Injector;
import com.google.inject.name.Named;
import com.mmxlabs.common.concurrent.JobExecutorFactory;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.PeriodDistribution;
import com.mmxlabs.models.lng.adp.PeriodDistributionProfileConstraint;
import com.mmxlabs.models.lng.adp.ProfileConstraint;
import com.mmxlabs.models.lng.adp.utils.ADPModelUtil;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.LightWeightSchedulerStage2Module;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ICargoToCargoCostCalculator;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ICargoVesselRestrictionsMatrixProducer;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ILightWeightOptimisationData;
import com.mmxlabs.models.lng.transformer.optimiser.common.AbstractOptimiserHelper.ShippingType;
import com.mmxlabs.models.lng.transformer.optimiser.pairing.IPairingMatrixOptimiser;
import com.mmxlabs.models.lng.transformer.optimiser.pairing.PairingOptimisationData;
import com.mmxlabs.models.lng.transformer.optimiser.pairing.PairingOptimiserHelper;
import com.mmxlabs.models.lng.transformer.optimiser.valuepair.LoadDischargePairValueCalculatorStep;
import com.mmxlabs.models.lng.transformer.optimiser.valuepair.ProfitAndLossExtractor;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.exceptions.InfeasibleSolutionException;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.util.MonthlyDistributionConstraint.Row;
import com.mmxlabs.scheduler.optimiser.providers.ConstraintInfo;
import com.mmxlabs.scheduler.optimiser.providers.ConstraintInfo.ViolationType;
import com.mmxlabs.scheduler.optimiser.providers.ILongTermSlotsProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVirtualVesselSlotProvider;

public class LightWeightOptimisationDataFactory {

	@Inject
	private PairingOptimisationData<?,?> optimiserRecorder;
	@Inject
	private ILongTermSlotsProvider longTermSlotsProvider;
	@Inject
	private IPairingMatrixOptimiser matrixOptimiser;
	@Inject
	private IPortSlotProvider portSlotProvider;
	@Inject
	private IVesselProvider vesselProvider;
	@Inject
	private IVirtualVesselSlotProvider virtualVesselProvider;
	@Inject
	private ICargoToCargoCostCalculator cargoToCargoCostCalculator;
	@Inject
	private ICargoVesselRestrictionsMatrixProducer cargoVesselRestrictionsMatrixProducer;
	@Inject
	private @Named(LightWeightSchedulerStage2Module.LIGHTWEIGHT_DESIRED_VESSEL_CARGO_COUNT) int[] desiredVesselCargoCount;
	@Inject
	private @Named(LightWeightSchedulerStage2Module.LIGHTWEIGHT_DESIRED_VESSEL_CARGO_WEIGHT) long[] desiredVesselCargoWeight;
	@Inject
	private @Named(OptimiserConstants.SEQUENCE_TYPE_INITIAL) ISequences initialSequences;
	@Inject
	private ModelEntityMap modelEntityMap;
	
	@Inject
	private Injector injector;

	public ILightWeightOptimisationData createLightWeightOptimisationData(final IVesselAvailability pnlVessel, final LoadDischargePairValueCalculatorStep calculator,
			final JobExecutorFactory jobExecutorFactory, final IProgressMonitor monitor) {
		// get the slot pairing matrix as a sparse binary matrix (sum of each row and column <= 1)
		final boolean[][] pairingsMatrix = createSlotPairingMatrix(pnlVessel, calculator, jobExecutorFactory, monitor);

		if (pairingsMatrix == null) {
			return null;
		}

		// Export the pairings matrix to a Map
		final Map<ILoadOption, IDischargeOption> pairingsMap = new LinkedHashMap<>();
		for (final ILoadOption load : optimiserRecorder.getSortedLoads()) {
			pairingsMap.put(load, optimiserRecorder.getPairedDischarge(load, pairingsMatrix));
		}

		if (LightWeightSchedulerStage2Module.DEBUG) {
			printPairings(pairingsMap);
		}

		// Create data for optimiser

		// add cargoes
		final Set<Integer> cargoIndexes = new HashSet<>();
		final List<List<IPortSlot>> shippedCargoes = PairingOptimiserHelper.getCargoes(optimiserRecorder.getSortedLoads(), optimiserRecorder.getSortedDischarges(), pairingsMatrix,
				ShippingType.SHIPPED);
		final List<List<IPortSlot>> nonshippedCargoes = PairingOptimiserHelper.getCargoes(optimiserRecorder.getSortedLoads(), optimiserRecorder.getSortedDischarges(), pairingsMatrix,
				ShippingType.NON_SHIPPED);
		if (shippedCargoes.isEmpty() && nonshippedCargoes.isEmpty()) {
			// No cargoes found!
			return null;
		}

		for (int i = 0; i < shippedCargoes.size(); i++) {
			cargoIndexes.add(i);
		}

		// add events
		final Set<Integer> eventIndexes = new HashSet<>();
		longTermSlotsProvider.getLongTermEvents().forEach(e -> {
			shippedCargoes.add(e);
			eventIndexes.add(shippedCargoes.size() - 1);
		});

		// add vessels
		final List<@NonNull IVesselAvailability> vessels = getVessels();

		// calculate shipping costs between two cargoes
		final long[][][] cargoToCargoCostsOnAvailability = cargoToCargoCostCalculator.createCargoToCargoCostMatrix(shippedCargoes, vessels);

		// set vessel restrictions
		final ArrayList<Set<Integer>> cargoVesselRestrictions = cargoVesselRestrictionsMatrixProducer.getIntegerCargoVesselRestrictions(shippedCargoes, vessels,
				cargoVesselRestrictionsMatrixProducer.getCargoVesselRestrictions(shippedCargoes, vessels));

		// Cargo Detail
		final LightWeightCargoDetails[] cargoDetails = getCargoDetails(shippedCargoes, vessels);

		// Cargo PNL
		final long[] cargoPNL = LightWeightOptimiserHelper.getCargoPNL(optimiserRecorder.getProfit(), shippedCargoes, optimiserRecorder.getSortedLoads(), optimiserRecorder.getSortedDischarges(),
				pnlVessel, cargoDetails);

		// Min Travel Time
		final int[][][] minCargoToCargoTravelTimesPerVessel = cargoToCargoCostCalculator.getMinCargoToCargoTravelTimesPerVessel(shippedCargoes, vessels);

		// Time window
		final int[][] minCargoStartToEndSlotTravelTimesPerVessel = cargoToCargoCostCalculator.getMinCargoStartToEndSlotTravelTimesPerVessel(shippedCargoes, vessels);

		// Charter costs
		final long[][] cargoCharterCostPerAvailability = cargoToCargoCostCalculator.getCargoCharterCostPerAvailability(shippedCargoes, vessels);

		// Vessel Capacity
		final long[] capacity = getVesselCapacities(vessels);

		// Cargo Volume
		final long[] cargoesVolumes = getCargoVolumes(shippedCargoes);

		// Vessel windows
		final ITimeWindow[] vesselStartWindows = vessels.stream().map(v -> v.getStartRequirement().getTimeWindow()).toArray(size -> new ITimeWindow[size]);
		final ITimeWindow[] vesselEndWindows = vessels.stream().map(v -> v.getEndRequirement().getTimeWindow()).toArray(size -> new ITimeWindow[size]);

		// Cargo windows
		final int[] cargoStartSlotDurations = cargoToCargoCostCalculator.getCargoStartSlotDurations(shippedCargoes);
		final int[] cargoEndSlotDurations = cargoToCargoCostCalculator.getCargoEndSlotDurations(shippedCargoes);
		final CargoWindowData[] cargoWindows = calculateCargoWindows(shippedCargoes, minCargoStartToEndSlotTravelTimesPerVessel, cargoIndexes);

		final LightWeightOptimisationData lightWeightOptimisationData = new LightWeightOptimisationData(shippedCargoes, nonshippedCargoes, vessels, capacity, cargoPNL, cargoToCargoCostsOnAvailability,
				cargoVesselRestrictions, minCargoToCargoTravelTimesPerVessel, minCargoStartToEndSlotTravelTimesPerVessel, pairingsMap, desiredVesselCargoCount, desiredVesselCargoWeight,
				cargoesVolumes, cargoDetails, cargoCharterCostPerAvailability, cargoIndexes, eventIndexes, vesselStartWindows, vesselEndWindows, cargoStartSlotDurations, cargoEndSlotDurations,
				cargoWindows);

		return lightWeightOptimisationData;
	}

	public ISequences createNominalADP(final IVesselAvailability pnlVessel, final LoadDischargePairValueCalculatorStep calculator, final JobExecutorFactory jobExecutorFactory,
			final IProgressMonitor monitor) {
		// get the slot pairing matrix as a sparse binary matrix (sum of each row and column <= 1)
		final boolean[][] pairingsMatrix = createSlotPairingMatrix(pnlVessel, calculator, jobExecutorFactory, monitor);

		if (pairingsMatrix == null) {
			return null;
		}

		// Export the pairings matrix to a Map
		final Map<ILoadOption, IDischargeOption> pairingsMap = new LinkedHashMap<>();
		for (final ILoadOption load : optimiserRecorder.getSortedLoads()) {
			pairingsMap.put(load, optimiserRecorder.getPairedDischarge(load, pairingsMatrix));
		}

		if (LightWeightSchedulerStage2Module.DEBUG) {
			printPairings(pairingsMap);
		}

		// Create data for optimiser

		// add cargoes
		final List<List<IPortSlot>> shippedCargoes = PairingOptimiserHelper.getCargoes(optimiserRecorder.getSortedLoads(), optimiserRecorder.getSortedDischarges(), pairingsMatrix, ShippingType.ALL);
		if (shippedCargoes.isEmpty()) {
			// No cargoes found!
			return null;
		}

		final Collection<IResource> resources = new LinkedHashSet<>();
		final IResource o_resource = vesselProvider.getResource(pnlVessel);
		resources.add(o_resource);

		// Gather FOB/DES resources
		for (final List<IPortSlot> cargo : shippedCargoes) {
			for (final IPortSlot e : cargo) {
				final IVesselAvailability va = virtualVesselProvider.getVesselAvailabilityForElement(portSlotProvider.getElement(e));
				if (va != null) {
					resources.add(vesselProvider.getResource(va));
				}
			}
		}

		final IModifiableSequences sequences = new ModifiableSequences(new ArrayList<>(resources));
		final IStartEndRequirementProvider startEndRequirementProvider = injector.getInstance(IStartEndRequirementProvider.class);

		@NonNull
		final IModifiableSequence modifiableSequence = sequences.getModifiableSequence(o_resource);
		modifiableSequence.add(startEndRequirementProvider.getStartElement(o_resource));
		Set<ISequenceElement> usedElements = new HashSet<>();

		for (final List<IPortSlot> cargo : shippedCargoes) {
			// Grab FOB/DES vessel
			IVesselAvailability va = null;
			for (final IPortSlot e : cargo) {
				va = virtualVesselProvider.getVesselAvailabilityForElement(portSlotProvider.getElement(e));
				if (va != null) {
					break;
				}
			}

			IModifiableSequence thisModifiableSequence = modifiableSequence;
			IResource thisResource = null;

			if (va != null) {
				thisResource = vesselProvider.getResource(va);
				thisModifiableSequence = sequences.getModifiableSequence(thisResource);

				thisModifiableSequence.add(startEndRequirementProvider.getStartElement(thisResource));
			}

			for (final IPortSlot e : cargo) {
				thisModifiableSequence.add(portSlotProvider.getElement(e));
				usedElements.add(portSlotProvider.getElement(e));
			}

			if (va != null) {
				thisResource = vesselProvider.getResource(va);
				thisModifiableSequence.add(startEndRequirementProvider.getEndElement(thisResource));
			}
		}

		modifiableSequence.add(startEndRequirementProvider.getEndElement(o_resource));

		List<@NonNull ISequenceElement> unusedElements = sequences.getModifiableUnusedElements();
		optimiserRecorder.getSortedLoads().stream() //
				.map(portSlotProvider::getElement) //
				.filter(e -> !usedElements.contains(e)) //
				.forEach(unusedElements::add);

		optimiserRecorder.getSortedDischarges().stream() //
				.map(portSlotProvider::getElement) //
				.filter(e -> !usedElements.contains(e)) //
				.forEach(unusedElements::add);

		// TODO: Vessel Events?

		return sequences;

	}

	private long[] getVesselCapacities(final List<@NonNull IVesselAvailability> vessels) {
		final long[] capacity = vessels.stream().mapToLong(v -> v.getVessel().getCargoCapacity()).toArray();
		return capacity;
	}

	private long[] getCargoVolumes(final List<List<IPortSlot>> shippedCargoes) {
		final long[] cargoesVolumes = shippedCargoes.stream().mapToLong(x -> {
			if (!(x.get(0) instanceof ILoadOption)) {
				return 1;
			}
			final long loadVolume = ((ILoadOption) x.get(0)).getMaxLoadVolume();
			final long dischargeVolume = ((IDischargeOption) x.get(1)).getMaxDischargeVolume(23);
			return Math.min(loadVolume, dischargeVolume);
		}).toArray();
		return cargoesVolumes;
	}

	private LightWeightCargoDetails[] getCargoDetails(final List<List<IPortSlot>> shippedCargoes, final List<@NonNull IVesselAvailability> vessels) {
		final LightWeightCargoDetails[] cargoDetails = shippedCargoes.stream().map(x -> {
			return new LightWeightCargoDetails(x.get(0).getPortType());
		}).toArray(LightWeightCargoDetails[]::new);
		return cargoDetails;
	}

	private List<@NonNull IVesselAvailability> getVessels() {
		final List<@NonNull IVesselAvailability> vessels = initialSequences.getResources().stream() //
				.sorted((a, b) -> a.getName().compareTo(b.getName())) //
				.map(v -> vesselProvider.getVesselAvailability(v)) //
				.filter(v -> PairingOptimiserHelper.isShippedVessel(v)).collect(Collectors.toList());
		return vessels;
	}

	private List<@NonNull IVesselAvailability> getAllVessels() {
		final List<@NonNull IVesselAvailability> vessels = initialSequences.getResources().stream() //
				.map(v -> vesselProvider.getVesselAvailability(v)) //
				.collect(Collectors.toList());
		return vessels;
	}

	/**
	 * Get the slot pairing matrix as a sparse binary matrix (sum of each row and column <= 1) ALEX_TODO: move this to LongTerm package
	 * 
	 * @param pnlVessel
	 * @param calculator
	 * @param executorService
	 * @param monitor
	 * @return
	 */
	public boolean[][] createSlotPairingMatrix(final IVesselAvailability pnlVessel, final LoadDischargePairValueCalculatorStep calculator, final JobExecutorFactory jobExecutorFactory,
			final IProgressMonitor monitor) {
		// (1) Identify LT slots
		@NonNull
		final Collection<IPortSlot> longTermSlots = getLongTermSlots();

		final List<ILoadOption> longtermLoads = getLongTermLoads(longTermSlots);
		final List<IDischargeOption> longTermDischarges = getLongTermDischarges(longTermSlots);
		optimiserRecorder.init(longtermLoads, longTermDischarges);

		// (2) Generate Slot to Slot bindings matrix for LT slots
		calculator.run(pnlVessel, optimiserRecorder.getSortedLoads(), optimiserRecorder.getSortedDischarges(), new ProfitAndLossExtractor(optimiserRecorder), jobExecutorFactory, monitor,
				getAllVessels());

		// (3) Optimise matrix
		final boolean[][] pairingsMatrix = matrixOptimiser.findOptimalPairings(optimiserRecorder.getProfitAsPrimitive(), optimiserRecorder.getOptionalLoads(),
				optimiserRecorder.getOptionalDischarges(), optimiserRecorder.getValid(), optimiserRecorder.getMaxDischargeGroupCount(), optimiserRecorder.getMinDischargeGroupCount(),
				optimiserRecorder.getMaxLoadGroupCount(), optimiserRecorder.getMinLoadGroupCount());

		if (pairingsMatrix == null) {
			final List<ConstraintInfo<ContractProfile, ProfileConstraint,?>> violatedConstraints = matrixOptimiser.findMinViolatedConstraints(optimiserRecorder, optimiserRecorder.getProfitAsPrimitive(), optimiserRecorder.getOptionalLoads(),
					optimiserRecorder.getOptionalDischarges(), optimiserRecorder.getValid(), optimiserRecorder.getMaxDischargeGroupCounts(), optimiserRecorder.getMinDischargeGroupCounts(),
					optimiserRecorder.getMaxLoadGroupCounts(), optimiserRecorder.getMinLoadGroupCounts());

			StringBuilder errorMessage = new StringBuilder();
			
			errorMessage.append("Some contract constraints cannot be satisfied:\n\n");
			
			addViolatedConstraintDetails(modelEntityMap, violatedConstraints, errorMessage);
			
			throw new InfeasibleSolutionException(errorMessage.toString());
		}
		
		return pairingsMatrix;
	}

	public static void addViolatedConstraintDetails(ModelEntityMap mem, final List<ConstraintInfo<ContractProfile, ProfileConstraint,?>> violatedConstraints, StringBuilder errorMessage) {
		Map<Contract, Set<String>> contractsToConstraintErrMsgs = new HashMap<>();
		
		if (violatedConstraints == null) {
			return;
		}
		
		for (ConstraintInfo<ContractProfile, ProfileConstraint,?> violated : violatedConstraints) {
			ContractProfile contract = (ContractProfile)violated.getContractProfile();
			ProfileConstraint constraint = (ProfileConstraint)violated.getProfileConstraint();

			Set<String> contractConstraints = contractsToConstraintErrMsgs.computeIfAbsent(contract.getContract(), key -> new HashSet<String>());

			StringBuilder constraintDetails = new StringBuilder();

			if (constraint instanceof PeriodDistributionProfileConstraint) {
				PeriodDistributionProfileConstraint pdc = (PeriodDistributionProfileConstraint)constraint;
				for (PeriodDistribution pd : pdc.getDistributions()) {
					if (violated.getMonths() == null) {
						if (pd.getMinCargoes() == violated.getBound() || pd.getMaxCargoes() == violated.getBound()) {
							constraintDetails.append(ADPModelUtil.getPeriodDistributionRangeString(pd));
							if (pd.isSetMinCargoes()) {
								constraintDetails.append(" Min:").append(pd.getMinCargoes());
							}
							if (pd.isSetMaxCargoes()) {
								constraintDetails.append(" Max:").append(pd.getMaxCargoes());
							}
							if (violated.getViolationType() == ViolationType.Min) {
								constraintDetails.append(" (Min violated, slots used = ").append(violated.getViolatedAmount()).append(")");
							}
							else if (violated.getViolationType() == ViolationType.Max) {
								constraintDetails.append(" (Max violated, slots used = ").append(violated.getViolatedAmount()).append(")");
							}
							constraintDetails.append("\n");
						}
						contractConstraints.add(constraintDetails.toString());
					}
				}								
			}

			if (violated.getMonths() != null) {
				Row row = (Row)violated.getMonths();
				List<YearMonth> yearMonths = row.getYearMonths();

				String monthsStr = getMonthsString(row, yearMonths);

				String minMaxStr = "";
				int utilized = violated.getViolatedAmount();
				int bound = 0;
				if (violated.getViolationType() == ViolationType.Min) {
					minMaxStr = "minimum";
					bound = row.getMin();
				}
				else if (violated.getViolationType() == ViolationType.Max) {
					minMaxStr = "maximum";
					bound = row.getMax();
				}
				String constraintStr = getViolationMsg(monthsStr, minMaxStr, bound, utilized);
				constraintDetails.append(constraintStr).append("\n");
				for (IPortSlot slot : violated.getSlots()) {
					Slot emfSlot = mem.getModelObject(slot, Slot.class);
					getSlotDetails(constraintDetails, emfSlot);
				}
				constraintDetails.append("\n");										
				contractConstraints.add(constraintDetails.toString());
			}
		}
		
		for (Contract contract : contractsToConstraintErrMsgs.keySet()) {
			String sideContract = (contract instanceof PurchaseContract ? "(Buy)" : "(Sell)");	
			Set<String> contractConstraints = contractsToConstraintErrMsgs.get(contract);
			for (String constraintName : contractConstraints) {
				errorMessage.append(contract.getName()).append(" ").append(sideContract).append(":\n");
				errorMessage.append(constraintName);
			}
		}
	}

	protected static void getSlotDetails(StringBuilder constraintDetails, Slot emfSlot) {
		constraintDetails.append(" "+emfSlot.getName()+" \t"+getSlotStartDateString(emfSlot)+getTimeWindowString(emfSlot)+"\n");
	}

	protected static String getTimeWindowString(Slot emfSlot) {
		String timeWindowString = "";
		if (emfSlot.getSchedulingTimeWindow().getSize() > 0) {
			timeWindowString += " +"+emfSlot.getSchedulingTimeWindow().getSize();
			switch (emfSlot.getSchedulingTimeWindow().getSizeUnits()) {
			case HOURS:
				timeWindowString += "h";
				break;
			case DAYS:
				timeWindowString += "d";
				break;
			case MONTHS:
				timeWindowString += "m";
				break;
			}
		}
		return timeWindowString;
	}
	
	protected static @NonNull String getSlotStartDateString(Slot emfSlot) {
		return emfSlot.getSchedulingTimeWindow().getStart().toLocalDate().toString().replace("-", "/");
	}

	protected static String getMonthsString(Row row, List<YearMonth> yearMonths) {
		StringBuilder constraintDetails = new StringBuilder();
		if (yearMonths.size() > 0) {
			constraintDetails.append("[");
		}

		if (yearMonths.size() > 2 && isContigous(row)) {
			if (constraintDetails.charAt(constraintDetails.length()-1) != '[') {
				constraintDetails.append(", ");
			}
			YearMonth ym0 = yearMonths.get(0);
			YearMonth ym1 = yearMonths.get(yearMonths.size()-1);
			String yearMonthStrFirst = getYearMonthString(ym0);						
			String yearMonthStrLast = getYearMonthString(ym1);						
			constraintDetails.append(yearMonthStrFirst+"-"+yearMonthStrLast);
		}
		else {
			for (YearMonth ym : yearMonths) {
				if (constraintDetails.charAt(constraintDetails.length()-1) != '[') {
					constraintDetails.append(", ");
				}
				String yearMonthStr = getYearMonthString(ym);
				constraintDetails.append(yearMonthStr);
			}
		}
		if (yearMonths.size() > 0) {
			constraintDetails.append("]");
		}
		return constraintDetails.toString();
	}

	private static String getViolationMsg(String monthsStr, String minMaxStr, int bound, int utilizable) {
		//During [Jan-Mar], a min of 3 is required but 0 of the following are used
		//"none" instead of zero and also "minimum of X slots is ..." instead of "min of X is..."
		String utilizableStr = "none";
		if (utilizable > 0) {
			utilizableStr = Integer.toString(utilizable);
		}
		return "During "+monthsStr+", a "+minMaxStr+" of "+bound+" slots is required but "+utilizableStr+" of the following are used";
	}
	
	protected static String getYearMonthString(YearMonth ym) {
		String yearMonthStr = String.format("%s '%02d", //
				ym.getMonth().getDisplayName(TextStyle.SHORT, Locale.getDefault()), //
				ym.getYear() % 100);
		return yearMonthStr;
	}

	private static boolean isContigous(Row row) {
		int maxGap = 1;
		int lastMonth = row.getMonths().get(0);
		for (int month : row.getMonths()) {
			if (month > lastMonth+1) {
				maxGap = 2;
			}
			lastMonth = month;
		}
		return maxGap == 1;
	}

	private Collection<IPortSlot> getLongTermSlots() {
		@NonNull
		final Collection<IPortSlot> longTermSlots = longTermSlotsProvider.getLongTermSlots();
		return longTermSlots;
	}

	private List<IDischargeOption> getLongTermDischarges(final Collection<IPortSlot> longTermSlots) {
		final List<IDischargeOption> longTermDischarges = longTermSlots.stream().filter(s -> (s instanceof IDischargeOption)).map(m -> (IDischargeOption) m)
				.collect(Collectors.toCollection(ArrayList::new));
		return longTermDischarges;
	}

	private List<ILoadOption> getLongTermLoads(final Collection<IPortSlot> longTermSlots) {
		final List<ILoadOption> longtermLoads = longTermSlots.stream().filter(s -> (s instanceof ILoadOption)).map(m -> (ILoadOption) m).collect(Collectors.toCollection(ArrayList::new));
		return longtermLoads;
	}

	private CargoWindowData[] calculateCargoWindows(final List<List<IPortSlot>> cargoes, final int[][] cargoMinTravelTimes, final Set<Integer> cargoIndexes) {
		final CargoWindowData[] cargoWindows = new CargoWindowData[cargoes.size()];

		for (int i = 0; i < cargoes.size(); i++) {
			if (cargoIndexes.contains(i)) {
				final ITimeWindow loadTW = cargoes.get(i).get(0).getTimeWindow();
				final ITimeWindow dischargeTW = cargoes.get(i).get(1).getTimeWindow();

				cargoWindows[i] = new CargoWindowData(loadTW.getInclusiveStart(), loadTW.getExclusiveEnd(), dischargeTW.getInclusiveStart(), dischargeTW.getExclusiveEnd());
			} else {
				final ITimeWindow tw = cargoes.get(i).get(0).getTimeWindow();

				// vessel events are vessel independent for duration so get time from first vessel
				cargoWindows[i] = new CargoWindowData(tw.getInclusiveStart(), tw.getExclusiveEnd(), tw.getInclusiveStart() + cargoMinTravelTimes[i][0],
						tw.getExclusiveEnd() + cargoMinTravelTimes[i][0]);
			}
		}

		return cargoWindows;
	}

	private void printSlotsToFile(final List<? extends IPortSlot> loads, final List<? extends IPortSlot> discharges) {
		final LocalDateTime date = LocalDateTime.now();
		PrintWriter writer;
		try {
			writer = new PrintWriter(String.format("c:/temp/slots-%s-%s-%s.txt", date.getHour(), date.getMinute(), date.getSecond()), "UTF-8");
			loads.forEach(l -> writer.println(l.getId()));
			discharges.forEach(d -> writer.println(d.getId()));
			writer.close();
		} catch (final FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void printPairingsToFile(final List<ILoadOption> sortedLoads, final Map<ILoadOption, IDischargeOption> pairingsMap) {
		final LocalDateTime date = LocalDateTime.now();
		PrintWriter writer;
		try {
			writer = new PrintWriter(String.format("c:/temp/pairings-%s-%s-%s.txt", date.getHour(), date.getMinute(), date.getSecond()), "UTF-8");
			for (final ILoadOption load : sortedLoads) {
				if (pairingsMap.get(load) != null) {
					writer.println(String.format("%s -> %s", load.getId(), pairingsMap.get(load)));
				}
			}
			writer.close();
		} catch (final FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void printPairings(final Map<ILoadOption, IDischargeOption> pairingsMap) {
		System.out.println("####Pairings####");
		for (final Entry<ILoadOption, IDischargeOption> entry : pairingsMap.entrySet()) {
			if (entry.getValue() != null) {
				System.out.println(String.format("%s -> %s", entry.getKey(), entry.getValue()));
			}
		}
	}

	private void printShippedCargoesToFile(final List<List<IPortSlot>> shippedCargoes) {
		final LocalDateTime date = LocalDateTime.now();
		PrintWriter writer;
		try {
			writer = new PrintWriter(String.format("c:/temp/shipped-cargoes-%s-%s-%s.txt", date.getHour(), date.getMinute(), date.getSecond()), "UTF-8");
			for (final List<IPortSlot> cargoe : shippedCargoes) {
				writer.println(cargoe.stream().map(s -> s.getId()).collect(Collectors.joining("-")));
			}
			writer.close();
		} catch (final FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
