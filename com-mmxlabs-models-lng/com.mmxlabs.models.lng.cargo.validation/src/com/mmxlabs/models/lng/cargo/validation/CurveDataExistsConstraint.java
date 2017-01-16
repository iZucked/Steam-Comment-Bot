/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.TaxRate;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.pricing.CommodityIndex;
import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.parser.Node;
import com.mmxlabs.models.lng.pricing.parser.RawTreeParser;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * A model constraint for checking that curves which are attached to objects have data for the dates associated with those objects.
 * 
 * @author Simon McGregor
 * 
 */
public class CurveDataExistsConstraint extends AbstractModelMultiConstraint {
	private final IndexStartFinder indexFinder = new IndexStartFinder();
	private final TaxRateStartFinder taxFinder = new TaxRateStartFinder();

	private final DateTimeFormatter sdf = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);

	interface CurveStartFinder<CurveType, DateType> {
		DateType getStart(CurveType curve);
	}

	class IndexStartFinder implements CurveStartFinder<Index<?>, YearMonth> {
		@Override
		public YearMonth getStart(final Index<?> curve) {
			YearMonth result = null;
			for (final YearMonth date : curve.getDates()) {
				if (result == null || result.isAfter(date)) {
					result = date;
				}
			}
			return result;
		}
	}

	// TaxRate objects should have been implemented as Index curves, but were not
	// until they are, this code works
	class TaxRateStartFinder implements CurveStartFinder<EList<TaxRate>, LocalDate> {

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

	public CurveDataExistsConstraint() {
	}

	private <T, U> Map<Object, U> getEarliestDates(final CurveStartFinder<T, U> finder, final IValidationContext ctx) {
		@SuppressWarnings("unchecked")
		Map<Object, U> result = (Map<Object, U>) ctx.getCurrentConstraintData();
		if (result == null) {
			result = new HashMap<Object, U>();

		}
		return result;
	}

	private <T, U> U getEarliestDate(final CurveStartFinder<T, U> finder, final T curve, final IValidationContext ctx) {
		final Map<Object, U> map = getEarliestDates(finder, ctx);
		U result = map.get(curve);
		if (result == null) {
			result = finder.getStart(curve);
			map.put(curve, result);
		}
		return result;
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

	private <T> boolean curveCovers(final YearMonth date, final CurveStartFinder<T, YearMonth> finder, final T curve, final IValidationContext ctx) {
		final YearMonth start = getEarliestDate(finder, curve, ctx);
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
	protected void validateSlot(final Slot slot, final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {

		final MMXRootObject rootObject = extraContext.getRootObject();
		if (rootObject instanceof LNGScenarioModel) {

			final PricingModel pricingModel = ((LNGScenarioModel) rootObject).getReferenceModel().getPricingModel();

			if (pricingModel == null) {
				return;
			}

			// earliest slot date
			final ZonedDateTime portLocalDate = slot.getWindowStartWithSlotOrPortTime();
			if (portLocalDate == null) {
				return;
			}
			final Port port = slot.getPort();
			if (port == null) {
				return;
			}
			final YearMonth utcDate = YearMonth.of(portLocalDate.toLocalDate().getYear(), portLocalDate.toLocalDate().getMonthValue());
			{
				if (!slot.isSetPriceExpression()) {
					return;
				}

				final String priceExpression = slot.getPriceExpression();
				if (priceExpression == null || priceExpression.isEmpty()) {
					return;
				}
				final RawTreeParser parser = new RawTreeParser();
				try {

					final Map<String, CommodityIndex> indexMap = pricingModel.getCommodityIndices().stream() //
							.collect(Collectors.toMap(CommodityIndex::getName, Function.identity()));

					final IExpression<Node> parsed = parser.parse(priceExpression);
					final List<Node> nodes = extract(parsed);
					for (final Node n : nodes) {
						if (indexMap.containsKey(n.token)) {
							final CommodityIndex index = indexMap.get(n.token);
							@Nullable
							YearMonth date = PriceExpressionUtils.getEarliestCurveDate(index);
							if (date == null || utcDate.isBefore(date)) {

								// if (!curveCovers(utcDate, indexFinder, index.getData(), ctx)) {
								final String format = "[Index|'%s'] No data for %s, the window start of slot '%s'.";
								final String failureMessage = String.format(format, index.getName(), format(slot), slot.getName());
								final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage), IStatus.WARNING);
								if (slot.isSetPriceExpression()) {
									dsd.addEObjectAndFeature(slot, CargoPackage.Literals.SLOT__PRICE_EXPRESSION);
								} else {
									dsd.addEObjectAndFeature(slot, CargoPackage.Literals.SLOT__CONTRACT);
								}
								dsd.addEObjectAndFeature(slot, CargoPackage.Literals.SLOT__WINDOW_START);
								failures.add(dsd);
							}
						}
					}

				} catch (final Exception e) {
					// Do nothing
				}
			}
			BaseLegalEntity entity = null;
			if (slot.isSetContract() && slot.getContract() != null) {
				entity = slot.getContract().getEntity();
			} else {
				entity = slot.getEntity();
			}

			// check entity tax rates
			if (entity != null && !curveCovers(portLocalDate.toLocalDate(), taxFinder, entity.getTradingBook().getTaxRates(), ctx)) {
				final String format = "[Entity|'%s'] No tax data for %02d/%04d, the window start of slot '%s'.";
				final String failureMessage = String.format(format, entity.getName(), utcDate.getMonthValue(), utcDate.getYear(), slot.getName());
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage), IStatus.WARNING);
				dsd.addEObjectAndFeature(slot, CargoPackage.Literals.SLOT__WINDOW_START);
				dsd.addEObjectAndFeature(slot, CargoPackage.Literals.SLOT__CONTRACT);
				dsd.addEObjectAndFeature(entity, CommercialPackage.Literals.BASE_LEGAL_ENTITY__TRADING_BOOK);
				dsd.addEObjectAndFeature(entity.getTradingBook(), CommercialPackage.Literals.BASE_ENTITY_BOOK__TAX_RATES);
				failures.add(dsd);
			}
		}

	}

	private @NonNull List<@NonNull Node> extract(final IExpression<Node> parsed) {
		@NonNull
		final List<@NonNull Node> l = new LinkedList<>();

		extract(parsed.evaluate(), l);

		return l;
	}

	private void extract(final Node n, @NonNull final List<@NonNull Node> l) {
		if (n == null) {
			return;
		}
		l.add(n);

		if (n.children != null) {
			for (final Node c : n.children) {
				extract(c, l);
			}
		}
	}

	private String format(final Slot slot) {
		final LocalDate windowStart = slot.getWindowStart();
		if (windowStart == null) {
			return "<no date>";
		}
		return windowStart.format(sdf);
	}

	/**
	 * Checks to see if a slot has any validation problems associated with missing curve data in: - entity tax rates (for the shipping entity)
	 * 
	 * @param cargo
	 * @param ctx
	 * @param failures
	 */
	protected void validateCargo(final Cargo cargo, final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {

		// final MMXRootObject rootObject = extraContext.getRootObject();
		// if (rootObject instanceof LNGScenarioModel) {
		// final CommercialModel commercialModel = ((LNGScenarioModel) rootObject).getCommercialModel();

		// for (final Slot slot : cargo.getSlots()) {

		// final ZonedDateTime date = slot.getWindowStartWithSlotOrPortTime();
		// final BaseLegalEntity entity = commercialModel.getShippingEntity(); // get default shipping entity

		// check entity tax rates
		// if (entity != null && !curveCovers(date, taxFinder, entity.getTaxRates(), ctx)) {
		// String format = "[Entity|'%s'] No tax data for '%s', the load date for cargo '%s'.";
		// final String failureMessage = String.format(format, entity.getName(), sdf.format(date), cargo.getName());
		// final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage), IStatus.WARNING);
		// dsd.addEObjectAndFeature(slot, CargoPackage.Literals.SLOT__WINDOW_START);
		// dsd.addEObjectAndFeature(entity, CommercialPackage.Literals.BASE_LEGAL_ENTITY__TAX_RATES);
		// failures.add(dsd);
		// }
		// }
		// }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.validation.AbstractModelConstraint#validate(org.eclipse .emf.validation.IValidationContext)
	 */
	@Override
	public String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject object = ctx.getTarget();

		// check slots for index data (price expressions or contracts) and tax rate data (entity)
		if (object instanceof Slot) {
			validateSlot((Slot) object, ctx, extraContext, statuses);
		}
		// check cargoes against the shipping entity tax curve
		else if (object instanceof Cargo) {
			validateCargo((Cargo) object, ctx, extraContext, statuses);
		}
		// check vessel availability against charter curve data (if relevant)
		else if (object instanceof Vessel) {

		}

		return Activator.PLUGIN_ID;
	}

}
