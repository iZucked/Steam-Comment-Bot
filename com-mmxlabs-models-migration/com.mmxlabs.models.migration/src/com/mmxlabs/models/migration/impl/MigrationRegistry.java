/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.migration.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.migration.IClientMigrationUnit;
import com.mmxlabs.models.migration.IMigrationRegistry;
import com.mmxlabs.models.migration.IMigrationUnit;
import com.mmxlabs.models.migration.IMigrationUnitExtension;
import com.mmxlabs.models.migration.extensions.ClientMigrationContextExtensionPoint;
import com.mmxlabs.models.migration.extensions.ClientMigrationUnitExtensionPoint;
import com.mmxlabs.models.migration.extensions.DefaultClientMigrationContextExtensionPoint;
import com.mmxlabs.models.migration.extensions.DefaultMigrationContextExtensionPoint;
import com.mmxlabs.models.migration.extensions.MigrationContextExtensionPoint;
import com.mmxlabs.models.migration.extensions.MigrationUnitExtensionExtensionPoint;
import com.mmxlabs.models.migration.extensions.MigrationUnitExtensionPoint;

/**
 * An implementation of {@link IMigrationRegistry} populated by extensions points.
 * 
 * @author Simon Goodall
 * 
 */
public class MigrationRegistry implements IMigrationRegistry {

	private static final Logger log = LoggerFactory.getLogger(MigrationRegistry.class);

	private final Map<String, Integer> contexts = new HashMap<>();
	private final Map<String, Map<Integer, IMigrationUnit>> fromVersionMap = new HashMap<>();
	private final Map<IMigrationUnit, String> migrationExtPointIDMap = new HashMap<>();
	private final Map<String, List<IMigrationUnitExtension>> migrationUnitExtensionsMap = new HashMap<>();
	private String defaultContext;

	private final Map<String, Integer> clientContexts = new HashMap<>();
	private final Map<String, Map<Integer, IClientMigrationUnit>> clientFromVersionMap = new HashMap<>();
	private final Map<IClientMigrationUnit, String> clientMigrationExtPointIDMap = new HashMap<>();
	private String defaultClientContext;

	/**
	 * Initialise the registry with the initial set of migration units and contexts. There should be a single defaultMigrationContext
	 * 
	 * @param defaultMigrationContexts
	 * @param migrationContexts
	 * @param migrationUnits
	 */
	@Inject
	public void init(final Iterable<MigrationContextExtensionPoint> migrationContexts, final Iterable<MigrationUnitExtensionPoint> migrationUnits,
			final Iterable<MigrationUnitExtensionExtensionPoint> migrationUnitExtensions, final Iterable<DefaultMigrationContextExtensionPoint> defaultMigrationContexts,
			final Iterable<ClientMigrationContextExtensionPoint> clientMigrationContexts, final Iterable<ClientMigrationUnitExtensionPoint> clientMigrationUnits,
			final Iterable<DefaultClientMigrationContextExtensionPoint> defaultClientMigrationContexts) {

		for (final MigrationContextExtensionPoint ext : migrationContexts) {
			final String contextName = ext.getContextName();
			try {
				if (contextName != null) {
					registerContext(contextName, Integer.parseInt(ext.getLatestVersion()));
				}
			} catch (final NumberFormatException e) {
				log.error("Unable to register context: " + contextName, e);
			}
		}
		for (final MigrationUnitExtensionPoint ext : migrationUnits) {
			try {
				if (ext != null) {
					final MigrationUnitProxy proxy = new MigrationUnitProxy(ext);
					registerMigrationUnit(ext.getID(), proxy);

				}
			} catch (final Exception e) {
				log.error("Unable to register migration unit for context: " + (ext == null ? "unknown" : ext.getContext()), e);
			}
		}
		for (final MigrationUnitExtensionExtensionPoint ext : migrationUnitExtensions) {
			try {
				if (ext != null) {
					final MigrationUnitExtensionProxy proxy = new MigrationUnitExtensionProxy(ext);
					registerMigrationUnitExtension(ext.getMigrationUnitID(), proxy);
				}
			} catch (final Exception e) {
				log.error("Unable to register migration unit extension for unit: " + (ext == null ? "unknown" : ext.getMigrationUnitID()), e);
			}
		}

		for (final DefaultMigrationContextExtensionPoint ext : defaultMigrationContexts) {
			if (defaultContext == null) {
				defaultContext = ext.getContext();
			} else {
				log.error("There is already a default migration context set. " + ext.getContext() + " will not be set as the default.");
			}
		}

		for (final ClientMigrationContextExtensionPoint ext : clientMigrationContexts) {
			final String contextName = ext.getClientContextName();
			try {
				if (contextName != null) {
					registerClientContext(contextName, Integer.parseInt(ext.getLatestClientVersion()));
				}
			} catch (final NumberFormatException e) {
				log.error("Unable to register client context: " + contextName, e);
			}
		}
		for (final ClientMigrationUnitExtensionPoint ext : clientMigrationUnits) {
			try {
				if (ext != null) {
					final ClientMigrationUnitProxy proxy = new ClientMigrationUnitProxy(ext);
					registerClientMigrationUnit(ext.getID(), proxy);

				}
			} catch (final Exception e) {
				log.error("Unable to register client migration unit for context: " + (ext == null ? "unknown" : ext.getClientContext()), e);
			}
		}

		for (final DefaultClientMigrationContextExtensionPoint ext : defaultClientMigrationContexts) {
			if (defaultClientContext == null) {
				defaultClientContext = ext.getClientContext();
			} else {
				log.error("There is already a default client migration context set. " + ext.getClientContext() + " will not be set as the default.");
			}
		}
	}

