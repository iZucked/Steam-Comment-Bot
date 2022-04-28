/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull.profile;

import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.cargo.Inventory;

@NonNullByDefault
public interface IMullSubprofile {

	public Inventory getInventory();

	public List<IEntityRow> getEntityRows();
}