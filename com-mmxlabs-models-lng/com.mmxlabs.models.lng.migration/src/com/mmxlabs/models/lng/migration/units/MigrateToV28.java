/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelLoader;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class MigrateToV28 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 27;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 28;
	}

	@Override
	protected void doMigrationWithHelper(final MetamodelLoader metamodelLoader, final EObjectWrapper model) {

		final EPackage package_PricingModel = metamodelLoader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_PricingModel);
		final EClass class_DataIndex = MetamodelUtils.getEClass(package_PricingModel, "DataIndex");

		final EObjectWrapper pricingModel = model.getRef("pricingModel");
		if (pricingModel != null) {
			for (final EObjectWrapper commodityIndex : pricingModel.getRefAsList("commodityIndices")) {
				processNamedIndexContainer(class_DataIndex, commodityIndex);
			}
			for (final EObjectWrapper charterIndex : pricingModel.getRefAsList("charterIndices")) {
				processNamedIndexContainer(class_DataIndex, charterIndex);
			}
			for (final EObjectWrapper baseFuelPrice : pricingModel.getRefAsList("baseFuelPrices")) {
				processNamedIndexContainer(class_DataIndex, baseFuelPrice);
			}
		}
	}

	private void processNamedIndexContainer(final EClass class_DataIndex, final EObjectWrapper namedIndex) {
		final EObjectWrapper data = namedIndex.getRef("data");
		if (class_DataIndex.isInstance(data)) {
			// Set of dates seen in this curve
			final Set<Date> seenDates = new HashSet<>();
			// Set of duplicated points
			final Set<EObject> pointsToRemove = new HashSet<>();

			final List<EObjectWrapper> points = data.getRefAsList("points");

			for (final EObjectWrapper indexPoint : points) {
				final Calendar cal = Calendar.getInstance();
				final Date date = indexPoint.getAttrib("date");
				cal.setTime(date);
				// Date should be in UTC
				cal.setTimeZone(TimeZone.getTimeZone("UTC"));

				// Set date to start of month
				cal.set(Calendar.MILLISECOND, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.DAY_OF_MONTH, 1);

				final Date newDate = cal.getTime();
				if (seenDates.add(newDate)) {
					// Modify date
					indexPoint.setAttrib("date", newDate);
				} else {
					// Mark as duplicate
					pointsToRemove.add(indexPoint);
				}
			}
			// Remove all duplicate points
			points.removeAll(pointsToRemove);
		}
	}

}
