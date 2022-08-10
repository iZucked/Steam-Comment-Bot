package com.mmxlabs.scheduler.optimiser.transfers;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.scheduler.optimiser.entities.IEntity;

/**
 * Basic representation of the Transfer Agreement
 * @author FM
 *
 */
@NonNullByDefault
public record BasicTransferAgreement(String name, IEntity fromEntity, IEntity toEntity, ICurve pricingSeries, String priceExpression) implements ITransferAgreement{
	
	@Override
	public boolean equals(@Nullable Object object) {
		if (object == this) {
			return true;
		} else if (object instanceof BasicTransferAgreement otherAgreement) {
			if (otherAgreement.name().equalsIgnoreCase(name)//
					&& otherAgreement.fromEntity() == fromEntity//
					&& otherAgreement.toEntity() == toEntity//
					&& otherAgreement.pricingSeries() == pricingSeries//
					&& otherAgreement.priceExpression.equalsIgnoreCase(priceExpression)) {
				return true;
			}
		}
		return false;
	}
}