	@Override
	@NonNull
	public Collection<String> getMigrationContexts() {
		return contexts.keySet();
	}

	@Override
	public boolean isContextRegistered(@NonNull final String context) {
		return contexts.containsKey(context);
	}

	@Override
	public int getLatestContextVersion(@NonNull final String context) {

		if (contexts.containsKey(context)) {
			return contexts.get(context);
		}

		throw new IllegalArgumentException("Unknown context: " + context);
	}

	@Override
	@NonNull
	public List<IMigrationUnit> getMigrationChain(@NonNull final String scenarioContext, final int fromScenarioVersion, final int toScenarioVersion, @Nullable final String clientContext,
			final int fromClientVersion, final int toClientVersion) {

		if (!contexts.containsKey(scenarioContext)) {
			throw new IllegalArgumentException("Context not registered: " + scenarioContext);
		}

		// Search through the map finding a set of IMigrationUnits to transform between the desired versions.
		final List<IMigrationUnit> chain = new ArrayList<IMigrationUnit>(Math.min(1, Math.abs(toScenarioVersion - fromScenarioVersion) + Math.abs(toClientVersion - fromClientVersion)));
		final Map<Integer, IMigrationUnit> scenarioFroms = fromVersionMap.get(scenarioContext);
		final Map<Integer, IClientMigrationUnit> clientFroms = clientFromVersionMap.get(clientContext);

		final boolean needClientMigration = clientContext != null && toClientVersion != fromClientVersion;
		int currentScenarioVersion = fromScenarioVersion;
		int currentClientVersion = fromClientVersion;

		while (currentScenarioVersion != toScenarioVersion || currentClientVersion != toClientVersion) {

			// Phase one, get client migration units for current scenario version.
			if (needClientMigration) {
				while (currentClientVersion != toClientVersion) {

					final IClientMigrationUnit nextUnit = clientFroms.get(currentClientVersion);
					// Is there another unit?
					if (nextUnit == null) {
						throw new RuntimeException(String.format("Unable to find migration chain between verions %d and %d for client context %s.", fromClientVersion, toClientVersion, clientContext));
					}

					if (nextUnit.getScenarioDestinationVersion() != currentScenarioVersion) {
						// Assume we need to perform a new scenario migration, thus break out of this loop and head on to the scenario migration code path.
						break;
					}

					// Add unit to chain
					chain.add(nextUnit);
					// Next version to find!
					currentClientVersion = nextUnit.getClientDestinationVersion();
				}

			}

			// Phase two, get scenario migration units
			if (currentScenarioVersion != toScenarioVersion) {

				IMigrationUnit nextUnit = scenarioFroms.get(currentScenarioVersion);
				// Is there another unit?
				if (nextUnit == null) {
					throw new RuntimeException(String.format("Unable to find migration chain between verions %d and %d for context %s.", fromScenarioVersion, toScenarioVersion, scenarioContext));
				}

				// Wrap the migration unit in any extension points found.
				if (migrationExtPointIDMap.containsKey(nextUnit)) {
					final String unitID = migrationExtPointIDMap.get(nextUnit);
					if (migrationUnitExtensionsMap.containsKey(unitID)) {
						final List<IMigrationUnitExtension> extensions = migrationUnitExtensionsMap.get(unitID);
						for (final IMigrationUnitExtension ext : extensions) {
							ext.setMigrationUnit(nextUnit);
							nextUnit = ext;
						}
					}
				}

				// Add unit to chain
				chain.add(nextUnit);
				// Next version to find!
				currentScenarioVersion = nextUnit.getScenarioDestinationVersion();
			} else {
				break;
			}
		}
		return chain;
	}

