/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.time.LocalDate;
import java.util.List;

import javax.swing.text.MutableAttributeSet;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com.google.common.collect.Lists;
import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelLoader;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class MigrateToV49 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 48;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 49;
	}

	@Override
	protected void doMigrationWithHelper(final MetamodelLoader loader, final EObjectWrapper model) {

		final EPackage package_CommercialModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_CommercialModel);
		final EClass class_BaseEntityBook = MetamodelUtils.getEClass(package_CommercialModel, "BaseEntityBook");
		final EReference reference_BaseEntityBook_taxRates = MetamodelUtils.getReference(class_BaseEntityBook, "taxRates");

		final EClass class_SimpleEntityBook = MetamodelUtils.getEClass(package_CommercialModel, "SimpleEntityBook");

		final EClass class_TaxRate = MetamodelUtils.getEClass(package_CommercialModel, "TaxRate");
		final EAttribute attribute_TaxRate_date = MetamodelUtils.getAttribute(class_TaxRate, "date");
		final EAttribute attribute_TaxRate_value = MetamodelUtils.getAttribute(class_TaxRate, "value");

		final EObjectWrapper referenceModel = model.getRef("referenceModel");
		if (referenceModel == null) {
			return;
		}
		final EObjectWrapper commercialModel = referenceModel.getRef("commercialModel");

		if (commercialModel == null) {
			return;
		}

		final List<EObjectWrapper> entities = commercialModel.getRefAsList("entities");
		if (entities != null) {
			for (final EObjectWrapper entity : entities) {
				final EObject upstreamBook = package_CommercialModel.getEFactoryInstance().create(class_SimpleEntityBook);
				entity.setRef("upstreamBook", upstreamBook);

				final EObject taxRate = package_CommercialModel.getEFactoryInstance().create(class_TaxRate);

				taxRate.eSet(attribute_TaxRate_date, LocalDate.of(1970, 1, 1));
				taxRate.eSet(attribute_TaxRate_value, 0.0f);

				upstreamBook.eSet(reference_BaseEntityBook_taxRates, Lists.newArrayList(taxRate));

			}
		}
	}
}
