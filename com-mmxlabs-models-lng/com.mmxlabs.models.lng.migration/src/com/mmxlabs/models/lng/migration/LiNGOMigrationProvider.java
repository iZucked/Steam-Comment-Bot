package com.mmxlabs.models.lng.migration;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.migration.IMigrationProvider;
import com.mmxlabs.models.migration.IMigrationUnit;
import com.mmxlabs.models.migration.impl.MigrationRegistry;

public class LiNGOMigrationProvider implements IMigrationProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(LiNGOMigrationProvider.class);

	/**
	 * Devs - change this number and follow existing class naming scheme for the
	 * reflective class loader to work.
	 */

	private static final int CURRENT_VERSION = 149;

	@Override
	public int priority() {
		return PRIORITY_MAIN;
	}

	@Override
	public void register(final MigrationRegistry migrationRegistry) {

		// Pair of ID used in client migration for the wrappers and the unit instance
		final List<Pair<String, IMigrationUnit>> entries = new LinkedList<>();

		for (int i = 1; i <= CURRENT_VERSION; ++i) {
			final String clsName = String.format("com.mmxlabs.models.lng.migration.units.MigrateToV%d", i);
			try {
				final Class<?> unit = Class.forName(clsName);
				entries.add(Pair.of(ModelsLNGMigrationConstants.getMigrationUnitID(i), (IMigrationUnit) unit.newInstance()));
			} catch (final Exception e) {
				throw new RuntimeException("Unable to load migration unit class", e);
			}
		}
		// Find latest client version - derived from latest client destination
		// registered
		int latestVersion = -1;
		for (final var entry : entries) {
			latestVersion = Math.max(entry.getSecond().getScenarioDestinationVersion(), latestVersion);
		}

		assert latestVersion == CURRENT_VERSION;

		// Register the context
		migrationRegistry.registerContext(ModelsLNGMigrationConstants.Context, CURRENT_VERSION);

		// Register the migration units
		for (final var entry : entries) {
			migrationRegistry.registerMigrationUnit(entry.getFirst(), entry.getSecond());
		}

		// Sanity check migration available from v1 to current
		try {
			migrationRegistry.getMigrationChain(ModelsLNGMigrationConstants.Context, 1, CURRENT_VERSION, //
					null, -1, -1);
		} catch (final Exception e) {
			LOGGER.error("Scenario migration is not configured correctly - unable to find a full migration chain.");
		}
	}
}
