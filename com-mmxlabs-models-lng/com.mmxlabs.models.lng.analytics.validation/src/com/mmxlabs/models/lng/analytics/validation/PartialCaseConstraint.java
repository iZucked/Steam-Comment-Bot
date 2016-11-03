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
import com.mmxlabs.models.lng.analytics.NominatedShippingOption;
import com.mmxlabs.models.lng.analytics.PartialCase;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.lng.analytics.SellOpportunity;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.SellReference;
import com.mmxlabs.models.lng.analytics.ShippingOption;
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
			
			/*
			 * Mismatched shipping type
			 */
			Set<PartialCaseRow> mismatchedShippingTypes = new HashSet<>();
			// First pass, find problem slots
			processPartialCaseRow(partialCase, (row) -> {
				if (row.getBuyOptions().stream().filter(isDESPurchase()).count() > 0
						&& row.getSellOptions().stream().filter(isFOBSale()).count() > 0) {
					mismatchedShippingTypes.add(row);
				}
			});
			// Second pass, report problem slots
			if (mismatchedShippingTypes.size() > 0) {
				// just add on distinct rows
				processPartialCaseRow(partialCase, (row) -> {
					if (mismatchedShippingTypes.contains(row)) {
						final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(String.format("%s - contains row(s) with a DES purchase and a FOB Sale", viewName)));
						deco.addEObjectAndFeature(row, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__PARTIAL_CASE);
						statuses.add(deco);
					}
				});
			}

			/*
			 * Mismatched shipping type
			 */
			Set<PartialCaseRow> mismatchedShippingOption = new HashSet<>();
			// First pass, find problem slots
			processPartialCaseRow(partialCase, (row) -> {
				if (row.getBuyOptions().stream().filter(isFOBPurchase()).count() > 0
						&& row.getSellOptions().stream().filter(isDESSale()).count() > 0
						&& row.getShipping().stream().filter(isNominated()).count() > 0) {
					mismatchedShippingOption.add(row);
				}
			});
			// Second pass, report problem slots
			if (mismatchedShippingOption.size() > 0) {
				// just add on distinct rows
				processPartialCaseRow(partialCase, (row) -> {
					if (mismatchedShippingOption.contains(row)) {
						final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(String.format("%s - contains row(s) with a FOB purchase and a DES Sale and a nominated vessel", viewName)));
						deco.addEObjectAndFeature(row, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__PARTIAL_CASE);
						statuses.add(deco);
					}
				});
			}

			/*
			 * Mismatched shipping type
			 */
			Set<PartialCaseRow> nullShippingOption = new HashSet<>();
			// First pass, find problem slots
			processPartialCaseRow(partialCase, (row) -> {
				if (row.getBuyOptions().stream().filter(isFOBPurchase()).count() > 0
						&& row.getSellOptions().stream().filter(isDESSale()).count() > 0
						&& row.getShipping().isEmpty()) {
					nullShippingOption.add(row);
				}
			});
			// Second pass, report problem slots
			if (nullShippingOption.size() > 0) {
				// just add on distinct rows
				processPartialCaseRow(partialCase, (row) -> {
					if (nullShippingOption.contains(row)) {
						final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(String.format("%s - contains row(s) with a FOB purchase and a DES Sale and no shipping option", viewName)));
						deco.addEObjectAndFeature(row, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__PARTIAL_CASE);
						statuses.add(deco);
					}
				});
			}


		}

		return Activator.PLUGIN_ID;
	}

	public static Predicate<BuyOption> isFOBPurchase() {
		return b -> ((b instanceof BuyReference && ((BuyReference) b).getSlot() != null && ((BuyReference) b).getSlot().isDESPurchase() == false)
				|| (b instanceof BuyOpportunity && ((BuyOpportunity) b).isDesPurchase() == false));
	}

	public static Predicate<BuyOption> isDESPurchase() {
		return b -> ((b instanceof BuyReference && ((BuyReference) b).getSlot() != null && ((BuyReference) b).getSlot().isDESPurchase() == true)
				|| (b instanceof BuyOpportunity && ((BuyOpportunity) b).isDesPurchase() == true));
	}
	
	public static Predicate<SellOption> isFOBSale() {
		return s -> ((s instanceof SellReference && ((SellReference) s).getSlot() != null && ((SellReference) s).getSlot().isFOBSale() == true)
				|| (s instanceof SellOpportunity && ((SellOpportunity) s).isFobSale() == true));
	}
	
	public static Predicate<SellOption> isDESSale() {
		return s -> ((s instanceof SellReference && ((SellReference) s).getSlot() != null && ((SellReference) s).getSlot().isFOBSale() == false)
				|| (s instanceof SellOpportunity && ((SellOpportunity) s).isFobSale() == false));
	}
	
	private static Predicate<ShippingOption> isNominated() {
		return s -> s != null && s instanceof NominatedShippingOption;
	}

	private static Predicate<ShippingOption> isShippingNull() {
		return s -> s != null;
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
