package com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.inject.Singleton;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler.DefaultLightWeightPostOptimisationStateModifier;
import com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler.DefaultLongTermSequenceElementFilter;
import com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler.ILightWeightPostOptimisationStateModifier;
import com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler.ISequenceElementFilter;
import com.mmxlabs.models.lng.transformer.optimiser.common.AbstractOptimiserHelper.ShippingType;
import com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.constraints.LightWeightShippingRestrictionsConstraintCheckerFactory;
import com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.fitnessfunctions.DefaultPNLLightWeightFitnessFunctionFactory;
import com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.fitnessfunctions.VesselCargoCountLightWeightFitnessFunctionFactory;
import com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.metaheuristic.TabuLightWeightSequenceOptimiser;
import com.mmxlabs.models.lng.transformer.optimiser.longterm.CargoVesselRestrictionsMatrixProducer;
import com.mmxlabs.models.lng.transformer.optimiser.longterm.ICargoToCargoCostCalculator;
import com.mmxlabs.models.lng.transformer.optimiser.longterm.ICargoVesselRestrictionsMatrixProducer;
import com.mmxlabs.models.lng.transformer.optimiser.longterm.ILongTermMatrixOptimiser;
import com.mmxlabs.models.lng.transformer.optimiser.longterm.LongTermMatrixOptimiser;
import com.mmxlabs.models.lng.transformer.optimiser.longterm.LongTermOptimisationData;
import com.mmxlabs.models.lng.transformer.optimiser.longterm.LongTermOptimiserHelper;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.moves.util.IFollowersAndPreceders;
import com.mmxlabs.scheduler.optimiser.moves.util.impl.FollowersAndPrecedersProviderImpl;
import com.mmxlabs.scheduler.optimiser.providers.IAllowedVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.ILongTermSlotsProvider;
import com.mmxlabs.scheduler.optimiser.providers.ILongTermSlotsProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.ILongTermVesselSlotCountFitnessProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashSetLongTermSlotsEditor;

public class LightWeightSchedulerModule extends AbstractModule {
	private static final String LIGHTWEIGHT_FITNESS_FUNCTION_NAMES = "LIGHTWEIGHT_FITNESS_FUNCTION_NAMES";
	private static final String LIGHTWEIGHT_CONSTRAINT_CHECKER_NAMES = "LIGHTWEIGHT_CONSTRAINT_CHECKER_NAMES";
	private static final String LIGHTWEIGHT_VESSELS = "LIGHTWEIGHT_VESSELS";
	private static final String LIGHTWEIGHT_DESIRED_VESSEL_CARGO_COUNT = "LIGHTWEIGHT_DESIRED_VESSEL_CARGO_COUNT";
	private static final String LIGHTWEIGHT_DESIRED_VESSEL_CARGO_WEIGHT = "LIGHTWEIGHT_DESIRED_VESSEL_CARGO_WEIGHT";
	private static final boolean DEBUG = false;

	Map<Thread, LightweightSchedulerOptimiser> threadCache;
	private CharterInMarket nominalCharterInMarket;
	private LNGDataTransformer dataTransformer;

	public LightWeightSchedulerModule(Map<Thread, LightweightSchedulerOptimiser> threadCache, CharterInMarket nominalCharterInMarket, LNGDataTransformer dataTransformer) {
		this.threadCache = threadCache;
		this.nominalCharterInMarket = nominalCharterInMarket;
		this.dataTransformer = dataTransformer;
	}

