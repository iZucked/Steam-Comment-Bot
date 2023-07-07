/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.util.SlotContractParamsHelper;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.TaxRate;
import com.mmxlabs.models.lng.pricing.AbstractYearMonthCurve;
import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.lng.pricing.CooldownPrice;
import com.mmxlabs.models.lng.pricing.CostModel;
import com.mmxlabs.models.lng.pricing.CurrencyCurve;
import com.mmxlabs.models.lng.pricing.util.ModelMarketCurveProvider;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils.PriceIndexType;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioElementNameHelper;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * A model constraint for checking that curves which are attached to objects have data for the dates associated with those objects.
 * 
 * @author Simon McGregor
 * 
 */
public class SlotCurveDataExistsConstraint extends AbstractModelMultiConstraint {

	public static final Object KEY_EXPRESSION_FAILURE = new Object();
	public static final Object KEY_CONTRACT_FAILURE = new Object();
	public static final Object KEY_MARKET_FAILURE = new Object();
	public static final Object KEY_TAX_FAILURE = new Object();
	public static final Object KEY_COOLDOWN_FAILURE = new Object();

	private final TaxRateStartFinder taxFinder = new TaxRateStartFinder();

	private interface CurveStartFinder<C, T> {
		T getStart(C curve);
	}

	// TaxRate objects should have been implemented as Index curves, but were not
	// until they are, this code works
	private class TaxRateStartFinder implements CurveStartFinder<EList<TaxRate>, LocalDate> {

		@Override
		public LocalDate getStart(final EList<TaxRate> curve) {
			LocalDate result = null;
			for (final TaxRate rate : curve) {
				final LocalDate date = rate.getDate();
				if (result == null || result.isAfter(date)) {
					result = date;
				}
			}
			return result;
		}
	}

	private <T, U> Map<T, U> getEarliestDates(final IValidationContext ctx) {
		@SuppressWarnings("unchecked")
		Map<T, U> result = (Map<T, U>) ctx.getCurrentConstraintData();
		if (result == null) {
			result = new HashMap<>();
		}
		return result;
	}

	private <T, U> U getEarliestDate(final CurveStartFinder<T, U> finder, final T curve, final IValidationContext ctx) {
		final Map<T, U> map = getEarliestDates(ctx);
		return map.computeIfAbsent(curve, finder::getStart);
	}

	private <T> boolean curveCovers(final LocalDate date, final CurveStartFinder<T, LocalDate> finder, final T curve, final IValidationContext ctx) {
		final LocalDate start = getEarliestDate(finder, curve, ctx);
		if (start == null) {
			return false;
		}
		if (date == null) {
			return true;
		}
		return !date.isBefore(start);
	}

