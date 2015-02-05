/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.importer;

import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.pricing.BaseFuelIndex;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.util.importer.IExportContext;
import com.mmxlabs.models.util.importer.IImportContext;
import com.mmxlabs.models.util.importer.IImportContext.IDeferment;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter;

/**
 * Base fuel importer which also handles importing the pricing model info
 * 
 * @author hinton
 * 
 */
public class BaseFuelImporter extends DefaultClassImporter {
	private static String indexKey = "index";

	@Override
	public ImportResults importObject(final EObject parent, final EClass eClass, final Map<String, String> row, final IImportContext context) {
		final ImportResults result = super.importObject(parent, eClass, row, context);
		final BaseFuel fuel = (BaseFuel) result.importedObject;

		if (row.containsKey(indexKey)) {

			final String indexName = row.get(indexKey);
			// cost.setPrice(Double.parseDouble(row.get("price")));

			context.doLater(new IDeferment() {

				@Override
				public void run(final IImportContext context) {
					final MMXRootObject rootObject = context.getRootObject();
					if (rootObject instanceof LNGScenarioModel) {
						final PricingModel pricingModel = ((LNGScenarioModel) rootObject).getPricingModel();
						final BaseFuelIndex index = (BaseFuelIndex) context.getNamedObject(indexName, PricingPackage.Literals.BASE_FUEL_INDEX);
						if (pricingModel != null) {

							final EList<BaseFuelCost> baseFuelPrices = pricingModel.getFleetCost().getBaseFuelPrices();
							for (final BaseFuelCost baseFuelCost : baseFuelPrices) {
								if (baseFuelCost.getFuel() == fuel) {
									baseFuelCost.setIndex(index);
									return;
								}
							}
							final BaseFuelCost baseFuelCost = PricingFactory.eINSTANCE.createBaseFuelCost();
							baseFuelCost.setFuel(fuel);
							baseFuelCost.setIndex(index);

							baseFuelPrices.add(baseFuelCost);
						}
					}
				}

				@Override
				public int getStage() {
					return IImportContext.STAGE_MODIFY_SUBMODELS;
				}
			});
		} else {
			context.addProblem(context.createProblem("Base fuel unit price missing", true, true, true));
		}

		return result;
	}

	@Override
	protected Map<String, String> exportObject(final EObject object, final IExportContext context) {
		final BaseFuel bf = (BaseFuel) object;
		final Map<String, String> result = super.exportObject(object, context);
		final MMXRootObject rootObject = context.getRootObject();
		if (rootObject instanceof LNGScenarioModel) {
			final PricingModel pm = ((LNGScenarioModel) rootObject).getPricingModel();
			if (pm != null) {
				for (final BaseFuelCost cost : pm.getFleetCost().getBaseFuelPrices()) {
					if (cost.getFuel() == bf) {
						result.put("index", cost.getIndex().getName());
						break;
					}
				}
			}
		}

		return result;
	}
}
