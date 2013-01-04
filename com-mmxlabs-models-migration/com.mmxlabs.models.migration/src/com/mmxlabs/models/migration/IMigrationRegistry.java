package com.mmxlabs.models.migration;

import java.util.Collection;

public interface IMigrationRegistry {

	Collection<String> getMigrationContexts();

	int getLatestContextVersion(String context);

	IMigrationChain getMigrationChain(String context, int fromVersion, int toVersion);
}
