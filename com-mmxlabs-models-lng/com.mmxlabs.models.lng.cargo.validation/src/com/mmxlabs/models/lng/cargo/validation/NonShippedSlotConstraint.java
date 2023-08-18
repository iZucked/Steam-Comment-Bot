/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioElementNameHelper;
import com.mmxlabs.models.lng.types.DESPurchaseDealType;
import com.mmxlabs.models.lng.types.FOBSaleDealType;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class NonShippedSlotConstraint extends AbstractModelMultiConstraint {

	@Override
	protected void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {

		final EObject target = ctx.getTarget();
		if (target instanceof LoadSlot loadSlot) {
			if (loadSlot.isDESPurchase()
					&& (loadSlot.getSlotOrDelegateDESPurchaseDealType() == DESPurchaseDealType.DEST_WITH_SOURCE || loadSlot.getSlotOrDelegateDESPurchaseDealType() == DESPurchaseDealType.DIVERTIBLE)) {
				String type = "DES Purchase";
				String name = loadSlot.getName();
				if (loadSlot.getSlotOrDelegateContractRestrictions().isEmpty() && loadSlot.getSlotOrDelegatePortRestrictions().isEmpty()) {
					final String message = String.format("%s %s| Port or contract restriction must be set with this DES type.", type, name);
					final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
					failure.addEObjectAndFeature(target, CargoPackage.Literals.LOAD_SLOT__DES_PURCHASE_DEAL_TYPE);
					failure.addEObjectAndFeature(target, CargoPackage.Literals.SLOT__RESTRICTED_CONTRACTS);
					failure.addEObjectAndFeature(target, CargoPackage.Literals.SLOT__RESTRICTED_PORTS);

					statuses.add(failure);
				}
				final Vessel nominatedVessel = loadSlot.getNominatedVessel();
				if (nominatedVessel != null) {
					final Set<Vessel> vesselRestrictions = SetUtils.getObjects(loadSlot.getSlotOrDelegateVesselRestrictions());
					final boolean arePermissive = loadSlot.getSlotOrDelegateVesselRestrictionsArePermissive();
					final boolean vesselContainedInRestrictions = vesselRestrictions.contains(nominatedVessel);
					if (arePermissive && !vesselContainedInRestrictions) {
						final String message = String.format("%s %s| %s is not in the allowed vessels.", type, name, ScenarioElementNameHelper.getName(nominatedVessel, "<Unknown vessel>"));
						final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
						failure.addEObjectAndFeature(target, CargoPackage.Literals.SLOT__NOMINATED_VESSEL);
						statuses.add(failure);
					} else if (!arePermissive && vesselContainedInRestrictions) {
						final String message = String.format("%s %s| %s is not allowed to be nominated.", type, name, ScenarioElementNameHelper.getName(nominatedVessel, "<Unknown vessel>"));
						final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
						failure.addEObjectAndFeature(target, CargoPackage.Literals.SLOT__NOMINATED_VESSEL);
						statuses.add(failure);
					}
					final Cargo cargo = loadSlot.getCargo();
					if (cargo != null) {
						final Optional<Slot<?>> optOtherSlot = cargo.getSlots().stream().filter(s -> s != loadSlot).findAny();
						if (optOtherSlot.isPresent()) {
							final Slot<?> otherSlot = optOtherSlot.get();
							final Set<Vessel> otherVesselRestrictions = SetUtils.getObjects(otherSlot.getSlotOrDelegateVesselRestrictions());
							final boolean otherVesselRestrictionsArePermissive = otherSlot.getSlotOrDelegateVesselRestrictionsArePermissive();
							final boolean vesselContainedInOtherVesselRestrictions = otherVesselRestrictions.contains(nominatedVessel);
							if (otherVesselRestrictionsArePermissive && !vesselContainedInOtherVesselRestrictions) {
								final String message = String.format("%s| Nominated vessel %s is not in the allowed vessels.", ScenarioElementNameHelper.getName(otherSlot, "<Unknown discharge slot>"),
										ScenarioElementNameHelper.getName(nominatedVessel, "<Unknown vessel>"));
								final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
								failure.addEObjectAndFeature(target, CargoPackage.Literals.SLOT__NOMINATED_VESSEL);
								statuses.add(failure);
							} else if (!otherVesselRestrictionsArePermissive && vesselContainedInOtherVesselRestrictions) {
								final String message = String.format("%s| %s is not allowed to be nominated.", ScenarioElementNameHelper.getName(otherSlot, "<Unknown discharge slot>"),
										ScenarioElementNameHelper.getName(nominatedVessel, "<Unknown vessel>"));
								final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
								failure.addEObjectAndFeature(target, CargoPackage.Literals.SLOT__NOMINATED_VESSEL);
								statuses.add(failure);
							}
						}
					}
				}
			}
		} else if (target instanceof DischargeSlot dischargeSlot) {
			if (dischargeSlot.isFOBSale() && dischargeSlot.getSlotOrDelegateFOBSaleDealType() == FOBSaleDealType.SOURCE_WITH_DEST) {
				String type = "FOB Sale";
				String name = dischargeSlot.getName();
				if (dischargeSlot.getSlotOrDelegateContractRestrictions().isEmpty() && dischargeSlot.getSlotOrDelegatePortRestrictions().isEmpty()) {
					final String message = String.format("%s %s| Port or contract restriction must be set with this FOB type.", type, name);
					final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
					failure.addEObjectAndFeature(target, CargoPackage.Literals.DISCHARGE_SLOT__FOB_SALE_DEAL_TYPE);
					failure.addEObjectAndFeature(target, CargoPackage.Literals.SLOT__RESTRICTED_CONTRACTS);
					failure.addEObjectAndFeature(target, CargoPackage.Literals.SLOT__RESTRICTED_PORTS);

					statuses.add(failure);
				}
				final Vessel nominatedVessel = dischargeSlot.getNominatedVessel();
				if (nominatedVessel != null) {
					final Set<Vessel> vesselRestrictions = SetUtils.getObjects(dischargeSlot.getSlotOrDelegateVesselRestrictions());
					final boolean arePermissive = dischargeSlot.getSlotOrDelegateVesselRestrictionsArePermissive();
					final boolean vesselContainedInRestrictions = vesselRestrictions.contains(nominatedVessel);
					if (arePermissive && !vesselContainedInRestrictions) {
						final String message = String.format("%s %s| %s is not in the allowed vessels.", type, name, ScenarioElementNameHelper.getName(nominatedVessel, "<Unknown vessel>"));
						final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
						failure.addEObjectAndFeature(target, CargoPackage.Literals.SLOT__NOMINATED_VESSEL);
						statuses.add(failure);
					} else if (!arePermissive && vesselContainedInRestrictions) {
						final String message = String.format("%s %s| %s is not allowed to be nominated.", type, name, ScenarioElementNameHelper.getName(nominatedVessel, "<Unknown vessel>"));
						final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
						failure.addEObjectAndFeature(target, CargoPackage.Literals.SLOT__NOMINATED_VESSEL);
						statuses.add(failure);
					}
					final Cargo cargo = dischargeSlot.getCargo();
					if (cargo != null) {
						final Optional<Slot<?>> optOtherSlot = cargo.getSlots().stream().filter(s -> s != dischargeSlot).findAny();
						if (optOtherSlot.isPresent()) {
							final Slot<?> otherSlot = optOtherSlot.get();
							final Set<Vessel> otherVesselRestrictions = SetUtils.getObjects(otherSlot.getSlotOrDelegateVesselRestrictions());
							final boolean otherVesselRestrictionsArePermissive = otherSlot.getSlotOrDelegateVesselRestrictionsArePermissive();
							final boolean vesselContainedInOtherRestrictions = otherVesselRestrictions.contains(nominatedVessel);
							if (otherVesselRestrictionsArePermissive && !vesselContainedInOtherRestrictions) {
								final String message = String.format("%s| Nominated vessel %s is not in the allowed vessels.", ScenarioElementNameHelper.getName(otherSlot, "<Unknown load slot>"),
										ScenarioElementNameHelper.getName(nominatedVessel, "<Unknown vessel>"));
								final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
								failure.addEObjectAndFeature(target, CargoPackage.Literals.SLOT__NOMINATED_VESSEL);
								statuses.add(failure);
							} else if (!otherVesselRestrictionsArePermissive && vesselContainedInOtherRestrictions) {
								final String message = String.format("%s| %s is not allowed to be nominated.", ScenarioElementNameHelper.getName(otherSlot, "<Unknown load slot>"),
										ScenarioElementNameHelper.getName(nominatedVessel, "<Unknown vessel>"));
								final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
								failure.addEObjectAndFeature(target, CargoPackage.Literals.SLOT__NOMINATED_VESSEL);
								statuses.add(failure);
							}
						}
					}
				}
			}
		}
	}
}
