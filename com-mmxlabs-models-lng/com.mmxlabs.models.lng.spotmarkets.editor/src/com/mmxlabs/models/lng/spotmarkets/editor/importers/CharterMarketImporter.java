/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.editor.importers;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.common.csv.FieldMap;
import com.mmxlabs.common.csv.IFieldMap;
import com.mmxlabs.models.datetime.importers.LocalDateAttributeImporter;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.CharterOutMarket;
import com.mmxlabs.models.lng.spotmarkets.CharterOutMarketParameters;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsFactory;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.util.importer.IAttributeImporter;
import com.mmxlabs.models.util.importer.IMMXExportContext;
import com.mmxlabs.models.util.importer.IMMXImportContext;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter;
import com.mmxlabs.models.util.importer.impl.OtherImportData;
import com.mmxlabs.models.util.importer.impl.SetReference;

public class CharterMarketImporter extends DefaultClassImporter {

	private static final String START_DATE_KEY = "charteroutstartdate";
	private static final String END_DATE_KEY = "charteroutenddate";

	private final LocalDateAttributeImporter dateAttributeImporter = new LocalDateAttributeImporter();

	@Override
	public ImportResults importObject(final EObject parent, final EClass eClass, final Map<String, String> row, final IMMXImportContext context) {

		if (row.get(START_DATE_KEY) != null && !row.get(START_DATE_KEY).trim().isEmpty()) {
			return OtherImportData.parseField(row, context, START_DATE_KEY, "charter out start date", SpotMarketsPackage.Literals.CHARTER_OUT_MARKET_PARAMETERS__CHARTER_OUT_START_DATE,
					dateAttributeImporter::parseLocalDate);
		}
		if (row.get(END_DATE_KEY) != null && !row.get(END_DATE_KEY).trim().isEmpty()) {
			return OtherImportData.parseField(row, context, END_DATE_KEY, "charter out end date", SpotMarketsPackage.Literals.CHARTER_OUT_MARKET_PARAMETERS__CHARTER_OUT_END_DATE,
					dateAttributeImporter::parseLocalDate);
		} else {

			final String kind = row.get(KIND_KEY);
			if ("CharterCostModel".equals(kind)) {
				final String vesselsStr = row.get("vesselclasses");
				if (vesselsStr != null) {
					final ImportResults results = new ImportResults(null);

					final String[] vessels = vesselsStr.split(",");
					for (final String vessel : vessels) {
						final String charterOutPrice = row.get("charteroutprice");
						if (charterOutPrice != null && !charterOutPrice.isEmpty()) {
							final CharterOutMarket market = SpotMarketsFactory.eINSTANCE.createCharterOutMarket();

							context.doLater(new SetReference(market, SpotMarketsPackage.Literals.CHARTER_OUT_MARKET__VESSELS,
									getEReferenceLinkType(market, SpotMarketsPackage.Literals.CHARTER_OUT_MARKET__VESSELS), vessel, context));

							market.setCharterOutRate(charterOutPrice);

							if (row.get("enabled") != null) {
								final IAttributeImporter ai = importerRegistry.getAttributeImporter(SpotMarketsPackage.Literals.SPOT_CHARTER_MARKET__ENABLED.getEAttributeType());
								if (ai != null) {
									ai.setAttribute(market, SpotMarketsPackage.Literals.SPOT_CHARTER_MARKET__ENABLED, row.get("enabled"), context);
								}
							}
							if (row.get("mincharteroutduration") != null) {
								final IAttributeImporter ai = importerRegistry.getAttributeImporter(SpotMarketsPackage.Literals.CHARTER_OUT_MARKET__MIN_CHARTER_OUT_DURATION.getEAttributeType());
								if (ai != null) {
									ai.setAttribute(market, SpotMarketsPackage.Literals.CHARTER_OUT_MARKET__MIN_CHARTER_OUT_DURATION, row.get("mincharteroutduration"), context);
								}
							}

							market.setName(String.format("%s - %s", vessel, charterOutPrice));

							results.add(market);
						}
						final String charterInPrice = row.get("charterinprice");
						if (charterInPrice != null && !charterInPrice.isEmpty()) {
							final CharterInMarket market = SpotMarketsFactory.eINSTANCE.createCharterInMarket();
							context.doLater(new SetReference(market, SpotMarketsPackage.Literals.CHARTER_IN_MARKET__VESSEL,
									getEReferenceLinkType(market, SpotMarketsPackage.Literals.CHARTER_IN_MARKET__VESSEL), vessel, context));

							market.setCharterInRate(charterInPrice);

							if (row.get("enabled") != null) {
								final IAttributeImporter ai = importerRegistry.getAttributeImporter(SpotMarketsPackage.Literals.SPOT_CHARTER_MARKET__ENABLED.getEAttributeType());
								if (ai != null) {
									ai.setAttribute(market, SpotMarketsPackage.Literals.SPOT_CHARTER_MARKET__ENABLED, row.get("enabled"), context);
								}
							}
							if (row.get("spotchartercount") != null) {
								final IAttributeImporter ai = importerRegistry.getAttributeImporter(SpotMarketsPackage.Literals.CHARTER_IN_MARKET__SPOT_CHARTER_COUNT.getEAttributeType());
								if (ai != null) {
									ai.setAttribute(market, SpotMarketsPackage.Literals.CHARTER_IN_MARKET__SPOT_CHARTER_COUNT, row.get("spotchartercount"), context);
								}
							}

							market.setName(String.format("%s - %s", vessel, charterInPrice));

							results.add(market);
						}
					}
					return results;
				}
				return new ImportResults(null);
			} else {
				final EClass rowClass = getTrueOutputClass(eClass, kind);
				try {
					final EObject instance = rowClass.getEPackage().getEFactoryInstance().create(rowClass);
					final ImportResults results = new ImportResults(instance);
					importAttributes(row, context, rowClass, instance);
					if (row instanceof IFieldMap) {
						importReferences((IFieldMap) row, context, rowClass, instance);
					} else {
						importReferences(new FieldMap(row), context, rowClass, instance);
					}
					return results;
				} catch (final IllegalArgumentException illegal) {
					context.addProblem(context.createProblem(kind + " is not a valid kind of " + rowClass.getName(), true, true, true));
					return new ImportResults(null);
				}
			}
		}
	}

	@Override
	public Collection<Map<String, String>> exportObjects(final Collection<? extends EObject> objects, final IMMXExportContext context) {

		final Collection<Map<String, String>> exportedObjects = new LinkedList<>();

		final List<EObject> generalExportObject = new LinkedList<>();
		for (final EObject obj : objects) {

			if (obj instanceof CharterOutMarketParameters) {
				final CharterOutMarketParameters charterOutMarketParameters = (CharterOutMarketParameters) obj;
				final Map<String, String> dateRow = new HashMap<>();

				if (charterOutMarketParameters.isSetCharterOutStartDate()) {
					dateRow.put(START_DATE_KEY, dateAttributeImporter.formatLocalDate(charterOutMarketParameters.getCharterOutStartDate()));
					exportedObjects.add(dateRow);
				}
				if (charterOutMarketParameters.isSetCharterOutEndDate()) {
					dateRow.put(END_DATE_KEY, dateAttributeImporter.formatLocalDate(charterOutMarketParameters.getCharterOutEndDate()));
					exportedObjects.add(dateRow);
				}
			} else {
				generalExportObject.add(obj);
			}
		}

		exportedObjects.addAll(super.exportObjects(generalExportObject, context));
		return exportedObjects;
	}
}