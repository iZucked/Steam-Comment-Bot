/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.importer;

import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.common.csv.FieldMap;
import com.mmxlabs.common.csv.IDeferment;
import com.mmxlabs.common.csv.IFieldMap;
import com.mmxlabs.common.csv.IImportContext;
import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.util.AssignmentEditorHelper;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.util.importer.IMMXExportContext;
import com.mmxlabs.models.util.importer.IMMXImportContext;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter;

/**
 */
public class VesselEventImporter extends DefaultClassImporter {

	protected boolean shouldImportReference(final EReference reference) {
		return reference != CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE;
	}

	protected Map<String, String> exportObject(final EObject object, final IMMXExportContext context) {
		final Map<String, String> result = new LinkedHashMap<String, String>();

		for (final EAttribute attribute : object.eClass().getEAllAttributes()) {

			if (object instanceof AssignableElement) {
				// final AssignableElement assignableElement = (AssignableElement) object;
				// yes yes both attribute and reference here, but easier to copy paste....
				if (attribute == CargoPackage.Literals.ASSIGNABLE_ELEMENT__SPOT_INDEX || attribute == CargoPackage.Literals.ASSIGNABLE_ELEMENT__SEQUENCE_HINT
						|| attribute == CargoPackage.Literals.ASSIGNABLE_ELEMENT__LOCKED) {
					// if (assignableElement.getVesselAssignmentType() == null) {
					continue;
					// }
				}
			}

			if (shouldExportFeature(attribute)) {
				exportAttribute(object, attribute, result, context);
			}
		}

		for (final EReference reference : object.eClass().getEAllReferences()) {

			if (object instanceof AssignableElement) {
				// final AssignableElement assignableElement = (AssignableElement) object;
				// yes yes both attribute and reference here, but easier to copy paste....
				if (reference == CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE) {
					// if (assignableElement.getVesselAssignmentType() == null) {
					continue;
					// }
				}
			}

			if (shouldExportFeature(reference)) {
				exportReference(object, reference, result, context);
			}
		}

		return result;
	}

	@Override
	public ImportResults importObject(final EObject parent, final EClass eClass, final Map<String, String> row, final IMMXImportContext context) {
		final ImportResults results = super.importObject(parent, eClass, row, context);
		addAssignmentTask(results.importedObject, new FieldMap(row), context);
		return results;
	}

	private void addAssignmentTask(final EObject target, final IFieldMap fields, final IMMXImportContext context) {
		if (target instanceof AssignableElement) {
			final AssignableElement assignableElement = (AssignableElement) target;

			final String vesselName = fields.get(CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE.getName().toLowerCase());
			final String assignment = fields.get("assignment");
			final String spotindex = fields.get("spotindex");

			if ((vesselName != null && !vesselName.isEmpty()) || (assignment != null && !assignment.isEmpty())) {
				context.doLater(new IDeferment() {

					@Override
					public void run(final IImportContext importContext) {

						final IMMXImportContext context = (IMMXImportContext) importContext;

						// New style
						if (vesselName != null && !vesselName.isEmpty()) {
							final CharterInMarket charterInMarket = (CharterInMarket) context.getNamedObject(vesselName.trim(), SpotMarketsPackage.Literals.CHARTER_IN_MARKET);
							if (charterInMarket != null) {
								assignableElement.setVesselAssignmentType(charterInMarket);
							} else {
								final Vessel v = (Vessel) context.getNamedObject(vesselName.trim(), FleetPackage.Literals.VESSEL);
								if (v != null) {
									final VesselAvailability availability = AssignmentEditorHelper.findVesselAvailability(v, assignableElement,
											((LNGScenarioModel) context.getRootObject()).getCargoModel().getVesselAvailabilities(), null);
									assignableElement.setVesselAssignmentType(availability);
								}
							}
						} else {
							// Old style
							if (spotindex != null && !spotindex.isEmpty()) {

								final VesselClass vc = (VesselClass) context.getNamedObject(vesselName.trim(), FleetPackage.Literals.VESSEL_CLASS);
								if (vc != null) {
									for (CharterInMarket charterInMarket : ((LNGScenarioModel) context.getRootObject()).getReferenceModel().getSpotMarketsModel().getCharterInMarkets()) {
										if (vc.equals(charterInMarket.getVesselClass())) {
											assignableElement.setVesselAssignmentType(charterInMarket);
											break;
										}
									}
								}

							} else {
								final Vessel v = (Vessel) context.getNamedObject(vesselName.trim(), FleetPackage.Literals.VESSEL);
								if (v != null) {
									final VesselAvailability availability = AssignmentEditorHelper.findVesselAvailability(v, assignableElement,
											((LNGScenarioModel) context.getRootObject()).getCargoModel().getVesselAvailabilities(), null);
									assignableElement.setVesselAssignmentType(availability);
								}
							}
						}
					}

					@Override
					public int getStage() {
						return IMMXImportContext.STAGE_MODIFY_SUBMODELS;
					}
				});
			}
		}
	}
}