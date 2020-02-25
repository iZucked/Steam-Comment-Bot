/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.validation;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.FleetProfile;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.lng.types.util.ValidationConstants;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class FleetProfileConstraint extends AbstractModelMultiConstraint {

	@Override
	protected void doValidate(@NonNull final IValidationContext ctx, @NonNull final IExtraValidationContext extraContext, @NonNull final List<IStatus> statuses) {

		final EObject target = ctx.getTarget();

		if (target instanceof FleetProfile) {
			final FleetProfile profile = (FleetProfile) target;

			final DetailConstraintStatusFactory factory = DetailConstraintStatusFactory.makeStatus() //
					.withName("ADP Fleet profile") //
					.withTag(ValidationConstants.TAG_ADP);

			if (profile.getDefaultNominalMarket() == null) {
				factory.copyName() //
						.withObjectAndFeature(profile, ADPPackage.Literals.FLEET_PROFILE__DEFAULT_NOMINAL_MARKET) //
						.withMessage("No default vessel specified") //
						.make(ctx, statuses);
			} else {
				final Vessel vessel = profile.getDefaultNominalMarket().getVessel();
				if (vessel != null) {
					final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(extraContext.getScenarioDataProvider());

					final List<LoadSlot> badLoadSlots = new LinkedList<>();
					final List<DischargeSlot> badDischargeSlots = new LinkedList<>();

					for (final LoadSlot slot : cargoModel.getLoadSlots()) {
						if (!slot.getSlotOrDelegateVesselRestrictionsArePermissive()) {
							if (SetUtils.getObjects(slot.getSlotOrDelegateVesselRestrictions()).contains(vessel)) {
								badLoadSlots.add(slot);
							}

						}
					}
					for (final DischargeSlot slot : cargoModel.getDischargeSlots()) {
						if (!slot.getSlotOrDelegateVesselRestrictionsArePermissive()) {
							if (SetUtils.getObjects(slot.getSlotOrDelegateVesselRestrictions()).contains(vessel)) {
								badDischargeSlots.add(slot);
							}

						}
					}

					if (!badLoadSlots.isEmpty() || !badDischargeSlots.isEmpty()) {
						String loadMsg = null;
						if (!badLoadSlots.isEmpty()) {
							loadMsg = badLoadSlots.stream().map(Slot::getName).sorted().collect(Collectors.joining(", "));
						}
						String dischargeMsg = null;
						if (!badDischargeSlots.isEmpty()) {
							dischargeMsg = badDischargeSlots.stream().map(Slot::getName).sorted().collect(Collectors.joining(", "));
						}

						String msg;
						if (loadMsg == null) {
							msg = String.format("Sell(s) %s are forbidden to use the ADP vessel", dischargeMsg);
						} else if (dischargeMsg == null) {
							msg = String.format("Buy(s) %s are forbidden to use the ADP vessel", loadMsg);
						} else {
							msg = String.format("Buy(s) %s and sell(%s) are forbidden to use the ADP vessel", loadMsg, dischargeMsg);
						}

						final DetailConstraintStatusFactory dcsd = factory.copyName() //
								.withObjectAndFeature(profile, ADPPackage.Literals.FLEET_PROFILE__DEFAULT_NOMINAL_MARKET) //
								.withMessage(msg); //

						badLoadSlots.forEach(s -> dcsd.withObjectAndFeature(s, CargoPackage.Literals.SLOT__RESTRICTED_VESSELS));
						badDischargeSlots.forEach(s -> dcsd.withObjectAndFeature(s, CargoPackage.Literals.SLOT__RESTRICTED_VESSELS));

						dcsd.make(ctx, statuses);

					}

					for (final VesselAvailability vesselCharter : cargoModel.getVesselAvailabilities()) {
						if (vesselCharter.getVessel() == vessel) {
							factory.copyName() //
									.withObjectAndFeature(vesselCharter, CargoPackage.Literals.VESSEL_AVAILABILITY__VESSEL) //
									.withMessage("ADP vessel cannot be used for a charter") //
									.make(ctx, statuses);
						}
					}

					final SpotMarketsModel spotMarketsModel = ScenarioModelUtil.getSpotMarketsModel(extraContext.getScenarioDataProvider());

					for (final CharterInMarket charterInMarket : spotMarketsModel.getCharterInMarkets()) {
						if (charterInMarket == profile.getDefaultNominalMarket()) {
							continue;
						}

						if (charterInMarket.getVessel() == vessel) {
							factory.copyName() //
									.withObjectAndFeature(charterInMarket, SpotMarketsPackage.Literals.CHARTER_IN_MARKET__VESSEL) //
									.withMessage("ADP vessel cannot be used for charter markets") //
									.make(ctx, statuses);
						}
					}
				}

			}
		}
	}
}
