/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class EmptyRestrictionsConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {

		final EObject target = ctx.getTarget();
		if (target instanceof Slot) {
			final Slot slot = (Slot) target;
			checkSlot(ctx, slot, statuses, extraContext.getRootObject());
		}

		return Activator.PLUGIN_ID;
	}
	
	/**
	 * Check slot2 can be paired to slot1 based on slot1's restrictions
	 * 
	 * @param ctx
	 * @param slot1
	 * @param slot2
	 * @param cargoName
	 * @param statuses
	 */
	private void checkSlot(final IValidationContext ctx, final Slot slot, final List<IStatus> statuses, final MMXRootObject rootObject) {

		if (slot.eContainer() == null || slot.eContainer() instanceof CargoModel){
			final boolean rSlotsEmpty = slot.getRestrictedSlots().isEmpty();
			final boolean rContractsEmpty = slot.getRestrictedContracts().isEmpty();
			final boolean rPortsEmpty = slot.getRestrictedPorts().isEmpty();
			final boolean rVesselsEmpty = slot.getRestrictedVessels().isEmpty();

			boolean restrictedVesselsArePermissive = slot.isRestrictedVesselsArePermissive();
			boolean restrictedSlotsArePermissive = slot.isRestrictedSlotsArePermissive();
			boolean restrictedPortsArePermissive = slot.isRestrictedPortsArePermissive();
			boolean restrictedContractsArePermissive = slot.isRestrictedContractsArePermissive();

			final String name = slot.getName();

			if (slot instanceof SpotSlot) {
				SpotSlot spotSlot = (SpotSlot) slot;
				SpotMarket market = spotSlot.getMarket();
				if (market != null) {
					if (rootObject instanceof LNGScenarioModel && ((LNGScenarioModel) rootObject).getAdpModel() == null) {
						checkSpotMarketSlot(CargoPackage.eINSTANCE.getSlot_RestrictedPortsArePermissive(), ctx, slot, statuses, "ports", name);
						checkSpotMarketSlot(CargoPackage.eINSTANCE.getSlot_RestrictedContractsArePermissive(), ctx, slot, statuses, "contracts", name);
						checkSpotMarketSlot(CargoPackage.eINSTANCE.getSlot_RestrictedVesselsArePermissive(), ctx, slot, statuses, "vessels", name);
					}
				}
				return;
			}

			if (slot.isSetContract()) {
				if (slot.eIsSet(CargoPackage.eINSTANCE.getSlot_RestrictedPortsArePermissive()) && !slot.eIsSet(CargoPackage.eINSTANCE.getSlot_RestrictedPorts())
						|| !slot.eIsSet(CargoPackage.eINSTANCE.getSlot_RestrictedPortsArePermissive()) && slot.eIsSet(CargoPackage.eINSTANCE.getSlot_RestrictedPorts())) {
					addFail(CargoPackage.eINSTANCE.getSlot_RestrictedPorts(),
							String.format("%s: Both restiction ports list and type should be overriden.", name), slot, ctx, statuses);
				}

				if (slot.eIsSet(CargoPackage.eINSTANCE.getSlot_RestrictedContractsArePermissive()) && !slot.eIsSet(CargoPackage.eINSTANCE.getSlot_RestrictedContracts())
						|| !slot.eIsSet(CargoPackage.eINSTANCE.getSlot_RestrictedContractsArePermissive()) && slot.eIsSet(CargoPackage.eINSTANCE.getSlot_RestrictedContracts())) {
					addFail(CargoPackage.eINSTANCE.getSlot_RestrictedContracts(),
							String.format("%s: Both restriction contracts list and type should be overriden.", name), slot, ctx, statuses);
				}

				if (slot.eIsSet(CargoPackage.eINSTANCE.getSlot_RestrictedVesselsArePermissive()) && !slot.eIsSet(CargoPackage.eINSTANCE.getSlot_RestrictedVessels())
						|| !slot.eIsSet(CargoPackage.eINSTANCE.getSlot_RestrictedVesselsArePermissive()) && slot.eIsSet(CargoPackage.eINSTANCE.getSlot_RestrictedVessels())) {
					addFail(CargoPackage.eINSTANCE.getSlot_RestrictedVessels(),
							String.format("%s: Both restriction vessels list and type should be overriden.", name), slot, ctx, statuses);
				}
				
				if (slot.isRestrictedContractsOverride() && slot.eIsSet(CargoPackage.eINSTANCE.getSlot_RestrictedContractsArePermissive())) {
					checkForEmptyRestrictions(CargoPackage.eINSTANCE.getSlot_RestrictedContractsArePermissive(), ctx, slot, statuses, rContractsEmpty && restrictedContractsArePermissive, "contracts", name);
				}
				if (slot.isRestrictedPortsOverride() && slot.eIsSet(CargoPackage.eINSTANCE.getSlot_RestrictedPortsArePermissive())) {
					checkForEmptyRestrictions(CargoPackage.eINSTANCE.getSlot_RestrictedPortsArePermissive(), ctx, slot, statuses, rPortsEmpty && restrictedPortsArePermissive, "ports", name);
				}
				if (slot.eIsSet(CargoPackage.eINSTANCE.getSlot_RestrictedSlotsArePermissive())) {
					checkForEmptyRestrictions(CargoPackage.eINSTANCE.getSlot_RestrictedSlotsArePermissive(), ctx, slot, statuses, rSlotsEmpty && restrictedSlotsArePermissive, "slots", name);
				}
				if (slot.isRestrictedVesselsOverride() && slot.eIsSet(CargoPackage.eINSTANCE.getSlot_RestrictedVesselsArePermissive())) {
					checkForEmptyRestrictions(CargoPackage.eINSTANCE.getSlot_RestrictedVesselsArePermissive(), ctx, slot, statuses, rVesselsEmpty && restrictedVesselsArePermissive, "vessels", name);
				}
			} else {
				checkForEmptyRestrictions(CargoPackage.eINSTANCE.getSlot_RestrictedContractsArePermissive(), ctx, slot, statuses, rContractsEmpty && restrictedContractsArePermissive, "contracts", name);
				checkForEmptyRestrictions(CargoPackage.eINSTANCE.getSlot_RestrictedPortsArePermissive(), ctx, slot, statuses, rPortsEmpty && restrictedPortsArePermissive, "ports", name);
				checkForEmptyRestrictions(CargoPackage.eINSTANCE.getSlot_RestrictedSlotsArePermissive(), ctx, slot, statuses, rSlotsEmpty && restrictedSlotsArePermissive, "slots", name);
				checkForEmptyRestrictions(CargoPackage.eINSTANCE.getSlot_RestrictedVesselsArePermissive(), ctx, slot, statuses, rVesselsEmpty && restrictedVesselsArePermissive, "vessels", name);
			}
		}
	}

	private void checkSpotMarketSlot(final EStructuralFeature feature, final IValidationContext ctx, final Slot slot, final List<IStatus> statuses, final String messageSubject, final String name) {
		if (slot.eIsSet(feature)) {
			addFail(feature, String.format("%s: Spot market slot's %s restrictions should not be changed!.", messageSubject, name), slot, ctx, statuses);
		}
	}
	
	private void checkForEmptyRestrictions(final EStructuralFeature feature, final IValidationContext ctx, final Slot slot, final List<IStatus> statuses, final boolean fail, 
			final String messageSubject, final String name) {
		if (fail) {
			addFail(feature, String.format("%s: Empty allowed %s restriction list.", messageSubject, name), slot, ctx, statuses);
		}
	}
	
	private void addFail( final EStructuralFeature feature,
			final String message, final Slot<?> slot, final IValidationContext ctx, final List<IStatus> statuses) {
		final DetailConstraintStatusDecorator d = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
		d.addEObjectAndFeature(slot, feature);
		statuses.add(d);
	}
}
