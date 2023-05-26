/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.transfers;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.scheduler.optimiser.entities.IEntity;

/**
 * Basic representation of the Transfer Agreement
 * @author FM
 *
 */
@NonNullByDefault
public record BasicTransferAgreement(String name, IEntity fromEntity, IEntity toEntity, String priceExpression) implements ITransferAgreement{
	
	@Override
	public boolean equals(@Nullable Object object) {
		if (object == this) {
			return true;
		} else if (object instanceof BasicTransferAgreement otherAgreement) {
			if (otherAgreement.name().equalsIgnoreCase(name)//
					&& otherAgreement.fromEntity() == fromEntity//
					&& otherAgreement.toEntity() == toEntity//
					&& otherAgreement.priceExpression.equalsIgnoreCase(priceExpression)) {
				return true;
			}
		}
		return false;
	}
}
