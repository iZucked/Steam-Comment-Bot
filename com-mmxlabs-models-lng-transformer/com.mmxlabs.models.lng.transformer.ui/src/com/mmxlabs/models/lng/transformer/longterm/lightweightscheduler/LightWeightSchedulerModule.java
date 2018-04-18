package com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.inject.Singleton;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.longterm.CargoVesselRestrictionsMatrixProducer;
import com.mmxlabs.models.lng.transformer.longterm.ICargoToCargoCostCalculator;
import com.mmxlabs.models.lng.transformer.longterm.ICargoVesselRestrictionsMatrixProducer;
import com.mmxlabs.models.lng.transformer.longterm.ILongTermMatrixOptimiser;
import com.mmxlabs.models.lng.transformer.longterm.LongTermOptimisationData;
import com.mmxlabs.models.lng.transformer.longterm.LongTermOptimiser;
import com.mmxlabs.models.lng.transformer.longterm.LongTermOptimiserHelper;
import com.mmxlabs.models.lng.transformer.longterm.LongTermOptimiserHelper.ShippingType;
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
import com.mmxlabs.scheduler.optimiser.providers.impl.HashSetLongTermSlotsEditor;

public class LightWeightSchedulerModule extends AbstractModule {
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
	private LightWeightOptimisationData provideLightWeightOptimisationData(LongTermOptimisationData optimiserRecorder, ILongTermSlotsProvider longTermSlotsProvider, ILongTermMatrixOptimiser matrixOptimiser,
			IVesselProvider vesselProvider, ICargoToCargoCostCalculator cargoToCargoCostCalculator, ICargoVesselRestrictionsMatrixProducer cargoVesselRestrictionsMatrixProducer) {
		// (1) Identify LT slots
		@NonNull
		Collection<IPortSlot> longTermSlots = longTermSlotsProvider.getLongTermSlots();
		List<ILoadOption> loads = longTermSlots.stream().filter(s -> (s instanceof ILoadOption)).map(m -> (ILoadOption) m).collect(Collectors.toCollection(ArrayList::new));
		List<IDischargeOption> discharges = longTermSlots.stream().filter(s -> (s instanceof IDischargeOption)).map(m -> (IDischargeOption) m).collect(Collectors.toCollection(ArrayList::new));
		optimiserRecorder.init(loads, discharges);
		
		// (2) Generate S2S bindings matrix for LT slots
		ExecutorService es = Executors.newSingleThreadExecutor();
		LongTermOptimiserHelper.getS2SBindings(loads, discharges, nominalCharterInMarket, es, dataTransformer, optimiserRecorder);
		
		// now using our profits recorder we have a full matrix of constraints and pnl
		Long[][] profit = optimiserRecorder.getProfit();

		// (3) Optimise matrix
		boolean[][] pairingsMatrix = matrixOptimiser.findOptimalPairings(optimiserRecorder.getProfitAsPrimitive(), optimiserRecorder.getOptionalLoads(), optimiserRecorder.getOptionalDischarges(), optimiserRecorder.getValid());
		
		if (pairingsMatrix == null) {
			return null;
		}
		// (4) Export the pairings matrix to a Map
		Map<ILoadOption, IDischargeOption> pairingsMap = new HashMap<>();
		for (ILoadOption load : loads) {
			pairingsMap.put(load, optimiserRecorder.getPairedDischarge(load, pairingsMatrix));
		}

		// create data for optimiser
		List<List<IPortSlot>> shippedCargoes = LongTermOptimiserHelper.getCargoes(loads, discharges, pairingsMatrix, ShippingType.SHIPPED);
		List<@NonNull IVesselAvailability> vessels = vesselProvider.getSortedResources().stream().map(v -> vesselProvider.getVesselAvailability(v)).filter(v -> LongTermOptimiserHelper.isShippedVessel(v)).collect(Collectors.toList());
		
		@NonNull
		IVesselAvailability pnlVessel = LongTermOptimiserHelper.getPNLVessel(dataTransformer, nominalCharterInMarket, vesselProvider);
		Long[][][] cargoToCargoCostsOnAvailability = cargoToCargoCostCalculator.createCargoToCargoCostMatrix(shippedCargoes, vessels);
		ArrayList<Set<Integer>> cargoVesselRestrictions = cargoVesselRestrictionsMatrixProducer.getIntegerCargoVesselRestrictions(shippedCargoes, vessels,
				cargoVesselRestrictionsMatrixProducer.getCargoVesselRestrictions(shippedCargoes, vessels));
		long[] cargoPNL = LongTermOptimiserHelper.getCargoPNL(profit, shippedCargoes, loads, discharges, pnlVessel);
		int[][][] minCargoToCargoTravelTimesPerVessel = cargoToCargoCostCalculator.getMinCargoToCargoTravelTimesPerVessel(shippedCargoes, vessels);
		int[][] minCargoStartToEndSlotTravelTimesPerVessel = cargoToCargoCostCalculator.getMinCargoStartToEndSlotTravelTimesPerVessel(shippedCargoes, vessels);
		
		LightWeightOptimisationData lightWeightOptimisationData = new LightWeightOptimisationData(shippedCargoes, vessels, cargoPNL, cargoToCargoCostsOnAvailability,
				cargoVesselRestrictions, minCargoToCargoTravelTimesPerVessel, minCargoStartToEndSlotTravelTimesPerVessel, pairingsMap);
		
		return lightWeightOptimisationData;
	}

}
