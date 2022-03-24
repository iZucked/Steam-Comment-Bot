package com.mmxlabs.models.lng.adp.mull.algorithm;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.commercial.SalesContract;

@NonNullByDefault
public class MullSalesContractWrapper implements IMullDischargeWrapper {
	private final SalesContract salesContract;

	public MullSalesContractWrapper(final SalesContract salesContract) {
		this.salesContract = salesContract;
	}

	public SalesContract getSalesContract() {
		return salesContract;
	}

	@Override
	public boolean equals(@Nullable Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof final MullSalesContractWrapper mullSalesContractWrapper) {
			return this.salesContract.equals(mullSalesContractWrapper.getSalesContract());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.salesContract.hashCode();
	}
}
