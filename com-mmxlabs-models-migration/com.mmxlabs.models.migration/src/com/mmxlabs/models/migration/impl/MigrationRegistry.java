package com.mmxlabs.models.migration.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.migration.IMigrationRegistry;
import com.mmxlabs.models.migration.IMigrationUnit;
import com.mmxlabs.models.migration.extensions.DefaultMigrationContextExtensionPoint;
import com.mmxlabs.models.migration.extensions.MigrationContextExtensionPoint;
import com.mmxlabs.models.migration.extensions.MigrationUnitExtensionPoint;

/**
 * An implementation of {@link IMigrationRegistry}.
 * 
 * @author Simon Goodall
 * 
 */
public class MigrationRegistry implements IMigrationRegistry {

	private static final Logger log = LoggerFactory.getLogger(MigrationRegistry.class);

	private final Map<String, Integer> contexts = new HashMap<String, Integer>();

	private final Map<String, Map<Integer, IMigrationUnit>> fromVersionMap = new HashMap<String, Map<Integer, IMigrationUnit>>();

	private String defaultContext;

	/**
	 * Initialise the registry with the initial set of migration units and contexts. There should be a single defaultMigrationContext
	 * 
	 * @param defaultMigrationContexts
	 * @param migrationContexts
	 * @param migrationUnits
	 */
	@Inject
	public void init(final Iterable<DefaultMigrationContextExtensionPoint> defaultMigrationContexts, final Iterable<MigrationContextExtensionPoint> migrationContexts,
			final Iterable<MigrationUnitExtensionPoint> migrationUnits) {

		for (final MigrationContextExtensionPoint ext : migrationContexts) {
			try {
				registerContext(ext.getName(), Integer.parseInt(ext.getLatestVersion()));
			} catch (final NumberFormatException e) {
				log.error("Unable to register context: " + ext.getName(), e);
			}
		}
		for (final MigrationUnitExtensionPoint ext : migrationUnits) {
			try {
				registerMigrationUnit(ext.createMigrationUnit());
			} catch (final Exception e) {
				log.error("Unable to register migration unit for context: " + ext.getContext(), e);
			}
		}

		for (final DefaultMigrationContextExtensionPoint ext : defaultMigrationContexts) {
			if (defaultContext == null) {
				defaultContext = ext.getContext();
			} else {
				log.error("There is already a default migration context set. " + ext.getContext() + " will not be set as the default.");
			}
		}
	}

	@Override
	public Collection<String> getMigrationContexts() {
		return contexts.keySet();
	}

	@Override
	public boolean isContextRegistered(final String context) {
		return contexts.containsKey(context);
	}

	@Override
	public int getLatestContextVersion(final String context) {

		if (contexts.containsKey(context)) {
			return contexts.get(context);
		}

		throw new IllegalArgumentException("Unknown context: " + context);
	}

	@Override
	public List<IMigrationUnit> getMigrationChain(final String context, final int fromVersion, final int toVersion) {

		if (!contexts.containsKey(context)) {
			throw new IllegalArgumentException("Context not registered: " + context);
		}

		// Search through the map finding a set of IMigrationUnits to transform between the desired versions.
		final List<IMigrationUnit> chain = new ArrayList<IMigrationUnit>(toVersion - fromVersion);
		final Map<Integer, IMigrationUnit> froms = fromVersionMap.get(context);

		int currentVersion = fromVersion;
		while (currentVersion != toVersion) {
			final IMigrationUnit nextUnit = froms.get(currentVersion);
			// Is there another unit?
			if (nextUnit == null) {
				throw new RuntimeException(String.format("Unable to find migration chain between verions %d and %d for context %s.", fromVersion, toVersion, context));
			}
			// Add unit to chain
			chain.add(nextUnit);
			// Next version to find!
			currentVersion = nextUnit.getDestinationVersion();
		}

		return chain;
	}

	/**
	 * Register a migration context and the latest version of the context.
	 * 
	 * @param context
	 * @param latestVersion
	 */
	public void registerContext(final String context, final int latestVersion) {

		if (contexts.containsKey(context)) {
			throw new IllegalStateException("Context already registered: " + context);
		}
		contexts.put(context, latestVersion);
		fromVersionMap.put(context, new HashMap<Integer, IMigrationUnit>());
	}

	/**
	 * Register an {@link IMigrationUnit} with this registry
	 * 
	 * @param unit
	 */
	public void registerMigrationUnit(final IMigrationUnit unit) {
		final Map<Integer, IMigrationUnit> map = fromVersionMap.get(unit.getContext());
		map.put(unit.getSourceVersion(), unit);
	}

	@Override
	public String getDefaultMigrationContext() {
		return defaultContext;
	}
}
