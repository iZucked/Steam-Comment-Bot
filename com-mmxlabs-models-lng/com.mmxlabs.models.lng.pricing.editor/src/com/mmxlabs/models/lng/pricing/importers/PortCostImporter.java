/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.importers;

import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.models.lng.pricing.PortCost;
import com.mmxlabs.models.lng.pricing.PortCostEntry;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.util.importer.IMMXExportContext;
import com.mmxlabs.models.util.importer.IMMXImportContext;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter;

/**
 */
public class PortCostImporter extends DefaultClassImporter {
	@Override
	public ImportResults importObject(final EObject parent, final EClass eClass, final Map<String, String> row, final IMMXImportContext context) {
		final PortCost cost = (PortCost) super.importObject(parent, eClass, row, context).importedObject;

		for (final PortCapability pc : PortCapability.values()) {
			final PortCostEntry pce = PricingFactory.eINSTANCE.createPortCostEntry();
			pce.setActivity(pc);

			if (row.containsKey(pc.name().toLowerCase() + "cost")) {
				try {
					pce.setCost(Integer.parseInt(row.get(pc.name().toLowerCase() + "cost")));
				} catch (final NumberFormatException nfe) {

				}
			}

			cost.getEntries().add(pce);
		}

		return new ImportResults((EObject) cost);
	}

	@Override
	protected Map<String, String> exportObject(final EObject object, final IMMXExportContext context) {
		final Map<String, String> result = super.exportObject(object, context);

		for (final PortCostEntry pce : ((PortCost) object).getEntries()) {
			result.put(pce.getActivity().name() + "Cost", Integer.toString(pce.getCost()));
		}

		return result;
	}

	@Override
	protected boolean shouldExportFeature(EStructuralFeature feature) {
		
		if (feature == PricingPackage.Literals.PORT_COST__ENTRIES) {
			return false;
		}
		
		return super.shouldExportFeature(feature);
	}
	
}
