/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.importWizards.portcosts;

import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.lng.pricing.CostModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.ui.actions.SimpleImportAction;
import com.mmxlabs.models.util.Activator;
import com.mmxlabs.models.util.importer.IClassImporter;

public class ImportPortCostsAction extends SimpleImportAction {
	
	public ImportPortCostsAction(final ImportHooksProvider iph, final CostModel container) {
		super(iph, container, PricingPackage.Literals.COST_MODEL__PORT_COSTS);
	}

	@Override
	protected IClassImporter getImporter(final EReference containment) {
		IClassImporter importer = Activator.getDefault().getImporterRegistry().getClassImporter(PricingPackage.eINSTANCE.getPortCost());
		return importer;
	}
}
