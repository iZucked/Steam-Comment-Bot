/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.io.financial;

import org.eclipse.emf.ecore.util.EcoreUtil;

import com.mmxlabs.lngdataserver.integration.models.financial.settled.SettledPriceCurve;
import com.mmxlabs.lngdataserver.integration.models.financial.settled.SettledPriceEntry;
import com.mmxlabs.lngdataserver.integration.models.financial.settled.SettledPricesVersion;
import com.mmxlabs.models.lng.pricing.DatePointContainer;
import com.mmxlabs.models.lng.pricing.PricingModel;

public class SettledPricesFromScenarioCopier {
	private SettledPricesFromScenarioCopier() {

	}

	public static SettledPricesVersion generateVersion(final PricingModel pricingModel) {

		final SettledPricesVersion version = new SettledPricesVersion();

		version.setIdentifier(pricingModel.getSettledPricesVersionRecord().getVersion());
		version.setCreatedBy(pricingModel.getSettledPricesVersionRecord().getCreatedBy());
		version.setCreatedAt(pricingModel.getSettledPricesVersionRecord().getCreatedAt());

		for (final DatePointContainer c : pricingModel.getSettledPrices()) {
			final SettledPriceCurve curve = new SettledPriceCurve();
			curve.setName(c.getName());

			c.getPoints().stream()//
					.map(pt -> {
						final SettledPriceEntry entry = new SettledPriceEntry();
						entry.setDate(pt.getDate());
						entry.setValue(pt.getValue());
						return entry;
					}).forEach(pt -> curve.getEntries().add(pt));

			version.getCurves().add(curve);
		}

		return version;
	}
}