	/**
	 * Register a migration context and the latest version of the context.
	 * 
	 * @param context
	 * @param latestVersion
	 */
	public void registerContext(@NonNull final String context, final int latestVersion) {

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
	public void registerMigrationUnit(@NonNull final IMigrationUnit unit) {
		final Map<Integer, IMigrationUnit> map = fromVersionMap.get(unit.getScenarioContext());
		map.put(unit.getScenarioSourceVersion(), unit);
	}

	/**
	 */
	public void registerMigrationUnit(final String id, @NonNull final IMigrationUnit unit) {
		registerMigrationUnit(unit);
		migrationExtPointIDMap.put(unit, id);
	}

	/**
	 */
	public void registerMigrationUnitExtension(final String unitID, @NonNull final IMigrationUnitExtension unitExtension) {
		final List<IMigrationUnitExtension> extensions;
		if (migrationUnitExtensionsMap.containsKey(unitID)) {
			extensions = migrationUnitExtensionsMap.get(unitID);
		} else {
			extensions = new LinkedList<IMigrationUnitExtension>();
			migrationUnitExtensionsMap.put(unitID, extensions);
		}
		extensions.add(unitExtension);
	}

	@Override
	public String getDefaultMigrationContext() {
		return defaultContext;
	}

	@Override
	public int getLastReleaseVersion(@NonNull final String context) {

		final Map<Integer, IMigrationUnit> map = fromVersionMap.get(context);
		int lastNumber = -1;
		for (final Integer v : map.keySet()) {
			if (v.intValue() > lastNumber) {
				lastNumber = v.intValue();
			}
		}
		return lastNumber;
	}

	/**
	 * Register a migration context and the latest version of the context.
	 * 
	 * @param context
	 * @param latestVersion
	 */
	public void registerClientContext(@NonNull final String context, final int latestVersion) {

		if (clientContexts.containsKey(context)) {
			throw new IllegalStateException("Client Context already registered: " + context);
		}
		clientContexts.put(context, latestVersion);
		clientFromVersionMap.put(context, new HashMap<Integer, IClientMigrationUnit>());
	}

	/**
	 * Register an {@link IMigrationUnit} with this registry
	 * 
	 * @param unit
	 */
	public void registerClientMigrationUnit(@NonNull final IClientMigrationUnit unit) {
		final Map<Integer, IClientMigrationUnit> map = clientFromVersionMap.get(unit.getClientContext());
		map.put(unit.getClientSourceVersion(), unit);
	}

	/**
	 */
	public void registerClientMigrationUnit(final String id, @NonNull final IClientMigrationUnit unit) {
		registerClientMigrationUnit(unit);
		clientMigrationExtPointIDMap.put(unit, id);
	}

	@Override
	public boolean isClientContextRegistered(@NonNull final String context) {
		return clientContexts.containsKey(context);
	}

	@Override
	@NonNull
	public Collection<String> getClientMigrationContexts() {
		return clientContexts.keySet();

	}

	@Override
	public int getLatestClientContextVersion(@NonNull final String context) {
		// Scenarios may not have a context, so return 0 here rather than throw exception later.
		if (context == null || context.isEmpty()) {
			return 0;
		}

		if (clientContexts.containsKey(context)) {
			return clientContexts.get(context);
		}

		throw new IllegalArgumentException("Unknown client context: " + context);
	}

	@Override
	@Nullable
	public String getDefaultClientMigrationContext() {
		return defaultClientContext;
	}

	@Override
	public int getLastReleaseClientVersion(@NonNull final String context) {

		final Map<Integer, IClientMigrationUnit> map = clientFromVersionMap.get(context);
		int lastNumber = -1;
		for (final Integer v : map.keySet()) {
			if (v.intValue() > lastNumber) {
				lastNumber = v.intValue();
			}
		}
		return lastNumber;
	}
}
