/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.util.Collections;
import java.util.LinkedList;
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
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.util.SetUtils;
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
			final List<Slot> restrictedSlots = slot.getRestrictedSlots();
			List<Contract> restrictedContracts = slot.getRestrictedContracts();
			List<APortSet<Port>> restrictedPortSets = slot.getRestrictedPorts();
			List<AVesselSet<Vessel>> restrictedVesselsSets= slot.getRestrictedVessels();

			boolean restrictedVesselsArePermissive = slot.isRestrictedVesselsArePermissive();
			boolean restrictedSlotsArePermissive = slot.isRestrictedSlotsArePermissive();
			boolean restrictedPortsArePermissive = slot.isRestrictedPortsArePermissive();
			boolean restrictedContractsArePermissive = slot.isRestrictedContractsArePermissive();

			final String name = slot.getName();

			if (slot instanceof SpotSlot) {
				SpotSlot spotSlot = (SpotSlot) slot;
				SpotMarket market = spotSlot.getMarket();
				if (market != null) {
					restrictedPortsArePermissive = market.isRestrictedPortsArePermissive();
					restrictedContractsArePermissive = market.isRestrictedContractsArePermissive();
					restrictedVesselsArePermissive = market.isRestrictedVesselsArePermissive();
					restrictedContracts = market.getRestrictedContracts();
					restrictedPortSets = new LinkedList<>(SetUtils.getObjects(market.getRestrictedPorts()));
					restrictedVesselsSets = new LinkedList<>(SetUtils.getObjects(market.getRestrictedVessels()));

					if (rootObject instanceof LNGScenarioModel && ((LNGScenarioModel) rootObject).getAdpModel() == null) {
						if (slot.eIsSet(CargoPackage.eINSTANCE.getSlot_RestrictedPortsArePermissive())) {
							addFail(CargoPackage.eINSTANCE.getSlot_RestrictedPortsArePermissive(), 
									String.format("%s: Spot market slot's port restrictions should not be changed!.", name), slot, ctx, statuses);
						}
						if (slot.eIsSet(CargoPackage.eINSTANCE.getSlot_RestrictedContractsArePermissive())) {
							addFail(CargoPackage.eINSTANCE.getSlot_RestrictedContractsArePermissive(),
									String.format("%s: Spot market slot's contracts restrictions should not be changed!.", name), slot, ctx, statuses);
						}
						if (slot.eIsSet(CargoPackage.eINSTANCE.getSlot_RestrictedVesselsArePermissive())) {
							addFail(CargoPackage.eINSTANCE.getSlot_RestrictedVesselsArePermissive(),
									String.format("%s: Spot market slot's vessels restrictions should not be changed!.", name), slot, ctx, statuses);
						}
					}
				} else {
					restrictedContracts = Collections.emptyList();
					restrictedPortSets = Collections.emptyList();
					restrictedVesselsSets = Collections.emptyList();
				}
			}

			if (restrictedContracts.isEmpty() && restrictedContractsArePermissive) {
				addFail(CargoPackage.eINSTANCE.getSlot_RestrictedContractsArePermissive(), 
						String.format("%s: Empty allowed contracts restriction list.", name), slot, ctx, statuses);
			}

			if (restrictedPortSets.isEmpty() && restrictedPortsArePermissive) {
				addFail(CargoPackage.eINSTANCE.getSlot_RestrictedPortsArePermissive(),
						String.format("%s: Empty allowed ports restriction list.", name), slot, ctx, statuses);
			}
			if (restrictedSlots.isEmpty() && restrictedSlotsArePermissive) {
				addFail(CargoPackage.eINSTANCE.getSlot_RestrictedSlotsArePermissive(),
						String.format("%s: Empty allowed slots list.", name), slot, ctx, statuses);
			}

			if (restrictedVesselsSets.isEmpty() && restrictedVesselsArePermissive) {
				addFail(CargoPackage.eINSTANCE.getSlot_RestrictedVesselsArePermissive(),
						String.format("%s: Empty allowed vessels list.", name), slot, ctx, statuses);
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
			}
		}
	}
	
	private void addFail( final EStructuralFeature feature,
			final String message, final Slot<?> slot, final IValidationContext ctx, final List<IStatus> statuses) {
		final DetailConstraintStatusDecorator d = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
		d.addEObjectAndFeature(slot, feature);
		statuses.add(d);
	}
}
