/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
import com.mmxlabs.models.lng.parameters.ActionPlanOptimisationStage;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGTransformerModule;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.AbstractRunnerHook;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.util.IRunnerHook;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.optimiser.core.IModifiableSequences;
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
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService.ModuleType;
import com.mmxlabs.scheduler.optimiser.voyage.impl.CapacityViolationType;

@SuppressWarnings("unused")
@RunWith(value = ShiroRunner.class)
public class CapacityViolationTestCase extends AbstractMicroTestCase {

	private static List<String> requiredFeatures = Lists.newArrayList("no-nominal-in-prompt", "optimisation-actionset");
	private static List<String> addedFeatures = new LinkedList<>();
	
	private VesselClass vesselClass;
	private Vessel vessel;
	private VesselAvailability vesselAvailability1;
	private Cargo cargo1;
	
	
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
			.withStartWindow(LocalDateTime.of(2015, 12, 4, 0, 0, 0), LocalDateTime.of(2015, 12, 8, 0, 0, 0)) //
			.withEndWindow(LocalDateTime.of(2016, 1, 1, 0, 0, 0))

			.build();

		// Create cargo 1
		cargo1 = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2015, 12, 10), portFinder.findPort("Point Fortin"), null, entity, "5") //
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build() //
				.withVesselAssignment(vesselAvailability1, 1) // -1 is nominal
				.withAssignmentFlags(false, false) //
				.build();
	}
	
	@Ignore("Uncertain behaviour regarding heel violations. Unable to produce multiple valid violations on one cargo.")
	@Test
	@Category({ MicroTest.class })
	public void testMultipleCapacityConstraints() throws Exception {
		
		LoadSlot loadSlot = cargoModelBuilder.makeFOBPurchase("L2", LocalDate.of(2015, 12, 9), portFinder.findPort("Point Fortin"), null, entity, "5", null)
				.build();
		
		DischargeSlot dischargeSlot = cargoModelBuilder.makeDESSale("D2", LocalDate.of(2015, 12, 20), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build();
		
		@NonNull
		VesselAvailability vesselAvailability2 = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2015, 12, 4, 0, 0, 0), LocalDateTime.of(2015, 12, 6, 0, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2015, 12, 6, 0, 0, 0),LocalDateTime.of(2016, 1, 1, 0, 0, 0))
				.withStartHeel(50.0,22.8, 1)
				.withEndHeel(50)//
				.build();

		optimiseWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();
			// Check single cargo
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());
			// Check correct cargoes remain and spot index has changed.
			final Cargo optCargo1 = optimiserScenario.getCargoModel().getCargoes().get(0);

			Assert.assertEquals(vesselAvailability1, optCargo1.getVesselAssignmentType());

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			Assert.assertNull(MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences));
			
			// Evaluate modified Sequence in the context of the first.
			ISequences rawSequences = SequenceHelper.createSequences(scenarioToOptimiserBridge,vesselAvailability1,loadSlot,dischargeSlot);
			final List<IEvaluatedStateConstraintChecker> failedConstraintCheckers = MicroTestUtils.validateEvaluatedStateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(),
					rawSequences);
			// Check that there is no ConstraintCheckers
			Assert.assertNull(failedConstraintCheckers);


			
		});
	}
	
	
	@Test
	@Category({ MicroTest.class })
	public void testViableCapacityConstraintSwap() throws Exception {
		
		LoadSlot loadSlot = cargoModelBuilder.makeFOBPurchase("L2", LocalDate.of(2015, 12, 6), portFinder.findPort("Point Fortin"), null, entity, "5", null)
				.build();
		
		DischargeSlot dischargeSlot = cargoModelBuilder.makeDESSale("D2", LocalDate.of(2015, 12, 20), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.build();

		optimiseWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();
			// Check single cargo
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());
			// Check correct cargoes remain and spot index has changed.
			final Cargo optCargo1 = optimiserScenario.getCargoModel().getCargoes().get(0);

			Assert.assertEquals(vesselAvailability1, optCargo1.getVesselAssignmentType());

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			Assert.assertNull(MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences));
			
			// Evaluate modified Sequence in the context of the first.
			ISequences rawSequences = SequenceHelper.createSequences(scenarioToOptimiserBridge,vesselAvailability1,loadSlot,dischargeSlot);
			final List<IEvaluatedStateConstraintChecker> failedConstraintCheckers = MicroTestUtils.validateEvaluatedStateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(),
					rawSequences);
			// Check that there is no ConstraintCheckers
			Assert.assertNull(failedConstraintCheckers);
		
		});
	}
	
	@Test
	@Category({ MicroTest.class })
	public void testInvalidCapacityConstraint() throws Exception {
		
		LoadSlot loadSlot = cargoModelBuilder.makeFOBPurchase("L2", LocalDate.of(2015, 12, 5), portFinder.findPort("Point Fortin"), null, entity, "5", null)
				.withVolumeLimits(100000, 150000, VolumeUnits.M3)//
				.build();
		
		DischargeSlot dischargeSlot = cargoModelBuilder.makeDESSale("D2", LocalDate.of(2015, 12, 11), portFinder.findPort("Dominion Cove Point LNG"), null, entity, "7") //
				.withVolumeLimits(200000, 250000, VolumeUnits.M3)
				.build();
		
		

		optimiseWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();
			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario();
			try {
				MicroCaseUtils.storeToFile(optimiserScenario,"X");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Check single cargo
			Assert.assertEquals(1, optimiserScenario.getCargoModel().getCargoes().size());
			// Check correct cargoes remain and spot index has changed.
			final Cargo optCargo1 = optimiserScenario.getCargoModel().getCargoes().get(0);

			Assert.assertEquals(vesselAvailability1, optCargo1.getVesselAssignmentType());

			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are valid
			Assert.assertNull(MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences));
			
			// Evaluate modified Sequence in the context of the first.
			ISequences rawSequences = SequenceHelper.createSequences(scenarioToOptimiserBridge,vesselAvailability1,loadSlot,dischargeSlot);
			final List<IEvaluatedStateConstraintChecker> failedConstraintCheckers = MicroTestUtils.validateEvaluatedStateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(),
					rawSequences);
			// Check that there is a singular constraint checker
			Assert.assertEquals(1, failedConstraintCheckers.size());
			// Check that it is CapacityEvaluatedStateChecker
			IEvaluatedStateConstraintChecker con = failedConstraintCheckers.get(0);
			Assert.assertTrue(con instanceof CapacityEvaluatedStateChecker);
			// Cast to CESC
			CapacityEvaluatedStateChecker castedCon = (CapacityEvaluatedStateChecker) con;
			// Check that there was a single initial violation
			Assert.assertEquals(1, castedCon.getInitialViolations());
			
			Set<IPortSlot> slots = castedCon.getCurrentViolatedSlots();
			long violations = castedCon.getTotalViolations();
			// Check that there is only a singular violated slot and a singular violation.
			Assert.assertEquals(1, violations);
			Assert.assertEquals(1, slots.size());
			// Check that the violation is a MIN_DISCHARGE
			Assert.assertEquals(CapacityViolationType.MIN_DISCHARGE,castedCon.getTriggeredViolations().get(0));
			
		});
	}

}
