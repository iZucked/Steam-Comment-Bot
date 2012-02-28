/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.mmxcore.jointmodel;
import java.util.Collection;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.edapt.migration.Metamodel;

/**
 * An {@link IJointModelRelease} is defined by a mapping from sub model keys (these are just well-known strings identifying the different submodels in a joint model)
 * to namespace URIs for the submodel version. The responsibility of a release is to handle any post-upgrade integration between different submodels, which it can access
 * as dynamic ecore models (as the compiled code for every version will not be in the platform).
 * 
 * The upgrade process will first try and match a release, using {@link #getReleaseVersion(String)} for all submodels. Once the release for a joint model has been found (by matching all submodel versions),
 * the joint model will be upgraded to the final version by calling the {@link #prepare()} method with all the old versions of the submodels, doing the edapt updates, and then calling the {@link #integrate()} method
 * with the new models.
 * 
 * @author hinton
 *
 */
public interface IJointModelRelease {
	int getReleaseVersion(final String modelKey);
	Collection<String> getSubModelKeys();
	void prepare(Map<String, URI> subModels, Map<URI, Metamodel> state);
	void integrate(Map<String, URI> subModels, Map<URI, Metamodel> state);
}
