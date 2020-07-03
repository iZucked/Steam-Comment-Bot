/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.importWizards.holidaycalendars;

import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.importers.HolidayCalendarImporter;
import com.mmxlabs.models.lng.ui.actions.SimpleImportAction;
import com.mmxlabs.models.util.importer.IClassImporter;

public class HolidayCalendarsImportAction extends SimpleImportAction {

	public HolidayCalendarsImportAction(final ImportHooksProvider iph, final PricingModel container) {
		super(iph, container, PricingPackage.Literals.PRICING_MODEL__HOLIDAY_CALENDARS);
	}

	@Override
	protected IClassImporter getImporter(final EReference containment) {
		HolidayCalendarImporter importer = new HolidayCalendarImporter();
		return importer;
	}

}
