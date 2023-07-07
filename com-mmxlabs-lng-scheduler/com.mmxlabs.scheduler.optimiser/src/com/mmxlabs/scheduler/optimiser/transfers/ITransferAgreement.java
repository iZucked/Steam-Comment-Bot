/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.transfers;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.scheduler.optimiser.entities.IEntity;

@NonNullByDefault
public interface ITransferAgreement {
	String priceExpression();
	IEntity fromEntity();
	IEntity toEntity();
}
