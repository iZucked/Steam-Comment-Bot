/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * A version of the {@link NameUniquenessConstraint} for Slots. We do not want
 * duplicate ids, but we can allow duplicates for spot slots.
 * 
 */
public class SlotNameUniquenessConstraint extends AbstractModelMultiConstraint {

	public static final String KEY_SLOT_NAME_UNIQUENESS = SlotNameUniquenessConstraint.class.getCanonicalName();

	private Collection<Type> getSlotTypes(final Slot slot) {
		if (slot instanceof LoadSlot) {
			final LoadSlot loadSlot = (LoadSlot) slot;
			if (slot instanceof SpotSlot) {
				if (loadSlot.isDESPurchase()) {
					return Collections.singleton(Type.SPOT_DES_PURCHASE);
				} else {
					return Collections.singleton(Type.SPOT_FOB_PURCHASE);
				}
			}
			return CollectionsUtil.makeHashSet(Type.LOAD, Type.SPOT_DES_PURCHASE, Type.SPOT_FOB_PURCHASE);
		} else if (slot instanceof DischargeSlot) {
			final DischargeSlot dischargeSlot = (DischargeSlot) slot;
			if (slot instanceof SpotSlot) {
				if (dischargeSlot.isFOBSale()) {
					return Collections.singleton(Type.SPOT_FOB_SALE);
				} else {
					return Collections.singleton(Type.SPOT_DES_SALE);
				}
			}
			return CollectionsUtil.makeHashSet(Type.DISCHARGE, Type.SPOT_DES_SALE, Type.SPOT_FOB_SALE);
		}
		throw new IllegalArgumentException();
	}

	enum Type {
		LOAD, DISCHARGE, SPOT_DES_PURCHASE, SPOT_FOB_PURCHASE, SPOT_DES_SALE, SPOT_FOB_SALE;
	}

	@Override
	public void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();
		if (target instanceof final Slot<?> slot) {
			final boolean isPurchase = slot instanceof LoadSlot;
			final EObject container = extraContext.getContainer(target);
			if (container instanceof final CargoModel cargoModel) {

				Map<Type, Set<String>> badNames = (Map<Type, Set<String>>) ctx.getCurrentConstraintData();
				if (badNames == null) {
					badNames = new HashMap<>();
					ctx.putCurrentConstraintData(badNames);
				}

				// STILL A PROBLEM
				// 1). We only generate slot type!
				// 2). Not easy to do all the tyes as0

				final Collection<Type> slotTypes = getSlotTypes(slot);
				for (final Type slotType : slotTypes) {
					Set<String> bad = badNames.get(slotType);
					if (bad == null) {
						bad = new HashSet<String>();
						badNames.put(slotType, bad);
						final List<EObject> objects = extraContext.getSiblings(target);
						objects.addAll(isPurchase ? cargoModel.getDischargeSlots() : cargoModel.getLoadSlots());

						final Set<String> temp = new HashSet<>();
						for (final EObject no : objects) {
							if (no instanceof Slot) {
								final Slot slot2 = (Slot) no;
								/**
								 * All slots should have unique name! Otherwise we can see some wiring glitches!
								 * 
								 * if (getSlotTypes(slot2).contains(Type.LOAD) ||
								 * getSlotTypes(slot2).contains(Type.DISCHARGE)) {
								 * 
								 */
								final String n = slot2.getName();
								if (temp.contains(n)) {
									bad.add(n);
								}
								temp.add(n);
							}
						}
					}

					final String name = slot.getName();
					if (bad.contains(name)) {
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
								(IConstraintStatus) ctx.createFailureStatus(target.eClass().getName() + " has non-unique name " + name));
						dsd.addEObjectAndFeature(target, MMXCorePackage.Literals.NAMED_OBJECT__NAME);
						dsd.setConstraintKey(KEY_SLOT_NAME_UNIQUENESS);
						statuses.add(dsd);
						break;
					}
				}
			}
		}
	}

}
