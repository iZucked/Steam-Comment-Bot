package com.mmxlabs.lingo.its.tests.csv;

import java.io.ByteArrayInputStream;
import java.time.YearMonth;
import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.junit.Assert;
import org.junit.Test;

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

			Assert.assertEquals(2, results.size());
			boolean foundMarket = false;
			for (final EObject e : results) {
				if (e instanceof SpotMarketGroup) {
					final SpotMarketGroup spotMarketGroup = (SpotMarketGroup) e;

				} else if (e instanceof SpotMarket) {
					final SpotMarket spotMarket = (SpotMarket) e;

					Assert.assertEquals(PricingEvent.START_LOAD, spotMarket.getPricingEvent());
					Assert.assertEquals(VolumeUnits.MMBTU, spotMarket.getVolumeLimitsUnit());
					Assert.assertEquals(2_000_000, spotMarket.getMinQuantity());
					Assert.assertEquals(3_000_000, spotMarket.getMaxQuantity());

					final SpotAvailability availability = spotMarket.getAvailability();
					Assert.assertNotNull(availability);
					Assert.assertEquals(2, availability.getConstant());
					Assert.assertEquals(1, availability.getCurve().getPoints().size());

					final IndexPoint<Integer> pt = availability.getCurve().getPoints().get(0);
					Assert.assertEquals(YearMonth.of(2016, 8), pt.getDate());
					Assert.assertEquals(6, (int) pt.getValue());

					foundMarket = true;
				}
			}

			Assert.assertTrue(foundMarket);
		}
	}

}
