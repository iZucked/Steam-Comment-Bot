package com.mmxlabs.models.lng.adp.mull.profile;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.adp.MullEntityRow;
import com.mmxlabs.models.lng.cargo.Inventory;

@NonNullByDefault
public class MullSubprofile implements IMullSubprofile {

	private final Inventory inventory;
	private final List<IEntityRow> entityRows;

	public MullSubprofile(final com.mmxlabs.models.lng.adp.MullSubprofile eMullSubprofile) {
		@Nullable
		final Inventory pInventory = eMullSubprofile.getInventory();
		if (pInventory == null) {
			throw new IllegalStateException("Inventory must be non-null");
		}
		this.inventory = pInventory;
		this.entityRows = new ArrayList<>();
		for (final MullEntityRow eMullEntityRow : eMullSubprofile.getEntityTable()) {
			if (eMullEntityRow == null) {
				throw new IllegalStateException("Mull entity row must be non-null");
			} else if (eMullEntityRow.getRelativeEntitlement() > 0.0) {
				entityRows.add(new EntityRow(eMullEntityRow));
			}
		}
		validateConstruction();
	}

	public MullSubprofile(final Inventory inventory, final List<IEntityRow> entityRows) {
		this.inventory = inventory;
		this.entityRows = entityRows.stream().filter(row -> row.getRelativeEntitlement() > 0.0).toList();
		validateConstruction();
	}

	private void validateConstruction() {
		if (entityRows.isEmpty()) {
			throw new IllegalStateException("No entity rows found");
		}
	}
	
	@Override
	public Inventory getInventory() {
		return this.inventory;
	}

	@Override
	public List<IEntityRow> getEntityRows() {
		return this.entityRows;
	}
	
}
