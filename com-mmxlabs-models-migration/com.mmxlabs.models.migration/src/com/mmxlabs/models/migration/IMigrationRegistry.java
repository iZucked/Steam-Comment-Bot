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

}
