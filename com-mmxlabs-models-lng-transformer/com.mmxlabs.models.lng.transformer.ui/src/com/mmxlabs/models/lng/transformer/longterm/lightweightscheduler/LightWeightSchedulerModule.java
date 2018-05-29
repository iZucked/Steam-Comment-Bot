package com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.inject.Singleton;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.longterm.CargoVesselRestrictionsMatrixProducer;
import com.mmxlabs.models.lng.transformer.longterm.ICargoToCargoCostCalculator;
import com.mmxlabs.models.lng.transformer.longterm.ICargoVesselRestrictionsMatrixProducer;
import com.mmxlabs.models.lng.transformer.longterm.ILongTermMatrixOptimiser;
import com.mmxlabs.models.lng.transformer.longterm.ILongTermVesselSlotCountFitnessProvider;
import com.mmxlabs.models.lng.transformer.longterm.LongTermOptimisationData;
import com.mmxlabs.models.lng.transformer.longterm.LongTermOptimiser;
import com.mmxlabs.models.lng.transformer.longterm.LongTermOptimiserHelper;
import com.mmxlabs.models.lng.transformer.longterm.LongTermOptimiserHelper.ShippingType;
import com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler.constraints.LightWeightShippingRestrictionsConstraintCheckerFactory;
import com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler.fitnessfunctions.DefaultPNLLightWeightFitnessFunctionFactory;
import com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler.fitnessfunctions.VesselCargoCountLightWeightFitnessFunctionFactory;
import com.mmxlabs.models.lng.transformer.longterm.SimpleCargoToCargoCostCalculator;
import com.mmxlabs.models.lng.transformer.longterm.metaheuristic.TabuLightWeightSequenceOptimiser;
import com.mmxlabs.models.lng.transformer.longterm.webservice.WebserviceLongTermMatrixOptimiser;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.moves.util.IFollowersAndPreceders;
import com.mmxlabs.scheduler.optimiser.moves.util.impl.FollowersAndPrecedersProviderImpl;
import com.mmxlabs.scheduler.optimiser.providers.ILongTermSlotsProvider;
import com.mmxlabs.scheduler.optimiser.providers.ILongTermSlotsProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashSetLongTermSlotsEditor;

