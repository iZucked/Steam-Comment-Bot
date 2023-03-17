/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.MigrationModelRecord;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

@NonNullByDefault
public class MigrateToV177 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 176;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 177;
	}

	/*
	 * Migration summary: //
	 * (1) Delete results - these are stale and the value matrix will have to be rerun //
	 * (2) Move value matrix parameters to parameters object //
	 * (3) Range parameters are moved to range object within parameters //
	 */
	@Override
	protected void doMigration(final MigrationModelRecord modelRecord) {
		// Value matrix data model refactor
		final EPackage analyticsPackage = modelRecord.getMetamodelLoader().getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_AnalyticsModel);

		final EFactory analyticsFactory = analyticsPackage.getEFactoryInstance();

		final EClass swapValueMatrixParametersClass = MetamodelUtils.getEClass(analyticsPackage, "SwapValueMatrixParameters");
		final EClass buyReferenceClass = MetamodelUtils.getEClass(analyticsPackage, "BuyReference");
		final EClass sellReferenceClass = MetamodelUtils.getEClass(analyticsPackage, "SellReference");
		final EClass existingVesselCharterOptionClass = MetamodelUtils.getEClass(analyticsPackage, "ExistingVesselCharterOption");
		final EClass buyMarketClass = MetamodelUtils.getEClass(analyticsPackage, "BuyMarket");
		final EClass sellMarketClass = MetamodelUtils.getEClass(analyticsPackage, "SellMarket");
		final EClass rangeClass = MetamodelUtils.getEClass(analyticsPackage, "Range");

		final Function<EClass, Supplier<EObjectWrapper>> createClassSupplier = clazz -> () -> (EObjectWrapper) analyticsFactory.create(clazz);
		final Supplier<EObjectWrapper> rangeFactory = () -> (EObjectWrapper) analyticsFactory.create(rangeClass);

		final EObjectWrapper scenarioModel = modelRecord.getModelRoot();
		final EObjectWrapper analyticsModel = scenarioModel.getRef("analyticsModel");
		@Nullable
		final List<@Nullable EObjectWrapper> valueMatrixModels = analyticsModel.getRefAsList("swapValueMatrixModels");

		if (valueMatrixModels != null) {
			for (final EObjectWrapper valueMatrixModel : valueMatrixModels) {
				// Deleting any existing results because they are stale
				if (valueMatrixModel.isSetFeature("swapValueMatrixResult")) {
					valueMatrixModel.unsetFeature("swapValueMatrixResult");
				}
				final EObjectWrapper parameters = (EObjectWrapper) analyticsFactory.create(swapValueMatrixParametersClass);

				setParametersValue(valueMatrixModel, "baseLoad", createClassSupplier.apply(buyReferenceClass), parameters);
				setParametersValue(valueMatrixModel, "baseDischarge", createClassSupplier.apply(sellReferenceClass), parameters);
				setParametersValue(valueMatrixModel, "baseVesselCharter", createClassSupplier.apply(existingVesselCharterOptionClass), parameters);
				setParametersValue(valueMatrixModel, "swapLoadMarket", createClassSupplier.apply(buyMarketClass), parameters);
				setParametersValue(valueMatrixModel, "swapDischargeMarket", createClassSupplier.apply(sellMarketClass), parameters);

				setAttribute(valueMatrixModel, "swapFee", "swapFee", parameters);

				setRangeValues(valueMatrixModel, "baseDischargeMinPrice", "baseDischargeMaxPrice", "baseDischargeStepSize", rangeFactory, "basePriceRange", parameters);
				setRangeValues(valueMatrixModel, "marketMinPrice", "marketMaxPrice", "marketStepSize", rangeFactory, "swapPriceRange", parameters);

				valueMatrixModel.setRef("parameters", parameters);
			}
		}
	}

	private void setRangeValues(final EObjectWrapper model, final String oldMinAttrName, final String oldMaxAttrName, final String oldStepSizeAttrName, final Supplier<EObjectWrapper> rangeFactory,
			final String newRefName, final EObjectWrapper parameters) {
		final EObjectWrapper range = rangeFactory.get();
		setAttribute(model, oldMinAttrName, "min", range);
		setAttribute(model, oldMaxAttrName, "max", range);
		setAttribute(model, oldStepSizeAttrName, "stepSize", range);
		parameters.setRef(newRefName, range);
	}

	private void setAttribute(final EObjectWrapper model, final String oldAttrName, final String newAttrName, final EObjectWrapper receiver) {
		if (model.isSetFeature(oldAttrName)) {
			receiver.setAttrib(newAttrName, model.getAttrib(oldAttrName));
			model.unsetFeature(oldAttrName);
		}
	}

	private void setParametersValue(final EObjectWrapper model, final String refName, final Supplier<EObjectWrapper> classFactory, final EObjectWrapper parameters) {
		final EObjectWrapper reference;
		if (model.isSetFeature(refName)) {
			final EObjectWrapper oldReference = model.getRef(refName);
			model.unsetFeature(refName);
			if (oldReference != null) {
				reference = oldReference;
			} else {
				reference = classFactory.get();
			}
		} else {
			reference = classFactory.get();
		}
		parameters.setRef(refName, reference);

	}
}