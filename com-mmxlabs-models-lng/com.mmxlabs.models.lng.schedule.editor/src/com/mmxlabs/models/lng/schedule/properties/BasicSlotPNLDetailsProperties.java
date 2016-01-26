/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.properties;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.schedule.BasicSlotPNLDetails;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.properties.DetailProperty;
import com.mmxlabs.models.ui.properties.PropertiesFactory;
import com.mmxlabs.models.ui.properties.factory.AbstractDetailPropertyFactory;
import com.mmxlabs.models.ui.properties.ui.StringFormatLabelProvider;

public class BasicSlotPNLDetailsProperties extends AbstractDetailPropertyFactory {

	private static final String CATEGORY_PNL = "pnl";

	@Override
	@Nullable
	public DetailProperty createProperties(@NonNull final EObject eObject) {
		if (eObject instanceof BasicSlotPNLDetails) {
			final BasicSlotPNLDetails slotPNLDetails = (BasicSlotPNLDetails) eObject;
			return createTree(slotPNLDetails, null);
		}

		return null;
	}

	private DetailProperty createTree(@NonNull final BasicSlotPNLDetails slotPNLDetails, @Nullable final MMXRootObject rootObject) {

		final DetailProperty details = PropertiesFactory.eINSTANCE.createDetailProperty();
		details.setName("General");

		boolean hasDetails = false;
		if (slotPNLDetails.isSetCancellationFees()) {
			final int additionalPNL = slotPNLDetails.getAdditionalPNL();

			addDetailProperty("Additional P&L", "", "$", "", additionalPNL, new StringFormatLabelProvider("%,d"), details);

			hasDetails = true;
		}
		if (slotPNLDetails.isSetCancellationFees()) {
			final int cancellationFees = slotPNLDetails.getCancellationFees();

			addDetailProperty("Cancellation Fees", "", "$", "", cancellationFees, new StringFormatLabelProvider("%,d"), details);

			hasDetails = true;
		}
		if (slotPNLDetails.isSetHedgingValue()) {
			final int hedgingValue = slotPNLDetails.getHedgingValue();
			addDetailProperty("Hedging Value", "", "$", "", hedgingValue, new StringFormatLabelProvider("%,d"), details);
			hasDetails = true;
		}

		if (hasDetails) {
			return details;
		}
		return null;
	}
}
