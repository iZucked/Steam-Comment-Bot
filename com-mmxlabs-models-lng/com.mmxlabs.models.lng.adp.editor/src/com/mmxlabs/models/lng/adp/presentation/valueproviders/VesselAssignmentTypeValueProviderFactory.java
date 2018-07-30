/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation.valueproviders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.common.Equality;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.ShippingOption;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CharterInMarketOverride;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.util.AssignmentEditorHelper;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;

public class VesselAssignmentTypeValueProviderFactory implements IReferenceValueProviderFactory {

	private ADPModel adpModel;

	public VesselAssignmentTypeValueProviderFactory(ADPModel adpModel) {

		this.adpModel = adpModel;
	}

	@Override
	public IReferenceValueProvider createReferenceValueProvider(final EClass owner, final EReference reference, final MMXRootObject theRootObject) {
		final MMXRootObject rootObject = theRootObject;
		if (!(rootObject instanceof LNGScenarioModel)) {
			return null;
		}
		final LNGScenarioModel scenarioModel = (LNGScenarioModel) rootObject;

		final EClass referenceClass = reference.getEReferenceType();
		final TypesPackage types = TypesPackage.eINSTANCE;

		if (referenceClass == types.getVesselAssignmentType()) {
			return new IReferenceValueProvider() {
				// @Override
				public List<Pair<String, EObject>> getAllowedValues(final EObject target, final EStructuralFeature field) {
					// get a list of globally permissible values

					final List<Pair<String, EObject>> vesselAvailabilityResult = new LinkedList<Pair<String, EObject>>();
					for (VesselAvailability vesselAvailability : adpModel.getFleetProfile().getVesselAvailabilities()) {
						if (vesselAvailability.getVessel() != null) {
							vesselAvailabilityResult.add(new Pair<>(vesselAvailability.getVessel().getName(), vesselAvailability));
						}
					}
					final List<Pair<String, EObject>> charterInMarketResult = new LinkedList<>();

					// determine the current value for the target object
					final VesselAssignmentType currentValue;
					{
						if (target instanceof ShippingOption) {
							ShippingOption shippingOption = (ShippingOption) target;
							currentValue = shippingOption.getVesselAssignmentType();
						} else {
							currentValue = null;
						}
					}

					// All scenario vessels - those we use to ship ourselves
					final Set<Vessel> scenarioVessels = new HashSet<>();
					for (final VesselAvailability va : adpModel.getFleetProfile().getVesselAvailabilities()) {
						scenarioVessels.add(va.getVessel());
					}

					boolean isVesselEvent = false;
					if (target instanceof VesselEvent) {
						isVesselEvent = true;
					}

					final boolean showThirdPartyVessels = !isVesselEvent;
					final boolean includeSpotVessels = !isVesselEvent;

					boolean noVesselsAllowed = false;
					final List<AVesselSet<Vessel>> allowedVessels = new ArrayList<>();
					// we only want to filter vessels if we are assigning them (i.e. don't do this if we are selecting vessels to add to the restricted list)
					if (field == CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE) {
						noVesselsAllowed = AssignmentEditorHelper.compileAllowedVessels(allowedVessels, target);
					}

					final Set<AVesselSet<Vessel>> expandedVessels = new HashSet<AVesselSet<Vessel>>();
					// filter the global list by the object's allowed values
					if (allowedVessels != null) {
						// Expand out VesselGroups
						for (final AVesselSet<Vessel> s : allowedVessels) {
							if (s instanceof Vessel) {
								expandedVessels.add(s);
							} else {
								// instanceof Vessel Group
								expandedVessels.addAll(SetUtils.getObjects(s));
							}
						}
					}

					// create list to populate
					final ArrayList<Pair<String, EObject>> result = new ArrayList<Pair<String, EObject>>();

					// filter the globally permissible values by the settings for this cargo
					for (final Pair<String, EObject> pair : vesselAvailabilityResult) {
						final VesselAvailability vesselAvailability = (VesselAvailability) pair.getSecond();
						if (vesselAvailability == null) {
							continue;
						}
						final Vessel vessel = vesselAvailability.getVessel();
						if (vessel == null) {
							continue;
						}

						boolean display = !noVesselsAllowed && (
						// show the option if the cargo allows this vessel-set
						// (an empty list of allowed vessels means "all vessels")
						expandedVessels.isEmpty() || expandedVessels.contains(vessel));

						// Filter out non-scenario vessels
						if (display) {
							if (vessel instanceof Vessel) {
								display = (scenarioVessels.contains(vessel)) || (showThirdPartyVessels && !scenarioVessels.contains(vessel));
							}
						}

						// Always show the option if the option is the null option
						// or the current value for the cargo is set to this vessel-set
						if (Equality.isEqual(vessel, currentValue) || (currentValue instanceof VesselAvailability && Equality.isEqual(vessel, ((VesselAvailability) currentValue).getVessel()))
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

							boolean display = !noVesselsAllowed && (
							// show the option if the cargo allows this vessel-set
							// (an empty list of allowed vessels means "all vessels")
							expandedVessels.isEmpty()
									// show the option if the cargo allows vessels of this class
									|| (vessel != null && expandedVessels.contains(vessel)));

							// Hide disable markets
							if (!charterInMarket.isEnabled() && !charterInMarket.isNominal()) {
								display = false;
							}
							
							// Always show the option if the option is the null option
							// or the current value for the cargo is set to this vessel-set
							if (Equality.isEqual(charterInMarket, currentValue) || charterInMarket == null) {
								display = true;
							}

							if (display) {
								result.add(new Pair<String, EObject>(pair.getFirst() + " (spot)", pair.getSecond()));
							}
						}}
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
				}

				protected Comparator<Pair<String, ?>> createComparator() {

					return new Comparator<Pair<String, ?>>() {
						@Override
						public int compare(final Pair<String, ?> o1, final Pair<String, ?> o2) {
							final Object v1 = o1.getSecond();
							final Object v2 = o2.getSecond();
							if (v1 instanceof CharterInMarketOverride) {
								if (!(v2 instanceof CharterInMarketOverride)) {
									return 1;
								}
							} else if (v1 instanceof CharterInMarket) {
								if (!(v2 instanceof CharterInMarket)) {
									return -1;
								}
							} else if (v1 instanceof VesselAvailability) {
								if (v2 instanceof CharterInMarket) {
									return -1;
								}
							} else if (v1 instanceof CharterInMarket) {
								if (v2 instanceof VesselAvailability) {
									return 1;
								}
							}
							// Sorting time charters after fleet
							if (v1 instanceof VesselAvailability && v2 instanceof VesselAvailability) {
								VesselAvailability vesselAvailability1 = (VesselAvailability) v1;
								VesselAvailability vesselAvailability2 = (VesselAvailability) v2;
								boolean isV1Charter = vesselAvailability1.isSetTimeCharterRate();
								boolean isV2Charter = vesselAvailability2.isSetTimeCharterRate();
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

					return false;
				}
			};
		}

		return null;
	}
}
