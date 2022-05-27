/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases.compare;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lingo.its.tests.microcases.AbstractMicroTestCase;
import com.mmxlabs.lingo.reports.services.SelectedDataProviderImpl;
import com.mmxlabs.lingo.reports.utils.ICustomRelatedSlotHandler;
import com.mmxlabs.lingo.reports.views.changeset.ChangeSetKPIUtil;
import com.mmxlabs.lingo.reports.views.changeset.ChangeSetKPIUtil.ResultType;
import com.mmxlabs.lingo.reports.views.changeset.ChangeSetToTableTransformer;
import com.mmxlabs.lingo.reports.views.changeset.ChangeSetToTableTransformer.SortMode;
import com.mmxlabs.lingo.reports.views.changeset.PinDiffResultPlanTransformer;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRoot;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRoot;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.tests.TransformerExtensionTestBootstrapModule;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder.LNGOptimisationRunnerBuilder;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.models.lng.types.DESPurchaseDealType;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.scenario.service.ScenarioResult;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.ui.ScenarioResultImpl;

@ExtendWith(ShiroRunner.class)
public class CompareViewTests {

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testSimpleDESCargoSwap() throws Exception {
		runTest(maker -> {
			maker.cargoModelBuilder.makeCargo() //
					// Purchase
					.makeDESPurchase("DP1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2015, 01, 10), maker.portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, maker.entity, "5", 22.8,
							null) //
					.withVolumeLimits(3_000_000, 3_000_000, VolumeUnits.MMBTU) //
					.build() //
					// Sale
					.makeDESSale("DS1", LocalDate.of(2015, 01, 10), maker.portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, maker.entity, "7") //
					.withVolumeLimits(3_000_000, 3_000_000, VolumeUnits.MMBTU) //

					.build() //
					// Cargo
					.build();

			maker.cargoModelBuilder.makeCargo() //
					// Purchase
					.makeDESPurchase("DP2", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2015, 01, 10), maker.portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, maker.entity, "3", 22.8,
							null) //
					.withVolumeLimits(3_000_000, 3_000_000, VolumeUnits.MMBTU) //

					.build() //
					// Sale
					.makeDESSale("DS2", LocalDate.of(2015, 01, 10), maker.portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, maker.entity, "8") //
					.withVolumeLimits(3_000_000, 3_000_000, VolumeUnits.MMBTU) //

					.build() //
					// Cargo
					.build();

		}, maker -> {
			maker.cargoModelBuilder.makeCargo() //
					// Purchase
					.makeDESPurchase("DP1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2015, 01, 10), maker.portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, maker.entity, "3", 22.8,
							null) //
					.withVolumeLimits(3_000_000, 3_000_000, VolumeUnits.MMBTU) //
					.build() //
					// Sale
					.makeDESSale("DS2", LocalDate.of(2015, 01, 10), maker.portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, maker.entity, "7") //
					.withVolumeLimits(3_000_000, 3_000_000, VolumeUnits.MMBTU) //
					.build() //
					// Cargo
					.build();

			maker.cargoModelBuilder.makeCargo() //
					// Purchase
					.makeDESPurchase("DP2", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2015, 01, 10), maker.portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, maker.entity, "5", 22.8,
							null) //
					.withVolumeLimits(3_000_000, 3_000_000, VolumeUnits.MMBTU) //
					.build() //
					// Sale
					.makeDESSale("DS1", LocalDate.of(2015, 01, 10), maker.portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, maker.entity, "8") //
					.withVolumeLimits(3_000_000, 3_000_000, VolumeUnits.MMBTU) //
					.build() //
					// Cargo
					.build();

		}, changeSetRoot -> {
			Assertions.assertEquals(1, changeSetRoot.getChangeSets().size());

			ChangeSetToTableTransformer transformer = new ChangeSetToTableTransformer();
			ChangeSetTableRoot tableRoot = transformer.createViewDataModel(changeSetRoot, false, null, SortMode.BY_GROUP);

			final ChangeSetTableGroup changeSet = tableRoot.getGroups().get(0);
			Assertions.assertEquals(2, changeSet.getRows().size());

			final ChangeSetTableRow row1 = changeSet.getRows().get(0);
			final ChangeSetTableRow row2 = changeSet.getRows().get(1);

			// Expect 7-5 * 3m
			Assertions.assertEquals(3_000_000 * (7 - 5), ChangeSetKPIUtil.getPNL(row1, ResultType.Before));
			// Expect 8-3 * 3m
			Assertions.assertEquals(3_000_000 * (8 - 3), ChangeSetKPIUtil.getPNL(row2, ResultType.Before));

			// Expect 7-3 * 3m
			Assertions.assertEquals(3_000_000 * (7 - 3), ChangeSetKPIUtil.getPNL(row1, ResultType.After));
			// Expect 8-5 * 3m
			Assertions.assertEquals(3_000_000 * (8 - 5), ChangeSetKPIUtil.getPNL(row2, ResultType.After));

			// P&L Sanity check -- no missing components in P&L
			for (final ChangeSetTableRow row : changeSet.getRows()) {
				Assertions.assertEquals(ChangeSetKPIUtil.getPNL(row, ResultType.Before), ChangeSetKPIUtil.getPNLSum(row, ResultType.Before));
				Assertions.assertEquals(ChangeSetKPIUtil.getPNL(row, ResultType.After), ChangeSetKPIUtil.getPNLSum(row, ResultType.After));
			}
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testSlotCancellationFee_CargoToOpen() throws Exception {
		runTest(maker -> {
			maker.cargoModelBuilder.makeCargo() //
					// Purchase
					.makeDESPurchase("DP1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2015, 01, 10), maker.portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, maker.entity, "5", 22.8,
							null) //
					.withOptional(true) //
					.withCancellationFee("50000") //
					.build() //
					// Sale
					.makeDESSale("DS", LocalDate.of(2015, 01, 10), maker.portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, maker.entity, "5") //
					.withOptional(true) //
					.withCancellationFee("60000") //
					.build() //
					// Cargo
					.build();

		}, maker -> {
			maker.cargoModelBuilder
					.makeDESPurchase("DP1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2015, 01, 10), maker.portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, maker.entity, "5", 22.8,
							null) //
					.withOptional(true) //
					.withCancellationFee("50000") //
					.build();

			maker.cargoModelBuilder.makeDESSale("DS", LocalDate.of(2015, 01, 10), maker.portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, maker.entity, "5") //
					.withOptional(true) //
					.withCancellationFee("60000") //
					.build();

		}, changeSetRoot -> {
			Assertions.assertEquals(1, changeSetRoot.getChangeSets().size());

			ChangeSetToTableTransformer transformer = new ChangeSetToTableTransformer();
			ChangeSetTableRoot tableRoot = transformer.createViewDataModel(changeSetRoot, false, null, SortMode.BY_GROUP);

			final ChangeSetTableGroup changeSet = tableRoot.getGroups().get(0);
			Assertions.assertEquals(2, changeSet.getRows().size());

			final ChangeSetTableRow row1 = changeSet.getRows().get(0);
			// Assertions.assertNotNull(row1.getOriginalDischargeAllocation());
			// Assertions.assertNotNull(row1.getOriginalLoadAllocation());
			// Assertions.assertNotNull(row1.getNewOpenLoadAllocation());

			final ChangeSetTableRow row2 = changeSet.getRows().get(1);
			// Assertions.assertNotNull(row2.getOriginalDischargeAllocation());

			// Expect 50K for load cancellation
			Assertions.assertEquals(-50_000, ChangeSetKPIUtil.getPNL(row1, ResultType.After));
			Assertions.assertEquals(-50_000, ChangeSetKPIUtil.getCargoOtherPNL(row1, ResultType.After));
			// Expect 60K for discharge cancellation
			Assertions.assertEquals(-60_000, ChangeSetKPIUtil.getPNL(row2, ResultType.After));
			Assertions.assertEquals(-60_000, ChangeSetKPIUtil.getCargoOtherPNL(row2, ResultType.After));

			// Original cargo is a zero sum P&L
			Assertions.assertEquals(0, ChangeSetKPIUtil.getPNL(row1, ResultType.Before));
			Assertions.assertEquals(0, ChangeSetKPIUtil.getCargoOtherPNL(row1, ResultType.Before));
			Assertions.assertEquals(0, ChangeSetKPIUtil.getPNL(row2, ResultType.Before));
			Assertions.assertEquals(0, ChangeSetKPIUtil.getCargoOtherPNL(row2, ResultType.Before));

			// P&L Sanity check -- no missing components in P&L
			for (final ChangeSetTableRow row : changeSet.getRows()) {
				Assertions.assertEquals(ChangeSetKPIUtil.getPNL(row, ResultType.Before), ChangeSetKPIUtil.getPNLSum(row, ResultType.Before));
				Assertions.assertEquals(ChangeSetKPIUtil.getPNL(row, ResultType.After), ChangeSetKPIUtil.getPNLSum(row, ResultType.After));
			}
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testSlotCancellationFee_OpenToCargo() throws Exception {
		runTest(maker -> {
			maker.cargoModelBuilder
					.makeDESPurchase("DP1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2015, 01, 10), maker.portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, maker.entity, "5", 22.8,
							null) //
					.withOptional(true) //
					.withCancellationFee("50000") //
					.build();

			maker.cargoModelBuilder.makeDESSale("DS", LocalDate.of(2015, 01, 10), maker.portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, maker.entity, "5") //
					.withOptional(true) //
					.withCancellationFee("60000") //
					.build();

		}, maker -> {
			maker.cargoModelBuilder.makeCargo() //
					// Purchase
					.makeDESPurchase("DP1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2015, 01, 10), maker.portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, maker.entity, "5", 22.8,
							null) //
					.withOptional(true) //
					.withCancellationFee("50000") //
					.build() //
					// Sale
					.makeDESSale("DS", LocalDate.of(2015, 01, 10), maker.portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, maker.entity, "5") //
					.withOptional(true) //
					.withCancellationFee("60000") //
					.build() //
					// Cargo
					.build();

		}, changeSetRoot -> {
			Assertions.assertEquals(1, changeSetRoot.getChangeSets().size());

			ChangeSetToTableTransformer transformer = new ChangeSetToTableTransformer();
			ChangeSetTableRoot tableRoot = transformer.createViewDataModel(changeSetRoot, false, null, SortMode.BY_GROUP);

			final ChangeSetTableGroup changeSet = tableRoot.getGroups().get(0);
			Assertions.assertEquals(1, changeSet.getRows().size());

			final ChangeSetTableRow row1 = changeSet.getRows().get(0);
			assert row1 != null;
			// Expect 50K for load cancellation and 60K for discharge cancellation
			Assertions.assertEquals(-50_000 + -60_000, ChangeSetKPIUtil.getPNL(row1, ResultType.Before));
			Assertions.assertEquals(-50_000 + -60_000, ChangeSetKPIUtil.getCargoOtherPNL(row1, ResultType.Before));

			// New cargo is a zero sum P&L
			Assertions.assertEquals(0, ChangeSetKPIUtil.getPNL(row1, ResultType.After));
			Assertions.assertEquals(0, ChangeSetKPIUtil.getCargoOtherPNL(row1, ResultType.After));

			// P&L Sanity check -- no missing components in P&L
			for (final ChangeSetTableRow row : changeSet.getRows()) {
				Assertions.assertEquals(ChangeSetKPIUtil.getPNL(row, ResultType.Before), ChangeSetKPIUtil.getPNLSum(row, ResultType.Before));
				Assertions.assertEquals(ChangeSetKPIUtil.getPNL(row, ResultType.After), ChangeSetKPIUtil.getPNLSum(row, ResultType.After));
			}
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testSlotMiscCosts_CargoToOpen() throws Exception {
		runTest(maker -> {
			maker.cargoModelBuilder.makeCargo() //
					// Purchase
					.makeDESPurchase("DP1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2015, 01, 10), maker.portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, maker.entity, "5", 22.8,
							null) //
					.withOptional(true) //
					.withMiscCosts(50000) //
					.build() //
					// Sale
					.makeDESSale("DS", LocalDate.of(2015, 01, 10), maker.portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, maker.entity, "5") //
					.withOptional(true) //
					.withMiscCosts(60000) //
					.build() //
					// Cargo
					.build();

		}, maker -> {
			maker.cargoModelBuilder
					.makeDESPurchase("DP1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2015, 01, 10), maker.portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, maker.entity, "5", 22.8,
							null) //
					.withOptional(true) //
					.withMiscCosts(50000) //
					.build();

			maker.cargoModelBuilder.makeDESSale("DS", LocalDate.of(2015, 01, 10), maker.portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, maker.entity, "5") //
					.withOptional(true) //
					.withMiscCosts(60000) //
					.build();

		}, changeSetRoot -> {
			Assertions.assertEquals(1, changeSetRoot.getChangeSets().size());

			ChangeSetToTableTransformer transformer = new ChangeSetToTableTransformer();
			ChangeSetTableRoot tableRoot = transformer.createViewDataModel(changeSetRoot, false, null, SortMode.BY_GROUP);

			final ChangeSetTableGroup changeSet = tableRoot.getGroups().get(0);
			Assertions.assertEquals(2, changeSet.getRows().size());

			final ChangeSetTableRow row1 = changeSet.getRows().get(0);
			// Assertions.assertNotNull(row1.getOriginalDischargeAllocation());
			// Assertions.assertNotNull(row1.getOriginalLoadAllocation());
			// Assertions.assertNotNull(row1.getNewOpenLoadAllocation());

			final ChangeSetTableRow row2 = changeSet.getRows().get(1);
			// Assertions.assertNotNull(row2.getOriginalDischargeAllocation());

			// Expect 50K for load costs and 60K for discharge costs
			Assertions.assertEquals(-50_000 + -60_000, ChangeSetKPIUtil.getPNL(row1, ResultType.Before));
			Assertions.assertEquals(-50_000 + -60_000, ChangeSetKPIUtil.getCargoOtherPNL(row1, ResultType.Before));
			// No costs on row two as part of row1 original P&L
			Assertions.assertEquals(0, ChangeSetKPIUtil.getPNL(row2, ResultType.Before));
			Assertions.assertEquals(0, ChangeSetKPIUtil.getCargoOtherPNL(row2, ResultType.Before));

			// Original cargo is a zero sum P&L
			Assertions.assertEquals(0, ChangeSetKPIUtil.getPNL(row1, ResultType.After));
			Assertions.assertEquals(0, ChangeSetKPIUtil.getCargoOtherPNL(row1, ResultType.After));
			Assertions.assertEquals(0, ChangeSetKPIUtil.getPNL(row2, ResultType.After));
			Assertions.assertEquals(0, ChangeSetKPIUtil.getCargoOtherPNL(row2, ResultType.After));

			// P&L Sanity check -- no missing components in P&L
			for (final ChangeSetTableRow row : changeSet.getRows()) {
				Assertions.assertEquals(ChangeSetKPIUtil.getPNL(row, ResultType.Before), ChangeSetKPIUtil.getPNLSum(row, ResultType.Before));
				Assertions.assertEquals(ChangeSetKPIUtil.getPNL(row, ResultType.After), ChangeSetKPIUtil.getPNLSum(row, ResultType.After));
			}
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testSlotMiscCosts_OpenToCargo() throws Exception {
		runTest(maker -> {
			maker.cargoModelBuilder
					.makeDESPurchase("DP1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2015, 01, 10), maker.portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, maker.entity, "5", 22.8,
							null) //
					.withOptional(true) //
					.withMiscCosts(50000) //
					.build();

			maker.cargoModelBuilder.makeDESSale("DS", LocalDate.of(2015, 01, 10), maker.portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, maker.entity, "5") //
					.withOptional(true) //
					.withMiscCosts(60000) //
					.build();

		}, maker -> {
			maker.cargoModelBuilder.makeCargo() //
					// Purchase
					.makeDESPurchase("DP1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2015, 01, 10), maker.portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, maker.entity, "5", 22.8,
							null) //
					.withOptional(true) //
					.withMiscCosts(50000) //
					.build() //
					// Sale
					.makeDESSale("DS", LocalDate.of(2015, 01, 10), maker.portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, maker.entity, "5") //
					.withOptional(true) //
					.withMiscCosts(60000) //
					.build() //
					// Cargo
					.build();

		}, changeSetRoot -> {
			Assertions.assertEquals(1, changeSetRoot.getChangeSets().size());

			ChangeSetToTableTransformer transformer = new ChangeSetToTableTransformer();
			ChangeSetTableRoot tableRoot = transformer.createViewDataModel(changeSetRoot, false, null, SortMode.BY_GROUP);

			final ChangeSetTableGroup changeSet = tableRoot.getGroups().get(0);
			Assertions.assertEquals(1, changeSet.getRows().size());

			final ChangeSetTableRow row1 = changeSet.getRows().get(0);
			assert row1 != null;
			// Expect 50K for load cost and 60K for discharge costs
			Assertions.assertEquals(-50_000 + -60_000, ChangeSetKPIUtil.getPNL(row1, ResultType.After));
			Assertions.assertEquals(-50_000 + -60_000, ChangeSetKPIUtil.getCargoOtherPNL(row1, ResultType.After));

			// original slot value is zero sum P&L
			Assertions.assertEquals(0, ChangeSetKPIUtil.getPNL(row1, ResultType.Before));
			Assertions.assertEquals(0, ChangeSetKPIUtil.getCargoOtherPNL(row1, ResultType.Before));

			// P&L Sanity check -- no missing components in P&L
			for (final ChangeSetTableRow row : changeSet.getRows()) {
				Assertions.assertEquals(ChangeSetKPIUtil.getPNL(row, ResultType.Before), ChangeSetKPIUtil.getPNLSum(row, ResultType.Before));
				Assertions.assertEquals(ChangeSetKPIUtil.getPNL(row, ResultType.After), ChangeSetKPIUtil.getPNLSum(row, ResultType.After));
			}
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testSlotCancellationFee_OpenToLDD() throws Exception {
		runTest(maker -> {

			// Create the required basic elements

			final Vessel vessel_1 = maker.fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

			final VesselCharter vesselCharter = maker.cargoModelBuilder.makeVesselCharter(vessel_1, maker.entity) //
					// .withCharterRate("50000") //
					.build();

			maker.cargoModelBuilder.makeFOBPurchase("FP1", LocalDate.of(2015, 01, 10), maker.portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, maker.entity, "5", 22.8) //
					.withOptional(true) //
					.withCancellationFee("50000") //
					.build();

			maker.cargoModelBuilder.makeDESSale("DS1", LocalDate.of(2015, 02, 10), maker.portFinder.findPortById(InternalDataConstants.PORT_DAHEJ), null, maker.entity, "5") //
					.withOptional(true) //
					.withCancellationFee("60000") //
					.build();

			maker.cargoModelBuilder.makeDESSale("DS2", LocalDate.of(2015, 03, 10), maker.portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, maker.entity, "5") //
					.withOptional(true) //
					.withCancellationFee("70000") //
					.build();

		}, maker -> {

			// Create the required basic elements

			final Vessel vessel_1 = maker.fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

			final VesselCharter vesselCharter = maker.cargoModelBuilder.makeVesselCharter(vessel_1, maker.entity) //
					// .withCharterRate("50000") //
					.build();

			maker.cargoModelBuilder.makeCargo() //
					// Purchase
					.makeFOBPurchase("FP1", LocalDate.of(2015, 01, 10), maker.portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, maker.entity, "5") //
					.withWindowSize(0, TimePeriod.HOURS) //
					.withOptional(true) //
					.withCancellationFee("50000") //
					.build() //
					// Sale
					.makeDESSale("DS1", LocalDate.of(2015, 02, 10), maker.portFinder.findPortById(InternalDataConstants.PORT_DAHEJ), null, maker.entity, "5") //
					.withWindowSize(0, TimePeriod.HOURS) //
					.withOptional(true) //
					.withCancellationFee("60000") //
					.build() //

					.makeDESSale("DS2", LocalDate.of(2015, 03, 10), maker.portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, maker.entity, "5") //
					.withWindowSize(0, TimePeriod.HOURS) //
					.withOptional(true) //
					.withCancellationFee("70000") //
					.build() //

					// Cargo
					.withVesselAssignment(vesselCharter, 1) //
					.build();

		}, changeSetRoot -> {
			Assertions.assertEquals(1, changeSetRoot.getChangeSets().size());

			ChangeSetToTableTransformer transformer = new ChangeSetToTableTransformer();
			ChangeSetTableRoot tableRoot = transformer.createViewDataModel(changeSetRoot, false, null, SortMode.BY_GROUP);

			final ChangeSetTableGroup changeSet = tableRoot.getGroups().get(0);
			Assertions.assertEquals(2, changeSet.getRows().size());

			final ChangeSetTableRow row1 = changeSet.getRows().get(0);
			final ChangeSetTableRow row2 = changeSet.getRows().get(1);
			assert row1 != null;
			// Expect 50K for load cancellation and 60K for discharge cancellation
			Assertions.assertEquals(-50_000 + -60_000, ChangeSetKPIUtil.getPNL(row1, ResultType.Before));
			Assertions.assertEquals(-50_000 + -60_000, ChangeSetKPIUtil.getCargoOtherPNL(row1, ResultType.Before));
			Assertions.assertEquals(-70_000, ChangeSetKPIUtil.getPNL(row2, ResultType.Before));
			Assertions.assertEquals(-70_000, ChangeSetKPIUtil.getCargoOtherPNL(row2, ResultType.Before));

			// No cancellation fee's after
			Assertions.assertEquals(0, ChangeSetKPIUtil.getCargoOtherPNL(row1, ResultType.After));
			Assertions.assertEquals(0, ChangeSetKPIUtil.getCargoOtherPNL(row2, ResultType.After));
			// No P&L in row 2
			Assertions.assertEquals(0, ChangeSetKPIUtil.getPNL(row2, ResultType.After));
			// Cargo P&L in row 1
			Assertions.assertNotEquals(0, ChangeSetKPIUtil.getPNL(row1, ResultType.After));

			// P&L Sanity check -- no missing components in P&L
			for (final ChangeSetTableRow row : changeSet.getRows()) {
				// Allow $1 "rounding"
				final int roudingDelta = 1;
				Assertions.assertEquals(ChangeSetKPIUtil.getPNL(row, ResultType.Before), ChangeSetKPIUtil.getPNLSum(row, ResultType.Before), roudingDelta);
				Assertions.assertEquals(ChangeSetKPIUtil.getPNL(row, ResultType.After), ChangeSetKPIUtil.getPNLSum(row, ResultType.After), roudingDelta);
			}
		});
	}

	@Test
	@Tag(TestCategories.MICRO_TEST)
	public void testLDToLDD() throws Exception {
		runTest(maker -> {

			// Create the required basic elements

			final Vessel vessel_1 = maker.fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

			final VesselCharter vesselCharter = maker.cargoModelBuilder.makeVesselCharter(vessel_1, maker.entity) //
					// .withCharterRate("50000") //
					.build();

			maker.cargoModelBuilder.makeCargo() //

					// Purchase
					.makeFOBPurchase("FP1", LocalDate.of(2015, 01, 10), maker.portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, maker.entity, "5") //
					.withVolumeLimits(1_000_000, 3_000_000, VolumeUnits.MMBTU) //
					.build() //
					// Sale 1
					.makeDESSale("DS1", LocalDate.of(2015, 02, 10), maker.portFinder.findPortById(InternalDataConstants.PORT_DAHEJ), null, maker.entity, "7") //
					.withVolumeLimits(1_000_000, 1_000_000, VolumeUnits.MMBTU) //
					.build() //

					// Cargo
					.withVesselAssignment(vesselCharter, 1) //
					.build();

			// Sale 2
			maker.cargoModelBuilder.makeDESSale("DS2", LocalDate.of(2015, 03, 10), maker.portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, maker.entity, "8") //
					.withVolumeLimits(1_000_000, 1_000_000, VolumeUnits.MMBTU) //
					.withCancellationFee("1000000") //
					.build();

		}, maker -> {

			final Vessel vessel_1 = maker.fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

			final VesselCharter vesselCharter = maker.cargoModelBuilder.makeVesselCharter(vessel_1, maker.entity) //
					.build();

			maker.cargoModelBuilder.makeCargo() //
					// Purchase
					.makeFOBPurchase("FP1", LocalDate.of(2015, 01, 10), maker.portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, maker.entity, "5") //
					.withVolumeLimits(1_000_000, 3_000_000, VolumeUnits.MMBTU) //
					.build() //

					// Sale 1
					.makeDESSale("DS1", LocalDate.of(2015, 02, 10), maker.portFinder.findPortById(InternalDataConstants.PORT_DAHEJ), null, maker.entity, "7") //
					.withVolumeLimits(1_000_000, 1_000_000, VolumeUnits.MMBTU) //
					.build() //

					// Sale 2
					.makeDESSale("DS2", LocalDate.of(2015, 03, 10), maker.portFinder.findPortById(InternalDataConstants.PORT_SAKAI), null, maker.entity, "7") //
					.withVolumeLimits(1_000_000, 1_000_000, VolumeUnits.MMBTU) //
					.withCancellationFee("1000000") //
					.build() //

					// Cargo
					.withVesselAssignment(vesselCharter, 1) //
					.build();

		}, changeSetRoot -> {
			Assertions.assertEquals(1, changeSetRoot.getChangeSets().size());

			ChangeSetToTableTransformer transformer = new ChangeSetToTableTransformer();
			ChangeSetTableRoot tableRoot = transformer.createViewDataModel(changeSetRoot, false, null, SortMode.BY_GROUP);

			final ChangeSetTableGroup changeSet = tableRoot.getGroups().get(0);
			Assertions.assertEquals(2, changeSet.getRows().size());

			final ChangeSetTableRow row1 = changeSet.getRows().get(0);
			final ChangeSetTableRow row2 = changeSet.getRows().get(1);
			// Expect 1m discharge 2 cancellation 2 - before only
			Assertions.assertEquals(0, ChangeSetKPIUtil.getCargoOtherPNL(row1, ResultType.Before));
			Assertions.assertEquals(-1_000_000, ChangeSetKPIUtil.getCargoOtherPNL(row2, ResultType.Before));
			Assertions.assertEquals(0, ChangeSetKPIUtil.getCargoOtherPNL(row1, ResultType.After));
			Assertions.assertEquals(0, ChangeSetKPIUtil.getCargoOtherPNL(row2, ResultType.After));

			// P&L Sanity check -- no missing components in P&L
			// NOTE: This is different to other test cases as second D sales revenue is
			// shown on the second D row rather than the cargo row.
			double totalBeforePNL = 0.0;
			double totalBeforePNLSum = 0.0;
			double totalAfterPNL = 0.0;
			double totalAfterPNLSum = 0.0;
			for (final ChangeSetTableRow row : changeSet.getRows()) {
				// Allow $1 "rounding"
				totalBeforePNL += ChangeSetKPIUtil.getPNL(row, ResultType.Before);
				totalBeforePNLSum += ChangeSetKPIUtil.getPNLSum(row, ResultType.Before);
				totalAfterPNL += ChangeSetKPIUtil.getPNL(row, ResultType.After);
				totalAfterPNLSum += ChangeSetKPIUtil.getPNLSum(row, ResultType.After);

			}
			final double roudingDelta = 2.0;
			Assertions.assertEquals(totalBeforePNL, totalBeforePNLSum, roudingDelta);
			Assertions.assertEquals(totalAfterPNL, totalAfterPNLSum, roudingDelta);
		});
	}

	public void runTest(final Consumer<ScenarioMaker> fromBuilder, final Consumer<ScenarioMaker> toBuilder, final Consumer<ChangeSetRoot> resultChecker) throws Exception {

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
			//
			try (ModelReference fromRef = pinnedRecord.aquireReference("CompareViewTests:1")) {
				try (ModelReference toRef = otherRecord.aquireReference("CompareViewTests:2")) {

					final List<ICustomRelatedSlotHandler> customRelatedSlotHandlers = new LinkedList<>();

					final SelectedDataProviderImpl selectedDataProvider = new SelectedDataProviderImpl();
					final Function<EObject, Collection<EObject>> getChildren = (scenarioModel) -> {
						final Collection<EObject> children = new HashSet<>();
						final Iterator<EObject> itr = scenarioModel.eAllContents();
						while (itr.hasNext()) {
							children.add(itr.next());
						}
						children.add(scenarioModel);
						return children;
					};
					final ScenarioResult pinnedResult = new ScenarioResultImpl(pinnedRecord);
					final ScenarioResult otherResult = new ScenarioResultImpl(otherRecord);
					selectedDataProvider.addScenario(pinnedResult, ScenarioModelUtil.getScheduleModel(from).getSchedule(), getChildren.apply(from));
					selectedDataProvider.addScenario(otherResult, ScenarioModelUtil.getScheduleModel(to).getSchedule(), getChildren.apply(to));
					selectedDataProvider.setPinnedScenarioInstance(pinnedResult);

					final PinDiffResultPlanTransformer transformer = new PinDiffResultPlanTransformer();
					final ChangeSetRoot root = transformer.createDataModel(pinnedResult, otherResult, new NullProgressMonitor());

					resultChecker.accept(root);

				}
			}
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

	protected @NonNull ExecutorService createExecutorService() {
		return Executors.newSingleThreadExecutor();
	}

	public void evaluate(@NonNull IScenarioDataProvider scenarioDataProvider, final LNGScenarioModel lngScenarioModel) {

		// Create UserSettings
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setGenerateCharterOuts(false);
		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		final OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, lngScenarioModel);

		// Generate internal data

		LNGOptimisationRunnerBuilder runner = LNGOptimisationBuilder.begin(scenarioDataProvider) //
				.withOptimisationPlan(optimisationPlan) //
				.withExtraModule(new TransformerExtensionTestBootstrapModule()) //
				.withThreadCount(1)//
				.buildDefaultRunner();

		runner.evaluateInitialState();

	}
}