	/**
	 * Checks to see if a slot has any validation problems associated with missing curve data in: - market indices (in any associated contract or price expression) - entity tax rates (in any
	 * associated contract)
	 * 
	 * @param slot
	 * @param ctx
	 * @param failures
	 */
	protected void validateSlot(final Slot<?> slot, final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {

		if (extraContext.getRootObject() instanceof final @NonNull LNGScenarioModel scenarioModel) {
			final ModelMarketCurveProvider marketCurveProvider = PriceExpressionUtils.getMarketCurveProvider();

			// Find earliest slot date used
			ZonedDateTime pricingDate = null;
			if (slot.isSetPricingDate()) {
				pricingDate = slot.getPricingDateAsDateTime();
			}
			if (pricingDate == null) {
				// Not strictly correct, may differ with actuals / scheduled date
				pricingDate = slot.getSchedulingTimeWindow().getStart();
			}

			final DetailConstraintStatusFactory baseFactory = DetailConstraintStatusFactory.makeStatus() //
					.withTypedName(ScenarioElementNameHelper.getTypeName(slot), ScenarioElementNameHelper.getNonNullString(slot.getName()));

			if (slot.isSetPriceExpression() && SlotContractParamsHelper.isSlotExpressionUsed(slot)) {
				final String priceExpression = slot.getPriceExpression();
				boolean checkExpression = true;
				if ("??".equals(priceExpression)) {
					if (!(slot.eContainer() instanceof CargoModel)) {
						// Special "changable price" expression for sandbox. Not expected to be in main
						// scenario
						checkExpression = false;
					}
				} else if ("?".equals(priceExpression)) {
					// Permit break even marker
					checkExpression = false;
				}

				if (checkExpression) {
					validateExpression(priceExpression, PriceIndexType.COMMODITY, pricingDate.toLocalDate(), slot, CargoPackage.Literals.SLOT__PRICE_EXPRESSION, null, KEY_EXPRESSION_FAILURE,
							marketCurveProvider, ctx, baseFactory, failures);
				}
			} else if (slot.getContract() != null) {
				final EObject pricParams = slot.getContract().getPriceInfo();
				if (pricParams != null) {
					for (final EAttribute attrib : pricParams.eClass().getEAllAttributes()) {
						if (attrib.getEType() == EcorePackage.Literals.ESTRING) {
							if (PriceExpressionUtils.hasPriceAnnotation(attrib)) {
								final String priceExpression = (String) pricParams.eGet(attrib);
								final PriceIndexType indexType = PriceExpressionUtils.getPriceIndexType(attrib);
								validateExpression(priceExpression, indexType, pricingDate.toLocalDate(), slot, CargoPackage.Literals.SLOT__CONTRACT, null, KEY_CONTRACT_FAILURE, marketCurveProvider,
										ctx, baseFactory, failures);
							}
						}
					}
				}
			} else if (slot instanceof SpotSlot spotSlot && spotSlot.getMarket() != null) {
				final EObject pricParams = spotSlot.getMarket().getPriceInfo();
				if (pricParams != null) {
					for (final EAttribute attrib : pricParams.eClass().getEAllAttributes()) {
						if (attrib.getEType() == EcorePackage.Literals.ESTRING) {
							if (PriceExpressionUtils.hasPriceAnnotation(attrib)) {
								final String priceExpression = (String) pricParams.eGet(attrib);
								final PriceIndexType indexType = PriceExpressionUtils.getPriceIndexType(attrib);
								validateExpression(priceExpression, indexType, pricingDate.toLocalDate(), slot, CargoPackage.Literals.SPOT_SLOT__MARKET, null, KEY_MARKET_FAILURE, marketCurveProvider,
										ctx, baseFactory, failures);
							}
						}
					}
				}
			}
			ZonedDateTime start = slot.getSchedulingTimeWindow().getStart();
			if (start != null) {
				LocalDate windowStart = start.toLocalDate();
				if (slot instanceof final LoadSlot loadSlot && !loadSlot.isDESPurchase()) {
					final CostModel costModel = ScenarioModelUtil.getCostModel(extraContext.getScenarioDataProvider());
					final List<CooldownPrice> cooldownCosts = costModel.getCooldownCosts();
					CooldownPrice portCP = null;
					for (final CooldownPrice cp : cooldownCosts) {
						if (cp.getPorts().contains(loadSlot.getPort())) {
							portCP = cp;
							break;
						}
					}
					if (portCP == null) {
						for (final CooldownPrice cp : cooldownCosts) {
							if (SetUtils.getObjects(cp.getPorts()).contains(loadSlot.getPort())) {
								portCP = cp;
								break;
							}
						}
					}
					if (portCP != null) {
						final String lumpsumExpression = portCP.getLumpsumExpression();
						if (lumpsumExpression != null && !lumpsumExpression.trim().isEmpty()) {
							validateExpression(lumpsumExpression, PriceIndexType.COMMODITY, windowStart, slot, CargoPackage.Literals.SLOT__PORT, "cooldown", KEY_COOLDOWN_FAILURE, marketCurveProvider,
									ctx, baseFactory, failures);
						}
						final String volumeExpression = portCP.getVolumeExpression();
						if (volumeExpression != null && !volumeExpression.trim().isEmpty()) {
							validateExpression(volumeExpression, PriceIndexType.COMMODITY, windowStart, slot, CargoPackage.Literals.SLOT__PORT, "cooldown", KEY_COOLDOWN_FAILURE, marketCurveProvider,
									ctx, baseFactory, failures);
						}
					}
				}

				BaseLegalEntity entity = slot.getSlotOrDelegateEntity();
				if (entity != null) {
					// check entity tax rates
					if (entity != null && !curveCovers(windowStart, taxFinder, entity.getTradingBook().getTaxRates(), ctx)) {
						final String format = "No tax data for %s %02d/%04d'";
						final String failureMessage = String.format(format, entity.getName(), windowStart.getMonthValue(), windowStart.getYear());
						baseFactory.copyName() //
								.withMessage(failureMessage) //
								.withConstraintKey(KEY_TAX_FAILURE) //

								.withObjectAndFeature(slot, CargoPackage.Literals.SLOT__WINDOW_START) //
								.withObjectAndFeature(slot, CargoPackage.Literals.SLOT__CONTRACT) //
								.withObjectAndFeature(entity, CommercialPackage.Literals.BASE_LEGAL_ENTITY__TRADING_BOOK) //
								.withObjectAndFeature(entity.getTradingBook(), CommercialPackage.Literals.BASE_ENTITY_BOOK__TAX_RATES) //
								//
								.make(ctx, failures);
					}
				}
			}
		}

	}

	@Override
	public void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject object = ctx.getTarget();

		// check slots for index data (price expressions or contracts) and tax rate data
		// (entity)
		if (object instanceof Slot) {
			validateSlot((Slot<?>) object, ctx, extraContext, statuses);
		}
	}

	private void validateExpression(String priceExpression, PriceIndexType indexType, LocalDate pricingDate, Slot<?> slot, EStructuralFeature feature, @Nullable String typePrefix,
			Object constraintKey, ModelMarketCurveProvider marketCurveProvider, final IValidationContext ctx, DetailConstraintStatusFactory factory, final List<IStatus> failures) {
		final Map<AbstractYearMonthCurve, LocalDate> linkedCurves = marketCurveProvider.getLinkedCurvesAndEarliestNeededDate(priceExpression, indexType, pricingDate);
		for (final var e : linkedCurves.entrySet()) {
			final AbstractYearMonthCurve index = e.getKey();

			if (index.isSetExpression()) {
				continue;
			}

			String type = "index";
			if (index instanceof CommodityCurve) {
				type = "commodity pricing";
			} else if (index instanceof CurrencyCurve) {
				type = "currency";
			}
			if (typePrefix != null) {
				type = typePrefix + " " + type;
			}
			final @Nullable YearMonth date = PriceExpressionUtils.getEarliestCurveDate(index);
			if (date == null) {
				factory.copyName() //
						.withObjectAndFeature(slot, feature) //
						.withMessage(String.format("There is no %s data for curve %s", type, index.getName())) //
						.withConstraintKey(constraintKey) //
						.make(ctx, failures);

			} else if (date.isAfter(YearMonth.from(e.getValue()))) {
				final String monthDisplayname = date.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());

				factory.copyName() //
						.withObjectAndFeature(slot, feature) //
						.withMessage(String.format("There is no %s data before %s %04d for curve %s", type, monthDisplayname, date.getYear(), index.getName())) //
						.withConstraintKey(constraintKey) //
						.make(ctx, failures);
			}
		}

	}

}
