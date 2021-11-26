package com.mmxlabs.models.migration;

import com.mmxlabs.models.migration.impl.MigrationRegistry;

public interface IMigrationProvider {

	void register(MigrationRegistry migrationRegistry);

}
