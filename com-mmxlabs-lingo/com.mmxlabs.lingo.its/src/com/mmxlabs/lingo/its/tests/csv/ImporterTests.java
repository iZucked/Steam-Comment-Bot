/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.csv;

import java.io.ByteArrayInputStream;
import java.time.YearMonth;
import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.mmxlabs.common.csv.CSVReader;
import com.mmxlabs.lingo.its.CSVBuilder;
import com.mmxlabs.models.lng.commercial.PricingEvent;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.spotmarkets.SpotAvailability;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketGroup;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.lng.spotmarkets.editor.importers.SpotMarketImporter;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.models.util.importer.impl.DefaultImportContext;

public class ImporterTests {

	@Test
	public void testSpotMarketAvailabilityImporter() throws Exception {

		final String csvData = new CSVBuilder() //
				.makeRow() //
				.addColumn("type", "FOB Purchase") //
				.addColumn("availability.kind", "SpotAvailability")//
				.addColumn("kind", "SpotMarketGroup")

				.makeRow()

				.addColumn("availability.constant", "2") //
				.addColumn("availability.kind", "SpotAvailability") //
				.addColumn("kind", "FOBPurchasesMarket") //
				.addColumn("name", "FobMarket") //
				.addColumn("enabled", "TRUE") //
				.addColumn("minQuantity", "2000000") //
				.addColumn("maxQuantity", "3000000") //
				.addColumn("volumeLimitsUnit", "MMBTU") //
				.addColumn("pricingEvent", "START_LOAD") //
				.addColumn("cv", "22.8") //
				.addColumn("priceInfo.priceExpression", "5") //
				.addColumn("entity", "Shipping") //
				.addColumn("notionalPort", "Point Fortin") //
				.addColumn("availability.curve.2016-08", "6") //

				.build();

		try (ByteArrayInputStream is = new ByteArrayInputStream(csvData.getBytes())) {
			final CSVReader reader = new CSVReader(',', is);

			final SpotMarketImporter importer = new SpotMarketImporter();

			final DefaultImportContext context = new DefaultImportContext(',');

			final Collection<EObject> results = importer.importObjects(SpotMarketsPackage.Literals.SPOT_MARKET, reader, context);

			Assertions.assertEquals(2, results.size());
			boolean foundMarket = false;
			for (final EObject e : results) {
				if (e instanceof SpotMarketGroup) {
					final SpotMarketGroup spotMarketGroup = (SpotMarketGroup) e;

				} else if (e instanceof SpotMarket) {
					final SpotMarket spotMarket = (SpotMarket) e;

					Assertions.assertEquals(PricingEvent.START_LOAD, spotMarket.getPricingEvent());
					Assertions.assertEquals(VolumeUnits.MMBTU, spotMarket.getVolumeLimitsUnit());
					Assertions.assertEquals(2_000_000, spotMarket.getMinQuantity());
					Assertions.assertEquals(3_000_000, spotMarket.getMaxQuantity());

					final SpotAvailability availability = spotMarket.getAvailability();
					Assertions.assertNotNull(availability);
					Assertions.assertEquals(2, availability.getConstant());
					Assertions.assertEquals(1, availability.getCurve().getPoints().size());

					final IndexPoint<Integer> pt = availability.getCurve().getPoints().get(0);
					Assertions.assertEquals(YearMonth.of(2016, 8), pt.getDate());
					Assertions.assertEquals(6, (int) pt.getValue());

					foundMarket = true;
				}
			}

			Assertions.assertTrue(foundMarket);
		}
	}

}
