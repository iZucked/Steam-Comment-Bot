/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.pricing;

import java.time.YearMonth;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
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
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.DerivedIndex;
import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.pricing.NamedIndexContainer;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;

public class PricingToScenarioCopier {

	private static final Logger LOGGER = LoggerFactory.getLogger(PricingToScenarioCopier.class);

	private PricingToScenarioCopier() {

	}

	public static Command getUpdateCommand(@NonNull final EditingDomain editingDomain, @NonNull final PricingModel pricingModel, final PricingVersion version) {

		final CompoundCommand cmd = new CompoundCommand("Update pricing");

		// Gather existing curves
		final EnumMap<CurveType, Map<String, NamedIndexContainer<?>>> map = new EnumMap<>(CurveType.class);
		final Set<NamedIndexContainer<?>> existing = new HashSet<>();

		map.put(CurveType.BASE_FUEL, new HashMap<>());
		map.put(CurveType.CHARTER, new HashMap<>());
		map.put(CurveType.COMMODITY, new HashMap<>());
		map.put(CurveType.CURRENCY, new HashMap<>());

		pricingModel.getBaseFuelPrices().forEach(c -> map.get(CurveType.BASE_FUEL).put(c.getName(), c));
		pricingModel.getCharterIndices().forEach(c -> map.get(CurveType.CHARTER).put(c.getName(), c));
		pricingModel.getCommodityIndices().forEach(c -> map.get(CurveType.COMMODITY).put(c.getName(), c));
		pricingModel.getCurrencyIndices().forEach(c -> map.get(CurveType.CURRENCY).put(c.getName(), c));
		pricingModel.getBaseFuelPrices().forEach(existing::add);
		pricingModel.getCharterIndices().forEach(existing::add);
		pricingModel.getCommodityIndices().forEach(existing::add);
		pricingModel.getCurrencyIndices().forEach(existing::add);

		final Set<NamedIndexContainer<?>> updated = new HashSet<>();
		for (final Map.Entry<String, Curve> e : version.getCurves().entrySet()) {
			final String name = e.getKey();
			final Curve curve = e.getValue();

			final boolean isIntegerType = curve.getType() == CurveType.CHARTER;
			Index index = null;
			if (curve instanceof ExpressionCurve) {
				final ExpressionCurve expressionCurve = (ExpressionCurve) curve;
				if (isIntegerType) {
					final DerivedIndex<Integer> derivedIndex = PricingPackage.eINSTANCE.getPricingFactory().createDerivedIndex();
					derivedIndex.setExpression(expressionCurve.getExpression());
					index = derivedIndex;
				} else {
					final DerivedIndex<Double> derivedIndex = PricingPackage.eINSTANCE.getPricingFactory().createDerivedIndex();
					derivedIndex.setExpression(expressionCurve.getExpression());
					index = derivedIndex;
				}
			} else if (curve instanceof DataCurve) {
				final DataCurve dataCurve = (DataCurve) curve;
				if (isIntegerType) {
					final DataIndex<Integer> dataIndex = PricingPackage.eINSTANCE.getPricingFactory().createDataIndex();
					dataCurve.getCurve().forEach(point -> {
						final IndexPoint<Integer> indexPoint = PricingPackage.eINSTANCE.getPricingFactory().createIndexPoint();
						indexPoint.setDate(YearMonth.of(point.getDate().getYear(), point.getDate().getMonthValue()));
						indexPoint.setValue(point.getValue().intValue());
						dataIndex.getPoints().add(indexPoint);
					});
					index = dataIndex;
				} else {
					final DataIndex<Double> dataIndex = PricingPackage.eINSTANCE.getPricingFactory().createDataIndex();
					dataCurve.getCurve().forEach(point -> {
						final IndexPoint<Double> indexPoint = PricingPackage.eINSTANCE.getPricingFactory().createIndexPoint();
						indexPoint.setDate(YearMonth.of(point.getDate().getYear(), point.getDate().getMonthValue()));
						indexPoint.setValue(point.getValue().doubleValue());
						dataIndex.getPoints().add(indexPoint);
					});
					index = dataIndex;
				}
			} else {
				// Unknown
			}

			NamedIndexContainer<?> existingCurve = map.get(curve.getType()).get(name);

			if (existingCurve == null) {
				final EReference ref;
				switch (curve.getType()) {
				case BASE_FUEL:
					existingCurve = PricingFactory.eINSTANCE.createBaseFuelIndex();
					ref = PricingPackage.Literals.PRICING_MODEL__BASE_FUEL_PRICES;
					break;
				case CHARTER:
					existingCurve = PricingFactory.eINSTANCE.createCharterIndex();
					ref = PricingPackage.Literals.PRICING_MODEL__CHARTER_INDICES;
					break;
				case COMMODITY:
					existingCurve = PricingFactory.eINSTANCE.createCommodityIndex();
					ref = PricingPackage.Literals.PRICING_MODEL__COMMODITY_INDICES;
					break;
				case CURRENCY:
					existingCurve = PricingFactory.eINSTANCE.createCurrencyIndex();
					ref = PricingPackage.Literals.PRICING_MODEL__CURRENCY_INDICES;
					break;
				default:
					throw new IllegalArgumentException();
				}

				existingCurve.setName(curve.getName());
				existingCurve.setVolumeUnit(curve.getUnit());
				existingCurve.setCurrencyUnit(curve.getCurrency());
				existingCurve.setData(index);

				// ADD COMMAND
				cmd.append(AddCommand.create(editingDomain, pricingModel, ref, existingCurve));

			} else {
				// Fails due to generics...
				// cmd.append(SetCommand.create(editingDomain, existingCurve, PricingPackage.Literals.NAMED_INDEX_CONTAINER__DATA, index));
				// ... instead assume we have not flipped between data and expression and update the data points
				if (index instanceof DataIndex) {
					cmd.append(SetCommand.create(editingDomain, existingCurve.getData(), PricingPackage.Literals.DATA_INDEX__POINTS, ((DataIndex) index).getPoints()));
				} else if (index instanceof DerivedIndex) {
					cmd.append(SetCommand.create(editingDomain, existingCurve.getData(), PricingPackage.Literals.DERIVED_INDEX__EXPRESSION, ((DerivedIndex) index).getExpression()));
				} else {
					throw new IllegalStateException();
				}

				cmd.append(SetCommand.create(editingDomain, existingCurve, PricingPackage.Literals.NAMED_INDEX_CONTAINER__VOLUME_UNIT, curve.getUnit()));
				cmd.append(SetCommand.create(editingDomain, existingCurve, PricingPackage.Literals.NAMED_INDEX_CONTAINER__CURRENCY_UNIT, curve.getCurrency()));
			}
			updated.add(existingCurve);
		}

		existing.removeAll(updated);
		if (!existing.isEmpty()) {
			cmd.append(DeleteCommand.create(editingDomain, existing));
		}
		
		cmd.append(SetCommand.create(editingDomain, pricingModel, PricingPackage.Literals.PRICING_MODEL__MARKET_CURVE_DATA_VERSION, version.getIdentifier()));

		
		return cmd;
	}
}
