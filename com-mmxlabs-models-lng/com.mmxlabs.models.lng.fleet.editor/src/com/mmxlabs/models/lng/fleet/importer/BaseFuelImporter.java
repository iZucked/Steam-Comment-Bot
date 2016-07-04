/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.importer;

import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.csv.IDeferment;
import com.mmxlabs.common.csv.IImportContext;
import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.pricing.BaseFuelIndex;
import com.mmxlabs.models.lng.pricing.CostModel;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.util.importer.IMMXExportContext;
import com.mmxlabs.models.util.importer.IMMXImportContext;
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
	public ImportResults importObject(final EObject parent, final EClass eClass, final Map<String, String> row, final IMMXImportContext context) {
		final ImportResults result = super.importObject(parent, eClass, row, context);
		final BaseFuel fuel = (BaseFuel) result.importedObject;

		if (row.containsKey(indexKey)) {

			final String indexName = row.get(indexKey);
			assert indexName != null;

			context.doLater(new IDeferment() {

				@Override
				public void run(@NonNull final IImportContext importContext) {
					final IMMXImportContext context = (IMMXImportContext) importContext;
					final MMXRootObject rootObject = context.getRootObject();
					if (rootObject instanceof LNGScenarioModel) {
						final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
						final CostModel costModel = ScenarioModelUtil.getCostModel(lngScenarioModel);
						final BaseFuelIndex index = (BaseFuelIndex) context.getNamedObject(indexName, PricingPackage.Literals.BASE_FUEL_INDEX);

						final EList<BaseFuelCost> baseFuelPrices = costModel.getBaseFuelCosts();
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

				@Override
				public int getStage() {
					return IMMXImportContext.STAGE_MODIFY_SUBMODELS;
				}
			});
		} else {
			context.addProblem(context.createProblem("Base fuel unit price missing", true, true, true));
		}

		return result;
	}

	@Override
	protected Map<String, String> exportObject(@NonNull final EObject object, @NonNull final IMMXExportContext context) {
		final BaseFuel bf = (BaseFuel) object;
		final Map<String, String> result = super.exportObject(object, context);
		final MMXRootObject rootObject = context.getRootObject();
		if (rootObject instanceof LNGScenarioModel) {
			final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
			final CostModel costModel = ScenarioModelUtil.getCostModel(lngScenarioModel);
			for (final BaseFuelCost cost : costModel.getBaseFuelCosts()) {
				if (cost.getFuel() == bf) {
					final BaseFuelIndex index = cost.getIndex();
					if (index != null) {
						result.put("index", index.getName());
					}
					break;
				}
			}
		}

		return result;
	}
}
