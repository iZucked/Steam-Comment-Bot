/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.valueproviders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.emf.ecore.EcorePackage;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CharterInMarketOverride;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.util.AssignmentEditorHelper;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;

public class VesselAssignmentTypeValueProviderFactory implements IReferenceValueProviderFactory {

	private final IReferenceValueProviderFactory vesselCharterProviderFactory;
	private final IReferenceValueProviderFactory charterInMarketProviderFactory;

	public VesselAssignmentTypeValueProviderFactory() {
		this.vesselCharterProviderFactory = Activator.getDefault().getReferenceValueProviderFactoryRegistry().getValueProviderFactory(EcorePackage.eINSTANCE.getEClass(),
				CargoPackage.eINSTANCE.getVesselCharter());

		this.charterInMarketProviderFactory = Activator.getDefault().getReferenceValueProviderFactoryRegistry().getValueProviderFactory(EcorePackage.eINSTANCE.getEClass(),
				SpotMarketsPackage.eINSTANCE.getCharterInMarket());
	}

	public VesselAssignmentTypeValueProviderFactory(final IReferenceValueProviderFactory vesselCharterProviderFactory, final IReferenceValueProviderFactory charterInMarketProviderFactory) {
		this.vesselCharterProviderFactory = vesselCharterProviderFactory;
		this.charterInMarketProviderFactory = charterInMarketProviderFactory;
	}

