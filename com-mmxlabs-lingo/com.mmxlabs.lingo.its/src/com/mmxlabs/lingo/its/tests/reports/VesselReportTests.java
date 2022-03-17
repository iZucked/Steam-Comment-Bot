/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.reports;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.function.Consumer;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mmxlabs.lingo.its.tests.AbstractOptimisationResultTester;
import com.mmxlabs.lingo.its.tests.ReportTester;
import com.mmxlabs.lingo.its.tests.ReportTesterHelper;
import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lingo.its.tests.microcases.AbstractMicroTestCase;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.tests.TransformerExtensionTestBootstrapModule;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder.LNGOptimisationRunnerBuilder;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;

@ExtendWith(ShiroRunner.class)
public class VesselReportTests extends AbstractOptimisationResultTester {

	@Test
	@Tag(TestCategories.REPORT_TEST)
	public void testThreeFleetInstances() throws Exception {
		runTest(maker -> {

			// Create the required basic elements

			final Vessel vessel_1 = maker.fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_138);
			final Vessel vessel_2 = maker.fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

			final VesselAvailability vesselAvailability_1_1 = maker.cargoModelBuilder.makeVesselAvailability(vessel_1, maker.entity) //
					.withCharterRate("50000") //
					.withCharterNumber(1)//
					.withStartWindow(LocalDateTime.of(2020, 1, 1, 0, 0))//
					.withEndWindow(LocalDateTime.of(2020, 2, 1, 0, 0))//
					.build();
			final VesselAvailability vesselAvailability_1_2 = maker.cargoModelBuilder.makeVesselAvailability(vessel_1, maker.entity) //
					.withCharterRate("50000") //
					.withCharterNumber(2)//
					.withStartWindow(LocalDateTime.of(2020, 2, 1, 1, 0))//
					.withEndWindow(LocalDateTime.of(2020, 3, 1, 0, 0))//
					.build();
			final VesselAvailability vesselAvailability_1_3 = maker.cargoModelBuilder.makeVesselAvailability(vessel_1, maker.entity) //
					.withCharterRate("50000") //
					.withCharterNumber(3)//
					.withStartWindow(LocalDateTime.of(2020, 3, 1, 1, 0))//
					.withEndWindow(LocalDateTime.of(2020, 4, 1, 0, 0))//
					.build();
			final VesselAvailability vesselAvailability_2_1 = maker.cargoModelBuilder.makeVesselAvailability(vessel_2, maker.entity) //
					.withCharterRate("40000") //
					.withCharterNumber(1)//
					.withStartWindow(LocalDateTime.of(2020, 3, 1, 1, 0))//
					.withEndWindow(LocalDateTime.of(2020, 4, 1, 0, 0))//
					.build();

			maker.cargoModelBuilder.makeCargo()
					// Purchase
					.makeFOBPurchase("P1", LocalDate.of(2020, 1, 1), maker.portFinder.findPortById(InternalDataConstants.PORT_ONSLOW), null, maker.entity, "1")
					.withVolumeLimits(100000, 150000, VolumeUnits.M3).build()
					// Sale
					.makeDESSale("S1", LocalDate.of(2020, 1, 15), maker.portFinder.findPortById(InternalDataConstants.PORT_FUTTSU), null, maker.entity, "10")
					.withVolumeLimits(100000, 150000, VolumeUnits.M3).build()
					// Cargo
					.withVesselAssignment(vesselAvailability_1_1, 0).build();
			maker.cargoModelBuilder.makeCargo()
					// Purchase
					.makeFOBPurchase("P2", LocalDate.of(2020, 2, 1), maker.portFinder.findPortById(InternalDataConstants.PORT_ONSLOW), null, maker.entity, "1")
					.withVolumeLimits(100000, 150000, VolumeUnits.M3).build()
					// Sale
					.makeDESSale("S2", LocalDate.of(2020, 2, 15), maker.portFinder.findPortById(InternalDataConstants.PORT_FUTTSU), null, maker.entity, "10")
					.withVolumeLimits(100000, 150000, VolumeUnits.M3).build()
					// Cargo
					.withVesselAssignment(vesselAvailability_1_2, 0).build();
			maker.cargoModelBuilder.makeCargo()
					// Purchase
					.makeFOBPurchase("P3", LocalDate.of(2020, 3, 1), maker.portFinder.findPortById(InternalDataConstants.PORT_ONSLOW), null, maker.entity, "1")
					.withVolumeLimits(100000, 150000, VolumeUnits.M3).build()
					// Sale
					.makeDESSale("S3", LocalDate.of(2020, 3, 15), maker.portFinder.findPortById(InternalDataConstants.PORT_FUTTSU), null, maker.entity, "10")
					.withVolumeLimits(100000, 150000, VolumeUnits.M3).build()
					// Cargo
					.withVesselAssignment(vesselAvailability_1_3, 0).build();
			maker.cargoModelBuilder.makeCargo()
					// Purchase
					.makeFOBPurchase("P4", LocalDate.of(2020, 3, 1), maker.portFinder.findPortById(InternalDataConstants.PORT_DARWIN), null, maker.entity, "1")
					.withVolumeLimits(100000, 150000, VolumeUnits.M3).build()
					// Sale
					.makeDESSale("S4", LocalDate.of(2020, 3, 15), maker.portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, maker.entity, "10")
					.withVolumeLimits(100000, 150000, VolumeUnits.M3).build()
					// Cargo
					.withVesselAssignment(vesselAvailability_2_1, 0).build();

		}, maker -> {

			// Create the required basic elements

			final Vessel vessel_1 = maker.fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_138);
			final Vessel vessel_2 = maker.fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

			final VesselAvailability vesselAvailability_1_1 = maker.cargoModelBuilder.makeVesselAvailability(vessel_1, maker.entity) //
					.withCharterRate("50000") //
					.withCharterNumber(1)//
					.withStartWindow(LocalDateTime.of(2020, 1, 1, 0, 0))//
					.withEndWindow(LocalDateTime.of(2020, 2, 1, 0, 0))//
					.build();
			final VesselAvailability vesselAvailability_1_2 = maker.cargoModelBuilder.makeVesselAvailability(vessel_1, maker.entity) //
					.withCharterRate("50000") //
					.withCharterNumber(2)//
					.withStartWindow(LocalDateTime.of(2020, 2, 1, 1, 0))//
					.withEndWindow(LocalDateTime.of(2020, 3, 1, 0, 0))//
					.build();
			final VesselAvailability vesselAvailability_1_3 = maker.cargoModelBuilder.makeVesselAvailability(vessel_1, maker.entity) //
					.withCharterRate("50000") //
					.withCharterNumber(3)//
					.withStartWindow(LocalDateTime.of(2020, 3, 1, 1, 0))//
					.withEndWindow(LocalDateTime.of(2020, 4, 1, 0, 0))//
					.build();
			final VesselAvailability vesselAvailability_2_1 = maker.cargoModelBuilder.makeVesselAvailability(vessel_2, maker.entity) //
					.withCharterRate("40000") //
					.withCharterNumber(1)//
					.withStartWindow(LocalDateTime.of(2020, 3, 1, 1, 0))//
					.withEndWindow(LocalDateTime.of(2020, 4, 1, 0, 0))//
					.build();

			maker.cargoModelBuilder.makeCargo()
					// Purchase
					.makeFOBPurchase("P1", LocalDate.of(2020, 1, 1), maker.portFinder.findPortById(InternalDataConstants.PORT_ONSLOW), null, maker.entity, "1")
					.withVolumeLimits(100000, 150000, VolumeUnits.M3).build()
					// Sale
					.makeDESSale("S1", LocalDate.of(2020, 1, 15), maker.portFinder.findPortById(InternalDataConstants.PORT_FUTTSU), null, maker.entity, "10")
					.withVolumeLimits(100000, 150000, VolumeUnits.M3).build()
					// Cargo
					.withVesselAssignment(vesselAvailability_1_1, 0).build();
			maker.cargoModelBuilder.makeCargo()
					// Purchase
					.makeFOBPurchase("P2", LocalDate.of(2020, 2, 1), maker.portFinder.findPortById(InternalDataConstants.PORT_ONSLOW), null, maker.entity, "1")
					.withVolumeLimits(100000, 150000, VolumeUnits.M3).build()
					// Sale
					.makeDESSale("S2", LocalDate.of(2020, 2, 15), maker.portFinder.findPortById(InternalDataConstants.PORT_FUTTSU), null, maker.entity, "10")
					.withVolumeLimits(100000, 150000, VolumeUnits.M3).build()
					// Cargo
					.withVesselAssignment(vesselAvailability_1_2, 0).build();
			maker.cargoModelBuilder.makeCargo()
					// Purchase
					.makeFOBPurchase("P3", LocalDate.of(2020, 3, 1), maker.portFinder.findPortById(InternalDataConstants.PORT_ONSLOW), null, maker.entity, "1")
					.withVolumeLimits(100000, 150000, VolumeUnits.M3).build()
					// Sale
					.makeDESSale("S3", LocalDate.of(2020, 3, 15), maker.portFinder.findPortById(InternalDataConstants.PORT_FUTTSU), null, maker.entity, "10")
					.withVolumeLimits(100000, 150000, VolumeUnits.M3).build()
					// Cargo
					.withVesselAssignment(vesselAvailability_2_1, 0).build();
			maker.cargoModelBuilder.makeCargo()
					// Purchase
					.makeFOBPurchase("P4", LocalDate.of(2020, 3, 1), maker.portFinder.findPortById(InternalDataConstants.PORT_DARWIN), null, maker.entity, "1")
					.withVolumeLimits(100000, 150000, VolumeUnits.M3).build()
					// Sale
					.makeDESSale("S4", LocalDate.of(2020, 3, 15), maker.portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, maker.entity, "10")
					.withVolumeLimits(100000, 150000, VolumeUnits.M3).build()
					// Cargo
					.withVesselAssignment(vesselAvailability_1_3, 0).build();
		});
	}

	public void runTest(final Consumer<ScenarioMaker> fromBuilder, final Consumer<ScenarioMaker> toBuilder) throws Exception {

		LNGScenarioModel from;
		IScenarioDataProvider fromDP;
		{
			final ScenarioMaker maker = new ScenarioMaker();
			maker.constructor();
			maker.buildIt(fromBuilder);
			from = maker.lngScenarioModel;
			fromDP = maker.scenarioDataProvider;
		}
		evaluate(fromDP, from);

		LNGScenarioModel to;
		IScenarioDataProvider toDP;
		{
			final ScenarioMaker maker = new ScenarioMaker();
			maker.constructor();
			maker.buildIt(toBuilder);
			to = maker.lngScenarioModel;
			toDP = maker.scenarioDataProvider;
		}
		evaluate(toDP, to);

		final ScenarioModelRecord pinnedRecord = ScenarioStorageUtil.createFromCopyOf("from", fromDP);
		final ScenarioModelRecord otherRecord = ScenarioStorageUtil.createFromCopyOf("to", toDP);

		try {
			final URL url = getClass().getResource("/reports/vessel-report/");
			ReportTester.testPinDiffReports(pinnedRecord, fromDP, otherRecord, toDP, url, ReportTesterHelper.VESSEL_REPORT_ID, ReportTesterHelper.VESSEL_REPORT_SHORTNAME, "html");
		} finally {
			pinnedRecord.dispose();
			otherRecord.dispose();
		}

	}

	static class ScenarioMaker extends AbstractMicroTestCase {
		void buildIt(final Consumer<ScenarioMaker> r) {
			r.accept(this);
		}
	}

	public void evaluate(@NonNull IScenarioDataProvider scenarioDataProvider, final LNGScenarioModel lngScenarioModel) {
		// Create UserSettings
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setGenerateCharterOuts(false);
		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		final OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, null, lngScenarioModel);
		// Generate internal data
		LNGOptimisationRunnerBuilder runner = LNGOptimisationBuilder.begin(scenarioDataProvider) //
				.withOptimisationPlan(optimisationPlan) //
				.withExtraModule(new TransformerExtensionTestBootstrapModule()) //
				.withThreadCount(1)//
				.buildDefaultRunner();

		runner.evaluateInitialState();
	}
}
