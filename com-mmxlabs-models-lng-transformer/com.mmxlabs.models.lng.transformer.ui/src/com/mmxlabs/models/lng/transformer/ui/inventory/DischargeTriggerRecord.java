/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.inventory;

import java.time.LocalDate;

import com.mmxlabs.models.lng.cargo.Inventory;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.types.VolumeUnits;

public class DischargeTriggerRecord {
	public Inventory inventory;
	public SalesContract contract;
	public Integer minQuantity;
	public Integer maxQuantity;
	public VolumeUnits volumeUnits;
	public LocalDate cutOffDate;
	public Integer trigger;
	public Integer matchingFlexibilityDays;
}
