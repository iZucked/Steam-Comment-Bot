/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.importer;

import java.util.Collection;
import java.util.Map;

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
import com.mmxlabs.models.util.importer.IImportContext;
import com.mmxlabs.models.util.importer.IImportContext.IDeferment;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter;

/**
 * Base fuel importer which also handles importing the pricing model info
 * 
 * @author hinton
 * @since 2.0
 * 
 */
public class BaseFuelImporter extends DefaultClassImporter {
	private static String indexKey = "index";
	
	@Override
	public Collection<EObject> importObject(final EClass eClass, final Map<String, String> row, final IImportContext context) {
		final Collection<EObject> result = super.importObject(eClass, row, context);
		final BaseFuel fuel = (BaseFuel) result.iterator().next();

		if (row.containsKey(indexKey)) {
			final BaseFuelCost cost = PricingFactory.eINSTANCE.createBaseFuelCost();
			cost.setFuel(fuel);
			final String indexName = row.get(indexKey);
			//cost.setPrice(Double.parseDouble(row.get("price")));

			context.doLater(new IDeferment() {

				@Override
				public void run(final IImportContext context) {
					final MMXRootObject rootObject = context.getRootObject();
					if (rootObject instanceof LNGScenarioModel) {
						final PricingModel pricingModel = ((LNGScenarioModel) rootObject).getPricingModel();
						BaseFuelIndex index = (BaseFuelIndex) context.getNamedObject(indexName, PricingPackage.Literals.BASE_FUEL_INDEX);
						cost.setIndex(index);
						if (pricingModel != null) {
							pricingModel.getFleetCost().getBaseFuelPrices().add(cost);
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
	protected Map<String, String> exportObject(final EObject object, final MMXRootObject rootObject) {
		final BaseFuel bf = (BaseFuel) object;
		final Map<String, String> result = super.exportObject(object, rootObject);
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
