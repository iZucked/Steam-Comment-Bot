/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.ops4j.peaberry.util.TypeLiterals;

import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.lingo.its.tests.category.MicroTest;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselStateAttributes;
import com.mmxlabs.models.lng.parameters.ActionPlanOptimisationStage;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelAmount;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUnit;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.extensions.entities.EntityTransformerExtensionFactory;
import com.mmxlabs.models.lng.transformer.extensions.restrictedelements.RestrictedElementsTransformerFactory;
import com.mmxlabs.models.lng.transformer.extensions.shippingtype.ShippingTypeRequirementTransformerFactory;
import com.mmxlabs.models.lng.transformer.extensions.simplecontracts.SimpleContractTransformerFactory;
import com.mmxlabs.models.lng.transformer.extensions.tradingexporter.BasicSlotPNLExporterExtensionFactory;
import com.mmxlabs.models.lng.transformer.extensions.tradingexporter.TradingExporterExtensionFactory;
import com.mmxlabs.models.lng.transformer.inject.IBuilderExtensionFactory;
import com.mmxlabs.models.lng.transformer.inject.IExporterExtensionFactory;
import com.mmxlabs.models.lng.transformer.inject.IPostExportProcessorFactory;
import com.mmxlabs.models.lng.transformer.inject.ITransformerExtensionFactory;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGTransformerModule;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScenarioTools;
import com.mmxlabs.models.lng.transformer.ui.AbstractRunnerHook;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.util.IRunnerHook;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IEvaluatedStateConstraintChecker;
import com.mmxlabs.scheduler.optimiser.cache.NotCaching;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.HeelOptions;
import com.mmxlabs.scheduler.optimiser.constraints.impl.AllowedVesselPermissionConstraintChecker;
import com.mmxlabs.scheduler.optimiser.constraints.impl.CapacityEvaluatedStateChecker;
import com.mmxlabs.scheduler.optimiser.constraints.impl.LatenessEvaluatedStateChecker;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.MinMaxUnconstrainedVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.UnconstrainedVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService.ModuleType;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.models.lng.schedule.CapacityViolationType;
import com.mmxlabs.models.lng.schedule.impl.CapacityMapEntryImpl;
import com.mmxlabs.models.lng.schedule.impl.IdleImpl;
import com.mmxlabs.models.lng.schedule.impl.JourneyImpl;
import com.mmxlabs.models.lng.schedule.impl.SlotVisitImpl;

@SuppressWarnings("unused")
@RunWith(value = ShiroRunner.class)
public class InPortBoilOffTests extends AbstractMicroTestCase {

	private static List<String> requiredFeatures = Lists.newArrayList("no-nominal-in-prompt", "optimisation-actionset");
	private static List<String> addedFeatures = new LinkedList<>();
	
	final int ROUNDING_EPSILON = 2;
	VesselClass vesselClass;
	Vessel vessel;
	VesselAvailability vesselAvailability1;
	Cargo cargo1;
	Port portA;
	Port portB;
	VesselStateAttributes attrLaden;
	VesselStateAttributes attrBal;
	
	private boolean writeScenario = true;

	public class boilOffOverride implements IOptimiserInjectorService {

		private boolean activateOverride = false;
		private boolean minMaxVolumeAllocator = false;

		public boilOffOverride(boolean activateOverride, boolean minMaxVolumeAllocator) {
			this.activateOverride = activateOverride;
			this.minMaxVolumeAllocator = minMaxVolumeAllocator;
		}

		@Override
		public @Nullable Module requestModule(@NonNull ModuleType moduleType, @NonNull Collection<@NonNull String> hints) {
			return null;
		}

		@Override
		public @Nullable List<@NonNull Module> requestModuleOverrides(@NonNull ModuleType moduleType, @NonNull Collection<@NonNull String> hints) {
			if (moduleType == ModuleType.Module_LNGTransformerModule) {
				return Collections.<@NonNull Module> singletonList(new AbstractModule() {
					@Override
					protected void configure() {
						if(minMaxVolumeAllocator)
							bind(IVolumeAllocator.class).annotatedWith(NotCaching.class).to(MinMaxUnconstrainedVolumeAllocator.class);
					}	
					@Provides
					@Named(LNGTransformerModule.COMMERCIAL_VOLUME_OVERCAPACITY)
					private boolean commercialVolumeOverCapacity() {
						return activateOverride;
					}	
				});
			}
			return null;
		}
	}
	


	@BeforeClass
	public static void hookIn() {
		for (final String feature : requiredFeatures) {
			if (!LicenseFeatures.isPermitted("features:" + feature)) {
				LicenseFeatures.addFeatureEnablements(feature);
				addedFeatures.add(feature);
			}
		}
	}

	@AfterClass
	public static void hookOut() {
		for (final String feature : addedFeatures) {
			LicenseFeatures.removeFeatureEnablements(feature);
		}
		addedFeatures.clear();
	}

