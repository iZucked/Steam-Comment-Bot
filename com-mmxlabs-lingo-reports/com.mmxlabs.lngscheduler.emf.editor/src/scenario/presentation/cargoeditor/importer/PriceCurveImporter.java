/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.importer;

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

/**
 * Markets have a non-standard import format which has columns for dates.
 * This method works by 
 * 
 * @author Tom Hinton
 * 
 */
public class PriceCurveImporter extends EObjectImporter {
	@Override
	protected void populateContainment(final EObject result,
			final EReference reference, final Map<String, String> fields,
			final Collection<DeferredReference> deferredReferences,
			final NamedObjectRegistry registry) {
		if (reference.equals(MarketPackage.eINSTANCE
				.getStepwisePriceCurve_Prices())) {
			// find all fields which can parse as a date, and use them to
			// populate the prices.
			
			final DateTimeParser dtp = DateTimeParser.getInstance();
			final List<StepwisePrice> prices = new ArrayList<StepwisePrice>();
			for (final String field : fields.keySet()) {
				final Date dt = dtp.parseDate(field);
				if (dt != null) {
					final String value = fields.get(field);
					final StepwisePrice price = MarketPackage.eINSTANCE
							.getMarketFactory().createStepwisePrice();
					price.setDate(dt);
					price.setPriceFromDate(Float.parseFloat(value));
					prices.add(price);
				}
			}
			
			Collections.sort(prices, new Comparator<StepwisePrice>() {
				@Override
				public int compare(StepwisePrice arg0, StepwisePrice arg1) {
					return arg0.getDate().compareTo(arg1.getDate());
				}				
			});

			((EList<EObject>) result.eGet(reference)).addAll(prices);
		} else {
			super.populateContainment(result, reference, fields,
					deferredReferences, registry);
		}
	}
}
