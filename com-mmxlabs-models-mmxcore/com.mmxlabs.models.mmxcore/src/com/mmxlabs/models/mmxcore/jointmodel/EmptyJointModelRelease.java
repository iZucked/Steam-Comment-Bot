/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.mmxcore.jointmodel;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.edapt.migration.Metamodel;

/**
 * A basic joint model release which does no integration, just specifies a set
 * of versions.
 * 
 * @author hinton
 * 
 */
public class EmptyJointModelRelease implements IJointModelRelease {
	private Map<String, Integer> versions;

	/**
	 * Create a release with the given version map.
	 * 
	 * @param versions
	 */
	public EmptyJointModelRelease(final Map<String, Integer> versions) {
		this.versions = versions;
	}

	/**
	 * Create a release with the versions in the varargs given; var args should
	 * be alternating strings and integers, so key, version, key, version etc.
	 * 
	 * @param stuff
	 */
	public EmptyJointModelRelease(final Object... stuff) {
		versions = new HashMap<String, Integer>();
		for (int i = 0; i < stuff.length; i += 2) {
			versions.put((String) stuff[i], (Integer) stuff[i + 1]);
		}
	}

	@Override
	public int getReleaseVersion(final String modelKey) {
		return versions.get(modelKey);
	}

	@Override
	public Collection<String> getSubModelKeys() {
		return versions.keySet();
	}

	@Override
	public void prepare(Map<String, URI> subModels, Map<URI, Metamodel> state) {
		// do nothing
	}

	@Override
	public void integrate(Map<String, URI> subModels, Map<URI, Metamodel> state) {
		// do nothing
	}
}