	@Before
	@Override
	public void constructor() throws MalformedURLException {

		super.constructor();
		// Set a default prompt in the past
		scenarioModelBuilder.setPromptPeriod(LocalDate.of(2014, 1, 1), LocalDate.of(2014, 3, 1));
		// Create the required basic elements
		vesselClass = fleetModelFinder.findVesselClass("STEAM-145");

		vessel = fleetModelBuilder.createVessel("Vessel1", vesselClass);
		vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2015, 12, 4, 7, 0, 0), LocalDateTime.of(2015, 12, 4, 13, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2015, 12, 30, 0, 0, 0))
				.build();

		portA = portFinder.findPort("Point Fortin");
		portA.setTimeZone("UTC");

		portB = portFinder.findPort("Dominion Cove Point LNG");
		portB.setTimeZone("UTC");
		portB.setDefaultStartTime(4);
		portB.setDefaultWindowSize(0);
	}
	
	public void changeBoilOffRates(double loadPortBoilOff, double dischargePortBoilOff){
		
		attrBal = vesselClass.getBallastAttributes();
		attrBal.setInPortNBORate(loadPortBoilOff);
		vesselClass.setBallastAttributes(attrBal);

		attrLaden = vesselClass.getLadenAttributes();
		attrLaden.setInPortNBORate(dischargePortBoilOff);
		vesselClass.setLadenAttributes(attrLaden);
	}
	
	
	scenarioResult runScenario(boolean compensate, boolean minMax){

		List<Integer> transferVolumes = new ArrayList<Integer>();
		List<EMap<CapacityViolationType, Long>> violations = new ArrayList<EMap<CapacityViolationType, Long>>();
		List<Integer> slotVolumes = new ArrayList<Integer>();
		List<Integer> slotFuelCosts = new ArrayList<Integer>();
		List<Integer> travelCosts = new ArrayList<Integer>();
		optimiseWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
	
			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();
			
			if(writeScenario){
				try {
					 MicroCaseUtils.storeToFile(optimiserScenario,"Trips");
					 } catch (Exception e) {
						 e.printStackTrace();
					 }
			}
			
			double ballastNBO = optimiserScenario.getCargoModel().getVesselAvailabilities().get(0).getVessel().getVesselClass().getBallastAttributes().getInPortNBORate();
			double ladenNBO = optimiserScenario.getCargoModel().getVesselAvailabilities().get(0).getVessel().getVesselClass().getLadenAttributes().getInPortNBORate();
//			Assert.assertEquals(ballastBoilOff, ballastNBO, 0);
//			Assert.assertEquals(ladenBoilOff, ladenNBO, 0);
			EList<CargoAllocation> cargoAllo = optimiserScenario.getScheduleModel().getSchedule().getCargoAllocations();

			for (CargoAllocation ca : cargoAllo) {
				List<SlotAllocation> slots = ca.getSlotAllocations();
				int index = 0;
				for (SlotAllocation slot : slots) {
					slotVolumes.add(slot.getSlot().getMaxQuantity());
					EMap<com.mmxlabs.models.lng.schedule.CapacityViolationType, Long> slotVio = slot.getSlotVisit().getViolations();
					violations.add(slotVio);	
					transferVolumes.add(slot.getVolumeTransferred()); 
				}
				// Get Fuel Costs for journey events;
				for(Event event : ca.getEvents()){
		
					if(event instanceof JourneyImpl){
						JourneyImpl journey = (JourneyImpl) event;
						travelCosts.add(sumFuelCosts(journey.getFuels()));
					} else if (event instanceof IdleImpl){
						IdleImpl idle = (IdleImpl) event;
						travelCosts.add(sumFuelCosts(idle.getFuels()));
					} else if (event instanceof SlotVisitImpl){
						SlotVisitImpl visit = (SlotVisitImpl) event;
						slotFuelCosts.add(sumFuelCosts(visit.getFuels()));	
					}		
				}
				
			}
			
		}, new boilOffOverride(compensate, minMax));
		
		return new scenarioResult(transferVolumes, slotVolumes, travelCosts, violations, slotFuelCosts);
	}
	
	public int sumFuelCosts(EList <FuelQuantity> fuels){
		int fuelTotal = 0;
		for(FuelQuantity fuel : fuels){
			for(FuelAmount fuelAmount : fuel.getAmounts()){
				if(fuelAmount.getUnit() == FuelUnit.M3){
					fuelTotal += fuelAmount.getQuantity();
				}
			}
		}
		return fuelTotal;
	}

	
	class scenarioResult {
		
		private double[] transferVolumes;
		private int[] slotMaximumVolumes;
		private int[] journeyIdleFuelInM3;
		private List<EMap<CapacityViolationType, Long>> violations;
		private int[] slotFuelCosts;
		
		
		scenarioResult(List<Integer> transferVolumes, List<Integer> slotMaximumVolumes, List<Integer> journeyIdleFuelInM3, List<EMap<CapacityViolationType, Long>> violations, List<Integer> slotFuelCosts){
			this.transferVolumes = transferVolumes.stream().mapToDouble(i->i).toArray();
			this.slotMaximumVolumes = slotMaximumVolumes.stream().mapToInt(i->i).toArray();
			this.journeyIdleFuelInM3 = journeyIdleFuelInM3.stream().mapToInt(i->i).toArray();
			this.violations = violations;
			this.slotFuelCosts = slotFuelCosts.stream().mapToInt(i->i).toArray();
		}
		
		double[] getTransferVolumes() {
			return transferVolumes;
		}

		int[] getSlotMaximumVolumes() {
			return slotMaximumVolumes;
		}

		int[] getJourneyFuelInM3() {
			return journeyIdleFuelInM3;
		}

		List<EMap<CapacityViolationType, Long>> getViolations() {
			return violations;
		}
		
		int getNonTransferFuel(){
			return IntStream.of(journeyIdleFuelInM3).sum();
		}
		
		int[] getSlotFuelCosts(){
			return slotFuelCosts;
		}
		
		int getViolationCount(){
			int violationCount = 0;
			for(EMap<CapacityViolationType, Long> violation : violations){
				violationCount += violation.size();
			}
			return violationCount;
		}
	}
	


}
