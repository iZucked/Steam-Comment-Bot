/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.importers;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.pricing.PortCost;
import com.mmxlabs.models.lng.pricing.PortCostEntry;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.util.importer.IImportContext;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter;

/**
 * @since 2.0
 */
public class PortCostImporter extends DefaultClassImporter {
	@Override
	public Collection<EObject> importObject(final EObject parent, EClass eClass,
			Map<String, String> row, IImportContext context) {
		final PortCost cost = (PortCost) super.importObject(parent, eClass, row, context).iterator().next();
		
		for (final PortCapability pc : PortCapability.values()) {
			final PortCostEntry pce = PricingFactory.eINSTANCE.createPortCostEntry();
			pce.setActivity(pc);

			if (row.containsKey(pc.name().toLowerCase() + "cost")) {
				try {
					pce.setCost(Integer.parseInt(row.get(pc.name().toLowerCase() + "cost")));
				} catch (NumberFormatException nfe) {
					
				}
			}
			
			cost.getEntries().add(pce);
		}
		
		return Collections.singleton((EObject)cost);
	}

	@Override
	protected Map<String, String> exportObject(EObject object, MMXRootObject root) {
		final Map<String, String> result = super.exportObject(object, root);
		
		for (final PortCostEntry pce : ((PortCost) object).getEntries()) {
			result.put(pce.getActivity().name() + "Cost", Integer.toString(pce.getCost()));
		}
		
		return result;
	}
	
}
