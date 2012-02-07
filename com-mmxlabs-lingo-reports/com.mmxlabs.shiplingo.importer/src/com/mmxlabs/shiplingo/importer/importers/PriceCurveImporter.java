/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.importer.importers;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import scenario.market.MarketPackage;
import scenario.market.StepwisePrice;
import scenario.market.StepwisePriceCurve;

/**
 * Markets have a non-standard import format which has columns for dates.
 * 
 * @author Tom Hinton
 * 
 */
public class PriceCurveImporter extends EObjectImporter {
	@Override
	protected void populateContainment(final String prefix, final EObject result, final EReference reference, final Map<String, String> fields, final Collection<DeferredReference> deferredReferences,
			final NamedObjectRegistry registry) {
		if (reference.equals(MarketPackage.eINSTANCE.getStepwisePriceCurve_Prices())) {
			// find all fields which can parse as a date, and use them to
			// populate the prices.

			final DateTimeParser dtp = DateTimeParser.getInstance();
			final List<StepwisePrice> prices = new ArrayList<StepwisePrice>();
			for (final String field : fields.keySet()) {
				try {
					final Date dt = dtp.parseDate(field);
					if (dt != null) {
						final String value = fields.get(field);
						if ((value == null) || value.isEmpty()) {
							continue;
						}
						final StepwisePrice price = MarketPackage.eINSTANCE.getMarketFactory().createStepwisePrice();
						price.setDate(dt);
						price.setPriceFromDate(Float.parseFloat(value));
						prices.add(price);
					}
				} catch (final ParseException pe) {

				}
			}

			Collections.sort(prices, new Comparator<StepwisePrice>() {
				@Override
				public int compare(final StepwisePrice arg0, final StepwisePrice arg1) {
					return arg0.getDate().compareTo(arg1.getDate());
				}
			});

			((EList<EObject>) result.eGet(reference)).addAll(prices);
		} else {
			super.populateContainment(prefix, result, reference, fields, deferredReferences, registry);
		}
	}

	@Override
	protected void flattenMultiContainment(final EObject object, final String prefix, final EReference reference, final Map<String, String> output) {
		if (reference.equals(MarketPackage.eINSTANCE.getStepwisePriceCurve_Prices())) {
			for (final StepwisePrice price : ((StepwisePriceCurve) object).getPrices()) {
				output.put(DateTimeParser.getInstance().formatDate(price.getDate(), "UTC"), String.format("%3g", price.getPriceFromDate()));
			}
		} else {
			super.flattenMultiContainment(object, prefix, reference, output);
		}
	}
}
