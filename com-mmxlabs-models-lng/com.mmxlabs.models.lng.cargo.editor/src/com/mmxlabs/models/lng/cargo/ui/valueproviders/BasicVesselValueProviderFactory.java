/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.valueproviders;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.common.Equality;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.util.AssignmentEditorHelper;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.spotmarkets.CharterCostModel;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;
import com.mmxlabs.models.ui.valueproviders.MergedReferenceValueProvider;

public class BasicVesselValueProviderFactory implements IReferenceValueProviderFactory {
	@Override
	public IReferenceValueProvider createReferenceValueProvider(final EClass owner, final EReference reference, final MMXRootObject theRootObject) {
		final MMXRootObject rootObject = theRootObject;
		if (!(rootObject instanceof LNGScenarioModel)) {
			return null;
		}
		final LNGScenarioModel scenarioModel = (LNGScenarioModel) rootObject;

		final FleetModel fleetModel = scenarioModel.getFleetModel();
		final CargoModel cargoModel = scenarioModel.getPortfolioModel().getCargoModel();
		final EClass referenceClass = reference.getEReferenceType();
		final TypesPackage types = TypesPackage.eINSTANCE;
		final FleetPackage fleet = FleetPackage.eINSTANCE;

		if (referenceClass == types.getAVesselSet()) {
			return new MergedReferenceValueProvider(fleetModel, fleet.getFleetModel_VesselClasses(), fleet.getFleetModel_Vessels()) {
				// @Override
				public List<Pair<String, EObject>> getAllowedValues(EObject target, final EStructuralFeature field) {
					// get a list of globally permissible values
					final List<Pair<String, EObject>> baseResult = super.getAllowedValues(target, field);

					// All scenario vessels - those we use to ship ourselves
					final Set<Vessel> scenarioVessels = new HashSet<>();
					for (final VesselAvailability va : cargoModel.getVesselAvailabilities()) {
						scenarioVessels.add(va.getVessel());
					}

					// determine the current value for the target object
					final AVesselSet<? extends Vessel> currentValue;
					{
						if (target instanceof AssignableElement) {
							final AssignableElement assignment = (AssignableElement) target;
							currentValue = assignment.getAssignment();
						} else {
							currentValue = null;
						}
					}

					if (target instanceof Cargo) {
						final Cargo cargo = (Cargo) target;
						for (final Slot s : cargo.getSlots()) {
							if (s instanceof LoadSlot) {
								final LoadSlot loadSlot = (LoadSlot) s;
								if (loadSlot.isDESPurchase()) {
									target = loadSlot;
								}
							} else if (s instanceof DischargeSlot) {
								final DischargeSlot dischargeSlot = (DischargeSlot) s;
								if (dischargeSlot.isFOBSale()) {
									target = dischargeSlot;
								}
							}
						}
					}
					boolean includeSpotVessels = true;

					if (target instanceof VesselEvent) {
						includeSpotVessels = false;

					}

					boolean isThirdPartyCargo = false;
					if (target instanceof LoadSlot) {
						isThirdPartyCargo = ((LoadSlot) target).isDESPurchase();
					} else if (target instanceof DischargeSlot) {
						isThirdPartyCargo = ((DischargeSlot) target).isFOBSale();
					}

					boolean showFleetVessels = true;
					boolean showThirdPartyVessels = false;

					if (isThirdPartyCargo) {
						// FOB/DES cargoes should only permit third party vessels.
						showFleetVessels = false;
						showThirdPartyVessels = true;
					} else {

						// Shipped cargoes can only use fleet vessels. Note: the allowed list can contain third party vessels for fob/des conversion.
						showFleetVessels = true;
						showThirdPartyVessels = field == CargoPackage.Literals.SLOT__ALLOWED_VESSELS;
					}

					boolean includeVesselClasses = field == CargoPackage.Literals.SLOT__ALLOWED_VESSELS || (field == CargoPackage.Literals.VESSEL_EVENT__ALLOWED_VESSELS && target instanceof CharterOutEvent);

					boolean noVesselsAllowed = false;
					final List<AVesselSet<Vessel>> allowedVessels = new ArrayList<>();
					// we only want to filter vessels if we are assigning them (i.e. don't do this if we are selecting vessels to add to the restricted list)
					if (field == CargoPackage.Literals.ASSIGNABLE_ELEMENT__ASSIGNMENT) {
						noVesselsAllowed = AssignmentEditorHelper.compileAllowedVessels(allowedVessels, target);
					}

					final Set<AVesselSet<Vessel>> expandedVessels = new HashSet<AVesselSet<Vessel>>();
					// filter the global list by the object's allowed values
					if (allowedVessels != null) {

						// Expand out VesselGroups
						for (final AVesselSet<Vessel> s : allowedVessels) {
							if (s instanceof Vessel) {
								expandedVessels.add(s);
							} else if (s instanceof VesselClass) {
								expandedVessels.add(s);
							} else {
								expandedVessels.addAll(SetUtils.getObjects(s));
							}
						}
					}

					final Set<VesselClass> availableSpotVesselClasses = new HashSet<>();
					if (includeSpotVessels) {
						final SpotMarketsModel spotMarketsModel = scenarioModel.getSpotMarketsModel();
						if (spotMarketsModel != null) {
							for (final CharterCostModel charteringSpotMarket : spotMarketsModel.getCharteringSpotMarkets()) {
								if (charteringSpotMarket.isEnabled()) {
									if (charteringSpotMarket.getSpotCharterCount() > 0) {
										availableSpotVesselClasses.addAll(charteringSpotMarket.getVesselClasses());
									}
								}
							}
						}
					}

					final Set<VesselClass> availableVesselClasses = new HashSet<>(fleetModel.getVesselClasses());

					// create list to populate
					final ArrayList<Pair<String, EObject>> result = new ArrayList<Pair<String, EObject>>();

					// filter the globally permissible values by the settings for this cargo
					for (final Pair<String, EObject> pair : baseResult) {
						final EObject vessel = pair.getSecond();

						// determine the vessel class (if any) for this option
						VesselClass vc = null;
						if (vessel instanceof Vessel) {
							vc = ((Vessel) vessel).getVesselClass();
						}
						boolean display = !noVesselsAllowed && (
						// show the option if the cargo allows this vessel-set
						// (an empty list of allowed vessels means "all vessels")
								expandedVessels.isEmpty() || expandedVessels.contains(vessel)
								// show the option if the cargo allows vessels of this class
								|| (vc != null && expandedVessels.contains(vc)));

						// Filter out non-scenario vessels
						if (display) {
							if (vessel instanceof Vessel) {
								display = (showFleetVessels && scenarioVessels.contains(vessel)) || (showThirdPartyVessels && !scenarioVessels.contains(vessel));
							} else if (vessel instanceof VesselClass) {
								display = (showFleetVessels && availableSpotVesselClasses.contains(vessel)) || (includeVesselClasses && availableVesselClasses.contains(vessel));
							}
						}

						// Always show the option if the option is the null option
						// or the current value for the cargo is set to this vessel-set
						if (Equality.isEqual(vessel, currentValue) || vessel == null) {
							display = true;
						}

						if (display) {
							result.add(new Pair<>(getName(target,(EReference) field, pair.getSecond()), pair.getSecond()));
						}
					}

					return result;
				}

				@Override
				protected Pair<String, EObject> getEmptyObject() {
					return new Pair<String, EObject>("<Unassigned>", null);
				}

				@Override
				public String getName(final EObject referer, final EReference feature, final EObject referenceValue) {
					String spotStr = (feature == CargoPackage.Literals.SLOT__ALLOWED_VESSELS || feature == CargoPackage.Literals.VESSEL_EVENT__ALLOWED_VESSELS) ? "": " (spot)" ;
					return super.getName(referer, feature, referenceValue) + ((referenceValue instanceof VesselClass) ? spotStr : "");
				}

				@Override
				protected Comparator<Pair<String, ?>> createComparator() {
					final Comparator superValue = super.createComparator();

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
							// Sorting time charters after fleet
							if (v1 instanceof Vessel && v2 instanceof Vessel) {
								boolean isV1Charter = false;
								boolean isV2Charter = false;
								for (final VesselAvailability va : cargoModel.getVesselAvailabilities()) {
									Vessel v = va.getVessel();
									if (v == v1) {
										isV1Charter = va.isSetTimeCharterRate();
									} else if (v == v2) {
										isV2Charter = va.isSetTimeCharterRate();
									}
								}
								if (!isV1Charter && isV2Charter) {
									return -1;
								} else if (isV1Charter && !isV2Charter) {
									return 1;
								}
							}
							return superValue.compare(o1, o2);
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

					return super.updateOnChangeToFeature(changedFeature);
				}

			};
		}

		return null;
	}
}
