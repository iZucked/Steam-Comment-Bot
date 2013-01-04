package com.mmxlabs.models.migration;

import java.util.List;

import org.eclipse.emf.common.util.URI;

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
	String getContext();

	/**
	 * Version number to upgrade the scenario from
	 * 
	 * @return
	 */
	int getSourceVersion();

	/**
	 * Version number the scenario will be upgraded to
	 * 
	 * @return
	 */
	int getDestinationVersion();

	void migrate(List<URI> uris) throws Exception;
}
