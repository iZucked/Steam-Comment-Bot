/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.validation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BuyOpportunity;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.analytics.PartialCase;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.lng.analytics.SellOpportunity;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.SellReference;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class PartialCaseConstraint extends AbstractModelMultiConstraint {

	public static final String viewName = "Options table";

	@Override
	protected void doValidate(@NonNull final IValidationContext ctx, @NonNull final IExtraValidationContext extraContext, @NonNull final List<IStatus> statuses) {

		final EObject target = ctx.getTarget();
		if (target instanceof @NonNull final PartialCase partialCase && SandboxConstraintUtils.shouldValidate(partialCase)) {

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
			}, (row, slot) -> {
			});

			final Set<PartialCaseRow> loadQuestion = new HashSet<>();
			final Set<PartialCaseRow> dischargeQuestion = new HashSet<>();

			// First pass, find problem slots
			processPartialCase(partialCase, (row, slot) -> {
				if (slot != null && slot instanceof final BuyOpportunity buyOpp) {
					final String priceExpression = buyOpp.getPriceExpression();
					if (priceExpression != null && priceExpression.contains("?")) {
						loadQuestion.add(row);
					}
				}
			}, (row, slot) -> {
				if (slot != null && slot instanceof final SellOpportunity sellOpp) {
					final String priceExpression = sellOpp.getPriceExpression();
					if (priceExpression != null && priceExpression.contains("?")) {
						dischargeQuestion.add(row);
					}
				}
			});

			// Second pass, report problem slots
			if (!loadQuestion.isEmpty() && !dischargeQuestion.isEmpty()) {
				// just add on distinct rows
				loadQuestion.addAll(dischargeQuestion);
				processPartialCaseRow(partialCase, row -> {
					if (loadQuestion.contains(row)) {
						final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
								(IConstraintStatus) ctx.createFailureStatus(String.format("%s - contains row(s) with a ? on a buy and load option", viewName)));
						deco.addEObjectAndFeature(row, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__PARTIAL_CASE);
						statuses.add(deco);
					}
				});
			}
		}
	}

	public static Predicate<BuyOption> isFOBPurchase() {
		return b -> ((b instanceof final BuyReference buyRef && buyRef.getSlot() != null && !buyRef.getSlot().isDESPurchase()) //
				|| (b instanceof final BuyOpportunity buyOpp && !buyOpp.isDesPurchase()));
	}

	public static Predicate<BuyOption> isDESPurchase() {
		return b -> ((b instanceof final BuyReference buyRef && buyRef.getSlot() != null && buyRef.getSlot().isDESPurchase()) //
				|| (b instanceof final BuyOpportunity buyOpp && buyOpp.isDesPurchase()));
	}

	public static Predicate<SellOption> isFOBSale() {
		return s -> ((s instanceof final SellReference sellRef && sellRef.getSlot() != null && sellRef.getSlot().isFOBSale()) //
				|| (s instanceof final SellOpportunity sellOpp && sellOpp.isFobSale()));
	}

	public static Predicate<SellOption> isDESSale() {
		return s -> ((s instanceof final SellReference sellRef && sellRef.getSlot() != null && !sellRef.getSlot().isFOBSale()) //
				|| (s instanceof final SellOpportunity sellOpp && !sellOpp.isFobSale()));
	}

	public void processPartialCase(final PartialCase partialCase, final BiConsumer<PartialCaseRow, BuyOption> visitLoadSlot, final BiConsumer<PartialCaseRow, SellOption> visitDischargeSlot) {
		for (final PartialCaseRow row : partialCase.getPartialCase()) {
			{
				for (final BuyOption buy : row.getBuyOptions()) {
					visitLoadSlot.accept(row, buy);
				}
			}
			{
				for (final SellOption sell : row.getSellOptions()) {
					visitDischargeSlot.accept(row, sell);
				}
			}
		}
	}

	public void processPartialCaseRow(final PartialCase partialCase, final Consumer<PartialCaseRow> rowFunction) {
		for (final PartialCaseRow row : partialCase.getPartialCase()) {
			rowFunction.accept(row);
		}
	}
}
