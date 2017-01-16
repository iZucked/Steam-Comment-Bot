/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelLoader;

public class MigrateToV34 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 33;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 34;
	}

	@Override
	protected void doMigrationWithHelper(final MetamodelLoader loader, final EObjectWrapper model) {

		final TreeIterator<EObject> itr = model.eAllContents();
		while (itr.hasNext()) {
			final EObject eObj = itr.next();
			final EClass eClass = eObj.eClass();
			for (final EStructuralFeature feature : eClass.getEAllAttributes()) {
				if (feature.getName().endsWith("Tmp")) {
					final Object mDate = eObj.eGet(feature);
					if (mDate != null) {
						final EStructuralFeature realFeature = eClass.getEStructuralFeature(feature.getName().substring(0, feature.getName().length() - 3));
						assert realFeature != null;
						eObj.eUnset(feature);
						eObj.eSet(realFeature, mDate);
					}
				}
			}
		}
	}
}
