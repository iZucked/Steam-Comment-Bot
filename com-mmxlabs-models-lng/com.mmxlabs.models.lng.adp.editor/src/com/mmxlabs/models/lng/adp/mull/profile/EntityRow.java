/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull.profile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.adp.DESSalesMarketAllocationRow;
import com.mmxlabs.models.lng.adp.MullEntityRow;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;

@NonNullByDefault
public class EntityRow implements IEntityRow {

	private final BaseLegalEntity entity;
	private final int initialAllocation;
	private final double relativeEntitlement;
	private final List<SalesContractAllocationRow> salesContractAllocationRows;
	private final List<DesSalesMarketAllocationRow> desSalesMarketAllocationRows;

	public EntityRow(final MullEntityRow mullEntityRow) {
		@Nullable
		final BaseLegalEntity pEntity = mullEntityRow.getEntity();
		if (pEntity == null) {
			throw new IllegalStateException("Entity must be non-null");
		}
		this.entity = pEntity;
		this.initialAllocation = Integer.parseInt(mullEntityRow.getInitialAllocation());
		this.relativeEntitlement = mullEntityRow.getRelativeEntitlement();
		this.salesContractAllocationRows = new ArrayList<>();
		for (final com.mmxlabs.models.lng.adp.SalesContractAllocationRow eSalesContractAllocationRow : mullEntityRow.getSalesContractAllocationRows()) {
			if (eSalesContractAllocationRow == null) {
				throw new IllegalStateException("Sales contract allocation must be non-null");
			} else if (eSalesContractAllocationRow.getWeight() > 0) {
				this.salesContractAllocationRows.add(new SalesContractAllocationRow(eSalesContractAllocationRow));
			}
		}
		this.desSalesMarketAllocationRows = new ArrayList<>();
		for (final DESSalesMarketAllocationRow eDesSalesMarketAllocationRow : mullEntityRow.getDesSalesMarketAllocationRows()) {
			if (eDesSalesMarketAllocationRow == null) {
				throw new IllegalStateException("DES sales market allocation must be non-null");
			}
			this.desSalesMarketAllocationRows.add(new DesSalesMarketAllocationRow(eDesSalesMarketAllocationRow));
		}
		validateConstruction();
	}

	public EntityRow(final BaseLegalEntity entity, final int initialAllocation, final double relativeEntitlement, final List<SalesContractAllocationRow> salesContractAllocationRows, final List<DesSalesMarketAllocationRow> desSalesMarketAllocationRows) {
		this.entity = entity;
		this.initialAllocation = initialAllocation;
		this.relativeEntitlement = relativeEntitlement;
		this.salesContractAllocationRows = salesContractAllocationRows.stream().filter(row -> row.getWeight() > 0).toList();
		this.desSalesMarketAllocationRows = desSalesMarketAllocationRows.stream().toList();
		validateConstruction();
	}

	private void validateConstruction() {
		validateRelativeEntitlement();
		if (this.desSalesMarketAllocationRows.isEmpty()) {
			throw new IllegalStateException("No DES sales market allocations found");
		} else if (this.desSalesMarketAllocationRows.size() > 1) {
			throw new IllegalStateException("Got more DES sales market allocations than expected");
		}
	}

	private void validateRelativeEntitlement() {
		if (this.relativeEntitlement <= 0.0) {
			throw new IllegalStateException("Relative entitlement must be positive");
		}
	}

	@Override
	public BaseLegalEntity getEntity() {
		return this.entity;
	}

	@Override
	public int getInitialAllocation() {
		return this.initialAllocation;
	}

	@Override
	public double getRelativeEntitlement() {
		return this.relativeEntitlement;
	}

	@Override
	public List<SalesContractAllocationRow> getSalesContractRows() {
		return this.salesContractAllocationRows;
	}

	@Override
	public List<DesSalesMarketAllocationRow> getDesSalesMarketRows() {
		return this.desSalesMarketAllocationRows;
	}

	@Override
	public Stream<IAllocationRow> streamAllocationRows() {
		return Stream.concat(this.salesContractAllocationRows.stream(), this.desSalesMarketAllocationRows.stream());
	}
}
