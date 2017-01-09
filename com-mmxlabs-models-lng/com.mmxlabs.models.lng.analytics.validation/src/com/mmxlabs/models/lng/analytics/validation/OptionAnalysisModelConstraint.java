/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.validation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.SellReference;
import com.mmxlabs.models.lng.analytics.validation.internal.Activator;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class OptionAnalysisModelConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(@NonNull final IValidationContext ctx, @NonNull final IExtraValidationContext extraContext, @NonNull final List<IStatus> statuses) {

		final EObject target = ctx.getTarget();
		if (target instanceof OptionAnalysisModel) {
			final OptionAnalysisModel model = (OptionAnalysisModel) target;

			// Check for duplicated existing slots.
			{
				final Set<LoadSlot> loadSlots = new HashSet<>();
				final Set<LoadSlot> duplicatedLoadSlots = new HashSet<>();

				final Set<DischargeSlot> dischargeSlots = new HashSet<>();
				final Set<DischargeSlot> duplicatdDischargeSlots = new HashSet<>();

				// First pass, find problem slots
				processModel(model, (buy, slot) -> {
					if (!loadSlots.add(slot)) {
						duplicatedLoadSlots.add(slot);
					}
				}, (sell, slot) -> {
					if (!dischargeSlots.add(slot)) {
						duplicatdDischargeSlots.add(slot);
					}
				});
				// Second pass, report problem slots
				processModel(model, (buy, slot) -> {
					if (duplicatedLoadSlots.contains(slot)) {
						final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
								(IConstraintStatus) ctx.createFailureStatus(String.format("Existing slot used multiple times (%s).", slot.getName())));
						deco.addEObjectAndFeature(buy, AnalyticsPackage.Literals.BUY_REFERENCE__SLOT);
						statuses.add(deco);
					}
				}, (sell, slot) -> {
					if (duplicatdDischargeSlots.contains(slot)) {
						final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
								(IConstraintStatus) ctx.createFailureStatus(String.format("Existing slot used multiple times (%s).", slot.getName())));
						deco.addEObjectAndFeature(sell, AnalyticsPackage.Literals.SELL_REFERENCE__SLOT);
						statuses.add(deco);
					}
				});

			}
		}

		return Activator.PLUGIN_ID;
	}

	public void processModel(final OptionAnalysisModel model, final BiConsumer<BuyOption, LoadSlot> visitLoadSlot, final BiConsumer<SellOption, DischargeSlot> visitDischargeSlot) {
		for (final BuyOption buy : model.getBuys()) {
			if (buy instanceof BuyReference) {
				final BuyReference buyReference = (BuyReference) buy;
				final LoadSlot slot = buyReference.getSlot();
				if (slot != null) {
					visitLoadSlot.accept(buy, slot);
				}
			}
		}
		for (final SellOption sell : model.getSells()) {
			if (sell instanceof SellReference) {
				final SellReference sellReference = (SellReference) sell;
				final DischargeSlot slot = sellReference.getSlot();
				if (slot != null) {
					visitDischargeSlot.accept(sell, slot);
				}
			}
		}
	}
}
