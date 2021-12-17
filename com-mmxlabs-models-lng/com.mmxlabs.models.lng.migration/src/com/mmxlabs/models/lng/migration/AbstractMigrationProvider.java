/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Supplier;

import org.eclipse.emf.common.util.URI;
import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.migration.IClientMigrationUnit;
import com.mmxlabs.models.migration.IMigrationProvider;
import com.mmxlabs.models.migration.IMigrationUnitExtension;
import com.mmxlabs.models.migration.PackageData;
import com.mmxlabs.models.migration.impl.AbstractMigrationWrapper;
import com.mmxlabs.models.migration.impl.MigrationRegistry;

/**
 * Abstract implementation of {@link IMigrationProvider} to do all the heavy
 * lifting for registering client migrations
 * 
 * @author Simon Goodall
 *
 */
public abstract class AbstractMigrationProvider implements IMigrationProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractMigrationProvider.class);

	private record Entry(IClientMigrationUnit unit, int lingoVersion, Supplier<IMigrationUnitExtension> wrapper) {
	}

	private final List<Entry> entries = new LinkedList<>();

	/**
	 * Subclasses should implement this method to register the different client
	 * metamodel versions. Versions are expected to be registered in order
	 */
	protected abstract void prepare();

	/**
	 * Return the client context for migration.
	 * 
	 * @return
	 */
	protected abstract String getClientContext();

	/**
	 * Register a client version.
	 * 
	 * @param unit             The client migration code
	 * @param fromLiNGOVersion The version number the metamodel change applies from
	 * @param wrapper          The wrapper class to pass the client metamodel into
	 *                         the main migration loop. May be null if a subsequent
	 *                         migration declare a wrapper on the same lingo
	 *                         version.
	 */
	protected void registerVersion(final IClientMigrationUnit unit, final int fromLiNGOVersion, final Supplier<IMigrationUnitExtension> wrapper) {
		entries.add(new Entry(unit, fromLiNGOVersion, wrapper));
	}

	/**
	 * Register a client version. LiNGO version from is based on the unit lingo
	 * version + 1
	 * 
	 * @param unit    The client migration code
	 * @param wrapper The wrapper class to pass the client metamodel into the main
	 *                migration loop. May be null if a subsequent migration declare
	 *                a wrapper on the same lingo version.
	 */
	protected void registerVersion(final IClientMigrationUnit unit, final Supplier<IMigrationUnitExtension> wrapper) {
		int lingoFromVersion = unit.getClientSourceVersion() == 0 ? 1 : unit.getScenarioDestinationVersion() + 1;
		registerVersion(unit, lingoFromVersion, wrapper);
	}

	/**
	 * Register a client version. LiNGO version from is based on the unit lingo and
	 * extension based on unit client dest version version + 1
	 * 
	 * @param unit The client migration code
	 */
	protected void registerVersion(final IClientMigrationUnit unit) {
		registerVersion(unit, createExtension(unit.getClientDestinationVersion()));
	}

	public void register(final MigrationRegistry registry) {

		prepare();

		// Find latest client version - derived from latest client destination
		// registered
		int latestVersion = -1;
		for (final Entry entry : entries) {
			latestVersion = Math.max(entry.unit.getClientDestinationVersion(), latestVersion);
		}

		// Register the client context and make it the default.
		registry.registerClientContext(getClientContext(), latestVersion, true);

		// Register the client migration units
		for (final Entry entry : entries) {
			registry.registerClientMigrationUnit(entry.unit);
		}

		// Create a tree set of lingo versions the wrapper (i.e. client metamodel
		// version) applies from.
		final TreeMap<Integer, Entry> wrappers = new TreeMap<>();
		for (final Entry entry : entries) {
			if (entry.wrapper != null) {
				wrappers.put(entry.lingoVersion, entry);
			}
		}

		// Query the latest LiNGO release so we can register a wrapper from v1 to
		// current.
		final int lingoVersion = registry.getLatestContextVersion(ModelsLNGMigrationConstants.Context);
		for (int v = 1; v <= lingoVersion; ++v) {
			// Find the matching wrapper based on the the last wrapper registered
			// numerically
			final Entry entry = wrappers.floorEntry(v).getValue();
			// Sanity check!
			assert v >= entry.lingoVersion;
			// We create a new instance in the supplier as during migration pre-process each
			// lingo migration unit is set in the wrapper registered against it..
			registry.registerMigrationUnitExtension(ModelsLNGMigrationConstants.getMigrationUnitID(v), entry.wrapper.get());
		}

		Entry v1Entry = entries.get(0);
		// Sanity check migration available from v1 to current
		try {
			// We start from the lingo version the client was introduced on rather than
			// lingo v1.
			registry.getMigrationChain(ModelsLNGMigrationConstants.Context, v1Entry.unit.getScenarioDestinationVersion(), lingoVersion, //
					getClientContext(), 1, latestVersion);
		} catch (Exception e) {
			LOGGER.error("Scenario migration is not configured correctly - unable to find a full migration chain.");
		}
	}

	/**
	 * Helper method to instantiate a simple wrapper class. This should cover most
	 * client migration code paths.
	 * 
	 * @param uri
	 * @param packageData
	 * @return
	 */
	protected IMigrationUnitExtension createSimpleExtension(String uri, PackageData packageData) {
		return new AbstractMigrationWrapper() {
			@Override
			protected void populateExtraPackages(final @NonNull Map<URI, PackageData> extraPackages) {
				extraPackages.put(URI.createPlatformPluginURI(uri, true), packageData);
			}
		};
	}

	protected abstract Supplier<IMigrationUnitExtension> createExtension(int version);

}