	@Override
	public IReferenceValueProvider createReferenceValueProvider(final EClass owner, final EReference reference, final MMXRootObject theRootObject) {
		final MMXRootObject rootObject = theRootObject;
		if (!(rootObject instanceof LNGScenarioModel)) {
			return null;
		}
		final LNGScenarioModel scenarioModel = (LNGScenarioModel) rootObject;

		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(scenarioModel);
		final FleetModel fleetModel = ScenarioModelUtil.getFleetModel(scenarioModel);
		final EClass referenceClass = reference.getEReferenceType();
		final TypesPackage types = TypesPackage.eINSTANCE;

		// Shouldn't need to pass in explicit references...
		final IReferenceValueProvider vesselCharterProvider = vesselCharterProviderFactory.createReferenceValueProvider(CargoPackage.Literals.CARGO_MODEL,
				CargoPackage.Literals.CARGO_MODEL__VESSEL_CHARTERS, rootObject);

		final IReferenceValueProvider charterInMarketProvider = charterInMarketProviderFactory.createReferenceValueProvider(SpotMarketsPackage.Literals.SPOT_MARKETS_MODEL,
				SpotMarketsPackage.Literals.SPOT_MARKETS_MODEL__CHARTER_IN_MARKETS, rootObject);

		if (referenceClass == types.getVesselAssignmentType()) {
			return new IReferenceValueProvider() {
				// @Override
				public List<Pair<String, EObject>> getAllowedValues(final EObject target, final ETypedElement field) {
					// get a list of globally permissible values
					final List<Pair<String, EObject>> vesselCharterResult = vesselCharterProvider.getAllowedValues(target, field);
					int id = System.identityHashCode(vesselCharterProvider);
					final List<Pair<String, EObject>> charterInMarketResult = charterInMarketProvider.getAllowedValues(target, field);

					// determine the current value for the target object
					final VesselAssignmentType currentValue;
					{
						if (target instanceof AssignableElement) {
							final AssignableElement assignment = (AssignableElement) target;
							currentValue = assignment.getVesselAssignmentType();
						} else {
							currentValue = null;
						}
					}

					// All scenario vessels - those we use to ship ourselves
					final Set<Vessel> scenarioVessels = new HashSet<>();
					for (final VesselCharter va : cargoModel.getVesselCharters()) {
						scenarioVessels.add(va.getVessel());
					}

					boolean isVesselEvent = false;
					if (target instanceof VesselEvent) {
						isVesselEvent = true;
					}

					final boolean showThirdPartyVessels = !isVesselEvent;
					final boolean includeSpotVessels = !isVesselEvent;

					final List<Vessel> allowedVessels;
					// we only want to filter vessels if we are assigning them (i.e. don't do this if we are selecting vessels to add to the restricted list)
					if (field == CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE) {
						allowedVessels = AssignmentEditorHelper.compileAllowedVessels(fleetModel.getVessels(), target);
					} else {
						allowedVessels = new ArrayList<>(fleetModel.getVessels());
					}

					// create list to populate
					final ArrayList<Pair<String, EObject>> result = new ArrayList<>();

					// filter the globally permissible values by the settings for this cargo
					for (final Pair<String, EObject> pair : vesselCharterResult) {
						final VesselCharter vesselCharter = (VesselCharter) pair.getSecond();
						boolean uniqueAvailability = uniqueAvailability(cargoModel.getVesselCharters(), vesselCharter);
						if (vesselCharter == null) {
							continue;
						}
						final Vessel vessel = vesselCharter.getVessel();
						if (vessel == null) {
							continue;
						}

						boolean display = (
						// show the option if the cargo allows this vessel-set
						// (an empty list of allowed vessels means "all vessels")
						allowedVessels.contains(vessel));

						// Filter out non-scenario vessels
						if (display) {
							if (vessel instanceof Vessel) {
								display = (scenarioVessels.contains(vessel)) || (showThirdPartyVessels && !scenarioVessels.contains(vessel));
							}
						}

						// Always show the option if the option is the null option
						// or the current value for the cargo is set to this vessel-set
						if (Objects.equals(vessel, currentValue) || (currentValue instanceof VesselCharter && Objects.equals(vessel, ((VesselCharter) currentValue).getVessel()))
								|| vessel == null) {
							display = true;
						}

						if (display) {
							result.add(pair);
						}
					}

					// Find available spot markets
					if (includeSpotVessels) {

						// filter the globally permissible values by the settings for this cargo
						for (final Pair<String, EObject> pair : charterInMarketResult) {
							final CharterInMarket charterInMarket = (CharterInMarket) pair.getSecond();
							if (charterInMarket == null) {
								continue;
							}
							final Vessel vessel = charterInMarket.getVessel();

							boolean display = (
							// show the option if the cargo allows this vessel-set
							// (an empty list of allowed vessels means "all vessels")

							// show the option if the cargo allows vessels of this class
							(vessel != null && allowedVessels.contains(vessel)));

							// Hide disable markets
							if (!charterInMarket.isEnabled() && !charterInMarket.isNominal()) {
								display = false;
							}

							// Always show the option if the option is the null option
							// or the current value for the cargo is set to this vessel-set
							if (Objects.equals(charterInMarket, currentValue) || charterInMarket == null) {
								display = true;
							}

							if (display) {
								result.add(new Pair<String, EObject>(pair.getFirst() + " (spot)", pair.getSecond()));
							}
						}
					}
					Collections.sort(result, createComparator());

					result.add(0, new Pair<String, EObject>("<Unassigned>", null));

					return result;
				}

				private boolean uniqueAvailability(List<VesselCharter> vesselCharter, VesselCharter va) {
					if (va.getVessel() == null) {
						return true;
					}
					return vesselCharter.stream() //
							.filter(v -> va.getVessel().equals(v.getVessel())) //
							.count() == 1;
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
					charterInMarketProvider.dispose();
					vesselCharterProvider.dispose();
				}

				protected Comparator<Pair<String, ?>> createComparator() {

					return new Comparator<Pair<String, ?>>() {
						@Override
						public int compare(final Pair<String, ?> o1, final Pair<String, ?> o2) {
							final Object v1 = o1.getSecond();
							final Object v2 = o2.getSecond();
							if (!(v1 instanceof CharterInMarketOverride)) {
								if (v2 instanceof CharterInMarketOverride) {
									return 1;
								}
							} else if (v1 instanceof CharterInMarketOverride) {
								if (!(v2 instanceof CharterInMarketOverride)) {
									return -1;
								}
							} else if (v1 instanceof VesselCharter) {
								if (v2 instanceof CharterInMarket) {
									return -1;
								}
							} else if (v1 instanceof CharterInMarket) {
								if (v2 instanceof VesselCharter) {
									return 1;
								}
							}
							// Sorting time charters after fleet
							if (v1 instanceof VesselCharter && v2 instanceof VesselCharter) {
								VesselCharter vesselCharter1 = (VesselCharter) v1;
								VesselCharter vesselCharter2 = (VesselCharter) v2;
								boolean isV1Charter = vesselCharter1.isSetTimeCharterRate();
								boolean isV2Charter = vesselCharter2.isSetTimeCharterRate();
								if (!isV1Charter && isV2Charter) {
									return -1;
								} else if (isV1Charter && !isV2Charter) {
									return 1;
								}
							}

							return o1.getFirst().compareTo(o2.getFirst());
						}

					};
				}

				@Override
				public boolean updateOnChangeToFeature(final Object changedFeature) {
					if (changedFeature == CargoPackage.eINSTANCE.getSlot_RestrictedVessels()) {
						return true;
					}

					if (changedFeature == CargoPackage.eINSTANCE.getVesselEvent_AllowedVessels()) {
						return true;
					}

					return vesselCharterProvider.updateOnChangeToFeature(changedFeature) || charterInMarketProvider.updateOnChangeToFeature(changedFeature);
				}

				public IReferenceValueProvider getVesselCharterProvider() {
					return vesselCharterProvider;
				}

				public IReferenceValueProvider getCharterIn() {
					return charterInMarketProvider;
				}

			};
		}

		return null;
	}

	public final IReferenceValueProviderFactory getVesselCharterProviderFactory() {
		return vesselCharterProviderFactory;
	}

	public final IReferenceValueProviderFactory getCharterInMarketProviderFactory() {
		return charterInMarketProviderFactory;
	}

}
