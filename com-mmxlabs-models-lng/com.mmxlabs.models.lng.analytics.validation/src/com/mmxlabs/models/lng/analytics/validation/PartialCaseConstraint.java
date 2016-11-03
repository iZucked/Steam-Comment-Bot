package com.mmxlabs.models.lng.analytics.validation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BuyOpportunity;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.PartialCase;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.lng.analytics.SellOpportunity;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.validation.internal.Activator;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class PartialCaseConstraint extends AbstractModelMultiConstraint {
	public static final String viewName = "Options table";
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
					final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(String.format("%s - existing slot used multiple times.", viewName)));
					deco.addEObjectAndFeature(row, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__BUY_OPTIONS);
					statuses.add(deco);
				}
			}, (row, slot) -> {
				if (duplicatdDischargeSlots.contains(slot)) {
					final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(String.format("%s - existing slot used multiple times.", viewName)));
					deco.addEObjectAndFeature(row, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SELL_OPTIONS);
					statuses.add(deco);
				}
			});
			
//			// question marks
//			final Set<PartialCaseRow> badRows = new HashSet<>();
//			// First pass
//			processPartialCaseRow(partialCase, (row) -> {
//				if (row.getBuyOptions().stream().filter(b -> (b != null) && (b instanceof BuyOpportunity) && ((BuyOpportunity) b).getPriceExpression().equals("?")).count() > 0
//						&& row.getSellOptions().stream().filter(s -> (s != null) && (s instanceof SellOpportunity) && ((SellOpportunity) s).getPriceExpression().equals("?")).count() > 0) {
//					badRows.add(row);
//				}
//			});
//			// Second pass
//			processPartialCaseRow(partialCase, (row) -> {
//				if (badRows.contains(row)) {
//					final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Row contains a ? on a buy and load option"));
//					deco.addEObjectAndFeature(row, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__PARTIAL_CASE);
//					statuses.add(deco);
//				}
//			});
			
			final Set<PartialCaseRow> loadQuestion = new HashSet<>();
			final Set<PartialCaseRow> dischargeQuestion = new HashSet<>();

			// First pass, find problem slots
			processPartialCase(partialCase, (row, slot) -> {
				if (slot != null && slot instanceof BuyOpportunity && "?".equals(((BuyOpportunity) slot).getPriceExpression())) {
					loadQuestion.add(row);
				}
			}, (row, slot) -> {
				if (slot != null && slot instanceof SellOpportunity && "?".equals(((SellOpportunity) slot).getPriceExpression())) {
					dischargeQuestion.add(row);
				}
			});
			// Second pass, report problem slots
			if (loadQuestion.size() > 0 && dischargeQuestion.size() > 0) {
				// just add on distinct rows
				loadQuestion.addAll(dischargeQuestion);
				processPartialCaseRow(partialCase, (row) -> {
					if (loadQuestion.contains(row)) {
						final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(String.format("%s - contains row(s) with a ? on a buy and load option", viewName)));
						deco.addEObjectAndFeature(row, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__PARTIAL_CASE);
						statuses.add(deco);
					}
				});
			}

		}

		return Activator.PLUGIN_ID;
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
