/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.transfers;

import com.mmxlabs.scheduler.optimiser.entities.IEntity;

public class TranferRecordAnnotation {
	public BasicTransferRecord transferRecord;
	public int tpPrice;
	public IEntity fromEntity;
	public long fromEntityRevenue;
	public long fromEntityCost;
	public IEntity toEntity;
	public long toEntityRevenue;
	public long toEntityCost;
}
