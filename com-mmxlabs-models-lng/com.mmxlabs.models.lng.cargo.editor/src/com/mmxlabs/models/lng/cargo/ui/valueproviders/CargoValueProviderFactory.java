/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.valueproviders;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;
import com.mmxlabs.models.ui.valueproviders.MergedReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.SimpleReferenceValueProvider;

public class CargoValueProviderFactory implements IReferenceValueProviderFactory {

	@Override
	public IReferenceValueProvider createReferenceValueProvider(final EClass owner, final EReference reference, final MMXRootObject rootObject) {
		if (rootObject instanceof LNGScenarioModel) {
			final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
			final CargoModel cm = lngScenarioModel.getCargoModel();
			if (cm == null) {
				return null;
			}
			if (CargoPackage.eINSTANCE.getVesselAvailability().isSuperTypeOf(reference.getEReferenceType())) {
				return new SimpleReferenceValueProvider(cm, CargoPackage.eINSTANCE.getCargoModel_VesselAvailabilities()) {
					@Override
					public String getName(final EObject referer, final EReference feature, final EObject referenceValue) {
						if (referenceValue instanceof VesselAvailability) {
							final VesselAvailability vesselAvailability = (VesselAvailability) referenceValue;
							final Vessel vessel = vesselAvailability.getVessel();
							if (vessel != null) {
								return vessel.getName();
							} else {
								return "";
							}

						}
						return super.getName(referer, feature, referenceValue);
					}

					@Override
					protected boolean isRelevantTarget(Object target, Object feature) {
						// check for a change to a vessel availability
						boolean isContainingReferenceSuperType = (containingReference.getEReferenceType().isSuperTypeOf(((EObject) target).eClass()));
						// check for a change to the vessel reference
						boolean isFeatureVessel = false;
						if (feature instanceof EReference) {
							isFeatureVessel = FleetPackage.Literals.VESSEL.equals(((EReference) feature).getEReferenceType());
						}
						// check for a change to the list of vessel availabilities
						boolean featureIsContainingReference = feature == containingReference;
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
