/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelLoader;

public class MigrateToV45 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 44;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 45;
	}

	@Override
	protected void doMigrationWithHelper(final MetamodelLoader loader, final EObjectWrapper model) {

		final TreeIterator<EObject> eAllContents = model.eAllContents();
		while (eAllContents.hasNext()) {
			final EObject o = eAllContents.next();

			final EClass cls = o.eClass();

			for (final EReference ref : cls.getEAllReferences()) {
				// Skip containment references
				if (ref.isContainment()) {
					continue;
				}
				// Skip singular references
				if (!ref.isMany()) {
					continue;
				}

				final List<?> items = (List<?>) o.eGet(ref);
				final LinkedHashSet<?> newItems = new LinkedHashSet<>(items);
				o.eSet(ref, new ArrayList<>(newItems));
			}
		}
	}
}
