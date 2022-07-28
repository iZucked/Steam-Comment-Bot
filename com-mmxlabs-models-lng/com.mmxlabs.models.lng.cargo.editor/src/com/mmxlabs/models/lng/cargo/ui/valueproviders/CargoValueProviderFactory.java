/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.valueproviders;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;
import com.mmxlabs.models.ui.valueproviders.MergedReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.SimpleReferenceValueProvider;

public class CargoValueProviderFactory implements IReferenceValueProviderFactory {

	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");

	@Override
	public IReferenceValueProvider createReferenceValueProvider(final EClass owner, final EReference reference, final MMXRootObject rootObject) {
		if (rootObject instanceof LNGScenarioModel lngScenarioModel) {
			final CargoModel cm = lngScenarioModel.getCargoModel();
			if (cm == null) {
				return null;
			}
			if (CargoPackage.eINSTANCE.getVesselCharter().isSuperTypeOf(reference.getEReferenceType())) {
				return new SimpleReferenceValueProvider(cm, CargoPackage.eINSTANCE.getCargoModel_VesselCharters()) {
					@Override
					public String getName(final EObject referer, final EReference feature, final EObject referenceValue) {
						if (referenceValue instanceof VesselCharter) {
							final VesselCharter vesselCharter = (VesselCharter) referenceValue;
							final Vessel vessel = vesselCharter.getVessel();
							final boolean uniqueAvailability = uniqueAvailability(cm.getVesselCharters(), vesselCharter);
							if (vessel != null && uniqueAvailability) {
								return vessel.getName();
							} else if (vessel != null && !uniqueAvailability) {
								final String startByStr = vesselCharter.getStartBy() != null ? formatter.format(vesselCharter.getStartBy()) : null;
								final String startAfterStr = vesselCharter.getStartAfter() != null ? formatter.format(vesselCharter.getStartAfter()) : null;
								final String dateString = startByStr != null ? startByStr : startAfterStr;
								if (dateString != null) {
									return String.format("%s (%s)", vessel.getName(), dateString);
								} else {
									return vessel.getName();
								}
							} else {
								return "";
							}

						}
						return super.getName(referer, feature, referenceValue);
					}

					private boolean uniqueAvailability(final List<VesselCharter> vesselCharter, final VesselCharter va) {
						if (va == null || (va != null && va.getVessel() == null)) {
							return true;
						}
						return vesselCharter.stream().filter(v -> va.getVessel().equals(v.getVessel())).count() == 1;
					}

					@Override
					protected boolean isRelevantTarget(final Object target, final Object feature) {
						// check for a change to a vessel availability
						final boolean isContainingReferenceSuperType = (containingReference.getEReferenceType().isSuperTypeOf(((EObject) target).eClass()));
						// check for a change to the vessel reference
						boolean isFeatureVessel = false;
						if (feature instanceof EReference) {
							isFeatureVessel = FleetPackage.Literals.VESSEL.equals(((EReference) feature).getEReferenceType());
						}
						// check for a change to the list of vessel availabilities
						final boolean featureIsContainingReference = feature == containingReference;
						return (isFeatureVessel && isContainingReferenceSuperType) || featureIsContainingReference;
					}

				};
			} else if (CargoPackage.eINSTANCE.getVesselEvent().isSuperTypeOf(reference.getEReferenceType())) {
				return new SimpleReferenceValueProvider(cm, CargoPackage.eINSTANCE.getCargoModel_VesselEvents());
			} else if (CargoPackage.eINSTANCE.getCargo().isSuperTypeOf(reference.getEReferenceType())) {
				return new SimpleReferenceValueProvider(cm, CargoPackage.eINSTANCE.getCargoModel_Cargoes());
			} else if (reference.getEReferenceType().isSuperTypeOf(CargoPackage.eINSTANCE.getCargoGroup())) {
				return new SimpleReferenceValueProvider(cm, CargoPackage.eINSTANCE.getCargoModel_CargoGroups());
			} else if (CargoPackage.eINSTANCE.getLoadSlot().isSuperTypeOf(reference.getEReferenceType())) {
				return new SimpleReferenceValueProvider(cm, CargoPackage.eINSTANCE.getCargoModel_LoadSlots());
			} else if (CargoPackage.eINSTANCE.getDischargeSlot().isSuperTypeOf(reference.getEReferenceType())) {
				return new SimpleReferenceValueProvider(cm, CargoPackage.eINSTANCE.getCargoModel_DischargeSlots());
			} else if (CargoPackage.eINSTANCE.getSlot().isSuperTypeOf(reference.getEReferenceType())) {
				return new MergedReferenceValueProvider(cm, CargoPackage.eINSTANCE.getCargoModel_LoadSlots(), CargoPackage.eINSTANCE.getCargoModel_DischargeSlots());
			}
		}
		return null;
	}
}
