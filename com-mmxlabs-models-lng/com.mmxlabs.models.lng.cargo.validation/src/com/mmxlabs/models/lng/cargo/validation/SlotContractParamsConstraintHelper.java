/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.SlotContractParams;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class SlotContractParamsConstraintHelper {

	private SlotContractParamsConstraintHelper() {

	}

	public static <U extends SlotContractParams> void checkBasicContractParams(@NonNull final IValidationContext ctx, @NonNull final IExtraValidationContext extraContext,
			@NonNull final List<IStatus> failures, @NonNull final Slot slot, @NonNull final String contractName, @Nullable final Class<U> slotParamsClass, final boolean requireSlotParams,
			final boolean forbidPricingExpression, final boolean forbidPricingEvent) {
		if (requireSlotParams && slotParamsClass != null) {
			final SlotContractParams p = findSlotContractExtension(slot, slotParamsClass);
			if (p == null) {
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
						(IConstraintStatus) ctx.createFailureStatus(String.format("No %s contract params found for slot %s. Try swapping contracts to create them.", contractName, slot.getName())));
				dsd.addEObjectAndFeature(slot, CargoPackage.Literals.SLOT__CONTRACT);
				failures.add(dsd);
			}
		}
		if (forbidPricingExpression) {
			if (slot.isSetPriceExpression()) {
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
						(IConstraintStatus) ctx.createFailureStatus(String.format("Slot|%s Expression cannot be used with %s contracts.", contractName, slot.getName())));
				dsd.addEObjectAndFeature(slot, CargoPackage.Literals.SLOT__PRICE_EXPRESSION);
				dsd.addEObjectAndFeature(slot, CargoPackage.Literals.SLOT__CONTRACT);
				failures.add(dsd);
			}
		}
		if (forbidPricingEvent) {
			if (slot.isSetPricingDate()) {
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
						(IConstraintStatus) ctx.createFailureStatus(String.format("Slot|%s Pricing Date cannot be used with %s contracts.", contractName, slot.getName())));
				dsd.addEObjectAndFeature(slot, CargoPackage.Literals.SLOT__PRICING_DATE);
				dsd.addEObjectAndFeature(slot, CargoPackage.Literals.SLOT__CONTRACT);
				failures.add(dsd);
			}
			pricingEventConstraint(ctx, extraContext, failures, slot, contractName);
		}

	}

	public static <U extends SlotContractParams> void checkDefaultEntity(@NonNull final IValidationContext ctx, @NonNull final IExtraValidationContext extraContext,
			@NonNull final List<IStatus> failures, @NonNull final Slot slot, @NonNull final String contractName) {

		if (slot.isSetEntity()) {
			final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
					(IConstraintStatus) ctx.createFailureStatus(String.format("Slot|%s Entity cannot be set with %s contracts.", contractName, slot.getName())));
			dsd.addEObjectAndFeature(slot, CargoPackage.Literals.SLOT__ENTITY);
			dsd.addEObjectAndFeature(slot, CargoPackage.Literals.SLOT__CONTRACT);
			failures.add(dsd);
		}

	}

	public static <U extends SlotContractParams> void checkForFOBSaleNominatedVessel(@NonNull final IValidationContext ctx, @NonNull final IExtraValidationContext extraContext,
			@NonNull final List<IStatus> failures, @NonNull final LoadSlot loadSlot, @NonNull final String contractName, @NonNull final Class<U> slotParamsClass) {

		final Cargo cargo = loadSlot.getCargo();
		if (cargo != null) {
			for (final Slot slot : cargo.getSlots()) {
				if (slot instanceof DischargeSlot dischargeSlot) {
					if (dischargeSlot.isFOBSale()) {
						if (dischargeSlot.getNominatedVessel() == null) {
							final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
									(IConstraintStatus) ctx.createFailureStatus(String.format("Slot|%s FOB Sales linked to %s purchases need a nominated vessel.", loadSlot.getName(), contractName)));
							dsd.addEObjectAndFeature(loadSlot, CargoPackage.Literals.SLOT__CONTRACT);
							dsd.addEObjectAndFeature(dischargeSlot, CargoPackage.Literals.SLOT__NOMINATED_VESSEL);
							failures.add(dsd);
						}
					}

				}
			}
		}

	}

	public static @Nullable <T> T findSlotContractExtension(@NonNull final Slot<?> slot, @NonNull final Class<T> cls) {
		for (final EObject object : slot.getExtensions()) {
			if (cls.isInstance(object)) {
				return cls.cast(object);
			}
		}
		return null;
	}

	public static void pricingEventConstraint(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures, final Slot slot, final String contractName) {
		if (slot.isSetPricingEvent()) {
			final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
					(IConstraintStatus) ctx.createFailureStatus(String.format("Slot|%s Pricing Event must be unset with %s contracts.", slot.getName(), contractName)));
			dsd.addEObjectAndFeature(slot, CargoPackage.Literals.SLOT__PRICING_EVENT);
			failures.add(dsd);
		}
	}
}
