/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.migration.MetamodelVersionsUtil;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.lng.parameters.ParametersPackage;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.migration.utils.MetamodelLoader;
import com.mmxlabs.models.migration.utils.MetamodelUtils;
import com.mmxlabs.models.mmxcore.MMXCorePackage;

public class MigrateToV4Test extends AbstractMigrationTestClass {

	static {
		// Trigger EMF initialisation outside of eclipse environment.
		@SuppressWarnings("unused")
		Object instance = null;
		instance = MMXCorePackage.eINSTANCE;
		instance = TypesPackage.eINSTANCE;
		instance = AnalyticsPackage.eINSTANCE;
		instance = CargoPackage.eINSTANCE;
		instance = CommercialPackage.eINSTANCE;
		instance = FleetPackage.eINSTANCE;
		instance = ParametersPackage.eINSTANCE;
		instance = PortPackage.eINSTANCE;
		instance = PricingPackage.eINSTANCE;
		instance = SchedulePackage.eINSTANCE;
		instance = LNGScenarioPackage.eINSTANCE;
	}

	@Test
	public void migrateDailyHireRateTest() throws IOException {

		// Construct a scenario
		File tmpFile = null;
		try {
			{
				final MetamodelLoader loader = new MigrateToV4().getSourceMetamodelLoader(null);

				final EPackage package_ScenarioModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScenarioModel);
				final EClass class_LNGScenarioModel = MetamodelUtils.getEClass(package_ScenarioModel, "LNGScenarioModel");
				final EClass class_LNGPortfolioModel = MetamodelUtils.getEClass(package_ScenarioModel, "LNGPortfolioModel");

				final EPackage package_ScheduleModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScheduleModel);
				final EClass class_ScheduleModel = MetamodelUtils.getEClass(package_ScheduleModel, "ScheduleModel");
				final EClass class_Schedule = MetamodelUtils.getEClass(package_ScheduleModel, "Schedule");
				final EClass class_Sequence = MetamodelUtils.getEClass(package_ScheduleModel, "Sequence");

				final EReference reference_LNGScenarioModel_portfolioModel = MetamodelUtils.getReference(class_LNGScenarioModel, "portfolioModel");
				final EReference reference_LNGPortfoldio_scheduleModel = MetamodelUtils.getReference(class_LNGPortfolioModel, "scheduleModel");

				final EReference reference_LNGScheduleModel_schedule = MetamodelUtils.getReference(class_ScheduleModel, "schedule");
				final EReference reference_Schedule_sequences = MetamodelUtils.getReference(class_Schedule, "sequences");
				final EAttribute attribute_Sequence_dailyHireRate = MetamodelUtils.getAttribute(class_Sequence, "dailyHireRate");

				final EFactory factory_ScenarioModel = package_ScenarioModel.getEFactoryInstance();
				final EFactory factory_ScheduleModel = package_ScheduleModel.getEFactoryInstance();

				final EObject scenarioModel = factory_ScenarioModel.create(class_LNGScenarioModel);

				final EObject portfolioModel = factory_ScenarioModel.create(class_LNGPortfolioModel);
				scenarioModel.eSet(reference_LNGScenarioModel_portfolioModel, portfolioModel);

				final EObject scheduleModel = factory_ScheduleModel.create(class_ScheduleModel);
				portfolioModel.eSet(reference_LNGPortfoldio_scheduleModel, scheduleModel);

				final EObject schedule = factory_ScheduleModel.create(class_Schedule);
				scheduleModel.eSet(reference_LNGScheduleModel_schedule, schedule);

				EObject sequence = factory_ScheduleModel.create(class_Sequence);
				sequence.eSet(attribute_Sequence_dailyHireRate, 100000);

				schedule.eSet(reference_Schedule_sequences, Collections.singletonList(sequence));

				// Save to tmp file
				tmpFile = File.createTempFile("migrationtest", ".xmi");

				final Resource r = loader.getResourceSet().createResource(URI.createFileURI(tmpFile.toString()));
				r.getContents().add(scenarioModel);
				r.save(null);
			}

			// Load v4 metamodels
			// Load tmp file under v4
			{
				final MigrateToV4 migrator = new MigrateToV4();

				final MetamodelLoader loader = migrator.getDestinationMetamodelLoader(null);

				final Resource r = loader.getResourceSet().createResource(URI.createFileURI(tmpFile.toString()));

				r.load(r.getResourceSet().getLoadOptions());
				final EObject model = r.getContents().get(0);

				// Run migration.
				migrator.migrateDailyHireRate(loader, model);

				r.save(null);
			}

			// Check output
			{

				final MetamodelLoader loader = MetamodelVersionsUtil.createV4Loader(null);
				final Resource r = loader.getResourceSet().createResource(URI.createFileURI(tmpFile.toString()));
				r.load(null);
				tmpFile.delete();
				tmpFile = null;
				final EPackage package_ScenarioModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScenarioModel);
				final EClass class_LNGScenarioModel = MetamodelUtils.getEClass(package_ScenarioModel, "LNGScenarioModel");
				final EClass class_LNGPortfolioModel = MetamodelUtils.getEClass(package_ScenarioModel, "LNGPortfolioModel");

				final EPackage package_ScheduleModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScheduleModel);
				final EClass class_ScheduleModel = MetamodelUtils.getEClass(package_ScheduleModel, "ScheduleModel");
				final EClass class_Schedule = MetamodelUtils.getEClass(package_ScheduleModel, "Schedule");
				final EClass class_Sequence = MetamodelUtils.getEClass(package_ScheduleModel, "Sequence");

				final EReference reference_LNGScenarioModel_portfolioModel = MetamodelUtils.getReference(class_LNGScenarioModel, "portfolioModel");
				final EReference reference_LNGPortfolio_scheduleModel = MetamodelUtils.getReference(class_LNGPortfolioModel, "scheduleModel");

				final EReference reference_LNGScheduleModel_schedule = MetamodelUtils.getReference(class_ScheduleModel, "schedule");
				final EReference reference_Schedule_sequences = MetamodelUtils.getReference(class_Schedule, "sequences");
				final EAttribute attribute_Sequence_dailyHireRate = MetamodelUtils.getAttribute(class_Sequence, "dailyHireRate");

				final EObject scenarioModel = r.getContents().get(0);

				Assert.assertNotNull(scenarioModel);

				final EObject portfolioModel = (EObject) scenarioModel.eGet(reference_LNGScenarioModel_portfolioModel);
				Assert.assertNotNull(portfolioModel);

				final EObject scheduleModel = (EObject) portfolioModel.eGet(reference_LNGPortfolio_scheduleModel);
				Assert.assertNotNull(scheduleModel);

				final EObject schedule = (EObject) scheduleModel.eGet(reference_LNGScheduleModel_schedule);
				Assert.assertNotNull(schedule);

				List<EObject> sequences = MetamodelUtils.getValueAsTypedList(schedule, reference_Schedule_sequences);
				Assert.assertNotNull(sequences);
				Assert.assertEquals(1, sequences.size());

				EObject sequence = sequences.get(0);
				Assert.assertNotNull(sequence);

				Assert.assertFalse(sequence.eIsSet(attribute_Sequence_dailyHireRate));
			}
		} finally {
			if (tmpFile != null) {
				tmpFile.delete();
			}
		}
	}
}