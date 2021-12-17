/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.migration;

import com.mmxlabs.models.migration.impl.MigrationRegistry;

public interface IMigrationProvider {

	void register(MigrationRegistry migrationRegistry);

}
