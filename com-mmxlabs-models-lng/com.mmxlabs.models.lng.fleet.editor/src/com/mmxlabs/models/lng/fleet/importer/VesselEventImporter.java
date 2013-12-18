/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.importer;

import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.lng.fleet.AssignableElement;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.util.importer.FieldMap;
import com.mmxlabs.models.util.importer.IFieldMap;
import com.mmxlabs.models.util.importer.IImportContext;
import com.mmxlabs.models.util.importer.IImportContext.IDeferment;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter;

/**
 * @since 2.0
 */
public class VesselEventImporter extends DefaultClassImporter {

	protected boolean shouldImportReference(final EReference reference) {
		return reference != FleetPackage.Literals.ASSIGNABLE_ELEMENT__ASSIGNMENT;
	}

	protected Map<String, String> exportObject(final EObject object, final MMXRootObject root) {
		final Map<String, String> result = new LinkedHashMap<String, String>();

		for (final EAttribute attribute : object.eClass().getEAllAttributes()) {

			if (object instanceof AssignableElement) {
				final AssignableElement assignableElement = (AssignableElement) object;
				// yes yes both attribute and reference here, but easier to copy paste....
				if (attribute == FleetPackage.Literals.ASSIGNABLE_ELEMENT__SPOT_INDEX || attribute == FleetPackage.Literals.ASSIGNABLE_ELEMENT__SEQUENCE_HINT
						|| attribute == FleetPackage.Literals.ASSIGNABLE_ELEMENT__LOCKED) {
					if (assignableElement.getAssignment() == null) {
						continue;
					}
				}
			}

			if (shouldExportFeature(attribute)) {
				exportAttribute(object, attribute, result);
			}
		}

		for (final EReference reference : object.eClass().getEAllReferences()) {

			if (object instanceof AssignableElement) {
				final AssignableElement assignableElement = (AssignableElement) object;
				// yes yes both attribute and reference here, but easier to copy paste....
				if (reference == FleetPackage.Literals.ASSIGNABLE_ELEMENT__ASSIGNMENT) {
					if (assignableElement.getAssignment() == null) {
						continue;
					}
				}
			}

			if (shouldExportFeature(reference)) {
				exportReference(object, reference, result, root);
			}
		}

		return result;
	}

	@Override
	public ImportResults importObject(final EObject parent, final EClass eClass, final Map<String, String> row, final IImportContext context) {
		ImportResults results = super.importObject(parent, eClass, row, context);
		addAssignmentTask(results.importedObject, new FieldMap(row), context);
		return results;
	}

	private void addAssignmentTask(final EObject target, final IFieldMap fields, final IImportContext context) {
		if (target instanceof AssignableElement) {
			final AssignableElement assignableElement = (AssignableElement) target;

			final String vesselName = fields.get(FleetPackage.Literals.ASSIGNABLE_ELEMENT__ASSIGNMENT.getName().toLowerCase());

			context.doLater(new IDeferment() {

				@Override
				public void run(final IImportContext context) {
					if (assignableElement.isSetSpotIndex()) {
						final NamedObject v2 = context.getNamedObject(vesselName.trim(), TypesPackage.eINSTANCE.getAVesselSet());
						if (v2 instanceof VesselClass) {
							assignableElement.setAssignment((VesselClass) v2);
						}
					} else {
						final Vessel v = (Vessel) context.getNamedObject(vesselName.trim(), FleetPackage.eINSTANCE.getVessel());
						if (v != null) {
							assignableElement.setAssignment(v);
						}
					}
				}

				@Override
				public int getStage() {
					return IImportContext.STAGE_MODIFY_SUBMODELS;
				}
			});
		}
	}
}