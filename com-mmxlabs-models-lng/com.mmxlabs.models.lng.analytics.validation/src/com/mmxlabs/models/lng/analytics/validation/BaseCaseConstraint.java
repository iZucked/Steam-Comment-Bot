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
import com.mmxlabs.models.lng.analytics.BaseCase;
import com.mmxlabs.models.lng.analytics.BaseCaseRow;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.SellReference;
import com.mmxlabs.models.lng.analytics.validation.internal.Activator;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class BaseCaseConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(@NonNull final IValidationContext ctx, @NonNull final IExtraValidationContext extraContext, @NonNull final List<IStatus> statuses) {

		final EObject target = ctx.getTarget();
		if (target instanceof BaseCase) {
			final BaseCase baseCase = (BaseCase) target;

			final Set<LoadSlot> loadSlots = new HashSet<>();
			final Set<DischargeSlot> dischargeSlots = new HashSet<>();

			final Set<LoadSlot> duplicatedLoadSlots = new HashSet<>();
			final Set<DischargeSlot> duplicatdDischargeSlots = new HashSet<>();

			// First pass, find problem slots
			processBaseCase(baseCase, (row, slot) -> {
				if (!loadSlots.add(slot)) {
					duplicatedLoadSlots.add(slot);
				}
			}, (row, slot) -> {
				if (!dischargeSlots.add(slot)) {
					duplicatdDischargeSlots.add(slot);
				}
			});
			// Second pass, report problem slots
			processBaseCase(baseCase, (row, slot) -> {
				if (duplicatedLoadSlots.contains(slot)) {
					final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Base case - existing slot used multiple times."));
					deco.addEObjectAndFeature(row, AnalyticsPackage.Literals.BASE_CASE_ROW__BUY_OPTION);
					statuses.add(deco);
				}
			}, (row, slot) -> {
				if (duplicatdDischargeSlots.contains(slot)) {
					final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Base case -existing slot used multiple times."));
					deco.addEObjectAndFeature(row, AnalyticsPackage.Literals.BASE_CASE_ROW__SELL_OPTION);
					statuses.add(deco);
				}
			});

		}

		return Activator.PLUGIN_ID;
	}

	public void processBaseCase(final BaseCase baseCase, final BiConsumer<BaseCaseRow, LoadSlot> visitLoadSlot, final BiConsumer<BaseCaseRow, DischargeSlot> visitDischargeSlot) {
		for (final BaseCaseRow row : baseCase.getBaseCase()) {
			{
				final BuyOption buy = row.getBuyOption();
				if (buy instanceof BuyReference) {
					final BuyReference buyReference = (BuyReference) buy;
					final LoadSlot slot = buyReference.getSlot();
					if (slot != null) {
						visitLoadSlot.accept(row, slot);
					}
				}
			}
			{
				final SellOption sell = row.getSellOption();
				if (sell instanceof SellReference) {
					final SellReference sellReference = (SellReference) sell;
					final DischargeSlot slot = sellReference.getSlot();
					if (slot != null) {
						visitDischargeSlot.accept(row, slot);
					}
				}
			}
		}
	}
}
