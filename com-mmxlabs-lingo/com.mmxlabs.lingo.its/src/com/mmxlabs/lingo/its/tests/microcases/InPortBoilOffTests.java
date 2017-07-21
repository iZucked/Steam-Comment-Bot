/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;

import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselStateAttributes;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.util.SimpleCargoAllocation;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGTransformerModule;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScheduleTools;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.scheduler.optimiser.cache.NotCaching;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.MinMaxUnconstrainedVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;

public class InPortBoilOffTests extends AbstractMicroTestCase {

	private static List<String> requiredFeatures = Lists.newArrayList("no-nominal-in-prompt", "optimisation-actionset");
	private static List<String> addedFeatures = new LinkedList<>();

	// Need largish epsilon as summed LNG fuel use has rounding applied to each component.
	final int ROUNDING_EPSILON = 5;
	Vessel vessel;
	VesselAvailability vesselAvailability1;
	Cargo cargo1;
	Port portA;
	Port portB;
	VesselStateAttributes attrLaden;
	VesselStateAttributes attrBal;

	private final boolean writeScenario = true;

	public class boilOffOverride implements IOptimiserInjectorService {

		private boolean activateOverride = false;
		private boolean minMaxVolumeAllocator = false;

		public boilOffOverride(final boolean activateOverride, final boolean minMaxVolumeAllocator) {
			this.activateOverride = activateOverride;
			this.minMaxVolumeAllocator = minMaxVolumeAllocator;
		}

		@Override
		public @Nullable Module requestModule(@NonNull final ModuleType moduleType, @NonNull final Collection<@NonNull String> hints) {
			return null;
		}

		@Override
		public @Nullable List<@NonNull Module> requestModuleOverrides(@NonNull final ModuleType moduleType, @NonNull final Collection<@NonNull String> hints) {
			if (moduleType == ModuleType.Module_LNGTransformerModule) {
				return Collections.<@NonNull Module> singletonList(new AbstractModule() {
					@Override
					protected void configure() {
						if (minMaxVolumeAllocator) {
							bind(IVolumeAllocator.class).annotatedWith(NotCaching.class).to(MinMaxUnconstrainedVolumeAllocator.class);
						}
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
	public void constructor() throws Exception {

		super.constructor();
		
		portModelBuilder.setAllExistingPortsToUTC();
		
		// Set a default prompt in the past
		scenarioModelBuilder.setPromptPeriod(LocalDate.of(2014, 1, 1), LocalDate.of(2014, 3, 1));
		// Create the required basic elements
		vessel = fleetModelFinder.findVessel("STEAM-145");

		vesselAvailability1 = cargoModelBuilder.makeVesselAvailability(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2015, 12, 4, 7, 0, 0), LocalDateTime.of(2015, 12, 4, 13, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2015, 12, 30, 0, 0, 0)).build();

		portA = portFinder.findPort("Point Fortin");

		portB = portFinder.findPort("Dominion Cove Point LNG");
		portB.setDefaultStartTime(4);
		portB.setDefaultWindowSize(0);
	}

	public void changeBoilOffRates(final double loadPortBoilOff, final double dischargePortBoilOff) {

		attrBal = vessel.getBallastAttributes();
		attrBal.setInPortNBORate(dischargePortBoilOff);
		vessel.setBallastAttributes(attrBal);

		attrLaden = vessel.getLadenAttributes();
		attrLaden.setInPortNBORate(loadPortBoilOff);
		vessel.setLadenAttributes(attrLaden);
	}

	SimpleCargoAllocation runScenario(final boolean compensate, final boolean minMax) {

		final List<SimpleCargoAllocation> SCAs = new ArrayList<SimpleCargoAllocation>();
		optimiseWithLSOTest(scenarioRunner -> {

			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Check spot index has been updated
			final LNGScenarioModel optimiserScenario = scenarioToOptimiserBridge.getOptimiserScenario().getTypedScenario(LNGScenarioModel.class);

			if (writeScenario) {
				try {
					MicroCaseUtils.storeToFile(scenarioToOptimiserBridge.getOptimiserScenario(), "Trips");
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}

			final Schedule schedule = optimiserScenario.getScheduleModel().getSchedule();
			Assert.assertNotNull(schedule);

			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation("L1", schedule);
			Assert.assertNotNull(cargoAllocation);
			SCAs.add(new SimpleCargoAllocation(cargoAllocation));

		}, new boilOffOverride(compensate, minMax));

		return SCAs.get(0);
	}

}
