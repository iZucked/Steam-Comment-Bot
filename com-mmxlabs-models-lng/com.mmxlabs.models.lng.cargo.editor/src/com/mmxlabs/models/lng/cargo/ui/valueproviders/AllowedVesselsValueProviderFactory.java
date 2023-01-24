/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.valueproviders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.emf.ecore.EcorePackage;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselGroup;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;

public class AllowedVesselsValueProviderFactory implements IReferenceValueProviderFactory {

	private final IReferenceValueProviderFactory vesselProviderFactory;
	private final IReferenceValueProviderFactory vesselGroupProviderFactory;

	public AllowedVesselsValueProviderFactory() {
		this.vesselProviderFactory = Activator.getDefault().getReferenceValueProviderFactoryRegistry().getValueProviderFactory(EcorePackage.eINSTANCE.getEClass(), FleetPackage.eINSTANCE.getVessel());

		this.vesselGroupProviderFactory = Activator.getDefault().getReferenceValueProviderFactoryRegistry().getValueProviderFactory(EcorePackage.eINSTANCE.getEClass(),
				FleetPackage.eINSTANCE.getVesselGroup());
	}

	@Override
	public IReferenceValueProvider createReferenceValueProvider(final EClass owner, final EReference reference, final MMXRootObject theRootObject) {
		final MMXRootObject rootObject = theRootObject;
		if (!(rootObject instanceof LNGScenarioModel)) {
			return null;
		}
		// final LNGScenarioModel scenarioModel = (LNGScenarioModel) rootObject;
		//
		// final FleetModel fleetModel = scenarioModel.getFleetModel();
		// final CargoModel cargoModel = scenarioModel.getPortfolioModel().getCargoModel();
		// final EClass referenceClass = reference.getEReferenceType();
		// final TypesPackage types = TypesPackage.eINSTANCE;
		// final FleetPackage fleet = FleetPackage.eINSTANCE;

		// Shouldn't need to pass in explicit references...
		final IReferenceValueProvider vesselProvider = vesselProviderFactory.createReferenceValueProvider(FleetPackage.Literals.FLEET_MODEL, FleetPackage.Literals.FLEET_MODEL__VESSELS, rootObject);

		final IReferenceValueProvider vesselGroupProvider = vesselGroupProviderFactory.createReferenceValueProvider(FleetPackage.Literals.FLEET_MODEL, FleetPackage.Literals.FLEET_MODEL__VESSEL_GROUPS,
				rootObject);

		if (reference == CargoPackage.Literals.SLOT__RESTRICTED_VESSELS 
			|| reference == CargoPackage.Literals.VESSEL_EVENT__ALLOWED_VESSELS
			|| reference == CargoPackage.Literals.SLOT__RESTRICTED_VESSELS ) {
			return new IReferenceValueProvider() {
				// @Override
				public List<Pair<String, EObject>> getAllowedValues(final EObject target, final ETypedElement field) {
					// get a list of globally permissible values
					final List<Pair<String, EObject>> vesselResult = vesselProvider.getAllowedValues(target, field);
					final List<Pair<String, EObject>> vesselGroupResult = vesselGroupProvider.getAllowedValues(target, field);

					// create list to populate
					final ArrayList<Pair<String, EObject>> result = new ArrayList<>();

					// filter the globally permissible values by the settings for this cargo
					for (final Pair<String, EObject> pair : vesselResult) {
						final Vessel vessel = (Vessel) pair.getSecond();
						if (vessel == null) {
							continue;
						}

						final boolean display = true;

						if (display) {
							result.add(pair);
						}
					}
					for (final Pair<String, EObject> pair : vesselGroupResult) {
						final VesselGroup vessel = (VesselGroup) pair.getSecond();
						if (vessel == null) {
							continue;
						}
						if (vessel.getVessels().isEmpty()) {
							continue;
						}

						final boolean display = true;

						if (display) {
							result.add(pair);
						}
					}

					Collections.sort(result, createComparator());

					return result;
				}

				@Override
				public String getName(final EObject referer, final EReference feature, final EObject referenceValue) {
					if (referenceValue instanceof NamedObject) {
						return ((NamedObject) referenceValue).getName();
					}
					return referenceValue.toString();
				}

				@Override
				public Iterable<Pair<Notifier, List<Object>>> getNotifiers(final EObject referer, final EReference feature, final EObject referenceValue) {
					return null;
				}

				@Override
				public void dispose() {
					vesselGroupProvider.dispose();
					vesselProvider.dispose();
				}

				protected Comparator<Pair<String, ?>> createComparator() {

					return new Comparator<Pair<String, ?>>() {
						@Override
						public int compare(final Pair<String, ?> o1, final Pair<String, ?> o2) {
							return o1.getFirst().compareTo(o2.getFirst());
						}

					};
				}

				@Override
				public boolean updateOnChangeToFeature(final Object changedFeature) {
					if (changedFeature == CargoPackage.eINSTANCE.getVesselEvent_AllowedVessels()) {
						return true;
					}
					
					if (changedFeature == CargoPackage.eINSTANCE.getSlot_RestrictedVessels()) {
						return true;
					}

					return vesselProvider.updateOnChangeToFeature(changedFeature) || vesselGroupProvider.updateOnChangeToFeature(changedFeature);
				}

			};
		}

		return null;
	}
}
