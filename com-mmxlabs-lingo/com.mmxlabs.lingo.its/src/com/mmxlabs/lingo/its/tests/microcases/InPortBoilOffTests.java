/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
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
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselStateAttributes;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.util.SimpleCargoAllocation;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScheduleTools;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.MinMaxUnconstrainedVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;

public abstract class InPortBoilOffTests extends AbstractLegacyMicroTestCase {

	// Need largish epsilon as summed LNG fuel use has rounding applied to each component.
	final int ROUNDING_EPSILON = 5;
	Vessel vessel;
	VesselCharter vesselCharter1;
	Cargo cargo1;
	Port portA;
	Port portB;
	VesselStateAttributes attrLaden;
	VesselStateAttributes attrBal;

	private final boolean writeScenario = false;

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
							bind(IVolumeAllocator.class).to(MinMaxUnconstrainedVolumeAllocator.class);
						}
					}

					@Provides
					@Named(SchedulerConstants.COMMERCIAL_VOLUME_OVERCAPACITY)
					private boolean commercialVolumeOverCapacity() {
						return activateOverride;
					}
				});
			}
			return null;
		}
	}

	@BeforeEach
	@Override
	public void constructor() throws Exception {

		super.constructor();
		
		portModelBuilder.setAllExistingPortsToUTC();
		
		// Set a default prompt in the past
		scenarioModelBuilder.setPromptPeriod(LocalDate.of(2014, 1, 1), LocalDate.of(2014, 3, 1));
		// Create the required basic elements
		vessel = fleetModelFinder.findVessel("STEAM-145");

		vesselCharter1 = cargoModelBuilder.makeVesselCharter(vessel, entity) //
				.withStartWindow(LocalDateTime.of(2015, 12, 4, 7, 0, 0), LocalDateTime.of(2015, 12, 4, 13, 0, 0)) //
				.withEndWindow(LocalDateTime.of(2015, 12, 30, 0, 0, 0)).build();

		portA = portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN);

		portB = portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT);
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
			Assertions.assertNotNull(schedule);

			final CargoAllocation cargoAllocation = ScheduleTools.findCargoAllocation("L1", schedule);
			Assertions.assertNotNull(cargoAllocation);
			SCAs.add(new SimpleCargoAllocation(cargoAllocation));

		}, new boilOffOverride(compensate, minMax));

		return SCAs.get(0);
	}

}