	@Override
	protected void configure() {
		bind(IFollowersAndPreceders.class).to(FollowersAndPrecedersProviderImpl.class).in(Singleton.class);

		bind(HashSetLongTermSlotsEditor.class).in(Singleton.class);
		bind(ILongTermSlotsProvider.class).to(HashSetLongTermSlotsEditor.class);
		bind(ILongTermSlotsProviderEditor.class).to(HashSetLongTermSlotsEditor.class);

		bind(ILongTermMatrixOptimiser.class).to(LongTermMatrixOptimiser.class);

		bind(ICargoToCargoCostCalculator.class).to(SimpleCargoToCargoCostCalculator.class);
		bind(ICargoVesselRestrictionsMatrixProducer.class).to(CargoVesselRestrictionsMatrixProducer.class);
		bind(ILightWeightSequenceOptimiser.class).to(TabuLightWeightSequenceOptimiser.class);

		bind(LongTermOptimisationData.class);
		bind(ILightWeightPostOptimisationStateModifier.class).to(DefaultLightWeightPostOptimisationStateModifier.class);
		
		
		bind(ISequenceElementFilter.class).to(DefaultLongTermSequenceElementFilter.class);
	}

	@Provides
	private LightweightSchedulerOptimiser providePerThreadBagMover(@NonNull final Injector injector) {

		LightweightSchedulerOptimiser lightweightSchedulerOptimiser = threadCache.get(Thread.currentThread());
		if (lightweightSchedulerOptimiser == null) {
			lightweightSchedulerOptimiser = new LightweightSchedulerOptimiser();
			injector.injectMembers(lightweightSchedulerOptimiser);
			threadCache.put(Thread.currentThread(), lightweightSchedulerOptimiser);
		}
		return lightweightSchedulerOptimiser;
	}

