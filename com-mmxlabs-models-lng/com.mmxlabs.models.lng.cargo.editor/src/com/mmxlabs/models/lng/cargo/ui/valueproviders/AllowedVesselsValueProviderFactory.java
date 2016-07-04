/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
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
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;

public class AllowedVesselsValueProviderFactory implements IReferenceValueProviderFactory {

	private final IReferenceValueProviderFactory vesselProviderFactory;
	private final IReferenceValueProviderFactory vesselClassProviderFactory;

	public AllowedVesselsValueProviderFactory() {
		this.vesselProviderFactory = Activator.getDefault().getReferenceValueProviderFactoryRegistry().getValueProviderFactory(EcorePackage.eINSTANCE.getEClass(), FleetPackage.eINSTANCE.getVessel());

		this.vesselClassProviderFactory = Activator.getDefault().getReferenceValueProviderFactoryRegistry()
				.getValueProviderFactory(EcorePackage.eINSTANCE.getEClass(), FleetPackage.eINSTANCE.getVesselClass());
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

		final IReferenceValueProvider vesselClassProvider = vesselClassProviderFactory.createReferenceValueProvider(FleetPackage.Literals.FLEET_MODEL,
				FleetPackage.Literals.FLEET_MODEL__VESSEL_CLASSES, rootObject);

		if (reference == CargoPackage.Literals.SLOT__ALLOWED_VESSELS || reference == CargoPackage.Literals.VESSEL_EVENT__ALLOWED_VESSELS) {
			return new IReferenceValueProvider() {
				// @Override
				public List<Pair<String, EObject>> getAllowedValues(final EObject target, final EStructuralFeature field) {
					// get a list of globally permissible values
					final List<Pair<String, EObject>> vesselResult = vesselProvider.getAllowedValues(target, field);
					final List<Pair<String, EObject>> vesselClassResult = vesselClassProvider.getAllowedValues(target, field);

					// // determine the current value for the target object
					// final VesselAssignmentType currentValue;
					// {
					// // if (target instanceof AssignableElement) {
					// // final AssignableElement assignment = (AssignableElement) target;
					// // currentValue = assignment.getVesselAssignmentType();
					// // } else {
					// currentValue = null;
					// // }
					// }

					// // All scenario vessels - those we use to ship ourselves
					// final Set<Vessel> scenarioVessels = new HashSet<>();
					// for (final VesselAvailability va : cargoModel.getVesselAvailabilities()) {
					// scenarioVessels.add(va.getVessel());
					// }

					final boolean includeVesselClasses;

					if (field == CargoPackage.Literals.VESSEL_EVENT__ALLOWED_VESSELS) {
						includeVesselClasses = target instanceof CharterOutEvent;
					} else if (field == CargoPackage.Literals.SLOT__ALLOWED_VESSELS) {
						includeVesselClasses = true;
					} else {
						throw new RuntimeException("Invalid code path");
					}

					// create list to populate
					final ArrayList<Pair<String, EObject>> result = new ArrayList<Pair<String, EObject>>();

					// filter the globally permissible values by the settings for this cargo
					for (final Pair<String, EObject> pair : vesselResult) {
						final Vessel vessel = (Vessel) pair.getSecond();
						if (vessel == null) {
							continue;
						}

						// determine the vessel class (if any) for this option
						final VesselClass vc = vessel.getVesselClass();
						if (vc == null) {
							continue;
						}

						final boolean display = true;

						if (display) {
							result.add(pair);
						}
					}

					// Find available spot markets
					if (includeVesselClasses) {

						// filter the globally permissible values by the settings for this cargo
						for (final Pair<String, EObject> pair : vesselClassResult) {
							final VesselClass vc = (VesselClass) pair.getSecond();
							if (vc == null) {
								continue;
							}

							final boolean display = true;

							// // Always show the option if the option is the null option
							// // or the current value for the cargo is set to this vessel-set
							// if (Equality.isEqual(charterInMarket, currentValue) || charterInMarket == null) {
							// display = true;
							// }

							if (display) {
								result.add(new Pair<String, EObject>(pair.getFirst(), pair.getSecond()));
							}
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
					vesselClassProvider.dispose();
					vesselProvider.dispose();
				}

				protected Comparator<Pair<String, ?>> createComparator() {

					return new Comparator<Pair<String, ?>>() {
						@Override
						public int compare(final Pair<String, ?> o1, final Pair<String, ?> o2) {
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

							return o1.getFirst().compareTo(o2.getFirst());
						}

					};
				}

				@Override
				public boolean updateOnChangeToFeature(final Object changedFeature) {
					if (changedFeature == CargoPackage.eINSTANCE.getSlot_AllowedVessels()) {
						return true;
					}

					if (changedFeature == CargoPackage.eINSTANCE.getVesselEvent_AllowedVessels()) {
						return true;
					}

					return vesselProvider.updateOnChangeToFeature(changedFeature) || vesselClassProvider.updateOnChangeToFeature(changedFeature);
				}

			};
		}

		return null;
	}
}
