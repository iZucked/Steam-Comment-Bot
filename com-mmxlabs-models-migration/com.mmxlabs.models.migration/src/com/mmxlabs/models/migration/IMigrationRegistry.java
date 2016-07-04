/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.migration;

import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

/**
 * The {@link IMigrationRegistry} stores the available migration contexts, the latest version of each context and various {@link IMigrationUnit} instances to transform between versions.
 * 
 * @author Simon Goodall
 * 
 */
public interface IMigrationRegistry {

	/**
	 * Returns true of the given string is a known migration context
	 * 
	 * @param context
	 * @return
	 */
	boolean isContextRegistered(@NonNull String context);

	/**
	 * Returns true of the given string is a known cient migration context
	 * 
	 * @param context
	 * @return
	 */
	boolean isClientContextRegistered(@NonNull String context);

	/**
	 * Returns a {@link Collection} of all registered migration contexts
	 * 
	 * @return
	 */
	@NonNull
	Collection<String> getMigrationContexts();

	/**
	 * Returns a {@link Collection} of all registered migration contexts
	 * 
	 * @return
	 */
	@NonNull
	Collection<String> getClientMigrationContexts();

	/**
	 * Returns the latest version registered against the given client context.
	 * 
	 * @param context
	 * @return
	 */
	int getLatestClientContextVersion(@NonNull String context);

	/**
	 * Returns the latest version registered against the given context.
	 * 
	 * @param context
	 * @return
	 */
	int getLatestContextVersion(@NonNull String context);

	/**
	 * Returns a ordered list of {@link IMigrationUnit}s to transform a scenario from the fromVersion to the toVersion. Throws a {@link RuntimeException} if it fails to find a migration chain.
	 * 
	 * @param context
	 * @param fromVersion
	 * @param toVersion
	 * @return
	 */
	@NonNull
	List<IMigrationUnit> getMigrationChain(@NonNull String scenarioContext, int fromScenarioVersion, int toScenarioVersion, @Nullable String clientContext, int fromClientVersion, int toClientVersion);

	/**
	 * Returns the default migration context for this application instance.
	 * 
	 * @return
	 */
	@Nullable
	String getDefaultMigrationContext();

	/**
	 * Returns the default migration context for this application instance.
	 * 
	 * @return
	 */
	@Nullable
	String getDefaultClientMigrationContext();

	/**
	 * Returns the greatest version number known for the context. This should be used when {@link #getLatestContextVersion(String)} is equals to {@link #SNAPSHOT_VERSION} to get the current release
	 * version number.
	 * 
	 * @param context
	 * @return
	 */
	int getLastReleaseVersion(@NonNull String context);

	/**
	 * Returns the greatest version number known for the context. This should be used when {@link #getLatestContextVersion(String)} is equals to {@link #SNAPSHOT_VERSION} to get the current release
	 * version number.
	 * 
	 * @param context
	 * @return
	 */
	int getLastReleaseClientVersion(@NonNull String context);

}
