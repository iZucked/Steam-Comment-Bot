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
import java.util.Set;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EList;
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
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.HeelOptions;
import com.mmxlabs.scheduler.optimiser.constraints.impl.AllowedVesselPermissionConstraintChecker;
import com.mmxlabs.scheduler.optimiser.constraints.impl.CapacityEvaluatedStateChecker;
import com.mmxlabs.scheduler.optimiser.constraints.impl.LatenessEvaluatedStateChecker;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService.ModuleType;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.impl.CapacityViolationType;
import com.mmxlabs.models.lng.schedule.impl.SlotVisitImpl;

@SuppressWarnings("unused")
@RunWith(value = ShiroRunner.class)
public class InPortBoilOffTests extends AbstractMicroTestCase {

	private static List<String> requiredFeatures = Lists.newArrayList("no-nominal-in-prompt", "optimisation-actionset");
	private static List<String> addedFeatures = new LinkedList<>();

	private VesselClass vesselClass;
	private Vessel vessel;
	private VesselAvailability vesselAvailability1;
	private Cargo cargo1;
	private Port portA;
	private Port portB;
	private VesselStateAttributes attrLaden;
	private VesselStateAttributes attrBal;

	private double ballastBoilOff = 2000.0;
	private double ladenBoilOff = 1000.0;
	private double ladenBase = 0.0;
	private double ballastBase = 0.0;
	
	private boolean writeScenario = true;

	public class boilOffOverride implements IOptimiserInjectorService {

		private boolean activateOverride = false;

		public boilOffOverride(boolean activateOverride) {
			this.activateOverride = activateOverride;
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

		attrBal = vesselClass.getBallastAttributes();
		attrBal.setInPortNBORate(ballastBoilOff);
		attrBal.setInPortBaseRate(ballastBase);
		vesselClass.setBallastAttributes(attrBal);

		attrLaden = vesselClass.getLadenAttributes();
		attrLaden.setInPortNBORate(ladenBoilOff);
		attrLaden.setInPortBaseRate(ladenBase);
		vesselClass.setLadenAttributes(attrLaden);

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

	@Test
	@Category({ MicroTest.class })
	public void LDBoilOffTest() throws Exception {
		
		// Create cargo 1
		cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 4), portA, null, entity, "9") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portB, null, entity, "9") //
				.build() //
				.withVesselAssignment(vesselAvailability1, 1) // -1 is nominal
				.withAssignmentFlags(false, false) //
				.build();

		Integer[] falseStats = runScenario(false);
		Integer[] trueStats = runScenario(true);
		
		double originalBallast = new Double(ballastBoilOff);
		double originalLaden = new Double(ladenBoilOff);

		Integer [] falseExpected = {140_000, (int) (136_612 - originalBallast - originalLaden)};
		Integer [] trueExpected = {(int) (140_000+originalLaden), (int) (136_612 - originalBallast)};
		
		ballastBoilOff = 0;
		ladenBoilOff = 0;
		attrLaden.setInPortNBORate(ladenBoilOff);
		attrBal.setInPortNBORate(ballastBoilOff);
		vesselClass.setLadenAttributes(attrLaden);
		vesselClass.setBallastAttributes(attrBal);
		
		Integer[] baseStats = runScenario(false);
		Integer [] baseExpected = {140_000,136_612};

		System.out.println(Arrays.toString(baseStats));
		System.out.println(Arrays.toString(falseStats));
		System.out.println(Arrays.toString(trueStats));
		Assert.assertArrayEquals(falseExpected, falseStats);
		Assert.assertArrayEquals(trueExpected, trueStats);
		Assert.assertArrayEquals(baseExpected, baseStats);
	}
	
	@Test
	@Category({ MicroTest.class })
	public void LDDBoilOffTest() throws Exception {
		
		Port portC = portFinder.findPort("Petrobras Pecem LNG");
		portC.setTimeZone("UTC");
		
		cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 4), portA, null, entity, "9") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portB, null, entity, "9")
				.withVolumeLimits(60_000, 70_000, VolumeUnits.M3)//
				.build() //
				.makeDESSale("D2", LocalDate.of(2015, 12, 20), portC, null, entity, "9")
				.withVolumeLimits(60_000, 70_000, VolumeUnits.M3)
				.build()
				.withVesselAssignment(vesselAvailability1, 1) // -1 is nominal
				.withAssignmentFlags(false, false) //
				.build();	
		
		Integer[] falseStats = runScenario(false);
		Integer[] trueStats = runScenario(true);
		
		int vesselCapacity = 140_000;
		int voyageFuel = 5_100;
		int maxDischarge = 70_000;
		Integer [] baseExpected = {vesselCapacity, maxDischarge, maxDischarge-voyageFuel};
		Integer [] falseExpected = {vesselCapacity, maxDischarge - 100,  (int) (maxDischarge- voyageFuel - (ballastBoilOff*2) - ladenBoilOff) + 100};
		Integer [] trueExpected = {(int) (vesselCapacity + ladenBoilOff), maxDischarge, (int) (maxDischarge - voyageFuel - (ballastBoilOff*2))};
		
		
		ballastBoilOff = 0;
		ladenBoilOff = 0;
		attrLaden.setInPortNBORate(ladenBoilOff);
		attrBal.setInPortNBORate(ballastBoilOff);
		vesselClass.setLadenAttributes(attrLaden);
		vesselClass.setBallastAttributes(attrBal);
		
		Integer[] baseStats = runScenario(false);
		
		
		
		
		System.out.println(Arrays.toString(baseStats));
		System.out.println(Arrays.toString(trueStats));
		System.out.println(Arrays.toString(falseStats));
		Assert.assertArrayEquals(falseExpected, falseStats);
		Assert.assertArrayEquals(trueExpected, trueStats);
		Assert.assertArrayEquals(baseExpected, baseStats);
		
		
		
	}

	
	public Integer[] runScenario(boolean compensate){

		List<Integer> stats = new ArrayList<Integer>();
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
			Assert.assertEquals(ballastBoilOff, ballastNBO, 0);
			Assert.assertEquals(ladenBoilOff, ladenNBO, 0);
			EList<CargoAllocation> cargoAllo = optimiserScenario.getScheduleModel().getSchedule().getCargoAllocations();

			for (CargoAllocation ca : cargoAllo) {
				EList<SlotAllocation> slots = ca.getSlotAllocations();
				int index = 0;
				for (SlotAllocation slot : slots) {
					String portName = slot.getPort().getName();		
					stats.add(slot.getVolumeTransferred()); 
				}
			}
		}, new boilOffOverride(compensate));

		Integer[] statsArray = stats.toArray(new Integer[stats.size()]);
		return statsArray;
	}
	
	


}