	@Provides
	@Singleton
	private ILightWeightOptimisationData provideLightWeightOptimisationData(LongTermOptimisationData optimiserRecorder, ILongTermSlotsProvider longTermSlotsProvider,
			ILongTermMatrixOptimiser matrixOptimiser, IVesselProvider vesselProvider, ICargoToCargoCostCalculator cargoToCargoCostCalculator,
			ICargoVesselRestrictionsMatrixProducer cargoVesselRestrictionsMatrixProducer, @Named(LIGHTWEIGHT_DESIRED_VESSEL_CARGO_COUNT) int[] desiredVesselCargoCount,
			@Named(LIGHTWEIGHT_DESIRED_VESSEL_CARGO_WEIGHT) long[] desiredVesselCargoWeight, @Named(OptimiserConstants.SEQUENCE_TYPE_INITIAL) ISequences initialSequences,
			IAllowedVesselProvider allowedVesselProvider) {
		// (1) Identify LT slots
		@NonNull
		Collection<IPortSlot> longTermSlots = longTermSlotsProvider.getLongTermSlots();

		List<ILoadOption> longtermLoads = longTermSlots.stream()
				.filter(s -> (s instanceof ILoadOption))
				.map(m -> (ILoadOption) m)
				.collect(Collectors.toCollection(ArrayList::new));
		List<IDischargeOption> longTermDischarges = longTermSlots.stream()
				.filter(s -> (s instanceof IDischargeOption))
				.map(m -> (IDischargeOption) m)
				.collect(Collectors.toCollection(ArrayList::new));
	
		optimiserRecorder.init(longtermLoads, longTermDischarges);

		// (2) Generate S2S bindings matrix for LT slots
		ExecutorService es = Executors.newSingleThreadExecutor();
		LightWeightOptimiserHelper.getS2SBindings(optimiserRecorder.getSortedLoads(),
				optimiserRecorder.getSortedDischarges(), nominalCharterInMarket, es,
				dataTransformer, optimiserRecorder);
		
		// now using our profits recorder we have a full matrix of constraints and pnl
		Long[][] profit = optimiserRecorder.getProfit();

//		LightWeightOptimiserHelper.produceDataForGurobi(optimiserRecorder, "/tmp/");
		
		// (3) Optimise matrix
		boolean[][] pairingsMatrix = matrixOptimiser.findOptimalPairings(optimiserRecorder.getProfitAsPrimitive(), optimiserRecorder.getOptionalLoads(), optimiserRecorder.getOptionalDischarges(),
				optimiserRecorder.getValid(), optimiserRecorder.getMaxDischargeGroupCount(), optimiserRecorder.getMinDischargeGroupCount(), optimiserRecorder.getMaxLoadGroupCount(),
				optimiserRecorder.getMinLoadGroupCount());

		if (pairingsMatrix == null) {
			return null;
		}

		// (4) Export the pairings matrix to a Map
		Map<ILoadOption, IDischargeOption> pairingsMap = new LinkedHashMap<>();
		for (ILoadOption load : optimiserRecorder.getSortedLoads()) {
			pairingsMap.put(load, optimiserRecorder.getPairedDischarge(load, pairingsMatrix));
		}

		if (DEBUG) {
			printPairings(pairingsMap);
		}

		Set<Integer> cargoIndexes = new HashSet<>();
		Set<Integer> eventIndexes = new HashSet<>();
		
		// create data for optimiser
		// Cargoes
		List<List<IPortSlot>> shippedCargoes = LongTermOptimiserHelper.getCargoes(optimiserRecorder.getSortedLoads(), optimiserRecorder.getSortedDischarges(), pairingsMatrix, ShippingType.SHIPPED);
		for (int i = 0; i < shippedCargoes.size(); i++) {
			cargoIndexes.add(i);
		}
		// add events
		longTermSlotsProvider.getLongTermEvents().forEach(e -> {
			shippedCargoes.add(CollectionsUtil.makeLinkedList(e));
			eventIndexes.add(shippedCargoes.size()-1);
		});
		
		// Now we add vessel events 
		
		
		// Vessel
		List<@NonNull IVesselAvailability> vessels = initialSequences.getResources().stream() //
				.sorted((a,b) -> a.getName().compareTo(b.getName())) //
				.map(v -> vesselProvider.getVesselAvailability(v)) //
				.filter(v -> LongTermOptimiserHelper.isShippedVessel(v))
				.collect(Collectors.toList());
		
		// Vessel PNL
		@NonNull
		IVesselAvailability pnlVessel = LightWeightOptimiserHelper.getPNLVessel(dataTransformer,
				nominalCharterInMarket, vesselProvider);
		
		// Cost
		Long[][][] cargoToCargoCostsOnAvailability = cargoToCargoCostCalculator.createCargoToCargoCostMatrix(shippedCargoes, vessels);
		
		// VesselRestriction
		ArrayList<Set<Integer>> cargoVesselRestrictions = cargoVesselRestrictionsMatrixProducer.getIntegerCargoVesselRestrictions(shippedCargoes, vessels,
				cargoVesselRestrictionsMatrixProducer.getCargoVesselRestrictions(shippedCargoes, vessels));
		
		// Cargo Detail
		LightWeightCargoDetails[] cargoDetails = shippedCargoes.stream().map(x -> { 

			// REVIEW: It is alright to only check the first element ?
			// ALEX_NOTE: not sure this is necessary
			PortType portType = x.get(0).getPortType();

			if (portType == PortType.CharterOut || portType == PortType.DryDock) {
				Collection<IVessel> permittedVessels = allowedVesselProvider.getPermittedVessels(x.get(0));
				int index = -1;
				for (int i = 0; i < vessels.size(); i++) {
					if (permittedVessels.contains(vessels.get(i).getVessel())) {
						index = i;
					}
				}
				if (index != -1) {
					return new LightWeightCargoDetails(x.get(0).getPortType(), index);
				} else {
					return new LightWeightCargoDetails(x.get(0).getPortType());
				}
			}
			return new LightWeightCargoDetails(x.get(0).getPortType());
		}).toArray(LightWeightCargoDetails[]::new);
		
		// Cargo PNL
		long[] cargoPNL = LightWeightOptimiserHelper.getCargoPNL(profit, shippedCargoes, optimiserRecorder.getSortedLoads(),
				optimiserRecorder.getSortedDischarges(), pnlVessel, cargoDetails);
		
		// Min Travel Time
		int[][][] minCargoToCargoTravelTimesPerVessel = cargoToCargoCostCalculator.getMinCargoToCargoTravelTimesPerVessel(shippedCargoes, vessels);
		
		// Time window
		int[][] minCargoStartToEndSlotTravelTimesPerVessel = cargoToCargoCostCalculator.getMinCargoStartToEndSlotTravelTimesPerVessel(shippedCargoes, vessels);
		
		// Charter costs
		long[][] cargoCharterCostPerAvailability = cargoToCargoCostCalculator.getCargoCharterCostPerAvailability(shippedCargoes, vessels);

		// Vessel Capacity
		double[] capacity = vessels.stream()
				.mapToDouble(v -> v.getVessel().getCargoCapacity() / 1000)
				.toArray();
		
		// Cargo Volume
		long[] cargoesVolumes = shippedCargoes.stream().mapToLong(x -> {
			if (!(x.get(0) instanceof ILoadOption)) {
				// volume of 1
				return 1;
			}
			long loadVolume = ((ILoadOption) x.get(0)).getMaxLoadVolume();
			long dischargeVolume = ((IDischargeOption) x.get(1)).getMaxDischargeVolume(23);
			return Math.min(loadVolume, dischargeVolume);
		}).toArray();
		
		

		LightWeightOptimisationData lightWeightOptimisationData = new LightWeightOptimisationData(shippedCargoes, vessels, capacity, cargoPNL,
				cargoToCargoCostsOnAvailability, cargoVesselRestrictions, minCargoToCargoTravelTimesPerVessel, minCargoStartToEndSlotTravelTimesPerVessel,
				pairingsMap, desiredVesselCargoCount, desiredVesselCargoWeight, cargoesVolumes, cargoDetails, cargoCharterCostPerAvailability,
				cargoIndexes, eventIndexes);
		
		return lightWeightOptimisationData;
	}

