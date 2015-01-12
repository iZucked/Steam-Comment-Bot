/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.editor.importers;

import java.text.ParseException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.CharterOutMarket;
import com.mmxlabs.models.lng.spotmarkets.CharterOutStartDate;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsFactory;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.ui.dates.DateAttributeImporter;
import com.mmxlabs.models.util.importer.FieldMap;
import com.mmxlabs.models.util.importer.IAttributeImporter;
import com.mmxlabs.models.util.importer.IExportContext;
import com.mmxlabs.models.util.importer.IFieldMap;
import com.mmxlabs.models.util.importer.IImportContext;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter;
import com.mmxlabs.models.util.importer.impl.SetReference;

public class CharterMarketImporter extends DefaultClassImporter {

	private static final String START_DATE_KEY = "charteroutstartdate";
	private final DateAttributeImporter dateAttributeImporter = new DateAttributeImporter();

	@Override
	public ImportResults importObject(final EObject parent, final EClass eClass, final Map<String, String> row, final IImportContext context) {

		if (row.get(START_DATE_KEY) != null && !row.get(START_DATE_KEY).trim().isEmpty()) {
			final CharterOutStartDate charterOutStartDate = SpotMarketsFactory.eINSTANCE.createCharterOutStartDate();
			try {
				charterOutStartDate.setCharterOutStartDate(dateAttributeImporter.parseDate(row.get(START_DATE_KEY)));
			} catch (final ParseException e) {
				context.addProblem(context.createProblem("Unable to parse date " + row.get(START_DATE_KEY), true, true, true));
				return new ImportResults(null);
			}
			final ImportResults results = new ImportResults(charterOutStartDate);
			return results;
		} else {

			final String kind = row.get(KIND_KEY);
			if ("CharterCostModel".equals(kind)) {
				final String vesselClassesStr = row.get("vesselclasses");
				if (vesselClassesStr != null) {
					final ImportResults results = new ImportResults(null);

					final String[] vesselClasses = vesselClassesStr.split(",");
					for (final String vesselClass : vesselClasses) {
						final String charterOutPrice = row.get("charteroutprice");
						if (charterOutPrice != null && !charterOutPrice.isEmpty()) {
							final CharterOutMarket market = SpotMarketsFactory.eINSTANCE.createCharterOutMarket();
							context.doLater(new SetReference(market, SpotMarketsPackage.Literals.SPOT_CHARTER_MARKET__VESSEL_CLASS,
									getEReferenceLinkType(SpotMarketsPackage.Literals.SPOT_CHARTER_MARKET__VESSEL_CLASS), vesselClass, context));
							context.doLater(new SetReference(market, SpotMarketsPackage.Literals.CHARTER_OUT_MARKET__CHARTER_OUT_PRICE,
									getEReferenceLinkType(SpotMarketsPackage.Literals.CHARTER_OUT_MARKET__CHARTER_OUT_PRICE), charterOutPrice, context));
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

							market.setName(String.format("%s - %s", vesselClass, charterOutPrice));

							results.add(market);
						}
						final String charterInPrice = row.get("charterinprice");
						if (charterInPrice != null && !charterInPrice.isEmpty()) {
							final CharterInMarket market = SpotMarketsFactory.eINSTANCE.createCharterInMarket();
							context.doLater(new SetReference(market, SpotMarketsPackage.Literals.SPOT_CHARTER_MARKET__VESSEL_CLASS,
									getEReferenceLinkType(SpotMarketsPackage.Literals.SPOT_CHARTER_MARKET__VESSEL_CLASS), vesselClass, context));

							context.doLater(new SetReference(market, SpotMarketsPackage.Literals.CHARTER_IN_MARKET__CHARTER_IN_PRICE,
									getEReferenceLinkType(SpotMarketsPackage.Literals.CHARTER_IN_MARKET__CHARTER_IN_PRICE), charterInPrice, context));

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

							market.setName(String.format("%s - %s", vesselClass, charterInPrice));

							results.add(market);
						}
					}
					return results;
				}
				return null;
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
	public Collection<Map<String, String>> exportObjects(final Collection<? extends EObject> objects, final IExportContext context) {

		final Collection<Map<String, String>> exportedObjects = new LinkedList<>();

		final List<EObject> generalExportObject = new LinkedList<>();
		for (final EObject obj : objects) {
			if (obj instanceof CharterOutStartDate) {
				final CharterOutStartDate charterOutStartDate = (CharterOutStartDate) obj;
				final Map<String, String> dateRow = new HashMap<String, String>();

				dateRow.put(START_DATE_KEY, dateAttributeImporter.formatDate(charterOutStartDate.getCharterOutStartDate(), TimeZone.getTimeZone("UTC"), false));
				exportedObjects.add(dateRow);
			} else {
				generalExportObject.add(obj);
			}
		}

		exportedObjects.addAll(super.exportObjects(generalExportObject, context));
		return exportedObjects;
	}
}
