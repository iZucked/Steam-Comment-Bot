/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.pricing;

import java.time.YearMonth;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lngdataserver.integration.pricing.model.Curve;
import com.mmxlabs.lngdataserver.integration.pricing.model.CurveType;
import com.mmxlabs.lngdataserver.integration.pricing.model.DataCurve;
import com.mmxlabs.lngdataserver.integration.pricing.model.ExpressionCurve;
import com.mmxlabs.lngdataserver.integration.pricing.model.PricingVersion;
import com.mmxlabs.models.lng.pricing.AbstractYearMonthCurve;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.YearMonthPoint;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.VersionRecord;

public class PricingToScenarioCopier {

	private static final Logger LOGGER = LoggerFactory.getLogger(PricingToScenarioCopier.class);

	private PricingToScenarioCopier() {

	}

	public static Command getUpdateCommand(@NonNull final EditingDomain editingDomain, @NonNull final PricingModel pricingModel, final PricingVersion version) {

		final CompoundCommand cmd = new CompoundCommand("Update pricing");

		// Gather existing curves
		final EnumMap<CurveType, Map<String, AbstractYearMonthCurve>> map = new EnumMap<>(CurveType.class);
		final Set<AbstractYearMonthCurve> existing = new HashSet<>();

		map.put(CurveType.BASE_FUEL, new HashMap<>());
		map.put(CurveType.CHARTER, new HashMap<>());
		map.put(CurveType.COMMODITY, new HashMap<>());
		map.put(CurveType.CURRENCY, new HashMap<>());

		pricingModel.getBunkerFuelCurves().forEach(c -> map.get(CurveType.BASE_FUEL).put(c.getName(), c));
		pricingModel.getCharterCurves().forEach(c -> map.get(CurveType.CHARTER).put(c.getName(), c));
		pricingModel.getCommodityCurves().forEach(c -> map.get(CurveType.COMMODITY).put(c.getName(), c));
		pricingModel.getCurrencyCurves().forEach(c -> map.get(CurveType.CURRENCY).put(c.getName(), c));
		pricingModel.getBunkerFuelCurves().forEach(existing::add);
		pricingModel.getCharterCurves().forEach(existing::add);
		pricingModel.getCommodityCurves().forEach(existing::add);
		pricingModel.getCurrencyCurves().forEach(existing::add);

		final Set<AbstractYearMonthCurve> updated = new HashSet<>();

		Collection<Curve> curves;
		if (!version.getCurvesList().isEmpty()) {
			curves = version.getCurvesList();
		} else {
			curves = version.getCurves().values();
		}

		for (final Curve curve : curves) {
			final String name = curve.getName();

			List<YearMonthPoint> points = new LinkedList<>();
			String expression = null;
			if (curve instanceof ExpressionCurve) {
				final ExpressionCurve expressionCurve = (ExpressionCurve) curve;
				expression = expressionCurve.getExpression();
			} else if (curve instanceof DataCurve) {
				final DataCurve dataCurve = (DataCurve) curve;
				if (dataCurve.getCurve() != null) {
					dataCurve.getCurve().forEach(point -> {
						final YearMonthPoint indexPoint = PricingPackage.eINSTANCE.getPricingFactory().createYearMonthPoint();
						indexPoint.setDate(YearMonth.of(point.getDate().getYear(), point.getDate().getMonthValue()));
						indexPoint.setValue(point.getValue().doubleValue());
						points.add(indexPoint);
					});
				}
			} else {
				// Unknown
				throw new IllegalArgumentException();
			}

			AbstractYearMonthCurve existingCurve = map.get(curve.getType()).get(name);

			if (existingCurve == null) {
				final EReference ref;
				switch (curve.getType()) {
				case BASE_FUEL:
					existingCurve = PricingFactory.eINSTANCE.createBunkerFuelCurve();
					ref = PricingPackage.Literals.PRICING_MODEL__BUNKER_FUEL_CURVES;
					break;
				case CHARTER:
					existingCurve = PricingFactory.eINSTANCE.createCharterCurve();
					ref = PricingPackage.Literals.PRICING_MODEL__CHARTER_CURVES;
					break;
				case COMMODITY:
					existingCurve = PricingFactory.eINSTANCE.createCommodityCurve();
					ref = PricingPackage.Literals.PRICING_MODEL__COMMODITY_CURVES;
					break;
				case CURRENCY:
					existingCurve = PricingFactory.eINSTANCE.createCurrencyCurve();
					ref = PricingPackage.Literals.PRICING_MODEL__CURRENCY_CURVES;
					break;
				default:
					throw new IllegalArgumentException();
				}

				existingCurve.setName(curve.getName());
				existingCurve.setVolumeUnit(curve.getUnit());
				existingCurve.setCurrencyUnit(curve.getCurrency());
				if (expression != null) {
					existingCurve.setExpression(expression);
				} else {
					existingCurve.getPoints().addAll(points);
				}

				// ADD COMMAND
				cmd.append(AddCommand.create(editingDomain, pricingModel, ref, existingCurve));

			} else {
				if (!existingCurve.getPoints().isEmpty()) {
					cmd.append(RemoveCommand.create(editingDomain, existingCurve, PricingPackage.Literals.YEAR_MONTH_POINT_CONTAINER__POINTS, new LinkedList<>(existingCurve.getPoints())));
				}
				if (expression == null) {
					cmd.append(AddCommand.create(editingDomain, existingCurve, PricingPackage.Literals.YEAR_MONTH_POINT_CONTAINER__POINTS, points));
					cmd.append(SetCommand.create(editingDomain, existingCurve, PricingPackage.Literals.ABSTRACT_YEAR_MONTH_CURVE__EXPRESSION, SetCommand.UNSET_VALUE));
				} else {
					cmd.append(SetCommand.create(editingDomain, existingCurve, PricingPackage.Literals.ABSTRACT_YEAR_MONTH_CURVE__EXPRESSION, expression));
				}

				cmd.append(SetCommand.create(editingDomain, existingCurve, PricingPackage.Literals.ABSTRACT_YEAR_MONTH_CURVE__VOLUME_UNIT, curve.getUnit()));
				cmd.append(SetCommand.create(editingDomain, existingCurve, PricingPackage.Literals.ABSTRACT_YEAR_MONTH_CURVE__CURRENCY_UNIT, curve.getCurrency()));
			}
			updated.add(existingCurve);
		}

		existing.removeAll(updated);
		if (!existing.isEmpty()) {
			cmd.append(DeleteCommand.create(editingDomain, existing));
		}

		VersionRecord record = pricingModel.getMarketCurvesVersionRecord();
		cmd.append(SetCommand.create(editingDomain, record, MMXCorePackage.Literals.VERSION_RECORD__CREATED_BY, version.getCreatedBy()));
		cmd.append(SetCommand.create(editingDomain, record, MMXCorePackage.Literals.VERSION_RECORD__CREATED_AT, version.getCreatedAt()));
		cmd.append(SetCommand.create(editingDomain, record, MMXCorePackage.Literals.VERSION_RECORD__VERSION, version.getIdentifier()));

		return cmd;
	}
}
