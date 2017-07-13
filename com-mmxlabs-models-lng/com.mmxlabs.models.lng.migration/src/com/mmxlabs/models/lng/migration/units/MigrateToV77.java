/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.time.YearMonth;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelLoader;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class MigrateToV77 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 76;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 77;
	}

	@Override
	protected void doMigrationWithHelper(final MetamodelLoader loader, final EObjectWrapper model) {

		final EPackage package_ParametersModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ParametersModel);
		final EClass class_UserSettings = MetamodelUtils.getEClass(package_ParametersModel, "UserSettings");

		final TreeIterator<EObject> itr = model.eAllContents();
		while (itr.hasNext()) {
			final EObject itr_obj = itr.next();
			if (class_UserSettings.isInstance(itr_obj)) {
				EObjectWrapper obj = (EObjectWrapper) itr_obj;
				final YearMonth old = (YearMonth) obj.getAttrib("periodStart");
				if (old != null) {
					obj.setAttrib("periodStartDate", old.atDay(1));
				}
				obj.unsetFeature("periodStart");
			}
		}
	}
}
