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
import com.mmxlabs.models.lng.analytics.PartialCase;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.SellReference;
import com.mmxlabs.models.lng.analytics.validation.internal.Activator;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class PartialCaseConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(@NonNull final IValidationContext ctx, @NonNull final IExtraValidationContext extraContext, @NonNull final List<IStatus> statuses) {

		final EObject target = ctx.getTarget();
		if (target instanceof PartialCase) {
			final PartialCase partialCase = (PartialCase) target;

			final Set<BuyOption> loadSlots = new HashSet<>();
			final Set<SellOption> dischargeSlots = new HashSet<>();

			final Set<BuyOption> duplicatedLoadSlots = new HashSet<>();
			final Set<SellOption> duplicatdDischargeSlots = new HashSet<>();

			// First pass, find problem slots
			processPartialCase(partialCase, (row, slot) -> {
				if (!loadSlots.add(slot)) {
					duplicatedLoadSlots.add(slot);
				}
			}, (row, slot) -> {
				if (!dischargeSlots.add(slot)) {
					duplicatdDischargeSlots.add(slot);
				}
			});
			// Second pass, report problem slots
			processPartialCase(partialCase, (row, slot) -> {
				if (duplicatedLoadSlots.contains(slot)) {
					final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("What if? - existing slot used multiple times."));
					deco.addEObjectAndFeature(row, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__BUY_OPTIONS);
					statuses.add(deco);
				}
			}, (row, slot) -> {
				if (duplicatdDischargeSlots.contains(slot)) {
					final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("What if? - existing slot used multiple times."));
					deco.addEObjectAndFeature(row, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SELL_OPTIONS);
					statuses.add(deco);
				}
			});
		}

		return Activator.PLUGIN_ID;
	}

	public void processPartialCase(final PartialCase partialCase, final BiConsumer<PartialCaseRow, BuyOption> visitLoadSlot, final BiConsumer<PartialCaseRow, SellOption> visitDischargeSlot) {
		for (final PartialCaseRow row : partialCase.getPartialCase()) {
			{
				for (final BuyOption buy : row.getBuyOptions()) {
					if (buy instanceof BuyReference) {
						visitLoadSlot.accept(row, buy);
					}
				}
			}
			{
				for (final SellOption sell : row.getSellOptions()) {
					if (sell instanceof SellReference) {
						visitDischargeSlot.accept(row, sell);
					}
				}
			}
		}
	}
}
