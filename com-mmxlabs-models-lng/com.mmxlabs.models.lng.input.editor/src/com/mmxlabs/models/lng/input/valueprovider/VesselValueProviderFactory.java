/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.input.valueprovider;

import java.util.Comparator;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;
import com.mmxlabs.models.ui.valueproviders.MergedReferenceValueProvider;

public class VesselValueProviderFactory implements IReferenceValueProviderFactory {
	@Override
	public IReferenceValueProvider createReferenceValueProvider(final EClass owner,
			final EReference reference, final MMXRootObject rootObject) {
		final FleetModel fleetModel = rootObject.getSubModel(FleetModel.class);
		final EClass referenceClass = reference.getEReferenceType();
		final TypesPackage types = TypesPackage.eINSTANCE;
		final FleetPackage fleet = FleetPackage.eINSTANCE;
		
		if (referenceClass == types.getAVesselSet()) {
			return new MergedReferenceValueProvider(fleetModel, fleet.getFleetModel_VesselClasses(), fleet.getFleetModel_Vessels()) {
				@Override
				protected Pair<String, EObject> getEmptyObject() {
					return new Pair<String, EObject>("<Unassigned>", null);
				}
				
								
				@Override
				public String getName(EObject referer, EReference feature,
						EObject referenceValue) {
					return super.getName(referer, feature, referenceValue) + ((referenceValue instanceof VesselClass) ? " (spot)" : "");
				}


				@Override
				protected Comparator<Pair<String, ?>> createComparator() {
					final Comparator superValue = super.createComparator();
					
					return new Comparator<Pair<String, ?>>() {
						@Override
						public int compare(Pair<String, ?> o1,Pair<String, ?> o2) {
							final Object v1 = o1.getSecond();
							final Object v2 = o2.getSecond();
							if (v1 instanceof Vessel) {
								if (v2 instanceof VesselClass) {
									return -1;
								}
							} else if (v1 instanceof VesselClass) {
								if (v2 instanceof Vessel) {
									return 1;
								}
							}
							return superValue.compare(o1, o2);
						}
						
					};
				}
			};
		}
		
		return null;
	}
	
}
