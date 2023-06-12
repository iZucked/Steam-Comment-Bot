/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
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
import com.mmxlabs.models.lng.analytics.BaseCase;
import com.mmxlabs.models.lng.analytics.BaseCaseRow;
import com.mmxlabs.models.lng.analytics.BuyOpportunity;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.SellOpportunity;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.SellReference;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.AnalyticsBuilder;
import com.mmxlabs.models.lng.analytics.util.SandboxModeConstants;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class BaseCaseConstraint extends AbstractModelMultiConstraint {

	@Override
	protected void doValidate(@NonNull final IValidationContext ctx, @NonNull final IExtraValidationContext extraContext, @NonNull final List<IStatus> statuses) {

		final EObject target = ctx.getTarget();
		if (target instanceof final BaseCase baseCase) {
			// Break-even check in optimise mode
			if (extraContext.getContainer(baseCase) instanceof final OptionAnalysisModel model && model.getMode() == SandboxModeConstants.MODE_OPTIMISE) {
				final Set<Object> seenSlots = new HashSet<>();
				for (final BaseCaseRow row : baseCase.getBaseCase()) {
					if (row.getBuyOption() instanceof final BuyOpportunity buy) {
						final String e = buy.getPriceExpression();
						if (e != null && e.contains("?")) {
							final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
									(IConstraintStatus) ctx.createFailureStatus("Sandbox|Base case - Cannot optimise with a break-even position"));
							deco.addEObjectAndFeature(row, AnalyticsPackage.Literals.BASE_CASE_ROW__BUY_OPTION);
							statuses.add(deco);
						}
					}
					if (row.getBuyOption() instanceof final BuyReference buy) {
						final String e = buy.getSlot() == null ? null : buy.getSlot().getPriceExpression();
						if (e != null && e.contains("?")) {
							seenSlots.add(buy.getSlot());
							final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
									(IConstraintStatus) ctx.createFailureStatus("Sandbox|Base case - Cannot optimise with a break-even position"));
							deco.addEObjectAndFeature(row, AnalyticsPackage.Literals.BASE_CASE_ROW__BUY_OPTION);
							statuses.add(deco);
						}
					}
					if (row.getSellOption() instanceof final SellOpportunity sell) {
						final String e = sell.getPriceExpression();
						if (e != null && e.contains("?")) {
							final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
									(IConstraintStatus) ctx.createFailureStatus("Sandbox|Base case - Cannot optimise with a break-even position"));
							deco.addEObjectAndFeature(row, AnalyticsPackage.Literals.BASE_CASE_ROW__SELL_OPTION);
							statuses.add(deco);
						}
					}
					if (row.getSellOption() instanceof final SellReference sell) {
						final String e = sell.getSlot() == null ? null : sell.getSlot().getPriceExpression();
						if (e != null && e.contains("?")) {
							seenSlots.add(sell.getSlot());
							final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
									(IConstraintStatus) ctx.createFailureStatus("Sandbox|Base case - Cannot optimise with a break-even position"));
							deco.addEObjectAndFeature(row, AnalyticsPackage.Literals.BASE_CASE_ROW__SELL_OPTION);
							statuses.add(deco);
						}
					}
				}
				// Also check the base case
				if (model.getBaseCase().isKeepExistingScenario()) {
					final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(extraContext.getScenarioDataProvider());
					for (final var s : cargoModel.getLoadSlots()) {
						if (seenSlots.contains(s)) {
							continue;
						}
						final String e = s.getPriceExpression();
						if (e != null && e.contains("?")) {
							final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
									(IConstraintStatus) ctx.createFailureStatus("Sandbox|Base case - Cannot optimise with a break-even position (" + s.getName() + ") from base case"));
							deco.addEObjectAndFeature(baseCase, AnalyticsPackage.Literals.BASE_CASE__KEEP_EXISTING_SCENARIO);
							statuses.add(deco);
						}
					}
					for (final var s : cargoModel.getDischargeSlots()) {
						if (seenSlots.contains(s)) {
							continue;
						}
						final String e = s.getPriceExpression();
						if (e != null && e.contains("?")) {
							final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
									(IConstraintStatus) ctx.createFailureStatus("Sandbox|Base case - Cannot optimise with a break-even position (" + s.getName() + ")  from base case"));
							deco.addEObjectAndFeature(baseCase, AnalyticsPackage.Literals.BASE_CASE__KEEP_EXISTING_SCENARIO);
							statuses.add(deco);
						}
					}
				}
			}

			// Check for duplicated existing slots.
			{
				final Set<BuyOption> loadSlots = new HashSet<>();
				final Set<SellOption> dischargeSlots = new HashSet<>();

				final Set<BuyOption> duplicatedLoadSlots = new HashSet<>();
				final Set<SellOption> duplicatedDischargeSlots = new HashSet<>();

				// First pass, find problem slots
				processBaseCase(baseCase, (row, slot) -> {
					if (!loadSlots.add(slot)) {
						duplicatedLoadSlots.add(slot);
					}
				}, (row, slot) -> {
					if (!dischargeSlots.add(slot)) {
						duplicatedDischargeSlots.add(slot);
					}
				});
				// Second pass, report problem slots
				processBaseCase(baseCase, (row, slot) -> {
					if (duplicatedLoadSlots.contains(slot)) {
						final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
								(IConstraintStatus) ctx.createFailureStatus("Sandbox|Base case - existing slot used multiple times."));
						deco.addEObjectAndFeature(row, AnalyticsPackage.Literals.BASE_CASE_ROW__BUY_OPTION);
						statuses.add(deco);
					}
				}, (row, slot) -> {
					if (duplicatedDischargeSlots.contains(slot)) {
						final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
								(IConstraintStatus) ctx.createFailureStatus("Sandbox|Base case - existing slot used multiple times."));
						deco.addEObjectAndFeature(row, AnalyticsPackage.Literals.BASE_CASE_ROW__SELL_OPTION);
						statuses.add(deco);
					}
				});
			}

			// Check there is at least one optioniser target
			boolean optioniserMode = false;
			if (extraContext.getContainer(baseCase) instanceof final OptionAnalysisModel model) {
				optioniserMode = model.getMode() == SandboxModeConstants.MODE_OPTIONISE;
			}
			if (optioniserMode) {
				int optioniseTargetsFound = 0;
				for (final BaseCaseRow bcr : baseCase.getBaseCase()) {
					if (bcr.isOptionise()) {
						if (bcr.getBuyOption() != null && !AnalyticsBuilder.isSpot(bcr.getBuyOption())) {
							optioniseTargetsFound++;
						}
						if (bcr.getSellOption() != null && !AnalyticsBuilder.isSpot(bcr.getSellOption())) {
							optioniseTargetsFound++;
						}
						if (bcr.getVesselEventOption() != null) {
							optioniseTargetsFound++;
						}
					}
				}
				if (optioniseTargetsFound == 0) {
					final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Sandbox|Base case - optionise mode needs a target"));
					deco.addEObjectAndFeature(baseCase, AnalyticsPackage.Literals.BASE_CASE__BASE_CASE);
					for (final BaseCaseRow row : baseCase.getBaseCase()) {
						deco.addEObjectAndFeature(row, AnalyticsPackage.Literals.BASE_CASE_ROW__OPTIONISE);
					}
					statuses.add(deco);
				}
				if (optioniseTargetsFound > 3) {
					final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus("Sandbox|Base case - high number of optionise targets found. Result quality may be affected"), IStatus.WARNING);
					deco.addEObjectAndFeature(baseCase, AnalyticsPackage.Literals.BASE_CASE__BASE_CASE);
					statuses.add(deco);
				}
			}
		}
	}

	public void processBaseCase(final BaseCase baseCase, final BiConsumer<BaseCaseRow, BuyOption> visitLoadSlot, final BiConsumer<BaseCaseRow, SellOption> visitDischargeSlot) {
		for (final BaseCaseRow row : baseCase.getBaseCase()) {
			final BuyOption buy = row.getBuyOption();
			if (buy != null) {
				visitLoadSlot.accept(row, buy);
			}
			final SellOption sell = row.getSellOption();

			if (sell != null) {
				visitDischargeSlot.accept(row, sell);
			}
		}
	}
}
