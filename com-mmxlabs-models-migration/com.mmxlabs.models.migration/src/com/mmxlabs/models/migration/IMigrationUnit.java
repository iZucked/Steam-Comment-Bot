package com.mmxlabs.models.migration;

import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter;

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

	/**
	 * Perform the migration. The {@link List} or {@link URI}s specify all of the ecore model instances in the scenario. These are likely to be different to the original location. The
	 * {@link URIConverter} will "normalize" convert between original URI and the new temporary URI. See {@link URIConverter#getURIMap()}.
	 * 
	 * @param uris
	 * @param uc
	 * @throws Exception
	 */
	void migrate(List<URI> uris, URIConverter uc) throws Exception;
}
