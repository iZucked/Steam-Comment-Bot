/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.migration;

import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

/**
 * A single migration unit to convert between two versions of a scenario version.
 * 
 * @author Simon Goodall
 * 
 */
public interface IMigrationUnit {

	/**
	 * The migration context this unit applies to.
	 * 
	 * @return
	 */
	String getScenarioContext();

	/**
	 * Version number to upgrade the scenario from
	 * 
	 * @return
	 */
	int getScenarioSourceVersion();

	/**
	 * Version number the scenario will be upgraded to
	 * 
	 * @return
	 */
	int getScenarioDestinationVersion();

	/**
	 * Perform the migration. The {@link URI} specifies the ecore model instance for the scenario. Extra packages can be passed into the process. This is intended for situations where the migration
	 * units only know of a subset of packages required to load a scenario.
	 * 
	 * @param uri
	 * @param extraPackages
	 * @throws Exception
	 */
	void migrate(@NonNull URI uri, @Nullable Map<URI, PackageData> extraPackages) throws Exception;
}
