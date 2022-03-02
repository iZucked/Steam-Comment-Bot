/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.manager;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scenario.service.manifest.Manifest;

/**
 * An interface specifying a service to handle scenario migration. Scenario services should require an instance of this service to be available to ensure scenarios can be properly loaded.
 * 
 */
public interface IScenarioMigrationService {

	boolean needsMigrating(final URI archiveURI, @NonNull final Manifest manifest);
	
	/**
	 * Attempt to migrate scenario to latest version. This method will do nothing if the scenario is already at the latest version. Any errors during a migration will an {@link Exception} of some
	 * kind.
	 * 
	 * @param rootObjectURI
	 * 
	 * @param scenarioService
	 *            - Needed to resolve relative URIs
	 * @param scenarioInstance
	 */
	void migrateScenario(final URI archiveURI, @NonNull final Manifest manifest, @NonNull IProgressMonitor monitor) throws Exception;

}