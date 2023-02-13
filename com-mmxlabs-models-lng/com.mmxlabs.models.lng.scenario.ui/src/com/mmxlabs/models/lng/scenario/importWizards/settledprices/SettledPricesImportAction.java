/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.importWizards.settledprices;

import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.importers.DatePointImporter;
import com.mmxlabs.models.lng.ui.actions.SimpleImportAction;
import com.mmxlabs.models.util.importer.IClassImporter;

public class SettledPricesImportAction extends SimpleImportAction {

	public SettledPricesImportAction(final ImportHooksProvider iph, final PricingModel container) {
		super(iph, container, PricingPackage.Literals.PRICING_MODEL__SETTLED_PRICES);
	}

	@Override
	protected IClassImporter getImporter(final EReference containment) {
		DatePointImporter importer = new DatePointImporter();
		return importer;
	}

}
