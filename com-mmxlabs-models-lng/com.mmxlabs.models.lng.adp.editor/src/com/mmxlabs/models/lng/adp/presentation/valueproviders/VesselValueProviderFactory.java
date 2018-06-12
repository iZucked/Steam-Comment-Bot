/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation.valueproviders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.FleetProfile;
import com.mmxlabs.models.lng.adp.ProfileVesselRestriction;
import com.mmxlabs.models.lng.adp.ShippingOption;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;

public class VesselValueProviderFactory implements IReferenceValueProviderFactory {

	public VesselValueProviderFactory() {
	}

	@Override
	public IReferenceValueProvider createReferenceValueProvider(final EClass owner, final EReference reference, final MMXRootObject theRootObject) {
		final MMXRootObject rootObject = theRootObject;
		if (!(rootObject instanceof LNGScenarioModel)) {
			return null;
		}
		final LNGScenarioModel scenarioModel = (LNGScenarioModel) rootObject;
		final EClass referenceClass = reference.getEReferenceType();

		if (referenceClass == FleetPackage.Literals.VESSEL) {
			return new IReferenceValueProvider() {
				@Override
				public List<Pair<String, EObject>> getAllowedValues(final EObject target, final EStructuralFeature field) {
					// get a list of globally permissible values

					final Set<Vessel> vessels = new HashSet<>();
					final ADPModel adpModel = getADPModel(target);
					if (adpModel != null) {
						final FleetProfile fleetProfile = adpModel.getFleetProfile();
						for (final VesselAvailability vesselAvailability : fleetProfile.getVesselAvailabilities()) {
							vessels.add(vesselAvailability.getVessel());
						}
						if (fleetProfile.isIncludeEnabledCharterMarkets()) {
							final SpotMarketsModel spotMarketsModel = ScenarioModelUtil.getSpotMarketsModel(scenarioModel);
							for (final CharterInMarket market : spotMarketsModel.getCharterInMarkets()) {
								if (market.isEnabled()) {
									vessels.add(market.getVessel());
								}
							}
						}
					}

					// determine the current value for the target object
					{
						if (target instanceof ShippingOption) {
							final ShippingOption shippingOption = (ShippingOption) target;
							vessels.add(shippingOption.getVessel());
						}
						if (target instanceof ProfileVesselRestriction) {
							final ProfileVesselRestriction profileVesselRestriction = (ProfileVesselRestriction) target;
							vessels.addAll(profileVesselRestriction.getVessels());
						}
					}
					final List<Pair<String, EObject>> result = new ArrayList<>();

					for (final Vessel vessel : vessels) {
						if (vessel == null) {
							continue;
						}
						result.add(new Pair<>(vessel.getName(), vessel));
					}

					Collections.sort(result, createComparator());
					if (!field.isMany()) {
						result.add(0, new Pair<String, EObject>("<Unassigned>", null));
					}

					return result;
				}

				@Override
				public String getName(final EObject referer, final EReference feature, final EObject referenceValue) {
					if (referenceValue instanceof NamedObject) {
						NamedObject namedObject = (NamedObject) referenceValue;
						return namedObject.getName();

					}
					return "<unknown object>";
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
							final Vessel v1 = (Vessel) o1.getSecond();
							final Vessel v2 = (Vessel) o2.getSecond();

							return v1.getName().compareTo(v2.getName());
						}

					};
				}

				@Override
				public boolean updateOnChangeToFeature(final Object changedFeature) {

					return reference == changedFeature;
				}
			};
		}

		return null;
	}

	private @Nullable ADPModel getADPModel(EObject target) {
		while (!(target instanceof ADPModel) && target != null) {
			target = target.eContainer();
		}
		if (target instanceof ADPModel) {
			return (ADPModel) target;
		}
		return null;
	}
}
