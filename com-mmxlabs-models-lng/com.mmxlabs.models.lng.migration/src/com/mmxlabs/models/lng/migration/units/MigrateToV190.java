/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.List;
import java.util.function.Consumer;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.MigrationModelRecord;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class MigrateToV190 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 189;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 190;
	}

	@Override
	protected void doMigration(@NonNull final MigrationModelRecord modelRecord) {

		EObjectWrapper scenarioModel = modelRecord.getModelRoot();
		EObjectWrapper referenceModel = scenarioModel.getRef("referenceModel");
		EObjectWrapper cargoModel = scenarioModel.getRef("cargoModel");
		
		final List<EObjectWrapper> paperDeals = cargoModel.getRefAsList("paperDeals");
		paperDeals.forEach( pd -> {
			pd.unsetFeature("startDate");
			pd.unsetFeature("endDate");
		});
		
		final EPackage lngTypesPackage = modelRecord.getMetamodelLoader().getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_LNGTypes);
		final EEnum pricingPeriodENum = MetamodelUtils.getEEnum(lngTypesPackage, "PricingPeriod");
		final EEnumLiteral monthsLiteral = MetamodelUtils.getEEnum_Literal(pricingPeriodENum, "MONTHS");
		
		EObjectWrapper pricingModel = referenceModel.getRef("pricingModel");
		final EPackage pricingPackage = modelRecord.getMetamodelLoader().getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_PricingModel);
		final EClass eClassInstrumentPeriod = MetamodelUtils.getEClass(pricingPackage, "InstrumentPeriod");
		final EFactory pricingFactory = pricingPackage.getEFactoryInstance();
		
		final List<EObjectWrapper> settleStrategies = pricingModel.getRefAsList("settleStrategies");
		settleStrategies.forEach( ss -> {
			ss.unsetFeature("settlePeriod");
			ss.unsetFeature("settlePeriodUnit");
			ss.unsetFeature("settleStartMonthsPrior");
			
			final EObjectWrapper pricingPeriod = (EObjectWrapper) pricingFactory.create(eClassInstrumentPeriod);
			pricingPeriod.setAttrib("periodSize", 1);
			pricingPeriod.setAttrib("periodSizeUnit", monthsLiteral);
			pricingPeriod.setAttrib("periodOffset", 1);
			final EObjectWrapper hedgingPeriod = (EObjectWrapper) pricingFactory.create(eClassInstrumentPeriod);
			hedgingPeriod.setAttrib("periodSize", 1);
			hedgingPeriod.setAttrib("periodSizeUnit", monthsLiteral);
			hedgingPeriod.setAttrib("periodOffset", 0);
			
			ss.setRef("pricingPeriod", pricingPeriod);
			ss.setRef("hedgingPeriod", hedgingPeriod);
		});
	}
}