	@Provides
	@Named(LIGHTWEIGHT_VESSELS)
	private List<@NonNull IVesselAvailability> getVessels(@Named(OptimiserConstants.SEQUENCE_TYPE_INITIAL) ISequences initialSequences, IVesselProvider vesselProvider) {
		return initialSequences
				.getResources().stream()
				.sorted((a,b) -> a.getName().compareTo(b.getName())) //
				.map(v -> vesselProvider.getVesselAvailability(v))
				.filter(v -> LightWeightOptimiserHelper.isShippedVessel(v))
				.collect(Collectors.toList());
	}

	@Provides
	@Named(LIGHTWEIGHT_DESIRED_VESSEL_CARGO_COUNT)
	private int[] getDesiredNumberSlotsPerVessel(@Named(LIGHTWEIGHT_VESSELS) List<@NonNull IVesselAvailability> vessels,
			ILongTermVesselSlotCountFitnessProvider vesselSlotCountFitnessProvider) {
		int[] desiredVesselCargoCount = new int[vessels.size()];
		if (vesselSlotCountFitnessProvider != null) {
			for (int i = 0; i < vessels.size(); i++) {
				desiredVesselCargoCount[i] = vesselSlotCountFitnessProvider.getCountForVessel(vessels.get(i));
			}
		}
		return desiredVesselCargoCount;
	}

	@Provides
	@Named(LIGHTWEIGHT_DESIRED_VESSEL_CARGO_WEIGHT)
	private long[] getDesiredNumberSlotsWeightingPerVessel(@Named(LIGHTWEIGHT_VESSELS) List<@NonNull IVesselAvailability> vessels,
			ILongTermVesselSlotCountFitnessProvider vesselSlotCountFitnessProvider) {
		long[] vesselReward = new long[vessels.size()];
		if (vesselSlotCountFitnessProvider != null) {
			for (int i = 0; i < vessels.size(); i++) {
				vesselReward[i] = vesselSlotCountFitnessProvider.getWeightForVessel(vessels.get(i));
			}
		}
		return vesselReward;
	}

