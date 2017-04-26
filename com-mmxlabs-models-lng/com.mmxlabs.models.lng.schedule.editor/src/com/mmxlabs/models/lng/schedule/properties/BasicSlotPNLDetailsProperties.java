/**
Non * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.properties;

import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.schedule.BasicSlotPNLDetails;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.properties.DetailProperty;
import com.mmxlabs.models.ui.properties.factory.DetailPropertyFactoryUtil;
import com.mmxlabs.models.ui.properties.ui.StringFormatLabelProvider;

public class BasicSlotPNLDetailsProperties {

	public static void populateTree(final DetailProperty details, final Slot slot, @Nullable final SlotAllocation slotAllocation, @Nullable final OpenSlotAllocation openSlotAllocation,
			@Nullable final BasicSlotPNLDetails slotPNLDetails, @Nullable final MMXRootObject rootObject) {

		if (slotAllocation != null) {
			DetailPropertyFactoryUtil.addDetailProperty("Volume", "", "", "m³", slotAllocation.getVolumeTransferred(), new StringFormatLabelProvider("%,d"), details);
			DetailPropertyFactoryUtil.addDetailProperty("Volume", "", "", "mmBtu", slotAllocation.getEnergyTransferred(), new StringFormatLabelProvider("%,d"), details);
			DetailPropertyFactoryUtil.addDetailProperty("Volume", "", "$", "", slotAllocation.getVolumeValue(), new StringFormatLabelProvider("%,d"), details);
			DetailPropertyFactoryUtil.addDetailProperty("Price", "", "$", "/mmBtu", slotAllocation.getPrice(), new StringFormatLabelProvider("%.3f"), details);
		}
		if (slotPNLDetails != null) {
			{
				final int additionalPNL = slotPNLDetails.getAdditionalPNL();
				DetailPropertyFactoryUtil.addDetailProperty("Additional P&L", "", "$", "", additionalPNL, new StringFormatLabelProvider("%,d"), details);
			}
			if (slotPNLDetails.isSetCancellationFees()) {
				final int cancellationFees = slotPNLDetails.getCancellationFees();
				DetailPropertyFactoryUtil.addDetailProperty("Cancellation Fees", "", "$", "", cancellationFees, new StringFormatLabelProvider("%,d"), details);
			}
			if (slotPNLDetails.isSetHedgingValue()) {
				final int hedgingValue = slotPNLDetails.getHedgingValue();
				DetailPropertyFactoryUtil.addDetailProperty("Hedging Value", "", "$", "", hedgingValue, new StringFormatLabelProvider("%,d"), details);
			}
			if (slotPNLDetails.isSetMiscCostsValue()) {
				final int miscCostsValue = slotPNLDetails.getMiscCostsValue();
				DetailPropertyFactoryUtil.addDetailProperty("Misc Costs Value", "", "$", "", miscCostsValue, new StringFormatLabelProvider("%,d"), details);
			}
		}
	}
}
