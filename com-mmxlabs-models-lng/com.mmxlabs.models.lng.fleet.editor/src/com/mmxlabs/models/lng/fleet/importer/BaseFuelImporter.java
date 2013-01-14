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
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;
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
	@Override
	public Collection<EObject> importObject(EClass eClass, Map<String, String> row, IImportContext context) {
		final Collection<EObject> result = super.importObject(eClass, row, context);
		final BaseFuel fuel = (BaseFuel) result.iterator().next();
		
		if (row.containsKey("price")) {
			final BaseFuelCost cost = PricingFactory.eINSTANCE.createBaseFuelCost();
			cost.setFuel(fuel);
			cost.setPrice(Double.parseDouble(row.get("price")));
			
			context.doLater(new IDeferment() {
				
				@Override
				public void run(IImportContext context) {
					final PricingModel pricingModel = context.getRootObject().getSubModel(PricingModel.class);
					if (pricingModel != null) {
						pricingModel.getFleetCost().getBaseFuelPrices().add(cost);
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
	protected Map<String, String> exportObject(EObject object, final MMXRootObject root) {
		final BaseFuel bf = (BaseFuel) object;
		final Map<String, String> result = super.exportObject(object, root);
		
	
		final PricingModel pm = root.getSubModel(PricingModel.class);
		if (pm != null) {
			for (final BaseFuelCost cost : pm.getFleetCost()
					.getBaseFuelPrices()) {
				if (cost.getFuel() == bf) {
					result.put("price", Double.toString(cost.getPrice()));
					break;
				}
			}
		}
		
		return result;
	}
}
