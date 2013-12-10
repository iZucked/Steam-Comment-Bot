/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013	
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.MetamodelVersionsUtil;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.PackageData;
import com.mmxlabs.models.migration.utils.MetamodelLoader;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

/**
 * @since 8.0
 */
public class MigrateToV6 extends AbstractMigrationUnit {

	private MetamodelLoader destinationLoader;
	private MetamodelLoader sourceLoader;

	@Override
	public String getContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getSourceVersion() {
		return 5;
	}

	@Override
	public int getDestinationVersion() {
		return -6;
	}

	@Override
	protected MetamodelLoader getSourceMetamodelLoader(final Map<URI, PackageData> extraPackages) {
		if (sourceLoader == null) {
			sourceLoader = MetamodelVersionsUtil.createV5Loader(extraPackages);
		}
		return sourceLoader;
	}

	@Override
	protected MetamodelLoader getDestinationMetamodelLoader(final Map<URI, PackageData> extraPackages) {
		if (destinationLoader == null) {
			destinationLoader = MetamodelVersionsUtil.createV6Loader(extraPackages);
		}
		return destinationLoader;
	}

	@Override
	protected void doMigration(final EObject model) {
		// Nothing to do - model is forward compatible.

		// Step 1:
		// Perform AssingmentModel migration.
		migrateAssignmentModel(model);
		
		// Step 2: Clean out old deprecated fields
		// DES Purchase Spot Market / FOB Sale origin ports
		// OptimiserSettings
		// Base fuel cost/index fields
		// LNG ScenarioModel (params, assignment)
		// Values on Sequence

		// Next step - clean up the ecore models - add v7
	}

	private void migrateAssignmentModel(final EObject model) {

		// This should get the cached loader instance
		final MetamodelLoader loader = getDestinationMetamodelLoader(null);

		final EPackage package_ScenarioModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScenarioModel);
		final EClass class_LNGScenarioModel = MetamodelUtils.getEClass(package_ScenarioModel, "LNGScenarioModel");
		final EClass class_LNGPortfolioModel = MetamodelUtils.getEClass(package_ScenarioModel, "LNGPortfolioModel");

		final EReference reference_LNGScenarioModel_portfolioModel = MetamodelUtils.getReference(class_LNGScenarioModel, "portfolioModel");
		final EReference reference_LNGPortfolioModel_assignmentModel = MetamodelUtils.getReference(class_LNGPortfolioModel, "assignmentModel");

		final EPackage package_AssignmentModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_AssignmentModel);
		final EPackage package_FleetModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_FleetModel);

		final EClass class_AssignmentModel = MetamodelUtils.getEClass(package_AssignmentModel, "AssignmentModel");
		final EClass class_ElementAssignment = MetamodelUtils.getEClass(package_AssignmentModel, "ElementAssignment");

		final EReference reference_AssignableModel_elementAssignments = MetamodelUtils.getReference(class_AssignmentModel, "elementAssignments");
		final EReference reference_ElementAssignment_assignment = MetamodelUtils.getReference(class_ElementAssignment, "assignment");
		final EReference reference_ElementAssignment_assignedObject = MetamodelUtils.getReference(class_ElementAssignment, "assignedObject");
		final EAttribute attribute_ElementAssignment_sequence = MetamodelUtils.getAttribute(class_ElementAssignment, "sequence");
		final EAttribute attribute_ElementAssignment_locked = MetamodelUtils.getAttribute(class_ElementAssignment, "locked");
		final EAttribute attribute_ElementAssignment_spotIndex = MetamodelUtils.getAttribute(class_ElementAssignment, "spotIndex");

		final EClass class_AssignableElement = MetamodelUtils.getEClass(package_FleetModel, "AssignableElement");

		final EReference reference_AssignableElement_assignment = MetamodelUtils.getReference(class_AssignableElement, "assignment");
		final EAttribute attribute_AssignableElement_sequenceHint = MetamodelUtils.getAttribute(class_AssignableElement, "sequenceHint");
		final EAttribute attribute_AssignableElement_locked = MetamodelUtils.getAttribute(class_AssignableElement, "locked");
		final EAttribute attribute_AssignableElement_spotIndex = MetamodelUtils.getAttribute(class_AssignableElement, "spotIndex");

		final EObject portfolioModel = (EObject) model.eGet(reference_LNGScenarioModel_portfolioModel);
		final EObject assignmentModel = (EObject) portfolioModel.eGet(reference_LNGPortfolioModel_assignmentModel);
		if (assignmentModel != null) {
			final EList<EObject> elementAssignments = MetamodelUtils.getValueAsTypedList(assignmentModel, reference_AssignableModel_elementAssignments);
			for (final EObject elementAssignment : elementAssignments) {
				final EObject assignedObject = (EObject) elementAssignment.eGet(reference_ElementAssignment_assignedObject);
				if (assignedObject != null) {
					// Check cast
					if (class_AssignableElement.isInstance(assignedObject)) {
						// Copy data across
						assignedObject.eSet(reference_AssignableElement_assignment, elementAssignment.eGet(reference_ElementAssignment_assignment));
						assignedObject.eSet(attribute_AssignableElement_locked, elementAssignment.eGet(attribute_ElementAssignment_locked));
						assignedObject.eSet(attribute_AssignableElement_sequenceHint, elementAssignment.eGet(attribute_ElementAssignment_sequence));
						assignedObject.eSet(attribute_AssignableElement_spotIndex, elementAssignment.eGet(attribute_ElementAssignment_spotIndex));
					}
				}
			}
			// Clear assignment model refernce
			portfolioModel.eUnset(reference_LNGPortfolioModel_assignmentModel);
		}
	}
}
