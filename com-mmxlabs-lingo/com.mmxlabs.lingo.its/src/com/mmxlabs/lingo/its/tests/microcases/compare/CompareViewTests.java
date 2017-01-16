/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases.compare;

import java.net.MalformedURLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
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
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.google.common.collect.Lists;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.lingo.its.tests.category.MicroTest;
import com.mmxlabs.lingo.its.tests.microcases.AbstractMicroTestCase;
import com.mmxlabs.lingo.reports.services.ScenarioComparisonServiceTransformer;
import com.mmxlabs.lingo.reports.services.SelectedDataProviderImpl;
import com.mmxlabs.lingo.reports.utils.ICustomRelatedSlotHandler;
import com.mmxlabs.lingo.reports.utils.ScheduleDiffUtils;
import com.mmxlabs.lingo.reports.views.changeset.ChangeSetKPIUtil;
import com.mmxlabs.lingo.reports.views.changeset.ChangeSetKPIUtil.ResultType;
import com.mmxlabs.lingo.reports.views.changeset.ScenarioComparisonTransformer;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRoot;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow;
import com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportFactory;
import com.mmxlabs.lingo.reports.views.schedule.model.Table;
import com.mmxlabs.models.lng.cargo.util.CargoModelBuilder;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.util.CommercialModelBuilder;
import com.mmxlabs.models.lng.commercial.util.CommercialModelFinder;
import com.mmxlabs.models.lng.fleet.util.FleetModelBuilder;
import com.mmxlabs.models.lng.fleet.util.FleetModelFinder;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.port.util.PortModelFinder;
import com.mmxlabs.models.lng.pricing.util.PricingModelBuilder;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelBuilder;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelFinder;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.spotmarkets.util.SpotMarketsModelBuilder;
import com.mmxlabs.models.lng.spotmarkets.util.SpotMarketsModelFinder;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.scenario.CSVImporter;
import com.mmxlabs.models.lng.transformer.its.tests.TransformerExtensionTestBootstrapModule;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.scenario.service.model.ModelReference;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioServiceFactory;

@RunWith(value = ShiroRunner.class)
public class CompareViewTests {
	private static List<String> requiredFeatures = Lists.newArrayList("difftools");
	private static List<String> addedFeatures = new LinkedList<>();

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

