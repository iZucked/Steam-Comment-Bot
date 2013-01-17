package com.mmxlabs.models.migration;

import java.util.Collection;
import java.util.List;

/**
 * The {@link IMigrationRegistry} stores the available migration contexts, the latest version of each context and various {@link IMigrationUnit} instances to transform between versions.
 * 
 * @author Simon Goodall
 * 
 */
public interface IMigrationRegistry {

	/**
	 * Constant indicating a snapshot version - one that is still in development.
	 */
	public static final int SNAPSHOT_VERSION = -1;

	/**
	 * Returns true of the given string is a known migration context
	 * 
	 * @param context
	 * @return
	 */
	boolean isContextRegistered(String context);

	/**
	 * Returns a {@link Collection} of all registered migration contexts
	 * 
	 * @return
	 */
	Collection<String> getMigrationContexts();

	/**
	 * Returns the latest version registered againt the given context.
	 * 
	 * @param context
	 * @return
	 */
	int getLatestContextVersion(String context);

	/**
	 * Returns a ordered list of {@link IMigrationUnit}s to transform a scenario from the fromVersion to the toVersion. Throws a {@link RuntimeException} if it fails to find a migration chain.
	 * 
	 * @param context
	 * @param fromVersion
	 * @param toVersion
	 * @return
	 */
	List<IMigrationUnit> getMigrationChain(String context, int fromVersion, int toVersion);

	/**
	 * Returns the default migration context for this application instance.
	 * 
	 * @return
	 */
	String getDefaultMigrationContext();

	/**
	 * Returns the greatest version number known for the context. This should be used when {@link #getLatestContextVersion(String)} is equals to {@link #SNAPSHOT_VERSION} to get the current release
	 * version number.
	 * 
	 * @param context
	 * @return
	 */
	int getLastReleaseVersion(String context);

}