	@Provides
	@Singleton
	private List<ILightWeightConstraintChecker> getConstraintCheckers(Injector injector, LightWeightConstraintCheckerRegistry registry,
			@Named(LIGHTWEIGHT_CONSTRAINT_CHECKER_NAMES) List<String> names) {
		List<ILightWeightConstraintChecker> constraintCheckers = new LinkedList<>();
		Collection<ILightWeightConstraintCheckerFactory> constraintCheckerFactories = registry.getConstraintCheckerFactories();
		for (String name : names) {
			for (ILightWeightConstraintCheckerFactory lightWeightConstraintCheckerFactory : constraintCheckerFactories) {
				if (lightWeightConstraintCheckerFactory.getName().equals(name)) {
					ILightWeightConstraintChecker constraintChecker = lightWeightConstraintCheckerFactory.createConstraintChecker();
					injector.injectMembers(constraintChecker);
					constraintCheckers.add(constraintChecker);
				}
			}
		}
		return constraintCheckers;
	}

	@Provides
	@Singleton
	private List<ILightWeightFitnessFunction> getFitnessFunctions(Injector injector, LightWeightFitnessFunctionRegistry registry, @Named(LIGHTWEIGHT_FITNESS_FUNCTION_NAMES) List<String> names) {
		List<ILightWeightFitnessFunction> fitnessFunctions = new LinkedList<>();
		Collection<ILightWeightFitnessFunctionFactory> fitnessFunctionFactories = registry.getFitnessFunctionFactories();
		for (String name : names) {
			for (ILightWeightFitnessFunctionFactory lightWeightFitnessFunctionFactory : fitnessFunctionFactories) {
				if (lightWeightFitnessFunctionFactory.getName().equals(name)) {
					ILightWeightFitnessFunction fitnessFunction = lightWeightFitnessFunctionFactory.createFitnessFunction();
					injector.injectMembers(fitnessFunction);
					fitnessFunction.init();
					fitnessFunctions.add(fitnessFunction);
				}
			}
		}
		return fitnessFunctions;
	}

	@Provides
	@Singleton
	LightWeightConstraintCheckerRegistry getLightweightConstraintCheckerRegistry() {
		LightWeightConstraintCheckerRegistry registry = new LightWeightConstraintCheckerRegistry();
		registry.registerConstraintCheckerFactory(new LightWeightShippingRestrictionsConstraintCheckerFactory());
		return registry;
	}

	@Provides
	@Singleton
	LightWeightFitnessFunctionRegistry getLightweightFitnessFunctionRegistry() {
		LightWeightFitnessFunctionRegistry registry = new LightWeightFitnessFunctionRegistry();
		registry.registerFitnessFunctionFactory(new DefaultPNLLightWeightFitnessFunctionFactory());
		registry.registerFitnessFunctionFactory(new VesselCargoCountLightWeightFitnessFunctionFactory());
		return registry;
	}

	@Provides
	@Named(LIGHTWEIGHT_FITNESS_FUNCTION_NAMES)
	List<String> getFitnessFunctionNames() {
		return CollectionsUtil.makeLinkedList("DefaultPNLLightWeightFitnessFunction", "VesselCargoCountLightWeightFitnessFunction");
	}

	@Provides
	@Named(LIGHTWEIGHT_CONSTRAINT_CHECKER_NAMES)
	List<String> getConstraintCheckerNames() {
		return CollectionsUtil.makeLinkedList("LightWeightShippingRestrictionsConstraintChecker");
	}

	private void printPairings(Map<ILoadOption, IDischargeOption> pairingsMap) {
		System.out.println("####Pairings####");
		for (Entry<ILoadOption, IDischargeOption> entry : pairingsMap.entrySet()) {
			if (entry.getValue() != null) {
				System.out.println(String.format("%s -> %s", entry.getKey(), entry.getValue()));
			}
		}
	}

}
