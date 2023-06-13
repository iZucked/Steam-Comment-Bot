/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.MigrationModelRecord;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class MigrateToV186 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 185;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 186;
	}

	@Override
	protected void doMigration(@NonNull final MigrationModelRecord modelRecord) {
		// Sandbox row groups

		final EObjectWrapper modelRoot = modelRecord.getModelRoot();
		final EPackage analyticsPackage = modelRecord.getMetamodelLoader().getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_AnalyticsModel);
		final EClass classBaseCaseRowGroup = MetamodelUtils.getEClass(analyticsPackage, "BaseCaseRowGroup");
		final EClass classPartialCaseRowGroup = MetamodelUtils.getEClass(analyticsPackage, "PartialCaseRowGroup");
		final EObjectWrapper analyticsModel = modelRoot.getRef("analyticsModel");
		final List<EObjectWrapper> sandboxes = analyticsModel.getRefAsList("optionModels");
		if (sandboxes != null) {
			for (final EObjectWrapper sandbox : sandboxes) {
				{
					final EObjectWrapper parent = sandbox.getRef("baseCase");
					final List<EObjectWrapper> rows = parent.getRefAsList("baseCase");
					final List<EObject> groups = new LinkedList<>();
					for (final EObjectWrapper row : rows) {
						final EObject group = analyticsPackage.getEFactoryInstance().create(classBaseCaseRowGroup);
						row.setRef("group", group);
						groups.add(group);
					}
					if (!groups.isEmpty()) {
						parent.setRef("groups", groups);
					}
				}
				{
					final EObjectWrapper parent = sandbox.getRef("partialCase");
					final List<EObjectWrapper> rows = parent.getRefAsList("partialCase");
					final List<EObject> groups = new LinkedList<>();
					for (final EObjectWrapper row : rows) {
						final EObject group = analyticsPackage.getEFactoryInstance().create(classPartialCaseRowGroup);
						row.setRef("group", group);
						groups.add(group);
					}
					if (!groups.isEmpty()) {
						parent.setRef("groups", groups);
					}
				}
			}
		}
	}
}