	@Test
	@Category({ MicroTest.class })
	public void testSimpleDESCargoSwap() throws Exception {
		runTest(maker -> {
			maker.cargoModelBuilder.makeCargo() //
					// Purchase
					.makeDESPurchase("DP1", false, LocalDate.of(2015, 01, 10), maker.portFinder.findPort("Sakai"), null, maker.entity, "5", null) //
					.withVolumeLimits(3_000_000, 3_000_000, VolumeUnits.MMBTU) //
					.build() //
					// Sale
					.makeDESSale("DS1", LocalDate.of(2015, 01, 10), maker.portFinder.findPort("Sakai"), null, maker.entity, "7") //
					.withVolumeLimits(3_000_000, 3_000_000, VolumeUnits.MMBTU) //

					.build() //
					// Cargo
					.build();

			maker.cargoModelBuilder.makeCargo() //
					// Purchase
					.makeDESPurchase("DP2", false, LocalDate.of(2015, 01, 10), maker.portFinder.findPort("Sakai"), null, maker.entity, "3", null) //
					.withVolumeLimits(3_000_000, 3_000_000, VolumeUnits.MMBTU) //

					.build() //
					// Sale
					.makeDESSale("DS2", LocalDate.of(2015, 01, 10), maker.portFinder.findPort("Sakai"), null, maker.entity, "8") //
					.withVolumeLimits(3_000_000, 3_000_000, VolumeUnits.MMBTU) //

					.build() //
					// Cargo
					.build();

		}, maker -> {
			maker.cargoModelBuilder.makeCargo() //
					// Purchase
					.makeDESPurchase("DP1", false, LocalDate.of(2015, 01, 10), maker.portFinder.findPort("Sakai"), null, maker.entity, "3", null) //
					.withVolumeLimits(3_000_000, 3_000_000, VolumeUnits.MMBTU) //
					.build() //
					// Sale
					.makeDESSale("DS2", LocalDate.of(2015, 01, 10), maker.portFinder.findPort("Sakai"), null, maker.entity, "7") //
					.withVolumeLimits(3_000_000, 3_000_000, VolumeUnits.MMBTU) //
					.build() //
					// Cargo
					.build();

			maker.cargoModelBuilder.makeCargo() //
					// Purchase
					.makeDESPurchase("DP2", false, LocalDate.of(2015, 01, 10), maker.portFinder.findPort("Sakai"), null, maker.entity, "5", null) //
					.withVolumeLimits(3_000_000, 3_000_000, VolumeUnits.MMBTU) //
					.build() //
					// Sale
					.makeDESSale("DS1", LocalDate.of(2015, 01, 10), maker.portFinder.findPort("Sakai"), null, maker.entity, "8") //
					.withVolumeLimits(3_000_000, 3_000_000, VolumeUnits.MMBTU) //
					.build() //
					// Cargo
					.build();

		}, changeSetRoot -> {
			Assert.assertEquals(1, changeSetRoot.getChangeSets().size());

			final ChangeSet changeSet = changeSetRoot.getChangeSets().get(0);
			Assert.assertEquals(2, changeSet.getChangeSetRowsToPrevious().size());

			final ChangeSetRow row1 = changeSet.getChangeSetRowsToPrevious().get(0);
			final ChangeSetRow row2 = changeSet.getChangeSetRowsToPrevious().get(1);

			// Expect 7-5 * 3m
			Assert.assertEquals(3_000_000 * (7 - 5), ChangeSetKPIUtil.getPNL(row1, ResultType.ORIGINAL));
			// Expect 8-3 * 3m
			Assert.assertEquals(3_000_000 * (8 - 3), ChangeSetKPIUtil.getPNL(row2, ResultType.ORIGINAL));

			// Expect 7-3 * 3m
			Assert.assertEquals(3_000_000 * (7 - 3), ChangeSetKPIUtil.getPNL(row1, ResultType.NEW));
			// Expect 8-5 * 3m
			Assert.assertEquals(3_000_000 * (8 - 5), ChangeSetKPIUtil.getPNL(row2, ResultType.NEW));

			// P&L Sanity check -- no missing components in P&L
			for (final ChangeSetRow row : changeSet.getChangeSetRowsToPrevious()) {
				Assert.assertEquals(ChangeSetKPIUtil.getPNL(row, ResultType.ORIGINAL), ChangeSetKPIUtil.getPNLSum(row, ResultType.ORIGINAL));
				Assert.assertEquals(ChangeSetKPIUtil.getPNL(row, ResultType.NEW), ChangeSetKPIUtil.getPNLSum(row, ResultType.NEW));
			}
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testSlotCancellationFee_CargoToOpen() throws Exception {
		runTest(maker -> {
			maker.cargoModelBuilder.makeCargo() //
					// Purchase
					.makeDESPurchase("DP1", false, LocalDate.of(2015, 01, 10), maker.portFinder.findPort("Sakai"), null, maker.entity, "5", null) //
					.withOptional(true) //
					.withCancellationFee("50000") //
					.build() //
					// Sale
					.makeDESSale("DS", LocalDate.of(2015, 01, 10), maker.portFinder.findPort("Sakai"), null, maker.entity, "5") //
					.withOptional(true) //
					.withCancellationFee("60000") //
					.build() //
					// Cargo
					.build();

		}, maker -> {
			maker.cargoModelBuilder.makeDESPurchase("DP1", false, LocalDate.of(2015, 01, 10), maker.portFinder.findPort("Sakai"), null, maker.entity, "5", 22.8, null) //
					.withOptional(true) //
					.withCancellationFee("50000") //
					.build();

			maker.cargoModelBuilder.makeDESSale("DS", LocalDate.of(2015, 01, 10), maker.portFinder.findPort("Sakai"), null, maker.entity, "5") //
					.withOptional(true) //
					.withCancellationFee("60000") //
					.build();

		}, changeSetRoot -> {
			Assert.assertEquals(1, changeSetRoot.getChangeSets().size());

			final ChangeSet changeSet = changeSetRoot.getChangeSets().get(0);
			Assert.assertEquals(2, changeSet.getChangeSetRowsToPrevious().size());

			final ChangeSetRow row1 = changeSet.getChangeSetRowsToPrevious().get(0);
			Assert.assertNotNull(row1.getOriginalDischargeAllocation());
			Assert.assertNotNull(row1.getOriginalLoadAllocation());
			Assert.assertNotNull(row1.getNewOpenLoadAllocation());

			final ChangeSetRow row2 = changeSet.getChangeSetRowsToPrevious().get(1);
			// Assert.assertNotNull(row2.getOriginalDischargeAllocation());

			// Expect 50K for load cancellation
			Assert.assertEquals(-50_000, ChangeSetKPIUtil.getPNL(row1, ResultType.NEW));
			Assert.assertEquals(-50_000, ChangeSetKPIUtil.getCargoOtherPNL(row1, ResultType.NEW));
			// Expect 60K for discharge cancellation
			Assert.assertEquals(-60_000, ChangeSetKPIUtil.getPNL(row2, ResultType.NEW));
			Assert.assertEquals(-60_000, ChangeSetKPIUtil.getCargoOtherPNL(row2, ResultType.NEW));

			// Original cargo is a zero sum P&L
			Assert.assertEquals(0, ChangeSetKPIUtil.getPNL(row1, ResultType.ORIGINAL));
			Assert.assertEquals(0, ChangeSetKPIUtil.getCargoOtherPNL(row1, ResultType.ORIGINAL));
			Assert.assertEquals(0, ChangeSetKPIUtil.getPNL(row2, ResultType.ORIGINAL));
			Assert.assertEquals(0, ChangeSetKPIUtil.getCargoOtherPNL(row2, ResultType.ORIGINAL));

			// P&L Sanity check -- no missing components in P&L
			for (final ChangeSetRow row : changeSet.getChangeSetRowsToPrevious()) {
				Assert.assertEquals(ChangeSetKPIUtil.getPNL(row, ResultType.ORIGINAL), ChangeSetKPIUtil.getPNLSum(row, ResultType.ORIGINAL));
				Assert.assertEquals(ChangeSetKPIUtil.getPNL(row, ResultType.NEW), ChangeSetKPIUtil.getPNLSum(row, ResultType.NEW));
			}
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testSlotCancellationFee_OpenToCargo() throws Exception {
		runTest(maker -> {
			maker.cargoModelBuilder.makeDESPurchase("DP1", false, LocalDate.of(2015, 01, 10), maker.portFinder.findPort("Sakai"), null, maker.entity, "5", 22.8, null) //
					.withOptional(true) //
					.withCancellationFee("50000") //
					.build();

			maker.cargoModelBuilder.makeDESSale("DS", LocalDate.of(2015, 01, 10), maker.portFinder.findPort("Sakai"), null, maker.entity, "5") //
					.withOptional(true) //
					.withCancellationFee("60000") //
					.build();

		}, maker -> {
			maker.cargoModelBuilder.makeCargo() //
					// Purchase
					.makeDESPurchase("DP1", false, LocalDate.of(2015, 01, 10), maker.portFinder.findPort("Sakai"), null, maker.entity, "5", null) //
					.withOptional(true) //
					.withCancellationFee("50000") //
					.build() //
					// Sale
					.makeDESSale("DS", LocalDate.of(2015, 01, 10), maker.portFinder.findPort("Sakai"), null, maker.entity, "5") //
					.withOptional(true) //
					.withCancellationFee("60000") //
					.build() //
					// Cargo
					.build();

		}, changeSetRoot -> {
			Assert.assertEquals(1, changeSetRoot.getChangeSets().size());

			final ChangeSet changeSet = changeSetRoot.getChangeSets().get(0);
			Assert.assertEquals(1, changeSet.getChangeSetRowsToPrevious().size());

			final ChangeSetRow row1 = changeSet.getChangeSetRowsToPrevious().get(0);
			assert row1 != null;
			// Expect 50K for load cancellation and 60K for discharge cancellation
			Assert.assertEquals(-50_000 + -60_000, ChangeSetKPIUtil.getPNL(row1, ResultType.ORIGINAL));
			Assert.assertEquals(-50_000 + -60_000, ChangeSetKPIUtil.getCargoOtherPNL(row1, ResultType.ORIGINAL));

			// New cargo is a zero sum P&L
			Assert.assertEquals(0, ChangeSetKPIUtil.getPNL(row1, ResultType.NEW));
			Assert.assertEquals(0, ChangeSetKPIUtil.getCargoOtherPNL(row1, ResultType.NEW));

			// P&L Sanity check -- no missing components in P&L
			for (final ChangeSetRow row : changeSet.getChangeSetRowsToPrevious()) {
				Assert.assertEquals(ChangeSetKPIUtil.getPNL(row, ResultType.ORIGINAL), ChangeSetKPIUtil.getPNLSum(row, ResultType.ORIGINAL));
				Assert.assertEquals(ChangeSetKPIUtil.getPNL(row, ResultType.NEW), ChangeSetKPIUtil.getPNLSum(row, ResultType.NEW));
			}
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testSlotHedingValue_CargoToOpen() throws Exception {
		runTest(maker -> {
			maker.cargoModelBuilder.makeCargo() //
					// Purchase
					.makeDESPurchase("DP1", false, LocalDate.of(2015, 01, 10), maker.portFinder.findPort("Sakai"), null, maker.entity, "5", null) //
					.withOptional(true) //
					.withHedgeValue(50000) //
					.build() //
					// Sale
					.makeDESSale("DS", LocalDate.of(2015, 01, 10), maker.portFinder.findPort("Sakai"), null, maker.entity, "5") //
					.withOptional(true) //
					.withHedgeValue(60000) //
					.build() //
					// Cargo
					.build();

		}, maker -> {
			maker.cargoModelBuilder.makeDESPurchase("DP1", false, LocalDate.of(2015, 01, 10), maker.portFinder.findPort("Sakai"), null, maker.entity, "5", 22.8, null) //
					.withOptional(true) //
					.withHedgeValue(50000) //
					.build();

			maker.cargoModelBuilder.makeDESSale("DS", LocalDate.of(2015, 01, 10), maker.portFinder.findPort("Sakai"), null, maker.entity, "5") //
					.withOptional(true) //
					.withHedgeValue(60000) //
					.build();

		}, changeSetRoot -> {
			Assert.assertEquals(1, changeSetRoot.getChangeSets().size());

			final ChangeSet changeSet = changeSetRoot.getChangeSets().get(0);
			Assert.assertEquals(2, changeSet.getChangeSetRowsToPrevious().size());

			final ChangeSetRow row1 = changeSet.getChangeSetRowsToPrevious().get(0);
			final ChangeSetRow row2 = changeSet.getChangeSetRowsToPrevious().get(1);

			// Expect 50K for load hedge
			Assert.assertEquals(50_000, ChangeSetKPIUtil.getPNL(row1, ResultType.NEW));
			// Assert.assertEquals(-50_000, ChangeSetKPIUtil.getCargoOtherPNL(row1, ResultType.NEW));
			// Expect 60K for discharge hedge
			Assert.assertEquals(60_000, ChangeSetKPIUtil.getPNL(row2, ResultType.NEW));
			// Assert.assertEquals(-60_000, ChangeSetKPIUtil.getCargoOtherPNL(row2, ResultType.NEW));

			// Original cargo is a zero sum P&L
			Assert.assertEquals(50_000 + 60_000, ChangeSetKPIUtil.getPNL(row1, ResultType.ORIGINAL));
			// Assert.assertEquals(0, ChangeSetKPIUtil.getCargoOtherPNL(row1, ResultType.ORIGINAL));
			Assert.assertEquals(0, ChangeSetKPIUtil.getPNL(row2, ResultType.ORIGINAL));
			// Assert.assertEquals(0, ChangeSetKPIUtil.getCargoOtherPNL(row1, ResultType.ORIGINAL));

			// P&L Sanity check -- no missing components in P&L
			for (final ChangeSetRow row : changeSet.getChangeSetRowsToPrevious()) {
				Assert.assertEquals(ChangeSetKPIUtil.getPNL(row, ResultType.ORIGINAL), ChangeSetKPIUtil.getPNLSum(row, ResultType.ORIGINAL));
				Assert.assertEquals(ChangeSetKPIUtil.getPNL(row, ResultType.NEW), ChangeSetKPIUtil.getPNLSum(row, ResultType.NEW));
			}
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testSlotHedgingValue_OpenToCargo() throws Exception {
		runTest(maker -> {
			maker.cargoModelBuilder.makeDESPurchase("DP1", false, LocalDate.of(2015, 01, 10), maker.portFinder.findPort("Sakai"), null, maker.entity, "5", 22.8, null) //
					.withOptional(true) //
					.withHedgeValue(50000) //
					.build();

			maker.cargoModelBuilder.makeDESSale("DS", LocalDate.of(2015, 01, 10), maker.portFinder.findPort("Sakai"), null, maker.entity, "5") //
					.withOptional(true) //
					.withHedgeValue(60000) //
					.build();

		}, maker -> {
			maker.cargoModelBuilder.makeCargo() //
					// Purchase
					.makeDESPurchase("DP1", false, LocalDate.of(2015, 01, 10), maker.portFinder.findPort("Sakai"), null, maker.entity, "5", null) //
					.withOptional(true) //
					.withHedgeValue(50000) //
					.build() //
					// Sale
					.makeDESSale("DS", LocalDate.of(2015, 01, 10), maker.portFinder.findPort("Sakai"), null, maker.entity, "5") //
					.withOptional(true) //
					.withHedgeValue(60000) //
					.build() //
					// Cargo
					.build();

		}, changeSetRoot -> {
			Assert.assertEquals(1, changeSetRoot.getChangeSets().size());

			final ChangeSet changeSet = changeSetRoot.getChangeSets().get(0);
			Assert.assertEquals(1, changeSet.getChangeSetRowsToPrevious().size());

			final ChangeSetRow row1 = changeSet.getChangeSetRowsToPrevious().get(0);
			assert row1 != null;
			// Expect 50K for load cancellation and 60K for discharge cancellation
			Assert.assertEquals(50_000 + 60_000, ChangeSetKPIUtil.getPNL(row1, ResultType.ORIGINAL));
			// Assert.assertEquals(-50_000 + -60_000, ChangeSetKPIUtil.getCargoOtherPNL(row1, ResultType.ORIGINAL));

			Assert.assertEquals(50_000 + 60_000, ChangeSetKPIUtil.getPNL(row1, ResultType.NEW));
			// Assert.assertEquals(0, ChangeSetKPIUtil.getCargoOtherPNL(row1, ResultType.NEW));
			// Assert.assertEquals(50_000 + 60_000, ChangeSetKPIUtil.getPNL(row1, ResultType.NEW));
			// Assert.assertEquals(0, ChangeSetKPIUtil.getCargoOtherPNL(row1, ResultType.NEW));

			// P&L Sanity check -- no missing components in P&L
			for (final ChangeSetRow row : changeSet.getChangeSetRowsToPrevious()) {
				Assert.assertEquals(ChangeSetKPIUtil.getPNL(row, ResultType.ORIGINAL), ChangeSetKPIUtil.getPNLSum(row, ResultType.ORIGINAL));
				Assert.assertEquals(ChangeSetKPIUtil.getPNL(row, ResultType.NEW), ChangeSetKPIUtil.getPNLSum(row, ResultType.NEW));
			}
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testSlotMiscCosts_CargoToOpen() throws Exception {
		runTest(maker -> {
			maker.cargoModelBuilder.makeCargo() //
					// Purchase
					.makeDESPurchase("DP1", false, LocalDate.of(2015, 01, 10), maker.portFinder.findPort("Sakai"), null, maker.entity, "5", null) //
					.withOptional(true) //
					.withMiscCosts(50000) //
					.build() //
					// Sale
					.makeDESSale("DS", LocalDate.of(2015, 01, 10), maker.portFinder.findPort("Sakai"), null, maker.entity, "5") //
					.withOptional(true) //
					.withMiscCosts(60000) //
					.build() //
					// Cargo
					.build();

		}, maker -> {
			maker.cargoModelBuilder.makeDESPurchase("DP1", false, LocalDate.of(2015, 01, 10), maker.portFinder.findPort("Sakai"), null, maker.entity, "5", 22.8, null) //
					.withOptional(true) //
					.withMiscCosts(50000) //
					.build();

			maker.cargoModelBuilder.makeDESSale("DS", LocalDate.of(2015, 01, 10), maker.portFinder.findPort("Sakai"), null, maker.entity, "5") //
					.withOptional(true) //
					.withMiscCosts(60000) //
					.build();

		}, changeSetRoot -> {
			Assert.assertEquals(1, changeSetRoot.getChangeSets().size());

			final ChangeSet changeSet = changeSetRoot.getChangeSets().get(0);
			Assert.assertEquals(2, changeSet.getChangeSetRowsToPrevious().size());

			final ChangeSetRow row1 = changeSet.getChangeSetRowsToPrevious().get(0);
			Assert.assertNotNull(row1.getOriginalDischargeAllocation());
			Assert.assertNotNull(row1.getOriginalLoadAllocation());
			Assert.assertNotNull(row1.getNewOpenLoadAllocation());

			final ChangeSetRow row2 = changeSet.getChangeSetRowsToPrevious().get(1);
			// Assert.assertNotNull(row2.getOriginalDischargeAllocation());

			// Expect 50K for load costs and 60K for discharge costs
			Assert.assertEquals(-50_000 + -60_000, ChangeSetKPIUtil.getPNL(row1, ResultType.ORIGINAL));
			Assert.assertEquals(-50_000 + -60_000, ChangeSetKPIUtil.getCargoOtherPNL(row1, ResultType.ORIGINAL));
			// No costs on row two as part of row1 original P&L
			Assert.assertEquals(0, ChangeSetKPIUtil.getPNL(row2, ResultType.ORIGINAL));
			Assert.assertEquals(0, ChangeSetKPIUtil.getCargoOtherPNL(row2, ResultType.ORIGINAL));

			// Original cargo is a zero sum P&L
			Assert.assertEquals(0, ChangeSetKPIUtil.getPNL(row1, ResultType.NEW));
			Assert.assertEquals(0, ChangeSetKPIUtil.getCargoOtherPNL(row1, ResultType.NEW));
			Assert.assertEquals(0, ChangeSetKPIUtil.getPNL(row2, ResultType.NEW));
			Assert.assertEquals(0, ChangeSetKPIUtil.getCargoOtherPNL(row2, ResultType.NEW));

			// P&L Sanity check -- no missing components in P&L
			for (final ChangeSetRow row : changeSet.getChangeSetRowsToPrevious()) {
				Assert.assertEquals(ChangeSetKPIUtil.getPNL(row, ResultType.ORIGINAL), ChangeSetKPIUtil.getPNLSum(row, ResultType.ORIGINAL));
				Assert.assertEquals(ChangeSetKPIUtil.getPNL(row, ResultType.NEW), ChangeSetKPIUtil.getPNLSum(row, ResultType.NEW));
			}
		});
	}

	@Test
	@Category({ MicroTest.class })
	public void testSlotMiscCosts_OpenToCargo() throws Exception {
		runTest(maker -> {
			maker.cargoModelBuilder.makeDESPurchase("DP1", false, LocalDate.of(2015, 01, 10), maker.portFinder.findPort("Sakai"), null, maker.entity, "5", 22.8, null) //
					.withOptional(true) //
					.withMiscCosts(50000) //
					.build();

			maker.cargoModelBuilder.makeDESSale("DS", LocalDate.of(2015, 01, 10), maker.portFinder.findPort("Sakai"), null, maker.entity, "5") //
					.withOptional(true) //
					.withMiscCosts(60000) //
					.build();

		}, maker -> {
			maker.cargoModelBuilder.makeCargo() //
					// Purchase
					.makeDESPurchase("DP1", false, LocalDate.of(2015, 01, 10), maker.portFinder.findPort("Sakai"), null, maker.entity, "5", null) //
					.withOptional(true) //
					.withMiscCosts(50000) //
					.build() //
					// Sale
					.makeDESSale("DS", LocalDate.of(2015, 01, 10), maker.portFinder.findPort("Sakai"), null, maker.entity, "5") //
					.withOptional(true) //
					.withMiscCosts(60000) //
					.build() //
					// Cargo
					.build();

		}, changeSetRoot -> {
			Assert.assertEquals(1, changeSetRoot.getChangeSets().size());

			final ChangeSet changeSet = changeSetRoot.getChangeSets().get(0);
			Assert.assertEquals(1, changeSet.getChangeSetRowsToPrevious().size());

			final ChangeSetRow row1 = changeSet.getChangeSetRowsToPrevious().get(0);
			assert row1 != null;
			// Expect 50K for load cost and 60K for discharge costs
			Assert.assertEquals(-50_000 + -60_000, ChangeSetKPIUtil.getPNL(row1, ResultType.NEW));
			Assert.assertEquals(-50_000 + -60_000, ChangeSetKPIUtil.getCargoOtherPNL(row1, ResultType.NEW));

			// original slot value is zero sum P&L
			Assert.assertEquals(0, ChangeSetKPIUtil.getPNL(row1, ResultType.ORIGINAL));
			Assert.assertEquals(0, ChangeSetKPIUtil.getCargoOtherPNL(row1, ResultType.ORIGINAL));

			// P&L Sanity check -- no missing components in P&L
			for (final ChangeSetRow row : changeSet.getChangeSetRowsToPrevious()) {
				Assert.assertEquals(ChangeSetKPIUtil.getPNL(row, ResultType.ORIGINAL), ChangeSetKPIUtil.getPNLSum(row, ResultType.ORIGINAL));
				Assert.assertEquals(ChangeSetKPIUtil.getPNL(row, ResultType.NEW), ChangeSetKPIUtil.getPNLSum(row, ResultType.NEW));
			}
		});
	}

	public void runTest(final Consumer<ScenarioMaker> fromBuilder, final Consumer<ScenarioMaker> toBuilder, final Consumer<ChangeSetRoot> resultChecker) throws Exception {

		LNGScenarioModel from;
		{
			final ScenarioMaker maker = new ScenarioMaker();
			maker.constructor();
			maker.buildIt(fromBuilder);
			from = maker.lngScenarioModel;
		}
		evaluate(from);
		LNGScenarioModel to;
		{
			final ScenarioMaker maker = new ScenarioMaker();
			maker.constructor();
			maker.buildIt(toBuilder);
			to = maker.lngScenarioModel;
		}
		evaluate(to);

		final ScenarioInstance pinned = makeScenarioInstance("from", from);
		final ScenarioInstance other = makeScenarioInstance("to", to);
		//
		try (ModelReference fromRef = pinned.getReference("CompareViewTests:1")) {
			try (ModelReference toRef = other.getReference("CompareViewTests:2")) {

				final List<ICustomRelatedSlotHandler> customRelatedSlotHandlers = new LinkedList<>();
				// if (customRelatedSlotHandlerExtensions != null) {
				// for (final ICustomRelatedSlotHandlerExtension h : customRelatedSlotHandlerExtensions) {
				// customRelatedSlotHandlers.add(h.getInstance());
				// }
				// }

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
				selectedDataProvider.addScenario(pinned, from, ScenarioModelUtil.getScheduleModel(from).getSchedule(), getChildren.apply(from));
				selectedDataProvider.addScenario(other, to, ScenarioModelUtil.getScheduleModel(to).getSchedule(), getChildren.apply(to));
				selectedDataProvider.setPinnedScenarioInstance(pinned);

				final ScheduleDiffUtils scheduleDiffUtils = new ScheduleDiffUtils();
				final Collection<ScenarioInstance> others = Collections.singleton(other);
				final ScenarioComparisonServiceTransformer.TransformResult result = ScenarioComparisonServiceTransformer.transform(pinned, others, selectedDataProvider, scheduleDiffUtils,
						customRelatedSlotHandlers);
				final Table table = result.table;
				table.setOptions(ScheduleReportFactory.eINSTANCE.createDiffOptions());

				// Take a copy of current diff options
				// table.setOptions(EcoreUtil.copy(diffOptions));
				// selectedDataProvider, pinned, others.iterator().next(), table, result.rootObjects, result.equivalancesMap);
				final ScenarioComparisonTransformer transformer = new ScenarioComparisonTransformer();
				final ChangeSetRoot root = transformer.createDataModel(selectedDataProvider, result.equivalancesMap, table, pinned, other, new NullProgressMonitor());

				resultChecker.accept(root);

			}
		}

	}

	private ScenarioInstance makeScenarioInstance(final String name, final LNGScenarioModel from) {

		final ScenarioInstance instance = ScenarioServiceFactory.eINSTANCE.createScenarioInstance();
		instance.setName(name);
		instance.setInstance(from);

		return instance;
	}

	static class ScenarioMaker {

		protected LNGScenarioModel lngScenarioModel;
		protected ScenarioModelFinder scenarioModelFinder;
		protected ScenarioModelBuilder scenarioModelBuilder;
		protected CommercialModelFinder commercialModelFinder;
		protected FleetModelFinder fleetModelFinder;
		protected PortModelFinder portFinder;
		protected CargoModelBuilder cargoModelBuilder;
		protected CommercialModelBuilder commercialModelBuilder;
		protected FleetModelBuilder fleetModelBuilder;
		protected SpotMarketsModelBuilder spotMarketsModelBuilder;
		protected SpotMarketsModelFinder spotMarketsModelFinder;
		protected PricingModelBuilder pricingModelBuilder;
		protected BaseLegalEntity entity;

		@NonNull
		public LNGScenarioModel importReferenceData() throws MalformedURLException {
			return importReferenceData("/referencedata/reference-data-1/");
		}

		@NonNull
		public LNGScenarioModel importReferenceData(final String url) throws MalformedURLException {

			final @NonNull String urlRoot = AbstractMicroTestCase.class.getResource(url).toString();
			final CSVImporter importer = new CSVImporter();
			importer.importPortData(urlRoot);
			importer.importCostData(urlRoot);
			importer.importEntityData(urlRoot);
			importer.importFleetData(urlRoot);
			importer.importMarketData(urlRoot);
			importer.importPromptData(urlRoot);
			importer.importMarketData(urlRoot);

			return importer.doImport();
		}

		@Before
		public void constructor() throws MalformedURLException {

			lngScenarioModel = importReferenceData();

			scenarioModelFinder = new ScenarioModelFinder(lngScenarioModel);
			scenarioModelBuilder = new ScenarioModelBuilder(lngScenarioModel);

			commercialModelFinder = scenarioModelFinder.getCommercialModelFinder();
			fleetModelFinder = scenarioModelFinder.getFleetModelFinder();
			portFinder = scenarioModelFinder.getPortModelFinder();
			spotMarketsModelFinder = scenarioModelFinder.getSpotMarketsModelFinder();

			pricingModelBuilder = scenarioModelBuilder.getPricingModelBuilder();
			commercialModelBuilder = scenarioModelBuilder.getCommercialModelBuilder();
			cargoModelBuilder = scenarioModelBuilder.getCargoModelBuilder();
			fleetModelBuilder = scenarioModelBuilder.getFleetModelBuilder();
			spotMarketsModelBuilder = scenarioModelBuilder.getSpotMarketsModelBuilder();

			entity = importDefaultEntity();

		}

		protected BaseLegalEntity importDefaultEntity() {
			return commercialModelFinder.findEntity("Shipping");
		}

		@After
		public void destructor() {
			lngScenarioModel = null;
			scenarioModelFinder = null;
			scenarioModelBuilder = null;
			commercialModelFinder = null;
			fleetModelFinder = null;
			portFinder = null;
			commercialModelBuilder = null;
			cargoModelBuilder = null;
			fleetModelBuilder = null;
			spotMarketsModelBuilder = null;
			pricingModelBuilder = null;
			entity = null;
		}

		void buildIt(final Consumer<ScenarioMaker> r) {
			r.accept(this);
		}
	}

	protected @NonNull ExecutorService createExecutorService() {
		return Executors.newSingleThreadExecutor();
	}

	public void evaluate(final LNGScenarioModel lngScenarioModel) {

		// Create UserSettings
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();
		userSettings.setBuildActionSets(false);
		userSettings.setGenerateCharterOuts(false);
		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);

		final OptimisationPlan optimisationPlan = OptimisationHelper.transformUserSettings(userSettings, null, lngScenarioModel);

		// Generate internal data
		final ExecutorService executorService = createExecutorService();
		try {
			final LNGScenarioRunner scenarioRunner = new LNGScenarioRunner(executorService, lngScenarioModel, optimisationPlan, new TransformerExtensionTestBootstrapModule(), null, true);
			scenarioRunner.evaluateInitialState();
		} finally {
			executorService.shutdownNow();
		}
	}
}
