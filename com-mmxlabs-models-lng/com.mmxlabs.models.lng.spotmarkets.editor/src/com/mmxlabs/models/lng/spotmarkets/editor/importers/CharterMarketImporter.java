/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.editor.importers;

import java.text.ParseException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.csv.FieldMap;
import com.mmxlabs.common.csv.FunctionalDeferment;
import com.mmxlabs.common.csv.IFieldMap;
import com.mmxlabs.models.datetime.importers.LocalDateAttributeImporter;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.CharterOutMarket;
import com.mmxlabs.models.lng.spotmarkets.CharterOutStartDate;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsFactory;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.importer.LNGFunctionalDeferment;
import com.mmxlabs.models.util.importer.IAttributeImporter;
import com.mmxlabs.models.util.importer.IMMXExportContext;
import com.mmxlabs.models.util.importer.IMMXImportContext;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter;
import com.mmxlabs.models.util.importer.impl.SetReference;

public class CharterMarketImporter extends DefaultClassImporter {

	private static final String DEFAULTFORNOMINAL = "defaultfornominal";
	private static final String START_DATE_KEY = "charteroutstartdate";
	private final LocalDateAttributeImporter dateAttributeImporter = new LocalDateAttributeImporter();

	@Override
	public ImportResults importObject(final EObject parent, final EClass eClass, final Map<String, String> row, final IMMXImportContext context) {

		if (row.get(START_DATE_KEY) != null && !row.get(START_DATE_KEY).trim().isEmpty()) {
			final CharterOutStartDate charterOutStartDate = SpotMarketsFactory.eINSTANCE.createCharterOutStartDate();
			try {
				charterOutStartDate.setCharterOutStartDate(dateAttributeImporter.parseLocalDate(row.get(START_DATE_KEY)));
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

							market.setName(String.format("%s - %s", vesselClass, charterOutPrice));

							results.add(market);
						}
						final String charterInPrice = row.get("charterinprice");
						if (charterInPrice != null && !charterInPrice.isEmpty()) {
							final CharterInMarket market = SpotMarketsFactory.eINSTANCE.createCharterInMarket();
							context.doLater(new SetReference(market, SpotMarketsPackage.Literals.SPOT_CHARTER_MARKET__VESSEL_CLASS,
									getEReferenceLinkType(SpotMarketsPackage.Literals.SPOT_CHARTER_MARKET__VESSEL_CLASS), vesselClass, context));

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

							if (row.get(DEFAULTFORNOMINAL) != null) {
								try {
									final Boolean isDefault = Boolean.valueOf(row.get(DEFAULTFORNOMINAL));
									if (isDefault) {
										context.doLater(new LNGFunctionalDeferment(IMMXImportContext.STAGE_RESOLVE_REFERENCES, (lngScenarioModel, mmxImportContext) -> {
											final SpotMarketsModel model = ScenarioModelUtil.getSpotMarketsModel(lngScenarioModel);
											model.setDefaultNominalMarket(market);
										}));
									}
								} catch (Exception e) {
									context.createProblem(String.format("Unable to parse '%s' as boolean", row.get(DEFAULTFORNOMINAL)), true, true, true);
								}
							}

							market.setName(String.format("%s - %s", vesselClass, charterInPrice));

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
					if (instance instanceof CharterInMarket && row.get(DEFAULTFORNOMINAL) != null) {
						final CharterInMarket market = (CharterInMarket) instance;
						try {
							final Boolean isDefault = Boolean.valueOf(row.get(DEFAULTFORNOMINAL));
							if (isDefault) {
								context.doLater(new LNGFunctionalDeferment(IMMXImportContext.STAGE_RESOLVE_REFERENCES, (lngScenarioModel, mmxImportContext) -> {
									final SpotMarketsModel model = ScenarioModelUtil.getSpotMarketsModel(lngScenarioModel);
									model.setDefaultNominalMarket(market);
								}));
							}
						} catch (Exception e) {
							context.createProblem(String.format("Unable to parse '%s' as boolean", row.get(DEFAULTFORNOMINAL)), true, true, true);
						}
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

			if (obj instanceof CharterOutStartDate) {
				final CharterOutStartDate charterOutStartDate = (CharterOutStartDate) obj;
				final Map<String, String> dateRow = new HashMap<String, String>();

				dateRow.put(START_DATE_KEY, dateAttributeImporter.formatLocalDate(charterOutStartDate.getCharterOutStartDate()));
				exportedObjects.add(dateRow);
			} else {
				generalExportObject.add(obj);
			}
		}

		exportedObjects.addAll(super.exportObjects(generalExportObject, context));
		return exportedObjects;
	}

	@Override
	protected Map<String, String> exportObject(@NonNull final EObject object, @NonNull final IMMXExportContext context) {
		final Map<String, String> result = super.exportObject(object, context);
		if (object instanceof CharterInMarket) {
			SpotMarketsModel spotMarketsModel = (SpotMarketsModel) object.eContainer();
			if (object == spotMarketsModel.getDefaultNominalMarket()) {
				result.put(DEFAULTFORNOMINAL, Boolean.TRUE.toString());
			}
		}

		return result;
	}
}