public class LightWeightSchedulerModule extends AbstractModule {
	private static final String LIGHTWEIGHT_FITNESS_FUNCTION_NAMES = "LIGHTWEIGHT_FITNESS_FUNCTION_NAMES";
	private static final String LIGHTWEIGHT_CONSTRAINT_CHECKER_NAMES = "LIGHTWEIGHT_CONSTRAINT_CHECKER_NAMES";
	private static final String LIGHTWEIGHT_VESSELS = "LIGHTWEIGHT_VESSELS";
	private static final String LIGHTWEIGHT_DESIRED_VESSEL_CARGO_COUNT = "LIGHTWEIGHT_DESIRED_VESSEL_CARGO_COUNT";
	private static final String LIGHTWEIGHT_DESIRED_VESSEL_CARGO_WEIGHT = "LIGHTWEIGHT_DESIRED_VESSEL_CARGO_WEIGHT";

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
		HashSetLongTermSlotsEditor longTermSlotEditor = new HashSetLongTermSlotsEditor();
		bind(ILongTermSlotsProvider.class).toInstance(longTermSlotEditor);
		bind(ILongTermSlotsProviderEditor.class).toInstance(longTermSlotEditor);
		WebserviceLongTermMatrixOptimiser matrixOptimiser = new WebserviceLongTermMatrixOptimiser();
		bind(ILongTermMatrixOptimiser.class).toInstance(matrixOptimiser);
		bind(ICargoToCargoCostCalculator.class).to(SimpleCargoToCargoCostCalculator.class);
		bind(ICargoVesselRestrictionsMatrixProducer.class).to(CargoVesselRestrictionsMatrixProducer.class);
		bind(ILightWeightSequenceOptimiser.class).to(TabuLightWeightSequenceOptimiser.class);
		bind(LongTermOptimisationData.class);
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
	private ILightWeightOptimisationData provideLightWeightOptimisationData(LongTermOptimisationData optimiserRecorder, ILongTermSlotsProvider longTermSlotsProvider, ILongTermMatrixOptimiser matrixOptimiser,
			IVesselProvider vesselProvider, ICargoToCargoCostCalculator cargoToCargoCostCalculator, ICargoVesselRestrictionsMatrixProducer cargoVesselRestrictionsMatrixProducer,
			@Named(LIGHTWEIGHT_DESIRED_VESSEL_CARGO_COUNT) int[] desiredVesselCargoCount, @Named(LIGHTWEIGHT_DESIRED_VESSEL_CARGO_WEIGHT) long[] desiredVesselCargoWeight) {
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
		LongTermOptimiserHelper.getS2SBindings(optimiserRecorder.getSortedLoads(),
				optimiserRecorder.getSortedDischarges(), nominalCharterInMarket, es,
				dataTransformer, optimiserRecorder);
		
		// now using our profits recorder we have a full matrix of constraints and pnl
		Long[][] profit = optimiserRecorder.getProfit();

		LongTermOptimiserHelper.produceDataForGurobi(optimiserRecorder, "/tmp/");
		// (3) Optimise matrix
		boolean[][] pairingsMatrix = matrixOptimiser.findOptimalPairings(optimiserRecorder.getProfitAsPrimitive(),
				optimiserRecorder.getOptionalLoads(), optimiserRecorder.getOptionalDischarges(),
				optimiserRecorder.getValid(), optimiserRecorder.getMaxDischargeGroupCount(),
				optimiserRecorder.getMinDischargeGroupCount());
		
		if (pairingsMatrix == null) {
			return null;
		}
		
		// (4) Export the pairings matrix to a Map
		Map<ILoadOption, IDischargeOption> pairingsMap = new HashMap<>();
		for (ILoadOption load : optimiserRecorder.getSortedLoads()) {
			pairingsMap.put(load, optimiserRecorder.getPairedDischarge(load, pairingsMatrix));
		}

		// create data for optimiser
		List<List<IPortSlot>> shippedCargoes = LongTermOptimiserHelper.getCargoes(optimiserRecorder.getSortedLoads(),
				optimiserRecorder.getSortedDischarges(), pairingsMatrix, ShippingType.SHIPPED);
		List<@NonNull IVesselAvailability> vessels = vesselProvider.getSortedResources().stream()
				.map(v -> vesselProvider.getVesselAvailability(v))
				.filter(v -> LongTermOptimiserHelper.isShippedVessel(v))
				.collect(Collectors.toList());
		@NonNull
		IVesselAvailability pnlVessel = LongTermOptimiserHelper.getPNLVessel(dataTransformer,
				nominalCharterInMarket, vesselProvider);
		Long[][][] cargoToCargoCostsOnAvailability = cargoToCargoCostCalculator.createCargoToCargoCostMatrix(shippedCargoes, vessels);
		ArrayList<Set<Integer>> cargoVesselRestrictions = cargoVesselRestrictionsMatrixProducer.getIntegerCargoVesselRestrictions(shippedCargoes, vessels,
				cargoVesselRestrictionsMatrixProducer.getCargoVesselRestrictions(shippedCargoes, vessels));
		long[] cargoPNL = LongTermOptimiserHelper.getCargoPNL(profit, shippedCargoes, optimiserRecorder.getSortedLoads(),
				optimiserRecorder.getSortedDischarges(), pnlVessel);
		int[][][] minCargoToCargoTravelTimesPerVessel = cargoToCargoCostCalculator.getMinCargoToCargoTravelTimesPerVessel(shippedCargoes, vessels);
		int[][] minCargoStartToEndSlotTravelTimesPerVessel = cargoToCargoCostCalculator.getMinCargoStartToEndSlotTravelTimesPerVessel(shippedCargoes, vessels);
		double[] capacity = vessels.stream()
				.mapToDouble(v -> v.getVessel().getCargoCapacity() / 1000)
				.toArray();
		
		((ILoadOption) shippedCargoes.get(0).get(0)).getMaxLoadVolume();
		double[] cargoesVolumes = shippedCargoes.stream().mapToDouble(x -> {
			double loadVolume = ((ILoadOption) x.get(0)).getMaxLoadVolume();
			double dischargeVolume = ((IDischargeOption) x.get(1)).getMaxDischargeVolume(23);
			return Math.min(loadVolume, dischargeVolume);
		}).toArray();

		List<PortType> cargoTypes = shippedCargoes.stream().map(x -> x.get(0).getPortType()).collect(Collectors.toList());
		
		LightWeightOptimisationData lightWeightOptimisationData = new LightWeightOptimisationData(shippedCargoes, vessels, capacity, cargoPNL,
				cargoToCargoCostsOnAvailability, cargoVesselRestrictions, minCargoToCargoTravelTimesPerVessel, minCargoStartToEndSlotTravelTimesPerVessel,
				pairingsMap, desiredVesselCargoCount, desiredVesselCargoWeight, cargoesVolumes, cargoTypes.toArray(new PortType[cargoTypes.size()]));
		
		return lightWeightOptimisationData;
	}
	
	@Provides
	@Named(LIGHTWEIGHT_VESSELS)
	private List<@NonNull IVesselAvailability> getVessels(IVesselProvider vesselProvider) {
		return vesselProvider
				.getSortedResources()
				.stream()
				.map(v -> vesselProvider.getVesselAvailability(v))
				.filter(v -> LongTermOptimiserHelper.isShippedVessel(v))
				.collect(Collectors.toList());
	}
	
	@Provides
	@Named(LIGHTWEIGHT_DESIRED_VESSEL_CARGO_COUNT)
	private int[] getDesiredNumberSlotsPerVessel(@Named(LIGHTWEIGHT_VESSELS) List<@NonNull IVesselAvailability> vessels, ILongTermVesselSlotCountFitnessProvider vesselSlotCountFitnessProvider) {
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
	private long[] getDesiredNumberSlotsWeightingPerVessel(@Named(LIGHTWEIGHT_VESSELS) List<@NonNull IVesselAvailability> vessels, ILongTermVesselSlotCountFitnessProvider vesselSlotCountFitnessProvider) {
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
	private List<ILightWeightFitnessFunction> getFitnessFunctions(Injector injector, LightWeightFitnessFunctionRegistry registry,
			@Named(LIGHTWEIGHT_FITNESS_FUNCTION_NAMES) List<String> names) {
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
	@Named(LIGHTWEIGHT_FITNESS_FUNCTION_NAMES) List<String> getFitnessFunctionNames() {
		return CollectionsUtil.makeLinkedList("DefaultPNLLightWeightFitnessFunction",
				"VesselCargoCountLightWeightFitnessFunction");
	}
	
	@Provides
	@Named(LIGHTWEIGHT_CONSTRAINT_CHECKER_NAMES) List<String> getConstraintCheckerNames() {
		return CollectionsUtil.makeLinkedList("LightWeightShippingRestrictionsConstraintChecker");
	}

}
