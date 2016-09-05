/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation.valueproviders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.adp.ShippingOption;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;

public class VesselValueProviderFactory implements IReferenceValueProviderFactory {

	private final IReferenceValueProviderFactory vesselProviderFactory;

	public VesselValueProviderFactory() {
		this.vesselProviderFactory = Activator.getDefault().getReferenceValueProviderFactoryRegistry().getValueProviderFactory(EcorePackage.eINSTANCE.getEClass(), FleetPackage.eINSTANCE.getVessel());

	}

	public VesselValueProviderFactory(final IReferenceValueProviderFactory vesselProviderFactory) {
		this.vesselProviderFactory = vesselProviderFactory;
	}

	@Override
	public IReferenceValueProvider createReferenceValueProvider(final EClass owner, final EReference reference, final MMXRootObject theRootObject) {
		final MMXRootObject rootObject = theRootObject;
		if (!(rootObject instanceof LNGScenarioModel)) {
			return null;
		}
		final LNGScenarioModel scenarioModel = (LNGScenarioModel) rootObject;

		final CargoModel cargoModel = scenarioModel.getCargoModel();
		final EClass referenceClass = reference.getEReferenceType();
		final TypesPackage types = TypesPackage.eINSTANCE;

		// Shouldn't need to pass in explicit references...
		final IReferenceValueProvider vesselProvider = vesselProviderFactory.createReferenceValueProvider(FleetPackage.Literals.FLEET_MODEL, FleetPackage.Literals.FLEET_MODEL__VESSELS, rootObject);

		if (referenceClass == FleetPackage.Literals.VESSEL) {
			return new IReferenceValueProvider() {
				@Override
				public List<Pair<String, EObject>> getAllowedValues(final EObject target, final EStructuralFeature field) {
					// get a list of globally permissible values
					final List<Pair<String, EObject>> vesselResult = vesselProvider.getAllowedValues(target, field);

					// determine the current value for the target object
					final Vessel currentValue;
					{
						if (target instanceof ShippingOption) {
							ShippingOption shippingOption = (ShippingOption) target;
							currentValue = shippingOption.getVessel();
						} else {
							currentValue = null;
						}
					}

					final ArrayList<Pair<String, EObject>> result = new ArrayList<>(vesselResult);

					Collections.sort(result, createComparator());

					result.add(0, new Pair<String, EObject>("<Unassigned>", null));

					return result;
				}

				@Override
				public String getName(final EObject referer, final EReference feature, final EObject referenceValue) {
					throw new UnsupportedOperationException();
				}

				@Override
				public Iterable<Pair<Notifier, List<Object>>> getNotifiers(final EObject referer, final EReference feature, final EObject referenceValue) {
					return null;
				}

				@Override
				public void dispose() {
					vesselProvider.dispose();
				}

				protected Comparator<Pair<String, ?>> createComparator() {

					return new Comparator<Pair<String, ?>>() {
						@Override
						public int compare(final Pair<String, ?> o1, final Pair<String, ?> o2) {
							final Vessel v1 = (Vessel) o1.getSecond();
							final Vessel v2 = (Vessel) o2.getSecond();

							return v1.getName().compareTo(v2.getName());
						}

					};
				}

				@Override
				public boolean updateOnChangeToFeature(final Object changedFeature) {

					return vesselProvider.updateOnChangeToFeature(changedFeature);
				}
			};
		}

		return null;
	}
}
