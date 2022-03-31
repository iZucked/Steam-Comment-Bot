package com.mmxlabs.models.lng.adp.mull.profile;

import java.util.List;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.commercial.BaseLegalEntity;

@NonNullByDefault
public interface IEntityRow {
	
	public BaseLegalEntity getEntity();

	public int getInitialAllocation();

	public double getRelativeEntitlement();

	public List<SalesContractAllocationRow> getSalesContractRows();
	
	public List<DesSalesMarketAllocationRow> getDesSalesMarketRows();

	public Stream<IAllocationRow> streamAllocationRows();
}
