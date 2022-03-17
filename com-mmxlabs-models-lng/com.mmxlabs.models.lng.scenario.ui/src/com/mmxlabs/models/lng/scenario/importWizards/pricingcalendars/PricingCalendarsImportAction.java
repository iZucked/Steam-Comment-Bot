/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.importWizards.pricingcalendars;

import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.importers.PricingCalendarImporter;
import com.mmxlabs.models.lng.ui.actions.SimpleImportAction;
import com.mmxlabs.models.util.importer.IClassImporter;

public class PricingCalendarsImportAction extends SimpleImportAction {

	public PricingCalendarsImportAction(final ImportHooksProvider iph, final PricingModel container) {
		super(iph, container, PricingPackage.Literals.PRICING_MODEL__PRICING_CALENDARS);
	}

	@Override
	protected IClassImporter getImporter(final EReference containment) {
		PricingCalendarImporter importer = new PricingCalendarImporter();
		return importer;
	}
}
