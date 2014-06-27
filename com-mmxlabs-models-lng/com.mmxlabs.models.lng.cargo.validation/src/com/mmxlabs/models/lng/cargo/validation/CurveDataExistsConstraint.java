/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.joda.time.DateTimeZone;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.TaxRate;
import com.mmxlabs.models.lng.commercial.parseutils.Exposures;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.pricing.CommodityIndex;
import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.pricing.PricingModel;
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

	private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	interface CurveStartFinder<CurveType> {
		Date getStart(CurveType curve);
	}

	class IndexStartFinder implements CurveStartFinder<Index<?>> {
		@Override
		public Date getStart(final Index<?> curve) {
			Date result = null;
			for (final Date date : curve.getDates()) {
				if (result == null || result.after(date)) {
					result = date;
				}
			}
			return result;
		}
	}

	// TaxRate objects should have been implemented as Index curves, but were not
	// until they are, this code works
	class TaxRateStartFinder implements CurveStartFinder<EList<TaxRate>> {

		@Override
		public Date getStart(final EList<TaxRate> curve) {
			Date result = null;
			for (final TaxRate rate : curve) {
				final Date date = rate.getDate();
				if (result == null || result.after(date)) {
					result = date;
				}
			}
			return result;
		}

	}

	public CurveDataExistsConstraint() {
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
	}

	private <T> Map<Object, Date> getEarliestDates(final CurveStartFinder<T> finder, final IValidationContext ctx) {
		@SuppressWarnings("unchecked")
		Map<Object, Date> result = (Map<Object, Date>) ctx.getCurrentConstraintData();
		if (result == null) {
			result = new HashMap<Object, Date>();

		}
		return result;
	}

	private <T> Date getEarliestDate(final CurveStartFinder<T> finder, final T curve, final IValidationContext ctx) {
		final Map<Object, Date> map = getEarliestDates(finder, ctx);
		Date result = map.get(curve);
		if (result == null) {
			result = finder.getStart(curve);
			map.put(curve, result);
		}
		return result;
	}

	private <T> boolean curveCovers(final Date date, final CurveStartFinder<T> finder, final T curve, final IValidationContext ctx) {
		final Date start = getEarliestDate(finder, curve, ctx);
		if (start == null) {
			return false;
		}
		if (date == null) {
			return true;
		}
		return !date.before(start);
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

			final PricingModel pricingModel = ((LNGScenarioModel) rootObject).getPricingModel();

			if (pricingModel == null) {
				return;
			}

			// earliest slot date
			final Date portLocalDate = slot.getWindowStartWithSlotOrPortTime();
			final Port port = slot.getPort();
			if (port == null) {
				return;
			}
			final DateTimeZone portDTZ = DateTimeZone.forID(port.getTimeZone());
			final Date utcDate = new Date(portDTZ.convertUTCToLocal(portLocalDate.getTime()));

			// check market indices
			for (final CommodityIndex index : pricingModel.getCommodityIndices()) {
				if (Exposures.getExposureCoefficient(slot, index) != 0) {
					if (!curveCovers(utcDate, indexFinder, index.getData(), ctx)) {
						final String format = "[Index|'%s'] No data for %s, the window start of slot '%s'.";
						final String failureMessage = String.format(format, index.getName(), sdf.format(slot.getWindowStart()), slot.getName());
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

			BaseLegalEntity entity = null;
			if (slot.isSetContract() && slot.getContract() != null) {
				entity = slot.getContract().getEntity();
			} else {
				entity = slot.getEntity();
			}

			// check entity tax rates
			if (entity != null && !curveCovers(portLocalDate, taxFinder, entity.getTradingBook().getTaxRates(), ctx)) {
				final String format = "[Entity|'%s'] No tax data for %s, the window start of slot '%s'.";
				final String failureMessage = String.format(format, entity.getName(), sdf.format(utcDate), slot.getName());
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage), IStatus.WARNING);
				dsd.addEObjectAndFeature(slot, CargoPackage.Literals.SLOT__WINDOW_START);
				dsd.addEObjectAndFeature(slot, CargoPackage.Literals.SLOT__CONTRACT);
				dsd.addEObjectAndFeature(entity, CommercialPackage.Literals.BASE_LEGAL_ENTITY__TRADING_BOOK);
				dsd.addEObjectAndFeature(entity.getTradingBook(), CommercialPackage.Literals.BASE_ENTITY_BOOK__TAX_RATES);
				failures.add(dsd);
			}
		}

	}

	/**
	 * Checks to see if a slot has any validation problems associated with missing curve data in: - entity tax rates (for the shipping entity)
	 * 
	 * @param cargo
	 * @param ctx
	 * @param failures
	 */
	protected void validateCargo(final Cargo cargo, final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {

		final MMXRootObject rootObject = extraContext.getRootObject();
		if (rootObject instanceof LNGScenarioModel) {
			final CommercialModel commercialModel = ((LNGScenarioModel) rootObject).getCommercialModel();

			for (final Slot slot : cargo.getSlots()) {

				final Date date = slot.getWindowStartWithSlotOrPortTime();
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
			}
		}
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
