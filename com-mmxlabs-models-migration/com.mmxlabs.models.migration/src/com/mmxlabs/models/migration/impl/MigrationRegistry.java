/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
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
import org.ops4j.peaberry.Peaberry;
import org.ops4j.peaberry.eclipse.EclipseRegistry;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
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
import com.mmxlabs.models.migration.internal.MigrationActivationModule;

/**
 * An implementation of {@link IMigrationRegistry} populated by extensions points.
 * 
 * @author Simon Goodall
 * 
 */
public class MigrationRegistry implements IMigrationRegistry {

	private static final Logger LOG = LoggerFactory.getLogger(MigrationRegistry.class);

	private final Map<String, Integer> contexts = new HashMap<>();
	private final Map<String, Map<Integer, List<IMigrationUnit>>> fromVersionMap = new HashMap<>();
	private final Map<IMigrationUnit, String> migrationExtPointIDMap = new HashMap<>();
	private final Map<String, List<IMigrationUnitExtension>> migrationUnitExtensionsMap = new HashMap<>();
	private String defaultContext;

	private final Map<String, Integer> clientContexts = new HashMap<>();
	private final Map<String, Map<Integer, List<IClientMigrationUnit>>> clientFromVersionMap = new HashMap<>();
	private final Map<IClientMigrationUnit, String> clientMigrationExtPointIDMap = new HashMap<>();
	private String defaultClientContext;

	
	public void activate() {
		final BundleContext bc = FrameworkUtil.getBundle(MigrationRegistry.class).getBundleContext();
		final Injector inj = Guice.createInjector(Peaberry.osgiModule(bc, EclipseRegistry.eclipseRegistry()), new MigrationActivationModule());
		inj.injectMembers(this);
	}
	
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
				LOG.error("Unable to register context: " + contextName, e);
			}
		}
		for (final MigrationUnitExtensionPoint ext : migrationUnits) {
			try {
				if (ext != null) {
					final MigrationUnitProxy proxy = new MigrationUnitProxy(ext);
					registerMigrationUnit(ext.getID(), proxy);

				}
			} catch (final Exception e) {
				LOG.error("Unable to register migration unit for context: " + (ext == null ? "unknown" : ext.getContext()), e);
			}
		}
		for (final MigrationUnitExtensionExtensionPoint ext : migrationUnitExtensions) {
			try {
				if (ext != null) {
					final MigrationUnitExtensionProxy proxy = new MigrationUnitExtensionProxy(ext);
					registerMigrationUnitExtension(ext.getMigrationUnitID(), proxy);
				}
			} catch (final Exception e) {
				LOG.error("Unable to register migration unit extension for unit: " + (ext == null ? "unknown" : ext.getMigrationUnitID()), e);
			}
		}

		for (final DefaultMigrationContextExtensionPoint ext : defaultMigrationContexts) {
			if (defaultContext == null) {
				defaultContext = ext.getContext();
			} else {
				LOG.error("There is already a default migration context set. " + ext.getContext() + " will not be set as the default.");
			}
		}

		for (final ClientMigrationContextExtensionPoint ext : clientMigrationContexts) {
			final String contextName = ext.getClientContextName();
			try {
				if (contextName != null) {
					registerClientContext(contextName, Integer.parseInt(ext.getLatestClientVersion()));
				}
			} catch (final NumberFormatException e) {
				LOG.error("Unable to register client context: " + contextName, e);
			}
		}
		for (final ClientMigrationUnitExtensionPoint ext : clientMigrationUnits) {
			try {
				if (ext != null) {
					final ClientMigrationUnitProxy proxy = new ClientMigrationUnitProxy(ext);
					registerClientMigrationUnit(ext.getID(), proxy);

				}
			} catch (final Exception e) {
				LOG.error("Unable to register client migration unit for context: " + (ext == null ? "unknown" : ext.getClientContext()), e);
			}
		}

		for (final DefaultClientMigrationContextExtensionPoint ext : defaultClientMigrationContexts) {
			if (defaultClientContext == null) {
				defaultClientContext = ext.getClientContext();
			} else {
				LOG.error("There is already a default client migration context set. " + ext.getClientContext() + " will not be set as the default.");
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
		final Map<Integer, List<IMigrationUnit>> scenarioFroms = fromVersionMap.get(scenarioContext);
		final Map<Integer, List<IClientMigrationUnit>> clientFroms = clientFromVersionMap.get(clientContext);

		final boolean needClientMigration = clientContext != null && toClientVersion != fromClientVersion;
		int currentScenarioVersion = fromScenarioVersion;
		int currentClientVersion = fromClientVersion;

		while (currentScenarioVersion != toScenarioVersion || currentClientVersion != toClientVersion) {

			// Phase one, get client migration units for current scenario version.
			if (needClientMigration) {
				while (currentClientVersion != toClientVersion) {

					final List<IClientMigrationUnit> nextUnits = clientFroms.get(currentClientVersion);
					// Is there another unit?
					if (nextUnits == null || nextUnits.isEmpty()) {
						throw new RuntimeException(String.format("Unable to find migration chain between versions %d and %d for client context %s.", fromClientVersion, toClientVersion, clientContext));
					}

					boolean foundUnit = false;
					for (final IClientMigrationUnit unit : nextUnits) {

						if (unit.getScenarioDestinationVersion() != currentScenarioVersion) {
							// Assume we need to perform a new scenario migration, thus break out of this loop and head on to the scenario migration code path.
							continue;
						}

						// Add unit to chain
						chain.add(unit);
						// Next version to find!
						currentClientVersion = unit.getClientDestinationVersion();
						foundUnit = true;
					}
					if (!foundUnit) {
						break;
					}
				}

			}

			// Phase two, get scenario migration units
			if (currentScenarioVersion != toScenarioVersion) {

				List<IMigrationUnit> nextUnits = scenarioFroms.get(currentScenarioVersion);
				// Is there another unit?
				if (nextUnits == null || nextUnits.isEmpty()) {
					throw new RuntimeException(String.format("Unable to find migration chain between verions %d and %d for context %s.", fromScenarioVersion, toScenarioVersion, scenarioContext));
				}

				boolean foundUnit = false;
				for (IMigrationUnit unit : nextUnits) {
					// Wrap the migration unit in any extension points found.
					if (migrationExtPointIDMap.containsKey(unit)) {
						final String unitID = migrationExtPointIDMap.get(unit);
						if (migrationUnitExtensionsMap.containsKey(unitID)) {
							final List<IMigrationUnitExtension> extensions = migrationUnitExtensionsMap.get(unitID);
							for (final IMigrationUnitExtension ext : extensions) {
								ext.setMigrationUnit(unit);
								unit = ext;
							}
						}
					}

					// Add unit to chain
					chain.add(unit);
					// Next version to find!
					currentScenarioVersion = unit.getScenarioDestinationVersion();
					foundUnit = true;
				}
				if (!foundUnit) {
					break;
				}

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
		fromVersionMap.put(context, new HashMap<Integer, List<IMigrationUnit>>());
	}

	/**
	 * Register an {@link IMigrationUnit} with this registry
	 * 
	 * @param unit
	 */
	public void registerMigrationUnit(@NonNull final IMigrationUnit unit) {
		final Map<Integer, List<IMigrationUnit>> map = fromVersionMap.get(unit.getScenarioContext());

		final int sourceVersion = unit.getScenarioSourceVersion();
		final List<IMigrationUnit> units;
		if (map.containsKey(sourceVersion)) {
			units = map.get(sourceVersion);
		} else {
			units = new LinkedList<>();
			map.put(sourceVersion, units);
		}
		units.add(unit);
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

		final Map<Integer, List<IMigrationUnit>> map = fromVersionMap.get(context);
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
		clientFromVersionMap.put(context, new HashMap<Integer, List<IClientMigrationUnit>>());
	}

	/**
	 * Register an {@link IMigrationUnit} with this registry
	 * 
	 * @param unit
	 */
	public void registerClientMigrationUnit(@NonNull final IClientMigrationUnit unit) {
		final Map<Integer, List<IClientMigrationUnit>> map = clientFromVersionMap.get(unit.getClientContext());
		final int clientSourceVersion = unit.getClientSourceVersion();
		final List<IClientMigrationUnit> units;
		if (map.containsKey(clientSourceVersion)) {
			units = map.get(clientSourceVersion);
		} else {
			units = new LinkedList<>();
			map.put(clientSourceVersion, units);
		}
		units.add(unit);
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

		final Map<Integer, List<IClientMigrationUnit>> map = clientFromVersionMap.get(context);
		int lastNumber = -1;
		for (final Integer v : map.keySet()) {
			if (v.intValue() > lastNumber) {
				lastNumber = v.intValue();
			}
		}
		return lastNumber;
	}
}
