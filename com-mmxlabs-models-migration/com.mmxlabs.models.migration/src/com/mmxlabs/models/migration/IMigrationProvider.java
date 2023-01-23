/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.migration;

import com.mmxlabs.models.migration.impl.MigrationRegistry;

public interface IMigrationProvider {

	static final int PRIORITY_MAIN = 0;
	
	static final int PRIORITY_CLIENT = 1;

	int priority();

	void register(MigrationRegistry migrationRegistry);

}
